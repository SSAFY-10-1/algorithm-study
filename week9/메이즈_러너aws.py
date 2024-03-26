n, m, k = tuple(map(int, input().split()))
# 모든 벽들의 상태를 기록해줍니다.
board = [
    [0] * (n + 1)
    for _ in range(n + 1)
]

for i in range(1, n + 1):
    board[i] = [0] + list(map(int, input().split()))

# 회전의 구현을 편하게 하기 위해 2차원 배열을 하나 더 정의해줍니다.
next_board = [
    [0] * (n + 1)
    for _ in range(n + 1)
]

# 참가자의 위치 정보를 기록해줍니다.
traveler = [(-1, -1)] + [
    tuple(map(int, input().split()))
    for _ in range(m)
]

# 출구의 위치 정보를 기록해줍니다.
exits = tuple(map(int, input().split()))

# 정답(모든 참가자들의 이동 거리 합)을 기록해줍니다.
ans = 0

# 회전해야 하는 최소 정사각형을 찾아 기록해줍니다.
sx, sy, square_size = 0, 0, 0


# 모든 참가자를 이동시킵니다.
def move_all_traveler():
    global exits, ans

    # m명의 모든 참가자들에 대해 이동을 진행합니다.
    for i in range(1, m + 1):
        # 이미 출구에 있는 경우 스킵합니다.
        if traveler[i] == exits:
            continue
        
        tx, ty = traveler[i]
        ex, ey = exits

        # 행이 다른 경우 행을 이동시켜봅니다.
        if tx != ex:
            nx, ny = tx, ty

            if ex > nx: 
                nx += 1
            else:
                nx -= 1

            # 벽이 없다면 행을 이동시킬 수 있습니다.
            # 이 경우 행을 이동시키고 바로 다음 참가자로 넘어갑니다.
            if not board[nx][ny]:
                traveler[i] = (nx, ny)
                ans += 1
                continue

        # 열이 다른 경우 열을 이동시켜봅니다.
        if ty != ey:
            nx, ny = tx, ty

            if ey > ny: 
                ny += 1
            else:
                ny -= 1

            # 벽이 없다면 행을 이동시킬 수 있습니다.
            # 이 경우 열을 이동시킵니다.
            if not board[nx][ny]:
                traveler[i] = (nx, ny)
                ans += 1
                continue


# 한 명 이상의 참가자와 출구를 포함한 가장 작은 정사각형을 찾습니다.
def find_minimum_square():
    global exits, sx, sy, square_size
    ex, ey = exits

    # 가장 작은 정사각형부터 모든 정사각형을 만들어봅니다.
    for sz in range(2, n + 1):
        # 가장 좌상단 r 좌표가 작은 것부터 하나씩 만들어봅니다.
        for x1 in range(1, n - sz + 2):
            # 가장 좌상단 c 좌표가 작은 것부터 하나씩 만들어봅니다.
            for y1 in range(1, n - sz + 2):
                x2, y2 = x1 + sz - 1, y1 + sz - 1

                # 만약 출구가 해당 정사각형 안에 없다면 스킵합니다.
                if not (x1 <= ex and ex <= x2 and y1 <= ey and ey <= y2):
                    continue

                # 한 명 이상의 참가자가 해당 정사각형 안에 있는지 판단합니다.
                is_traveler_in = False
                for l in range(1, m + 1):
                    tx, ty = traveler[l]
                    if x1 <= tx and tx <= x2 and y1 <= ty and ty <= y2:
                        # 출구에 있는 참가자는 제외합니다.
                        if not (tx == ex and ty == ey):
                            is_traveler_in = True

                # 만약 한 명 이상의 참가자가 해당 정사각형 안에 있다면
                # sx, sy, square_size 정보를 갱신하고 종료합니다.
                if is_traveler_in:
                    sx = x1
                    sy = y1
                    square_size = sz

                    return


# 정사각형 내부의 벽을 회전시킵니다.
def rotate_square():
    # 우선 정사각형 안에 있는 벽들을 1 감소시킵니다.
    for x in range(sx, sx + square_size):
        for y in range(sy, sy + square_size):
            if board[x][y]: 
                board[x][y] -= 1

    # 정사각형을 시계방향으로 90' 회전합니다.
    for x in range(sx, sx + square_size):
        for y in range(sy, sy + square_size):
            # Step 1. (sx, sy)를 (0, 0)으로 옮겨주는 변환을 진행합니다. 
            ox, oy = x - sx, y - sy
            # Step 2. 변환된 상태에서는 회전 이후의 좌표가 (x, y) . (y, square_n - x - 1)가 됩니다.
            rx, ry = oy, square_size - ox - 1
            # Step 3. 다시 (sx, sy)를 더해줍니다.
            next_board[rx + sx][ry + sy] = board[x][y]

    # next_board 값을 현재 board에 옮겨줍니다.
    for x in range(sx, sx + square_size):
        for y in range(sy, sy + square_size):
            board[x][y] = next_board[x][y]


# 모든 참가자들 및 출구를 회전시킵니다.
def rotate_traveler_and_exit():
    global exits

    # m명의 참가자들을 모두 확인합니다.
    for i in range(1, m + 1):
        tx, ty = traveler[i]
        # 해당 참가자가 정사각형 안에 포함되어 있을 때에만 회전시킵니다.
        if sx <= tx and tx < sx + square_size and sy <= ty and ty < sy + square_size:
            # Step 1. (sx, sy)를 (0, 0)으로 옮겨주는 변환을 진행합니다. 
            ox, oy = tx - sx, ty - sy
            # Step 2. 변환된 상태에서는 회전 이후의 좌표가 (x, y) . (y, square_n - x - 1)가 됩니다.
            rx, ry = oy, square_size - ox - 1
            # Step 3. 다시 (sx, sy)를 더해줍니다.
            traveler[i] = (rx + sx, ry + sy)

    # 출구에도 회전을 진행합니다.
    ex, ey = exits
    if sx <= ex and ex < sx + square_size and sy <= ey and ey < sy + square_size:
        # Step 1. (sx, sy)를 (0, 0)으로 옮겨주는 변환을 진행합니다. 
        ox, oy = ex - sx, ey - sy
        # Step 2. 변환된 상태에서는 회전 이후의 좌표가 (x, y) . (y, square_n - x - 1)가 됩니다.
        rx, ry = oy, square_size - ox - 1
        # Step 3. 다시 (sx, sy)를 더해줍니다.
        exits = (rx + sx, ry + sy)


for _ in range(k):
    # 모든 참가자를 이동시킵니다.
    move_all_traveler()

    # 모든 사람이 출구로 탈출했는지 판단합니다.
    is_all_escaped = True
    for i in range(1, m + 1):
        if traveler[i] != exits:
            is_all_escaped = False

    # 만약 모든 사람이 출구로 탈출했으면 바로 종료합니다.
    if is_all_escaped: 
        break

    # 한 명 이상의 참가자와 출구를 포함한 가장 작은 정사각형을 찾습니다.
    find_minimum_square()

    # 정사각형 내부의 벽을 회전시킵니다.
    rotate_square()
    # 모든 참가자들 및 출구를 회전시킵니다.
    rotate_traveler_and_exit()

print(ans)

ex, ey = exits
print(ex, ey)