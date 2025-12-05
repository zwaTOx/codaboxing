from typing import Any, Dict, Optional
from pydantic import BaseModel, Field, field_validator


class TestCaseResult(BaseModel):
    inputData: Dict[str, Any]
    expectedOutput: Any
    actualOutput: Any = None
    status: str #'FAILED', 'PASSED', 'ERROR'
    errorMessage: Optional[str] = Field(default="")
    executionTime: Optional[float] = None
    
    @field_validator('executionTime', mode='before')
    @classmethod
    def ensure_float_and_round(cls, v):
        if v is None:
            return None
        try:
            return round(float(v), 2)
        except (ValueError, TypeError):
            return None
