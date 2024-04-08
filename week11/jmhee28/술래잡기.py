import sys
input = sys.stdin.readline

dx = [-1, 0, 1, 0] # 상 우 하 좌
dy = [0, 1, 0, -1]
# 좌우로 움직이는 사람은 항상 오른쪽을 보고 시작 d = 1
# 상하로 움직이는 사람은 항상 아래쪽을 보고 시작 d = 2

N, M, H, K = map(int, input().split()) # N 은 홀수

runners = []
trees = []
Mid = N//2
catcher = [Mid, Mid]
catcherNext = [[0] * N for _ in range(N)]
catcherRevNext = [[0] * N for _ in range(N)]
cmoveNum = 1 # 술래가 움직일 횟수

cmoveFlag = True # mid에서 시작
CRANGE = 3
score = 0
oppositDirection = {
    1:3,
    2:0,
    3:1,
    0:2
}
treeBoard = [[0] * (N) for _ in range(N)]

for i in range(M): # 도망자의 위치, 방법
    x, y, d = map(int, input().split())
    runners.append([x-1, y-1, d, True])

for i in range(H): # 나무의 위치
    x, y = map(int, input().split())
    treeBoard[x-1][y-1] = 1

def inRange(x, y):
    return 0 <= x < N and 0 <= y < N
def calcDistance(pos1, pos2):
    return abs(pos1[0] - pos2[0]) + abs(pos1[1] - pos2[1])

def moveRunners(): # m명의 도망자가 먼저 동시
    global runners, catcher
    for i in range(M):
        x, y, d, status = runners[i]
        if not status:
            continue
        if calcDistance(runners[i], catcher) > 3:
            continue
        nx, ny = x + dx[d], y + dy[d]
        if not inRange(nx, ny):
            d = oppositDirection[d]
            nx, ny = x + dx[d], y + dy[d]
            if not (nx == catcher[0] and ny == catcher[1]):
                runners[i] = [nx, ny, d, status]
            else:
                runners[i] = [x, y, d, status]
        else:
            if not (nx == catcher[0] and ny == catcher[1]):
                runners[i] = [nx, ny, d, status]

def setCatcherNext():
    global catcherNext
    x, y, movedNum = Mid, Mid, 0
    cmoveNum = 1
    d = 0
    while x or y:
        for _ in range(cmoveNum):
            catcherNext[x][y] = d
            x, y = x + dx[d], y + dy[d]
            catcherRevNext[x][y] = d + 2 if d < 2 else d - 2
            if not x and not y:
                break
        d = (d + 1) % 4
        if d == 0 or d == 2:
            cmoveNum += 1

def checkRunners(x, y):
    global runners
    cnt = 0
    for i in range(M):
        rx, ry, d, status = runners[i]
        if not status: continue
        if [rx, ry] == [x, y]:
            cnt += 1
            runners[i] = [rx, ry, d, False]
    return cnt

setCatcherNext()
catchedTotal = 0
for k in range(1, K+1):
    if catchedTotal >= M:
        break
    catchedRunner = 0
    moveRunners()
    cx, cy = catcher
    cd = -1
    if cmoveFlag:
        cd = catcherNext[cx][cy]
    else:
        cd = catcherRevNext[cx][cy]
    cx, cy = cx + dx[cd], cy + dy[cd]

    if [cx, cy] == [0, 0]:
        cmoveFlag = False
    elif [cx, cy] == [Mid, Mid]:
        cmoveFlag = True
    catcher = [cx, cy]
    if cmoveFlag:
        cd = catcherNext[cx][cy]
    else:
        cd = catcherRevNext[cx][cy]

    for i in range(CRANGE): # 0, 1, 2

        nx, ny = cx + dx[cd] * i, cy + dy[cd] * i
        if not inRange(nx, ny):
            continue
        if treeBoard[nx][ny] == 1:
            continue
        catchedRunner += checkRunners(nx, ny)
    catchedTotal += catchedRunner
    score += k * catchedRunner

print(score)