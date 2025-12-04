from typing import Any, Dict, List, Optional
from pydantic import BaseModel, Field

class CompileRequest(BaseModel):
    code: str
    timeout: Optional[int] = Field(default=5)
    max_length: Optional[int] = Field(default=1000)
    inputs: List[Dict[str, Any]]
    outputs:  List[Dict[str, Any]]