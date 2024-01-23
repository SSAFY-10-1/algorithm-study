import java.util.*;

public class BOJ3190 {
    static int[] dx = {0, 1, 0, -1}; // right, down, left, up
    static int[] dy = {1, 0, -1, 0};
    static int N, K, L;
    static int[][] map;
    static Map<Integer, Character> snakeTurns = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        N = scanner.nextInt();
        K = scanner.nextInt(); // Number of apples
        map = new int[N][N];

        for (int i = 0; i < K; i++) {
            int r = scanner.nextInt();
            int c = scanner.nextInt();
            map[r - 1][c - 1] = 1; // Mark apples on the map
        }

        L = scanner.nextInt();
        for (int i = 0; i < L; i++) {
            int t = scanner.nextInt();
            char C = scanner.next().charAt(0);
            snakeTurns.put(t, C);
        }

        System.out.println(playGame());
    }

    private static int playGame() {
        int time = 0, x = 0, y = 0, currentDirection = 0, snakeLength = 1;
        Deque<int[]> snakePosition = new ArrayDeque<>();
        snakePosition.offerFirst(new int[]{0, 0});
        map[0][0] = 2;

        while (true) {
            if (snakeTurns.containsKey(time)) {
                currentDirection = turn(currentDirection, snakeTurns.get(time));
            }

            int nx = x + dx[currentDirection];
            int ny = y + dy[currentDirection];
            time++;

            // Check for game over conditions
            if (nx < 0 || ny < 0 || nx >= N || ny >= N || map[nx][ny] == 2) {
                return time;
            }

            if (map[nx][ny] == 1) { // Apple found
                snakeLength++;
            } else {
                int[] tail = snakePosition.pollLast();
                map[tail[0]][tail[1]] = 0;
            }

            snakePosition.offerFirst(new int[]{nx, ny});
            map[nx][ny] = 2;
            x = nx;
            y = ny;
        }
    }

    private static int turn(int direction, char C) {
        if (C == 'D') {
            return (direction + 1) % 4;
        } else {
            return (direction + 3) % 4;
        }
    }
}
