from typing import Any, Dict, List, Optional
from pydantic import BaseModel, Field

class TestCase(BaseModel):
    input: Dict[str, Any]
    output:  Any


class CompileRequest(BaseModel):
    code: str
    timeout: Optional[int] = Field(default=5)
    max_length: Optional[int] = Field(default=1000)
    test_cases: List[TestCase]