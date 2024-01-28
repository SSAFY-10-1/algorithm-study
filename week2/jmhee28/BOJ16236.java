import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {

    static final int MAX_INT = 21;
    static final int MAX_VAL = 401;
    static int n, result;
    static int[][] a = new int[MAX_INT][MAX_INT];
    static int[][] check = new int[MAX_INT][MAX_INT];
    static int shark_x, shark_y, eat_cnt, shark_size = 2;
    static int min_dist, min_x, min_y;

    static int[] dx = {0, 0, 1, -1};
    static int[] dy = {-1, 1, 0, 0};

    static class Info {
        int x, y;

        Info(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static void initCheck() {
        min_dist = MAX_VAL;
        min_x = MAX_INT;
        min_y = MAX_INT;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                check[i][j] = -1;
            }
        }
    }

    static void bfs(int x, int y) {
        Queue<Info> q = new LinkedList<>();
        check[x][y] = 0;
        q.add(new Info(x, y));

        while (!q.isEmpty()) {
            Info cur = q.poll();
            int cx = cur.x;
            int cy = cur.y;

            for (int i = 0; i < 4; i++) {
                int nx = cx + dx[i];
                int ny = cy + dy[i];

                if (nx < 1 || nx > n || ny < 1 || ny > n) continue;
                if (check[nx][ny] != -1 || a[nx][ny] > shark_size) continue;

                check[nx][ny] = check[cx][cy] + 1;

                if (a[nx][ny] != 0 && a[nx][ny] < shark_size) {
                    if (min_dist > check[nx][ny]) {
                        min_x = nx;
                        min_y = ny;
                        min_dist = check[nx][ny];
                    } else if (min_dist == check[nx][ny]) {
                        if (min_x == nx) {
                            if (min_y > ny) {
                                min_x = nx;
                                min_y = ny;
                            }
                        } else if (min_x > nx) {
                            min_x = nx;
                            min_y = ny;
                        }
                    }
                }

                q.add(new Info(nx, ny));
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                a[i][j] = scanner.nextInt();
                if (a[i][j] == 9) {
                    shark_x = i;
                    shark_y = j;
                    a[i][j] = 0;
                }
            }
        }

        while (true) {
            initCheck();
            bfs(shark_x, shark_y);

            if (min_x != MAX_INT && min_y != MAX_INT) {
                result += check[min_x][min_y];
                eat_cnt++;

                if (eat_cnt == shark_size) {
                    shark_size++;
                    eat_cnt = 0;
                }

                a[min_x][min_y] = 0;
                shark_x = min_x;
                shark_y = min_y;
            } else {
                break;
            }
        }

        System.out.println(result);
        scanner.close();
    }
}
