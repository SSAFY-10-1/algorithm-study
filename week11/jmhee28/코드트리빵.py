import sys
from collections import deque

input = sys.stdin.readline
dx = [-1, 0, 0, 1] # 상 좌 우 하
dy = [0, -1, 1, 0]
INT_MAX = sys.maxsize

n, m = map(int, input().split())
board = [[0] * (n + 1) for _ in range(n + 1)]
cboard = [[0] * (n + 1) for _ in range(n + 1)]
baseCamp = [[0, 0, False]] # x, y, status - False: 빈 상태
cvs = [[0, 0, False]] # 편의점 x, y, status - False: 빈 상태
people = [[0, 0, False, sys.maxsize]] # x, y, status, 편의점까지의 거리 cdis - False: 편의점 도착 안한 상태
cvsArrived = 0 # 편의점에 도착한 사람 수
baseCnt = 0
baseOccupied = 0
available = [[0] * (n + 1) for _ in range(n + 1)] # 0: 가능 1: 불가능

for i in range(1, n + 1):
    inputArr = [0] + list(map(int, input().split()))
    for j in range(1, n+1):
        board[i][j] = inputArr[j]
        if board[i][j] == 1:
            baseCnt += 1
            board[i][j] = baseCnt
            baseCamp.append([i, j, False])

for i in range(1, m + 1):
    cx, cy = map(int, input().split())
    cvs.append([cx, cy, False])
    cboard[cx][cy] = i
    people.append([0, 0, False, sys.maxsize])

def inRange(x, y):
    return 1 <= x <= n and 1 <= y <= n

def makeDistance(cIdx, distances):
    visited = [[0] * (n + 1) for _ in range(n + 1)]
    cx, cy, cstatus = cvs[cIdx]
    q = deque()
    distances[cx][cy] = 0
    q.append((cx, cy))
    visited[cx][cy] = 1

    while q:
        x, y = q.popleft()

        for i in range(4):
            nx, ny = x + dx[i], y + dy[i]
            if inRange(nx, ny) and visited[nx][ny] == 0 and available[nx][ny] == 0:
                distances[nx][ny] = distances[x][y] + 1
                visited[nx][ny] = 1
                q.append((nx, ny))



def movePerson(idx):
    global cvsArrived, people
    minDist = sys.maxsize
    px, py, pstatus, cdis = people[idx]
    mx, my = px, py
    cx, cy, cstatus = cvs[idx]
    distances = [[INT_MAX for _ in range(n + 1)]  for _ in range(n + 1)]
    makeDistance(idx, distances)
    for d in range(4):
        nx, ny = px + dx[d], py + dy[d]
        if inRange(nx, ny):
            distance = distances[nx][ny]
            if distance < minDist:
                minDist = distance
                mx, my = nx, ny
    if mx == cx and my == cy: # 편의점에 도착
        pstatus = True
        cvsArrived += 1
    people[idx] = [mx, my, pstatus, distances[mx][my]]

def getMinCvsToBase(i, distances):
    baseIdx = 0
    minDist = sys.maxsize

    makeDistance(i, distances)
    for b in range(1, baseCnt+1):
        bx, by, bstatus = baseCamp[b]
        if bstatus:
            continue
        if distances[bx][by] < minDist:
            baseIdx = b
            minDist = distances[bx][by]
        elif distances[bx][by] == minDist:
            if baseCamp[baseIdx][0] > bx:
                baseIdx = b
                minDist = distances[bx][by]
            elif baseCamp[baseIdx][0] == bx and baseCamp[baseIdx][1] > by:
                baseIdx = b
                minDist = distances[bx][by]
    return baseIdx

T = 1
while cvsArrived < m:
    for i in range(1, m + 1): # 1번 행동
        px, py, pstatus, cdis = people[i]
        cx, cy, cstatus = cvs[i]
        if [px, py] == [0, 0] or pstatus: # 격자에 없음 or 편의점에 들어간 상태
            continue
        movePerson(i)

    # 2번 행동 사람들이 모두 이동 후, 편의점에 도착했다면 이동 불가
    for i in range(1, m + 1):
        px, py, pstatus, cdis = people[i]
        if pstatus:
            cvs[i][2] = True
            available[px][py] = 1 # 이동불가 체크

    if baseOccupied < baseCnt:  # 3번 행동
        for i in range(1, m + 1):
            if i > T:
                break
            px, py, pstatus, cdis = people[i]
            cx, cy, cstatus = cvs[i]
            if pstatus:
                continue
            distances = [[INT_MAX for _ in range(n + 1)]  for _ in range(n + 1)]

            baseIdx = getMinCvsToBase(i, distances)
            bx, by, bstatus = baseCamp[baseIdx]
            if cdis > distances[bx][by]:
                people[i] = [bx, by, pstatus, distances[bx][by]]

    for i in range(1, m + 1):
        px, py, pstatus, cdis = people[i]
        if pstatus:
            continue
        for k in range(1, baseCnt + 1):
            bx, by, bstatus = baseCamp[k]
            if px == bx and py == by:
                baseCamp[k] = [bx, by, True]
                baseOccupied += 1
                available[px][py] = 1 # 이동불가 체크
    T += 1
print(T - 1)