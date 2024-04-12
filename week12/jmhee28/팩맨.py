import sys
input = sys.stdin.readline
import copy
# 한 턴
# 몬스터 복제 시도
# 몬스터 이동
# 팩맨 이동
# 몬스터 시체 소멸
# 몬스터 복제 완성

dx = [ -1, -1, 0, 1, 1, 1, 0, -1] #  ↑, ↖, ←, ↙, ↓, ↘, →, ↗
dy = [ 0, -1, -1, -1, 0, 1, 1, 1]

pdx = [ -1, 0, 1, 0] #  상-좌-하-우
pdy = [ 0, -1, 0, 1]

N = 4
M, T = map(int, input().split()) # 몬스터의 마리 수 m과 진행되는 턴의 수 t
px, py = map(int, input().split())  # 팩맨 위치
px, py = px-1, py-1
monsters = [] # x, y, d
board = [[{'body':[], 'monster':[], 'egg': []} for _ in range(N)] for _ in range(N)]
answer = 0
packPath = []
packScore = 0

for i in range(M):
    x, y, d = map(int, input().split())
    board[x-1][y-1]['monster'].append(d-1)
    monsters.append([x-1, y-1, d-1])

def duplicate():
    global board
    for i in range(4):
        for j in range(4):
            if len(board[i][j]['monster']) > 0:
                for d in board[i][j]['monster']:
                    board[i][j]['egg'].append(d) # 방향

def moveMonster(x, y, d):
    nx, ny = x, y
    sd = d
    flag = False
    for i in range(8):
        nx, ny = x + dx[d], y + dy[d]
        if 0 <= nx < 4 and 0 <= ny < 4 and len(board[nx][ny]['body']) == 0 and [nx, ny] != [px, py]:
            flag = True
            break
        else:
            d = (d + 1) % 8
    if flag:
        return [nx, ny, d]
    else:
        return [x, y, sd]

def moveMonsters():
    global board
    newBoard = [[{'body': [], 'monster': [], 'egg': []} for _ in range(N)] for _ in range(N)]
    for i in range(4):
        for j in range(4):
            if len(board[i][j]['monster']) > 0:
                for d in board[i][j]['monster']:
                    nx, ny, nd = moveMonster(i, j, d)
                    newBoard[nx][ny]['monster'].append(nd)
    for i in range(4):
        for j in range(4):
            board[i][j]['monster'] = newBoard[i][j]['monster']

def movePackman(x, y, mcnt, visited, path, mon):
    global packPath, packScore, px, py
    if mcnt >= 3:
        if packScore < mon:
            px, py = x, y
            packScore = mon
            packPath = path
        return

    for i in range(4):
        nx, ny = x + pdx[i], y + pdy[i]
        if not (0 <= nx < 4 and 0 <= ny < 4):
            continue
        # if visited[nx][ny] == 1: continue

        cmon = 0
        if visited[nx][ny] == 0:
            cmon = len(board[nx][ny]['monster'])
        visited[nx][ny] += 1
        path.append([nx, ny])
        movePackman(nx, ny, mcnt + 1, visited, copy.deepcopy(path), mon + cmon)
        visited[nx][ny] -= 1
        path.pop()

def killMonsters():
    global packPath, board
    visited = set()
    for path in packPath:
        x, y = path
        if (x, y) not in visited:
            visited.add((x, y))
            monsterCount = len(board[x][y]['monster'])
            for i in range(monsterCount):
                board[x][y]['body'].append(0)
            board[x][y]['monster'] = []

def removeBody():
    for i in range(4):
        for j in range(4):
            bodyCount = len(board[i][j]['body'])
            if bodyCount > 0:
                newBody = []
                for b in range(bodyCount):
                    bodyTurn = board[i][j]['body'][b]
                    if bodyTurn < 2:
                        newBody.append(bodyTurn + 1)
                board[i][j]['body'] = newBody

def awakeEgg():
    global board
    for i in range(4):
        for j in range(4):
            eggCount = len(board[i][j]['egg'])
            if eggCount > 0:
                board[i][j]['monster'] = board[i][j]['monster'] + copy.deepcopy(board[i][j]['egg'])
                board[i][j]['egg'] = []
def getAnswer():
    global answer
    for i in range(4):
        for j in range(4):
            answer += len(board[i][j]['monster'])
def printBoard():
    for i in range(4):
        for j in range(4):
            if [i, j] == [px, py]:
                print('*', end=' ')
            else:
                print(len(board[i][j]['monster']), end=' ')
        print()
    print('-------------------')
for t in range(T):
    packScore = -1
    packPath = []
    duplicate()
    moveMonsters()
    visited = [[0] * 4 for _ in range(4)]
    visited[px][py] = 1
    movePackman(px, py, 0, visited,[], 0)
    if packScore > 0:
        killMonsters()
    removeBody()
    awakeEgg()
    # printBoard()
getAnswer()
print(answer)