from fastapi import APIRouter, status

from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from typing import List, Optional

from src.schemas.CompileRequests import CompileRequest
from src.schemas.CompileResponses import TestCaseResult
from src.service.CompileService import CompileService

router = APIRouter(prefix="/api/v1", tags=["compile"])

@router.post('/compile',
    status_code=status.HTTP_201_CREATED,
    response_model=List[TestCaseResult])
async def compile_code(
    request: CompileRequest
):
    result = await CompileService(timeout=request.timeout).execute_python_code(request)
    return result