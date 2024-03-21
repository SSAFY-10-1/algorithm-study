import sys
from collections import deque

input = sys.stdin.readline
L, N, Q = map(int, input().split())
board = [] 
commands = [] 
knights = []
knightsBoard = [[-1] * L for _ in range(L)]
damages = [0] * N
# 위쪽, 오른쪽, 아래쪽, 왼쪽 
dx = [-1, 0, 1, 0]
dy = [0, 1, 0, -1]

def init():
    for i in range(L):
        # 0 빈칸, 1 함정,  2벽
        board.append(list(map(int, input().split())))

    for i in range(0,N):
        # 처음 위치는 (r,c)를 좌측 상단 꼭지점
        # 세로 h, 가로 w인 직사각형 형태
        # 초기 체력이 k
        r, c, h, w, k  = map(int, input().split())
        r-=1
        c-=1
        knights.append([r, c, h, w, k, True])
        
        for x in range(r, r + h):
            for y in range(c, c + w):
                knightsBoard[x][y] = i 
        
    for i in range(Q):
        idx, d = map(int, input().split())
        commands.append([idx-1, d])

def executeCommands():
    for command in commands:
        idx, d = command
        if knights[idx][5]:
            executeCommand(idx, d)
        
def executeCommand(idx, d):
    crashed = [idx]
    v = validPoss(idx, d,   knights[idx], crashed)
    if v:
        moveKnights(idx, d, crashed)
   
def moveKnights(cidx, dir, knightsIdx = []):
    # cidx : 명령을 받은 기사
    # knights: 기사들의 idx
    global knightsBoard
    moved = [[-1] * L for _ in range(L)]
    eraseTargets =[]
    for idx in knightsIdx:
        damage = moveKnight(idx, dir, moved)
        if cidx != idx:
            
            if damages[idx] + damage >= knights[idx][4]:
                knights[idx][5] = False
                eraseTargets.append(idx)
                # eraseKnight(idx)
            else:
                damages[idx] += damage
    for i in range(L):
        for j in range(L):
            curIdx = knightsBoard[i][j]
            if knightsBoard[i][j] in knightsIdx:
                        knightsBoard[i][j] = -1
            if moved[i][j] != -1:
                   knightsBoard[i][j] = moved[i][j] 
    # 탈락한거 지우기              
    for eraseTarget in eraseTargets:
        r, c, h, w, k, status = knights[eraseTarget]
        for x in range(r, r + h):
            for y in range(c, c+w):
                knightsBoard[x][y] = -1
                
        
def moveKnight(idx, dir, moved):  
    global damages,knights
    r, c, h, w, k, status  = knights[idx] 
    damage = 0
    nr = r + dx[dir]
    nc = c + dy[dir]
    for i in range(nr, nr + h):
        for j in range(nc, nc + w):
            moved[i][j] = idx
            if board[i][j] == 1:
                damage += 1
    knights[idx] = [nr, nc, h, w, k, status]
    return damage         
               
def validPoss(idx, d, knight, crashed = []):
    q = deque()
    q.append([idx, knight])
    visited = [0] * N
    visited[crashed[0]] = 1

    while(q):
        curIdx, curKnight = q.popleft()
        r, c, h, w, k, status = curKnight
        nx = r + dx[d]
        ny = c + dy[d]
        topR = nx
        bottomR = nx + h
        leftC = ny
        rightC = ny + w
        
        for x in range(topR, bottomR):
            for y in range(leftC, rightC):
                if validPos(x, y) == False:
                    return False
                if board[x][y] == 2:
                    return False
                if knightsBoard[x][y] >= 0 and knightsBoard[x][y] != curIdx:
                    crashedIdx = knightsBoard[x][y]
                    if visited[crashedIdx] == 0:
                        visited[crashedIdx] = 1
                        crashKnight = knights[crashedIdx]
                        crashed.append(crashedIdx)
                        q.append([crashedIdx, crashKnight])
    return True

def validPos(x, y):
    return 0 <= x < L and 0 <= y < L               

init()

# for b in board:
#     print(b)
# print()
executeCommands()
s = 0
for i in range(N):
    if knights[i][5]:
        s += damages[i]
print(s)