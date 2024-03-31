import sys
input = sys.stdin.readline

n, m, k = map(int, input().split())
#  ↑, →, ↓, ←
dx = [-1, 0, 1, 0]
dy = [0, 1, 0, -1]
pairDir = {
    0:2, 
    2:0,
    1:3,
    3:1
}
scores = [0] * (m+1)
board = [[-1] * (n + 1) for _ in range(n + 1)]
# x, y, d, s, gun
players = [[-1, -1, -1, -1. -1]] # x, y)는 플레이어의 위치, d는 방향, s는 플레이어의 초기 능력치

for i in range(1, n+1):
    inputArr = [-1] + list(map(int, input().split()))
    for j in range(1, n+1):
        if inputArr[j] > 0:
            board[i][j]= [inputArr[j]]  
        else:
            board[i][j]= []

for i in range(m):
    x, y, d, s = map(int, input().split())
    players.append([x, y, d, s, 0])

def inRange(x, y):
    return 0 < x <= n and 0 < y <= n

def checkPlayerExist(idx, cx, cy):
    global players, board
    for i in range(1, m+1):
        x, y, d, s, gun = players[i]
        if i != idx and x == cx and y == cy:
            return i
    return 0

def compete(idx, eidx):
    global players, board
    x, y, d, s, gun = players[idx]
    ex, ey, ed, es, egun = players[eidx]
    power, epower = s + gun, es + egun
    winIdx, loseIdx = -1, -1
    if power > epower:
        winIdx, loseIdx = idx, eidx                            
    elif power == epower:
        if s > es:
            winIdx, loseIdx = idx, eidx   
        else:
            winIdx, loseIdx  = eidx, idx
    else:
        winIdx, loseIdx  = eidx, idx
    return (winIdx, loseIdx)

def losePlayer(idx): # 2-2-2
    global players, board
    x, y, d, s, gun = players[idx]
    
    nx, ny = x, y
    for i in range(4):
        nx, ny = x + dx[d], y + dy[d]
        if inRange(nx, ny) and checkPlayerExist(idx, nx, ny) == 0:
            break        
        d = (d + 1) % 4 # 90 도 회전
    boardGuns = board[nx][ny]
    if len(boardGuns) > 0:
        boardGuns.sort()
        bgun = boardGuns[-1]
        players[idx] = [nx, ny, d, s, bgun]
        board[nx][ny].pop()   
    else:
        players[idx] = [nx, ny, d, s, gun]

def winPlayer(idx): # 2-2-3
    global players, board
    x, y, d, s, gun = players[idx]
    boardGuns = board[x][y]
    if len(boardGuns) > 0 :
        boardGuns.sort()
        bgun = boardGuns[-1]
        if bgun > gun:        
            players[idx] = [x, y, d, s, bgun]
            board[x][y].pop()       
            if gun > 0:
                board[x][y].append(gun)
        
def movePlayers():
    global players, scores, board
    for i in range(1, m+1):
        x, y, d, s, gun = players[i]
        nx, ny = x + dx[d], y + dy[d]
        if not inRange(nx, ny):
            d = pairDir[d] # 격자를 벗어나는 경우에는 정반대 방향으로 방향을 바꾸어서
            nx, ny = x + dx[d], y + dy[d]
            players[i][2] = d
        eplayerIdx = checkPlayerExist(i, nx, ny) # 이동한 방향의 플레이어
        
        if eplayerIdx == 0: # 2-1 이동한 방향에 플레이어가 없다면 
            boardGuns = board[nx][ny]
            if len(boardGuns) > 0 :
                boardGuns.sort()
                bgun = boardGuns[-1]
                if bgun > gun:                                         
                    board[nx][ny].pop()  
                    if gun > 0:
                        board[nx][ny].append(gun)
                    gun = bgun                 
            players[i] = [nx, ny, d, s, gun]
        else:  # 2-2-1 이동한 방향에 플레이어가 있다면
            winIdx, loseIdx = compete(i, eplayerIdx)
            ex, ey, ed, es, egun = players[eplayerIdx]
            power, epower = s + gun, es + egun
            scores[winIdx] += abs(power - epower)
            wx, wy, wd, ws, wgun = players[winIdx]
            lx, ly, ld, ls, lgun = players[loseIdx]
            # 2-2-2. 진 플레이어는 본인이 가지고 있는 총을 해당 격자에 내려놓고
            if lgun > 0:
                board[nx][ny].append(lgun)
            players[i][0], players[i][1] = nx, ny        
            players[loseIdx] = [nx, ny, ld, ls, 0]           
            losePlayer(loseIdx)            
            winPlayer(winIdx)
                    
                        
            
            
for i in range(k):
    movePlayers()

for i in range(1, m+1):
    print(scores[i], end= " ")