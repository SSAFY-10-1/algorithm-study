N, M, diceX, diceY, K = map(int, input().split())
DNUM = 6
board = []
commands = []

dice = [0] * DNUM

dx = [0, 0, 0, -1, 1] # 동서북남
dy = [0, 1, -1, 0, 0]

for i in range(N):
    board.append(list(map(int, input().split())))

commands = list(map(int, input().split()))

def rotateEast():
    global dice
    newDice = [0] * DNUM
    newDice[0] = dice[3]
    newDice[1] = dice[1]
    newDice[2] = dice[0]
    newDice[3] = dice[5]
    newDice[4] = dice[4]
    newDice[5] = dice[2]
    dice = newDice

def rotateWest():
    global dice
    newDice = [0] * DNUM
    newDice[0] = dice[2]
    newDice[1] = dice[1]
    newDice[2] = dice[5]
    newDice[3] = dice[0]
    newDice[4] = dice[4]
    newDice[5] = dice[3]
    dice = newDice

def rotateSouth():
    global dice
    newDice = [0] * DNUM
    newDice[0] = dice[1]
    newDice[1] = dice[5]
    newDice[2] = dice[2]
    newDice[3] = dice[3]
    newDice[4] = dice[0]
    newDice[5] = dice[4]
    dice = newDice

def rotateNorth():
    global dice
    newDice = [0] * DNUM
    newDice[0] = dice[4]
    newDice[1] = dice[0]
    newDice[2] = dice[2]
    newDice[3] = dice[3]
    newDice[4] = dice[5]
    newDice[5] = dice[1]
    dice = newDice


def inRange(x, y):
    return 0 <= x < N and 0 <= y < M

for command in commands:
    nx, ny = diceX + dx[command], diceY + dy[command]
    if not inRange(nx, ny):
        continue
    diceX, diceY = nx, ny
    if command == 1:
        rotateEast()
    elif command == 2:
        rotateWest()
    elif command == 3:
        rotateNorth()
    else:
        rotateSouth()
    if board[diceX][diceY] == 0:
        board[diceX][diceY] = dice[-1]
    else:
        dice[-1] = board[diceX][diceY]
        board[diceX][diceY] = 0
    print(dice[0])


