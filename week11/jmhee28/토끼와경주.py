# 토끼 우선 순위 :
# 총 점프 횟수가 적은 토끼, 현재 서있는 행 번호 + 열 번호가 작은 토끼, 행 번호가 작은 토끼, 열 번호가 작은 토끼, 고유번호가 작은 토끼

import sys
import heapq

input = sys.stdin.readline

dx = [-1, 0, 0, 1] # 상, 좌, 우, 하 | 격자를 벗어나게 된다면 방향을 반대로  3 - d
dy = [0, -1, 1, 0]

commands = []
rabbits = [] # 점프횟수, 행 번호 + 열 번호, 행 번호, 열 번호, 고유번호
scores = {}
distances = {} # 이동해야 하는 거리

ix, iy = 0, 0 # 초기 행, 열
N, M = 0, 0
P = 0 # 토끼 수
Q = int(input())
jumpScores = {} # 점프한 토끼, 점수

def inRange(x, y):
    return 1 <= x <= N and 1 <= y <= M

def jumpRabbit(rabbit):
    global scores, rabbits
    jumpCnt, rc, x, y, pid = rabbit
    distance = distances[pid]
    positions = []
    nx, ny = x, y
    # 상
    nx = (x - distance) % (2 * (N - 1))
    if nx >= N:
        nx = 2 * (N - 1) - nx
    heapq.heappush(positions, [-(nx + y), -nx, -y])

    # 하
    nx = (x + distance) % (2 * (N - 1))
    if nx >= N:
        nx = 2 * (N - 1) - nx
    heapq.heappush(positions, [-(nx + y), -nx, -y])

    # 좌
    ny = (y - distance) % (2 * (M-1))
    if ny >= M:
        ny = 2 * (M - 1) - ny
    heapq.heappush(positions,[-(x + ny), -x, -ny] )

    # 우
    ny = (y + distance) % (2 * (M - 1))
    if ny >= M:
        ny = 2 * (M - 1) - ny

    heapq.heappush(positions, [-(x + ny), -x, -ny])

    jumpCnt += 1
    r, c = -positions[0][1], -positions[0][2]
    score = -positions[0][0]

    jumpScores[pid] += (score+2)
    return [jumpCnt, r + c, r, c, pid]

def startRace(K, S):
    global rabbits, scores, jumpScores
    tempRabbit = []
    for k in range(K):
        pid_j = rabbits[0][4]
        rabbit = heapq.heappop(rabbits)
        newRabbit = jumpRabbit(rabbit)
        heapq.heappush(tempRabbit, [-newRabbit[1], -newRabbit[2], -newRabbit[3], -newRabbit[4]])
        heapq.heappush(rabbits, newRabbit)
        # S 더하기
    # K번 턴이 진행된 직후
    # 뽑혔던 적 있던 토끼 중 가장 우선순위가 높은 토끼

    selectedRabbit = tempRabbit[0]
    spid = -selectedRabbit[-1]
    scores[spid] += S

for q in range(Q):
    commandArr = list(map(int, input().split()))
    commands.append(commandArr)

for command in commands:
    if command[0] == 100: # 경주 시작 준비
        N, M = command[1], command[2]
        P = command[3]
        for i in range(4, len(command), 2):
            pid, distance = command[i], command[i + 1]
            scores[pid] = 0
            jumpScores[pid] = 0
            distances[pid] = distance
            heapq.heappush(rabbits, [0, ix + iy, ix, iy, pid])
    elif command[0] == 200: # 경주 진행
        K, S = command[1], command[2]
        startRace(K, S)
    elif command[0] == 300: # 이동거리 변경
        pid_t, L = command[1], command[2] # 고유번호가 pid t 인 토끼의 이동거리를 L배
        distances[pid_t] *= L
    else:
        for k, v in jumpScores.items():
            for sk, sv in scores.items():
                if sk != k:
                    scores[sk] += v
        sortedScores = sorted(scores.items(), key = lambda x: x[1] ,reverse=True)
        print(sortedScores[0][1])