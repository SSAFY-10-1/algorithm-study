import sys
from collections import deque
import copy

input = sys.stdin.readline

N, M, K = map(int, input().split())  # 격자의 크기 n, 팀의 개수 m, 라운드 수 k
board = []  # n * n, 0은 빈칸, 1은 머리사람, 2는 머리사람과 꼬리사람이 아닌 나머지, 3은 꼬리사람, 4는 이동 선, 3 ≤ n ≤ 20
HEAD, TAIL = 1, 3

tc = 0
initialHeads = []
moveLines = []
headToTails = []
teamInfos = []  # 사람수
dx = [0, 1, 0, -1]  # 우 하 좌 상
dy = [1, 0, -1, 0]
tIdx = 1
teamBoard = [[0] * N for _ in range(N)]
dchr = ["우", "하", "좌", "상"]

for i in range(N):
    arr = list(map(int, input().split()))
    board.append(arr)
    for j in range(N):
        if arr[j] == 1:
            initialHeads.append([i, j])


def inRange(x, y):
    return 0 <= x < N and 0 <= y < N

def getInitialDir(head):
    hx, hy = head
    d = 0
    for i in range(4):
        nx, ny = hx + dx[i], hy + dy[i]
        if inRange(nx, ny) and board[nx][ny] == 2:
            d = i
            break
    return d

def dfs(x, y, tidx, headToTail):
    global  headToTails
    if [x, y] == initialHeads[tidx]:
        return

def teamPaths(head):
    global teamBoard, headToTails
    hx, hy = head
    d = getInitialDir(head)
    x, y = hx, hy
    headToTail = deque()
    while 1:
        if 1 <= board[x][y] <= 4:
            teamBoard[x][y] = tIdx
            headToTail.append([x, y])
        else:
            break
        nx, ny = x + dx[d], y + dy[d]
        if not inRange(nx, ny) or not 1 <= board[nx][ny] <= 4:
            for i in range(4):
                d = (d + 1) % 4
                nx, ny = x + dx[d], y + dy[d]
                if inRange(nx, ny) and 1 <= board[nx][ny] <= 4 and teamBoard[nx][ny] == 0:
                    break
        x, y = nx, ny
        if not inRange(x, y):
            break
        if [x, y] == head:
            break
    nextHead = headToTail[1]
    nhx, nhy = nextHead
    h = headToTail.popleft()
    if board[nhx][nhy] != 2:
        headToTail.reverse()
    headToTail.appendleft(h)
    headToTails.append(headToTail)
    pcnt = 0
    for pos in headToTail:
        px, py = pos
        if 1 <= board[px][py] <= 4:
            pcnt += 1
        if board[px][py] == 3:
            break
    teamInfos.append(pcnt)


for head in initialHeads:
    teamPaths(head)
    tIdx += 1


def moveToHead():
    global headToTails, board
    for i in range(M):
        headToTails[i].rotate(1)
        for j in range(len(headToTails[i])):
            x, y = headToTails[i][j]
            if j == 0:
                board[x][y] = 1
            elif j < teamInfos[i] - 1:
                board[x][y] = 2
            elif j == teamInfos[i] - 1:
                board[x][y] = 3
            else:
                board[x][y] = 4
def throwBall(k):
    global headToTails, board
    score  = 0
    ck = k % (4 * N)
    sx, sy = 0,0
    d = 0 # 우 하 좌 상
    # case 1 round 0 < k <= n => 행 : k - 1, 열 : 0
    # case 2 round n < k <= 2n => k = k - n | 행: n-1 열: K
    # case 3 round 2n < k <= 3n => k = k - 2n | 행: n - k - 1, 열 n - 1
    # case 3 round 3n < k <= 4n => k = k - 3n | 행: 0, 열 n - k - 1
    if ck == 0:
        d = 1
        sx, sy = 0, 0
    elif 0 < ck <= N:
        d = 0
        sx, sy = ck - 1, 0
    elif N < ck <= 2 * N:
        d = 3
        ck -= N
        sx, sy = N - 1, ck - 1
    elif 2 * N < ck <= 3 * N:
        d = 2
        ck -= 2*N
        sx, sy = N - ck, N - 1
    elif 3 * N < ck <= 4 * N:
        d = 1
        ck -= 3*N
        sx, sy = 0, N - ck
    # print(sx, sy, dchr[d])
    x, y = sx, sy
    flag = False
    teamIdx = -1
    pIdx =  -1
    for i in range(N): #  최초에 만나게 되는 사람만이 공을 얻게 되어 점수
        if 1 <= board[x][y] <= 3:
            teamIdx = teamBoard[x][y] - 1
            for j in range(len(headToTails[teamIdx])):
                if headToTails[teamIdx][j] == [x, y]:
                    pIdx = j + 1
                    flag = True
                    break
        if flag:
            break
        x, y = x + dx[d], y + dy[d]

    # 머리 바꾸기
    if flag:
        score += pIdx ** 2
        pcnt = teamInfos[teamIdx]
        tcnt = len(headToTails[teamIdx])
        newHTT = deque()
        t = pcnt - 1
        c = 0
        while c < tcnt:
            newHTT.append(headToTails[teamIdx][t])
            t -= 1
            c += 1
        headToTails[teamIdx] = newHTT
        hx, hy = headToTails[teamIdx][0]
        tx, ty = headToTails[teamIdx][pcnt-1]
        board[hx][hy] = 1
        board[tx][ty] = 3
    return score

score = 0
for k in range(1, K+1):
    moveToHead()
    score += throwBall(k)

print(score)
