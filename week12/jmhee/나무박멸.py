import sys
import copy
input = sys.stdin.readline

# n * n
dx = [-1, 1, 0, 0] # 상하좌우
dy = [0, 0, -1, 1]

kdx = [1, 1, -1, -1] # 4개의 대각선
kdy = [1, -1, 1, -1]

N, M, K, C = map(int, input().split()) # 격자의 크기 n, 박멸이 진행되는 년 수 m, 제초제의 확산 범위 k, 제초제가 남아있는 년 수 c
WALL = -1
board = [] # 나무, 벽: -1
killer = [[0]* N for _  in range(N)] # 제초제
answer = 0

for _ in range(N):
    board.append(list(map(int, input().split())))

def inRange(x, y):
    return 0 <= x < N and 0 <= y < N

def growTrees():
    global board
    for i in range(N):
        for j in range(N):
            if board[i][j] > 0:
                nearTree = 0
                for d in range(4):
                    nx, ny = i + dx[d], j + dy[d]
                    if inRange(nx, ny) and board[nx][ny] > 0:
                        nearTree += 1
                board[i][j] += nearTree
def breed():
    global board
    newBoard = copy.deepcopy(board)
    for i in range(N):
        for j in range(N):
            if board[i][j] > 0:
                breedPos = []
                for d in range(4):
                    nx, ny = i + dx[d], j + dy[d]
                    if not inRange(nx, ny) : continue
                    if killer[nx][ny] > 0 : continue
                    if not board[nx][ny] == 0: continue
                    breedPos.append([nx, ny])
                if len(breedPos) > 0:
                    breedAmount = board[i][j] // len(breedPos)
                    for pos in breedPos:
                        px, py = pos
                        newBoard[px][py] += breedAmount
    board = newBoard

def killPath(x, y):
    flags = [True, True, True, True]
    path = []
    treeCnt = board[x][y]
    path.append([x, y])
    for i in range(1, K+1):
        for j in range(4):
            nx, ny = x + kdx[j] * i, y + kdy[j] * i
            if not flags[j]: continue
            if not inRange(nx, ny):
                flags[j] = False
                continue
            if board[nx][ny] <= 0:
                path.append([nx, ny])
                flags[j] = False
                continue
            if board[nx][ny] > 0:
                path.append([nx, ny])
                treeCnt += board[nx][ny]
    return [treeCnt, path]

def spreadKiller():
    # 가장 많이 박멸되는 칸 구하기
    global killer, board, answer
    maxTreeCnt = 0
    maxTreePath = [[0, 0]]
    for i in range(N):
        for j in range(N):
            if board[i][j] > 0:
                treeCnt, path = killPath(i, j)
                if maxTreeCnt < treeCnt:
                    maxTreeCnt = treeCnt
                    maxTreePath = path

    answer += maxTreeCnt
    for pos in maxTreePath:
        kx, ky = pos
        killer[kx][ky] = C
        if board[kx][ky] != -1:
            board[kx][ky] = 0

def passKiller():
    global killer
    for i in range(N):
        for j in range(N):
            if killer[i][j] > 0:
                killer[i][j] -= 1


for m in range(M):
    growTrees()
    breed()
    passKiller()
    spreadKiller()
print(answer)