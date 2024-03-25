# https://www.codetree.ai/training-field/frequent-problems/problems/rudolph-rebellion/description?page=1&pageSize=20

import sys
input = sys.stdin.readline
INT_MAX = sys.maxsize
# 상우좌하 / 대각선 
dx = [-1, 0, 0, 1, 1, 1, -1, -1] 
dy = [0, 1, -1, 0, 1, -1, 1, -1]
# 반대방향 짝
dirPair = {
    0:3,
    3:0,
    1:2,
    2:1,
    4:7,
    7:4,
    5:6,
    6:5    
}
# 게임 진행 
# 1. 루돌프 움직임
# 2. 1 ~ P번 산타 움직임
#
# N: 게임판의 크기 (3≤N≤50)
# M: 게임 턴 수 (1≤M≤1000)
# P: 산타의 수 (1≤P≤30)
# C: 루돌프의 힘 (1≤C≤N)
# D: 산타의 힘 (1≤D≤N)



N, M, P, C, D = map(int, input().split())
rdpx, rdpy = map(int, input().split())
rdpPos = [rdpx - 1, rdpy - 1] # 루돌프 위치
board = [[0] * N for _ in range(N) ] # val:1 산타, 
santaInfos = [] # [[p, r, c, score, status]] status: 1: active, 0: 기절, -1: 탈락

for i in range(P):
    idx, x, y = list(map(int, input().split()))
    board[x-1][y-1] = 1
    santaInfos.append([idx, x-1, y-1, 0, 1])
   
def validPos(x,y):
    if 0 <= x < N and 0 <= y < N:
        return True
    return False

def calcDist(pos1, pos2):
    row = pos1[0] - pos2[0]
    col = pos1[1] - pos2[1]
    return row * row + col * col

def comparSantas(santa1, santa2):
    if len(santa1) == 0:
        return santa2
    if santa1[1] > santa2[1]:
        return santa1
    elif santa1[1] < santa2[1]:
        return santa2
    
    if santa1[2] > santa[2]:
        return santa1
    elif santa1[2] < santa2[2]:
        return santa2

def getCloseSanta():
    global santaInfos
    closeSanta = []
    minDist = INT_MAX
    for santa in santaInfos:
        spos = [santa[1], santa[2]]
        dist = calcDist(rdpPos, spos)
        if dist < minDist:
            minDist = dist
            closeSanta = santa
        elif dist == minDist:
            closeSanta = comparSantas(closeSanta, santa)
    return closeSanta

def moveRDP(closeSanta): # 루돌프 돌진
    global rdpPos
    rx, ry = rdpPos
    minDist = INT_MAX
    newRdp = []
    direction = 0
    for d in range(8):
        nx = rx + dx[d]
        ny = ry + dy[d]
        if validPos(nx, ny):
            dist = calcDist([nx, ny], closeSanta)
            if dist < minDist:
                minDist = dist
                direction = d
                newRdp = [nx, ny]
    return [newRdp, direction]

def moveSanta(santa): # 산타이동
    p, x, y, score, status = santa
    minDist = calcDist([x, y], rdpPos)
    newSanta = [p, x, y, score]
    direction = -1
    for d in range(4):
        nx = x + dx[d]
        ny = y + dy[d]
        if validPos(nx, ny):
            dist = calcDist([nx, ny], rdpPos)
            if dist < minDist and board[nx][ny] != 1:
                minDist = dist
                newSanta = [p, nx, ny, score]
                direction = d
    return [newSanta, direction]

def moveAllSantas():
    global santaInfos
    for santa in santaInfos:
        idx, x, y, score, status = santa        
        if status == -1 : # 탈락
            continue
        elif status == 0: # 기절상태
            status += 1
            continue
        board[x][y] = 0
        santa, direction = moveSanta(santa)
        if direction == -1:
            continue
        mx, my = santa[1], santa[2]        
        oppositeDir = dirPair[direction]
        if santa == rdpPos: ## 산타가 움직여서 충돌
            nx = dx[oppositeDir] * D
            ny = dy[oppositeDir] * D
            if validPos(nx, ny):                
                if board[nx][ny] > 0:
                    interaction(nx, ny, oppositeDir)
                board[nx][ny] = idx
                santa[1] = nx
                santa[2] = ny
                santa[3] += D        
                santa.status = 0        
            else:
                board[mx][my] = 0
                santa[-1] = -1
                
def interaction(bx, by, direction):
    global santaInfos
    santa = santaInfos[board[bx][by]]
    idx, sx, sy, score, status = santa
    nx = sx + dx[direction]
    ny = sy + dy[direction]
    if validPos(nx, ny) == False:
        santa[4] = -1
        return
    else:
        if board[nx][ny] == 0:
            board[nx][ny] = idx
        else:
            nextSanta = santaInfos[board[nx][ny]]
            board[nx][ny] = idx
            interaction(nextSanta[1], nextSanta[2], direction)
     
     
def printScores():
    for s in santaInfos:
        print(s[3], end=" ")
        
santaInfos.sort(key = lambda x: x[0]) 
for m in range(M):
    closeSanta = getCloseSanta()
    
    # 루돌프 움직임
    rdpPos, direction = moveRDP(closeSanta)    
    x, y = rdpPos
    if board[x][y] > 0:
        santa = board[x][y]
        idx, sx, sy, score, status = santa
        nx = x + dx[direction] * C
        ny = y + dy[direction] * C
        if validPos(nx, ny):
            if board[nx][ny] > 0:
                interaction(nx, ny, direction)
            board[nx][ny] = idx
            santa[1] = nx
            santa[2] = ny
            santa[3] += C    
            santa[4] = 0
        else:
            board[sx][sy] = 0
            santa[-1] = -1
            
                
    # 산타 움직임
    moveAllSantas()
 
printScores()               
        