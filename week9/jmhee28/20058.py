import sys
from collections import deque
input = sys.stdin.readline

# 2 ≤ N ≤ 6
# 1 ≤ Q ≤ 1,000
# 0 ≤ A[r][c] ≤ 100
# 0 ≤ Li ≤ N

dx = [0, 0, 1, -1]
dy = [1, -1, 0, 0]
N, Q = map(int, input().split())
SIZE = 1 << N
magicQ = []
board = []
for i in range(SIZE):
    board.append(list(map(int, input().split())))

magicQ = list((map(int, input().split())))   
visited = [[0 for _ in range(SIZE)] for _ in range(SIZE)]
curSum = 0

def rotate(splitSize, start_row, start_col):
    global board, visited
    subBoard = [[0 for _ in range(SIZE)] for _ in range(SIZE)]
    if start_row >= SIZE or start_col >= SIZE or visited[start_row][start_col] == 1: 
        return
    visited[start_row][start_col] = 1
    
    for c in range(start_col, start_col + splitSize):
        for r in range(start_row, start_row + splitSize):
            subBoard[c - start_col][r - start_row] = board[r][c]
            
    for r in range(start_row, start_row + splitSize):
        sub_col = splitSize - 1
        for c in range(start_col, start_col + splitSize):
            visited[r][c] = 1
            board[r][c] = subBoard[r - start_row][sub_col]
            sub_col -= 1
            

def shrink():
    global board, curSum
    s = set()
    for i in range(SIZE):
        for j in range(SIZE):
            ice = 0
            curSum += board[i][j]
            for d in range(4):
                nx = i + dx[d]
                ny = j + dy[d]
                if 0 <= nx < SIZE and 0 <= ny < SIZE and board[nx][ny] > 0:
                     ice += 1
            if ice < 3:
                if board[i][j] > 0:
                    s.add((i, j))
                    curSum -= 1
    for pos in s:
        x, y = pos
        board[x][y] -= 1
   
        
def maxChunk():
    maxChunkNum = 0
    chunk = [[0 for _ in range(SIZE)] for _ in range(SIZE)]
    for i in range(SIZE):
        for j in range(SIZE):
            if chunk[i][j] == 0 and board[i][j] > 0:
                bfsResult = bfs(chunk, i, j)
                # print(bfsResult)
                maxChunkNum = max(maxChunkNum, bfsResult)
    return maxChunkNum

def bfs(chunk, x, y):
    q = deque()
    q.append((x, y))
    chunkSize = 0
    chunk[x][y] = 1
    while q:
        x, y = q.popleft()
        chunkSize += 1
        for d in range(4):
            nx = x + dx[d]
            ny = y + dy[d]
            if 0 <= nx < SIZE and 0 <= ny < SIZE and board[nx][ny] > 0 and chunk[nx][ny] != 1:
                chunk[nx][ny] = 1
                q.append((nx, ny))    
    return chunkSize
    
    
        
def print2DArray(arr):
    for i in range(len(arr)):
        print(arr[i])  

 
for magic in magicQ:
    curSum = 0
    if magic >= 1:    
        visited = [[0 for _ in range(SIZE)] for _ in range(SIZE)]
        splitSize = 1 << magic
        start_row = 0
        start_col = 0
        count = SIZE // splitSize
        for i in range(count):
            for j in range(count):
                rotate(splitSize, start_row, start_col)
                start_col = start_col + splitSize
            start_col = 0
            start_row += splitSize
    shrink()
    # print2DArray(board)


print(curSum)
print(maxChunk())    
            