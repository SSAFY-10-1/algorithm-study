import tools 
print2DArray = tools.print2DArray
import sys
input = sys.stdin.readline
INT_MAX = sys.maxsize
# 상하좌우
dx = [-1, 1, 0, 0, 1, -1, 1, -1]
dy = [0, 0, -1, 1, 1, -1, -1, 1]
# K : 시간
###############입력########################
N, M, K = map(int, input().split())
maze = []


people = [] # 참가자 좌표 x, y
exitPos = []
secs = 0 # 시간 
movedDistances = [0] * N # 참가자 별 움직인 거리
exitedCnt = 0

for _ in range(N):
    maze.append(list(map(int, input().split())))

for i in range(M):
    x, y = map(int, input().split())
    people.append([x-1, y-1, True])
    
exitX, exitY = map(int, input().split())
exitX -=1
exitY -=1
maze[exitX][exitY] = -1
################################################
def validPos(x, y):
    global N
    return 0 <= x < N and 0 <= y < N

def getDistToExit(x, y):
    global exitX, exitY
    return abs(x-exitX) + abs(y - exitY)

# 참가자가 이동할 수 있는 위치
def getClosePos(x, y):
    global maze
    minDist = getDistToExit(x, y)
    minX = x
    minY = y
    for i in range(4):
        nx = x + dx[i]
        ny = y + dy[i]
        if validPos(nx, ny) and maze[nx][ny] <= 0:
            dist = getDistToExit(nx, ny)
            if dist < minDist:
                minX = nx
                minY = ny
                minDist = dist
    return [minX, minY]

#한 명 이상의 참가자와 출구를 포함한 가장 작은 정사각형을 잡습니다.
# 출구 죄표로부터 가장 가까운 참가자
def movePeople():
    global people, exitedCnt, exitX, exitY
    # print("beforeMove: ", people)
    for i in range(M): # i : idx
        x, y, status = people[i]
        if status == False:
            continue
        mx, my = getClosePos(x, y) # 움직일 자리
        if mx == x and my == y:
            continue
        board[x][y].remove(i)

        movedDistances[i] += 1
        if mx == exitX and my == exitY:
            people[i] = [mx, my, False]
            exitedCnt += 1
        else:
            people[i] = [mx, my, True]
            board[mx][my].append(i)
    # print("afterMove: ", people)       
def getRectangle():
    global people, exitedCnt, exitX, exitY
    length = 1
    person = 0
    x = exitX
    y = exitY
    # 죄상 기준 -1, -1
    leftTopX = -1
    leftTopY = -1
    while length <= N:
        if person > 0:
            break
        leftTopX = exitX + (length * -1)
        leftTopY = exitY + (length * -1)
        if leftTopY < 0: leftTopY = 0
        if leftTopX < 0: leftTopX = 0
        if leftTopY >= N: leftTopY = N-1
        if leftTopX >= N: leftTopX = N-1
        
        for i in range(leftTopX, leftTopX + length):
            for j in range(leftTopY, leftTopY + length):
                if 0 <= i < N and 0 <= j < N:
                    personYn = check(i, j, length)
                    if personYn: 
                        person += 1
                        return [i, j, length]
        length += 1
    return [-1, -1, length]  
                  
def check(x, y, length):
    global board, exitX, exitY
    eflag = False
    pflag = False
    for i in range(x, x + length):
            for j in range(y, y + length):
                if i == exitX and j == exitY:
                    eflag = True
                if validPos(i, j) and len(board[i][j]) > 0:
                    pflag = True
                if pflag and eflag:
                    return True
    return False                   

def decreaseMaze(x, y, length):
    global maze
    for i in range(x, x + length):
            for j in range(y, y + length):
                   if validPos(i, j) and maze[i][j] > 0:
                        maze[i][j] -= 1              

def rotateMaze(start_row, start_col, length):
    global N, maze, board, exitX, exitY
    newMaze = [[0] * N for _ in range(N)]
    newBoard = [[[] for row in range(N)] for depth in range(N)]
    for c in range(start_col, start_col + length):
        for r in range(start_row, start_row + length):
            newBoard[c - start_col][r - start_row] = board[r][c]
            newMaze[c - start_col][r - start_row] = maze[r][c]
            
    for r in range(start_row, start_row + length):
        sub_col = length - 1
        for c in range(start_col, start_col + length):
            maze[r][c] = newMaze[r -start_row][sub_col]
            board[r][c] = newBoard[r -start_row][sub_col]
            for p in board[r][c]:
                people[p][0]=r
                people[p][1]=c              
            if maze[r][c] == -1:
                exitX = r
                exitY = c    
            sub_col -= 1

# def rotateMaze():
    # pos, size = getRectaglePos()
    
###########################################      

distSum = 0
for i in range(K):
    print("time:", i+1)
    flag = True
    print("before")
    print(people)
    print(exitX, exitY)
    print2DArray(board)
    print2DArray(maze)
    # print("exitedCnt:", exitedCnt)
    
    movePeople() # 모든 참가자가 이동
    # print(people)
    flag = True
    for p in people:
        if p[2] == True:
            flag = False
            break
    if flag == True:
        break
    leftTopX, leftTopY, length = getRectangle()
    print("left: ", leftTopX, leftTopY, length)
    rotateMaze(leftTopX, leftTopY, length)
    decreaseMaze(leftTopX, leftTopY, length)
    print("after")
    print(people)
    print(exitX, exitY)
    print2DArray(board)
    print2DArray(maze)
    if flag == True:
        break
    
    
    
    
    # 미로 회전
    

print(sum(movedDistances))
print(exitX+1, exitY+1)