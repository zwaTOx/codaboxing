from contextlib import asynccontextmanager
from fastapi import FastAPI

from src.view.CompileController import router as compile_router

@asynccontextmanager
async def lifespan(app: FastAPI):
    print('Server is starting')
    yield
    print('Server is shutting down')

app = FastAPI(
    title='AI Task Traker',
    lifespan=lifespan
)

@app.get('/')
async def ping():
    return {'msg': 'pong'}

app.include_router(compile_router)
