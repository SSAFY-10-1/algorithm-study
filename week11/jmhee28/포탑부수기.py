import sys

import heapq
from collections import deque
import copy
dx = [0, 1, 0, -1, 1, 1, -1, -1] # 우하좌상
dy = [1, 0, -1, 0, 1, -1, 1, -1]

input = sys.stdin.readline
board = []

N, M = 0,0
attackPath = []
arrived = False
N, M, K = map(int, input().split())
attackTime =[[0] * M for _ in range(N)]

for i in range(N):
    board.append(list(map(int, input().split())))

def weakest(): # 가장 약한 포탑
    minBomb = sys.maxsize
    minPos = [] # 공격 시점, 행열 합, 열값

    for i in range(N):
        for j in range(M):
            if board[i][j] == 0:
                continue
            if board[i][j] < minBomb:
                minBomb = board[i][j]
                minPos = [[-attackTime[i][j], -(i + j), -j]]
            elif board[i][j] == minBomb:
                heapq.heappush(minPos, [-attackTime[i][j], -(i + j), -j])
    a, rc, c = minPos[0]
    c = -c
    r = -rc - c
    return [r, c]

def stongest(attacker):
    maxBomb = 0
    maxPos = []  # 공격 시점, 행열 합, 열값

    for i in range(N):
        for j in range(M):
            if board[i][j] == 0 or [i, j] == attacker:
                continue
            if board[i][j] > maxBomb:
                maxBomb = board[i][j]
                maxPos = [[attackTime[i][j], (i + j), j]]
            elif board[i][j] == maxBomb:
                heapq.heappush(maxPos, [attackTime[i][j], (i + j), j])
    if len(maxPos) > 0:
        a, rc, c = maxPos[0]
        r = rc - c
        return [r, c]
    else:
        return [-1, -1]


def bfs(x, y, attackee):
    global attackPath, arrived
    q = deque()
    q.append(([[x, y]], (x, y)))
    visited = [[0] * M for _ in range(N)]
    visited[x][y] = 1
    while q:
        path, (x, y) = q.popleft()
        if [x, y] == attackee:
            attackPath = path
            break
        for i in range(4):
            nx, ny = x + dx[i], y + dy[i]
            if nx >= N:
                nx = 0
            elif nx < 0:
                nx = N - 1
            if ny >= M:
                ny = 0
            elif ny < 0:
                ny = M - 1
            if board[nx][ny] == 0 or visited[nx][ny] == 1:
                continue
            visited[nx][ny] = 1
            newPath = copy.deepcopy(path) + [[nx, ny]]
            q.append((newPath, (nx, ny)))

def bombAttack(attacker, attackee, halfPower):
    global board, attackPath
    x, y = attackee
    for i in range(8):
        nx, ny = x + dx[i], y + dy[i]
        if nx >= N:
            nx = 0
        elif nx < 0:
            nx = N - 1
        if ny >= M:
            ny = 0
        elif ny < 0:
            ny = M - 1
        if [nx, ny] == attacker:
            continue
        attackPath.append([nx, ny])
        board[nx][ny] -= halfPower
        if board[nx][ny] < 0:
            board[nx][ny] = 0
def repair():
    global attackPath
    paths = set(list(map(tuple, attackPath)))
    for i in range(N):
        for j in range(M):
            if board[i][j] == 0:
                continue
            if (i, j) not in paths:
                board[i][j] += 1

def getAnswer():
    maxBomb = 0
    maxPos = []  # 공격 시점, 행열 합, 열값

    for i in range(N):
        for j in range(M):
            if board[i][j] == 0:
                continue
            if board[i][j] > maxBomb:
                maxBomb = board[i][j]
                maxPos = [[attackTime[i][j], (i + j), j]]
            elif board[i][j] == maxBomb:
                heapq.heappush(maxPos, [attackTime[i][j], (i + j), j])
    a, rc, c = maxPos[0]
    r = rc - c
    return board[r][c]

for k in range(1, K + 1):
    attackPath = []
    arrived = False
    # 공격 포탄, 공격받는 포탄 정하기
    attacker = weakest()
    attackee = stongest(attacker)
    wx, wy = attacker
    sx, sy = attackee

    if attackee != [-1, -1]:
        board[wx][wy] += (N + M)  # 핸디캡이 적용
        attackTime[wx][wy] = k  # 공격 시점 기록
        bfs(wx, wy, attackee)

        power = board[wx][wy]
        halfPower = power // 2

        board[sx][sy] -= power

        if board[sx][sy] < 0:
            board[sx][sy] = 0
        if len(attackPath) > 0:
            for path in attackPath:
                px, py = path
                if path == attacker:
                    continue
                if path != attackee:
                    board[px][py] -= halfPower
                    if board[px][py] < 0:
                        board[px][py] = 0
        else: # 포탄 공격
            attackPath.append(attackee)
            attackPath.append(attacker)
            bombAttack(attacker, attackee, halfPower)

        repair()
    else:
        break
print(getAnswer())