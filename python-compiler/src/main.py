from contextlib import asynccontextmanager
from fastapi import FastAPI

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
