from collections import deque

n, k = map(int, input().split())
A = deque(map(int, input().split()))
robot = deque([0] * n)
result = 0

def rotateBelt():
    global robot, A
    A.rotate(1)
    robot[-1] = 0
    
    robot.rotate(1)
    robot[-1] = 0
    
def rotateRobot():
    global robot, A
    for i in range(n-2, -1, -1):
        if robot[i+1] == 0 and robot[i] == 1 and A[i+1] > 0 :
            robot[i] = 0
            robot[i+1] = 1
            A[i+1] -= 1

while True:
    result += 1
    rotateBelt()
    rotateRobot()
    
    if robot[0] == 0 and A[0] > 0:
        robot[0] = 1
        A[0] -= 1
    if A.count(0) >= k:
        break
    
print(result)
