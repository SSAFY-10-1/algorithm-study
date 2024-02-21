import sys
import copy
input = sys.stdin.readline

N, M = map(int, input().split())
room = []
cctvlocs = []

for i in range(N):
    inputArr = list(map(int, input().split()))
    room.append(inputArr)
    for j in range(M):
        if 1 <= inputArr[j] <= 5:
            cctvlocs.append((inputArr[j], i, j))
totalCctv = len(cctvlocs)    

dx = [1, -1, 0, 0] 
dy = [0, 0, -1, 1] 

right = 3
left = 2
up = 1
down = 0
answer = int(1e9)
answerRoom = copy.deepcopy(room)
cctvs = {
    1: [[right], [left], [up], [down]],
    2: [[right, left], [up, down]],
    3: [[up, right], [down, right], [left, down], [up, left]],
    4: [[up, right, left], [up, right,down], [down, right,left], [down, up,left]],
    5: [[up, right, left, down]]
}


def expandCCTV(x, y, dir, curRoom):
    nx = x + dx[dir]
    ny = y + dy[dir]
    cnt = 0
    while 0 <= nx < N and 0 <= ny < M:
        if curRoom[nx][ny] == 6:
            break
        elif curRoom[nx][ny] == 0:
            curRoom[nx][ny] = 7
            cnt+= 1
        nx += dx[dir]
        ny += dy[dir]
    return cnt
    

def dfs(depth, curRoom):
    global answer, answerRoom, totalCctv
    if depth == totalCctv:
        curCnt = countInvisible(curRoom)
        if curCnt < answer:
            answer = curCnt
            answerRoom = curRoom
        return
    cctvNum, i, j = cctvlocs[depth]
    directions = cctvs[cctvNum]
    for direction in directions:
        expandedRoom = copy.deepcopy(curRoom)
        for d in direction:
            expandCCTV(i, j, d, expandedRoom)
        dfs(depth+1,  expandedRoom)
                    
     
def countInvisible(curRoom):
    cnt = 0
    for i in range(N):
        for j in range(M):
            if curRoom[i][j] == 0:  
                cnt += 1
    return cnt
                
    
def solve(curRoom):
    global answer, answerRoom
    dfs(0,curRoom)
    print(answer)
    # for r in answerRoom:
    #     print(r)
    return answer

solve(room)