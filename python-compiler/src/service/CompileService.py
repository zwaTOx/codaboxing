import asyncio
import time
import json
import traceback
import subprocess
import sys
from typing import List, Dict, Any, Optional

from src.schemas.CompileRequests import CompileRequest, TestCase  
from src.schemas.CompileResponses import TestCaseResult 

class CompileService:
    def __init__(self, timeout: int = 5, python_path: str = "python"):
        self.timeout = timeout
        self.python_path = python_path
    
    async def execute_python_code(
        self, 
        compile_request: CompileRequest
    ) -> List[TestCaseResult]:
        test_cases = self._create_test_cases_from_request(compile_request)
        
        results = []
        
        for test_case in test_cases:
            try:
                input_value = self._extract_input_values(test_case.inputData)
                
                args_str = self._prepare_arguments_for_execute(input_value)
                
                full_script = f"""
import sys
import json

{compile_request.code}

def main():
    result = execute({args_str})
    try:
        print(json.dumps({{"result": result}}))
    except (TypeError, ValueError):
        print(json.dumps({{"result": str(result)}}))

if __name__ == "__main__":
    main()
"""
                start_time = time.time()

                try:
                    completed_process = await asyncio.to_thread(
                        self._run_subprocess_sync,
                        full_script,
                        self.timeout
                    )
                    
                    execution_time = time.time() - start_time

                    result = self._process_execution_result(
                        test_case=test_case,
                        stdout=completed_process['stdout'].encode() if completed_process['stdout'] else b"",
                        stderr=completed_process['stderr'].encode() if completed_process['stderr'] else b"",
                        return_code=completed_process['return_code'],
                        execution_time=execution_time
                    )
                    
                    results.append(result)
                    
                except asyncio.TimeoutError:
                    result = TestCaseResult(
                        inputData=test_case.inputData,
                        expectedOutput=test_case.expectedOutput,
                        status="ERROR",
                        errorMessage=f"Таймаут выполнения ({self.timeout} секунд)"
                    )
                    results.append(result)
                    
            except Exception as e:
                error_type = type(e).__name__
                error_msg = str(e) if str(e) else "Неизвестная ошибка"
                error_details = traceback.format_exc()

                result = TestCaseResult(
                    inputData=test_case.inputData,
                    expectedOutput=test_case.expectedOutput,
                    status="ERROR",
                    errorMessage=f"Ошибка запуска: {error_type if not error_msg else error_msg}"
                )
                results.append(result)
        
        return results
    
    def _prepare_arguments_for_execute(self, input_values: List[Any]) -> str:
        if not input_values:
            return ""
        args_list = []
        for arg in input_values:
            if arg is None:
                args_list.append("None")
            else:
                try:
                    args_list.append(json.dumps(arg))
                except (TypeError, ValueError):
                    args_list.append(repr(arg))
        
        return ", ".join(args_list)
    
    def _extract_input_values(self, input_dict: Dict[str, Any]) -> List[Any]:
        if not input_dict:
            return []
        return list(input_dict.values())

    def _run_subprocess_sync(self, script: str, timeout: int) -> Dict[str, Any]:
        try:
            completed_process = subprocess.run(
                [self.python_path, "-c", script],
                capture_output=True,
                text=True,
                timeout=timeout,
                encoding='utf-8',
                errors='ignore'
            )
            
            return {
                "stdout": completed_process.stdout,
                "stderr": completed_process.stderr,
                "return_code": completed_process.returncode
            }
        except subprocess.TimeoutExpired:
            return {
                "stdout": "",
                "stderr": f"Timeout expired after {timeout} seconds",
                "return_code": -1
            }
        except Exception as e:
            return {
                "stdout": "",
                "stderr": f"Subprocess error: {str(e)}",
                "return_code": -1
            }
    
    def _create_test_cases_from_request(self, request: CompileRequest) -> List[TestCaseResult]:
        test_cases_results = []
        
        for i, test_case in enumerate(request.test_cases):
            test_case_result = TestCaseResult(
                inputData=test_case.input,
                expectedOutput=test_case.output,
                status="PENDING",
            )
            test_cases_results.append(test_case_result)
        
        return test_cases_results
    
    
    def _process_execution_result(self, 
                                 test_case: TestCaseResult,
                                 stdout: bytes,
                                 stderr: bytes,
                                 return_code: int,
                                 execution_time: float) -> TestCaseResult:
        stdout_str = stdout.decode('utf-8', errors='ignore').strip() if stdout else ""
        stderr_str = stderr.decode('utf-8', errors='ignore').strip() if stderr else ""
        
        actual_output = None
        error_message = stderr_str
        status = "ERROR"
        
        if stdout_str:
            try:
                output_data = json.loads(stdout_str)
                actual_output = output_data.get("result", "")
            except json.JSONDecodeError as je:
                actual_output = stdout_str
        else:
            actual_output = ""
        
        if stderr_str or return_code != 0:
            status = "ERROR"
            error_message = stderr_str or f"Процесс завершился с кодом {return_code}"
        else:
            try:
                expected_value = test_case.expectedOutput                
                if str(expected_value) == str(actual_output):
                    status = "PASSED"
                    error_message = None
                else:
                    status = "FAILED"
                    error_message = f"Ожидалось: {expected_value}, Получено: {actual_output}"
            except Exception as e:
                status = "ERROR"
                error_message = f"Ошибка сравнения: {str(e)}"
        
        return TestCaseResult(
            inputData=test_case.inputData,
            expectedOutput=test_case.expectedOutput,
            actualOutput=actual_output if not isinstance(actual_output, dict) else actual_output,
            status=status,
            errorMessage=error_message,
            executionTime=execution_time
        )