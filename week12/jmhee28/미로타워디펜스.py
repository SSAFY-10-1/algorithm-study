import sys
input = sys.stdin.readline

charD = ["우", "히","좌", "상"]
dx = [0, 1, 0, -1] # 우하좌상
dy = [1, 0, -1, 0]

N, M = map(int, input().split())
board = []
playerInfo = []
MID = N // 2
px, py = MID, MID # player 위치
answer = 0
boardMap = [[0] * N for _ in range(N)]

for i in range(N):
    inputArr = list(map(int, input().split()))
    board.append(inputArr)

for i in range(M):
    d, p = map(int, input().split())
    playerInfo.append([d, p]) # 공격 방향 d과 공격 칸 수 p

def initBoardMap():
    global boardMap
    d = 2
    x, y = px, py
    movedNum = 0
    targetMove = 1
    while [x, y] != [0, 0]:
        boardMap[x][y] = d
        x, y = x + dx[d], y + dy[d]
        movedNum += 1
        if movedNum >= targetMove:
            movedNum = 0
            d = d - 1
            if d < 0:
                d += 4
            if d == 0 or d == 2:
                targetMove += 1
    boardMap[0][0] = d

def inRange(x, y):
    return 0 <= x < N and 0 <= y < N

def attack(d, p): # 1. 몬스터를 공격
    global board, answer
    x, y = px, py
    for i in range(p):
        nx, ny = x + dx[d], y + dy[d]
        if not inRange(nx, ny): break
        answer += board[nx][ny]
        board[nx][ny] = 0
        x, y = nx, ny

def fillMonster(): # 2. 비어있는 공간만큼 몬스터는 앞으로 이동하여 빈 공간을 채웁니다.
    monsters = []
    x, y = px, py
    while inRange(x, y):
        d = boardMap[x][y]
        x, y = x + dx[d], y + dy[d]
        if board[x][y] > 0:
            monsters.append(board[x][y])
    return monsters


def removeMonsters(monsters): # 3번
    global answer
    while 1:
        newMonsters = []
        mcnt = len(monsters)
        check = [0] * mcnt
        i, j = 0, 0
        while i < mcnt - 1:
            curNum = monsters[i]
            cnt = 1
            end = i
            for j in range(i+1, mcnt):
                end = j
                if monsters[j] == curNum:
                    cnt += 1
                else:
                    break
            if cnt < 4:
                for k in range(i, end):
                    check[k] = 1
                    newMonsters.append(monsters[k])
            else:
                answer += (cnt * curNum)
            i = end

        if check[-1] == 0 and check[-2] == 1:
            check[-1] = 1
            newMonsters.append(monsters[-1])

        if len(newMonsters) == mcnt:
            break
        monsters = newMonsters
    return newMonsters
def refillMonsters(monsters): # 4번
    global board
    newMonsters = []
    mcnt = len(monsters)
    i, j = 0, 0
    while i < mcnt - 1:
        curNum = monsters[i]
        cnt = 1
        end = i
        for j in range(i+1, mcnt):
            end = j
            if monsters[j] == curNum:
                cnt += 1
            else:
                break
        newMonsters.append(cnt)
        newMonsters.append(curNum)

        i = end

    if monsters[-1] != monsters[-2]:
        newMonsters.append(1)
        newMonsters.append(monsters[-1])
    # print(newMonsters)
    x, y = px, py
    newBoard = [[0] * N for _ in range(N)]
    for i in range(len(newMonsters)):
        d = boardMap[x][y]
        x, y = x + dx[d], y + dy[d]
        if not inRange(x, y): break
        newBoard[x][y] = newMonsters[i]
    board = newBoard



initBoardMap()
for i in range(M):
    d, p = playerInfo[i]
    attack(d, p)
    monsters = fillMonster()
    monsters = removeMonsters(monsters)
    refillMonsters(monsters)
print(answer)