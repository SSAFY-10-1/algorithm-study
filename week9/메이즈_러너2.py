import tools 
print2DArray = tools.print2DArray
import sys
input = sys.stdin.readline
# K : 시간
###############입력########################
N, M, K = map(int, input().split())
maze = []
newMaze = [[0] * N for _ in range(N)]
people = [] # 참가자 좌표 x, y
exitPos = []
dist = 0 # 참가자 별 움직인 거리
exitedCnt = 0

for _ in range(N):
    maze.append(list(map(int, input().split())))
    
for i in range(M):
    x, y = map(int, input().split())
    people.append([x-1, y-1])
    
ex, ey = map(int, input().split())
exitPos = [ex - 1, ey - 1]

def movePeople():
    global exitPos, dist, people, maze
    exitX, exitY = exitPos
    for i in range(M):
        x, y = people[i]
        
        if people[i] == exitPos:
            continue
        
        if exitX != x:
            nx, ny = x, y
            if exitX > nx:
                nx += 1
            else:
                nx -= 1
            if not maze[nx][ny]:
                dist += 1
                people[i] = [nx, ny]
                continue
        
        if exitY != y:
            nx, ny = x, y
            if exitY > ny:
                ny += 1
            else:
                ny -= 1
            if not maze[nx][ny]:
                dist += 1
                people[i] = [nx, ny]
                continue

def getMinSquare():
    global exitPos, dist, people, maze
    
    for s in range(2, N):
        for x in range(0, N - s):
            for y in range(0, N - s):
                x2, y2 = x + s - 1, x + s - 1                
                if not (x <= exitPos[0] <= x2 and y <= exitPos[1] <= y2):
                    continue
                personFlag = False
                for i in range(M):
                    px, py = people[i]
                    if people[i] == exitPos:
                        continue
                    if x <= px <= x2 and y <= py <= y2:
                        personFlag = True
                if personFlag:
                    return [s, x, y]
                
def decreaseMaze(x, y, length):
    global maze
    for i in range(x, x + length):
            for j in range(y, y + length):
                   if maze[i][j] > 0:
                        maze[i][j] -= 1

def rotateMaze(sx, sy, s):
    global exitPos, dist, people, maze, newMaze
    for x in range(sx, sx + s):
        for y in range(sy, sy + s):
            ox, oy = x - sx, y - sy
            rx, ry = oy, s - ox -1
            newMaze[rx + sx][ry + sy] = maze[x][y]
    
    for x in range(sx, sx + s):
        for y in range(sy, sy + s):
            maze[x][y] = newMaze[x][y]    
            
    for i in range(M):
        px, py = people[i]
        if sx <= px < sx + s  and sy <= py <  sy + s:
            ox, oy = px - sx, py - sy
            rx, ry = oy, s - ox - 1
            people[i] = [rx + sx, ry + sy] 
                       
    exitX, exitY = exitPos        
    if sx <= exitX < sx + s  and sy <= exitY < sy + s:
        ox, oy = exitX - sx, exitY - sy
        rx, ry = oy, s - ox - 1
        exitPos = [rx + sx, ry + sy]
    
            
print2DArray(maze)                                                  
for sec in range(K):
    movePeople()
    # 가장 작은 사각형 좌표, 길이 구하기
    s, sx, sy = getMinSquare()
    # 사각형 내구도 깎기
    decreaseMaze(sx, sy, s)
    # 회전 - maze, people, 출구
    rotateMaze(sx, sy, s)
    allEscaped = True
    for i in range(M):
        if people[i] != exitPos:
            allEscaped = False
    print("time:", sec+1)
    print(s)
    print(exitPos[0], exitPos[1])
    print2DArray(maze)
    if allEscaped:
        break
    
print(dist)
print(exitPos[0] + 1, exitPos[1] + 1)