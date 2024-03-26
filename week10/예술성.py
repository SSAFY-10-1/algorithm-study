# // 1. bfs 돌려서 그룹구하기
# // 2. 조화로움 계산
# // 3. 십자 회전
# // 4. 4개 정사각형 회전
# //초기 예술 점수, 1회전 이후 예술 점수, 2회전 이후 예술 점수, 그리고 3회전 이후 예술 점수의 합
from collections import deque

dx = [0, 0, -1, 1]
dy = [1, -1, 0, 0]
n = int(input())

board = []

scores = [0, 0,0,0]
gCnt = 0 # idx
 # (idx, num, size, pos = [(x, y)])

for i in range(n): 
    board.append(list(map(int, input().split())))

def bfs(x, y, gIdx, visited, idxBoard):
    global board, touch
    q = deque()
    q.append((x, y))
    visited[x][y] = 1
    num = board[x][y]
    size = 0
    visited[x][y] = 1
    posList = []
    while q:
        x, y = q.popleft()
        size += 1
        idxBoard[x][y] = gIdx
        posList.append((x, y))
        for i in range(4):
            nx = x + dx[i]
            ny = y + dy[i]
            if 0 <= nx < n and 0 <= ny < n  and visited[nx][ny] == 0:
                nNum = board[nx][ny]
                if nNum == num:
                    q.append((nx, ny))
                    visited[nx][ny] = 1
    return [gIdx, num, size, posList]
   
def getTouch(touch, groups, idxBoard):
    global gCnt 
    
    for i in range(gCnt):
        gIdx, num, size, posList = groups[i]
        for pos in posList:
            x, y = pos
            for d in range(4):
                nx, ny = x + dx[d], y + dy[d]
                if 0 <= nx < n and 0 <= ny < n:
                    nNum = idxBoard[nx][ny]
                    if nNum != gIdx:
                        touch[nNum][gIdx] += 1                    

def getArtScore():
    global gCnt
    idxBoard = [[-1] * n for _ in range(n) ]
    visited = [[0] * n for _ in range(n)]
    groups = {}
    for i in range(n):
        for j in range(n):
            if visited[i][j]  == 0:
                groupInfo = bfs(i, j, gCnt, visited, idxBoard)
                groups[gCnt] = groupInfo
                gCnt += 1
    touch = [[0] * gCnt for i in range(gCnt)] 
    getTouch(touch, groups, idxBoard)

    temp = 0
    for i in range(gCnt-1):
        gIdx, num, size, posList = groups[i]
        for j in range(i+1, gCnt):
            gIdx2, num2, size2, posList2 = groups[j]
            temp += (size + size2) * num * num2 * touch[i][j]
    return temp

def rotateSquare(sx, sy, s, nextBoard):
    global board
    for x in range(sx, sx + s):
        for y in range(sy, sy + s):
            ox , oy = x - sx, y -sy
            rx, ry = oy, s - ox - 1
            nextBoard[rx + sx][ry + sy] = board[x][y]       
    
def rotateMid(nextBoard, mid):
    global board
    temp = 0
    w = [0] * n # 가로
    h = [0] * n # 세로
    ow = board[mid]
    oh = [0] * n
    for i in range(n):
        oh[i] = board[i][mid]

    nextBoard[mid] = oh
    for i in range(n):
        nextBoard[i][mid] = ow[n - i - 1]  
            
# print(getArtScore())
for i in range(4):
    gCnt = 0
    scores[i] = getArtScore()
    nextBoard = [[0] * n for _ in range(n)]
    size = n // 2
    spos = [(0, 0), (0, size + 1), (size + 1, 0), (size + 1, size + 1)]
    for pos in spos:
        sx, sy = pos
        rotateSquare(sx, sy, size, nextBoard)
    rotateMid(nextBoard, size)    
    for x in range(n):
        for y in range(n):
            board[x][y] = nextBoard[x][y]
print(sum(scores))