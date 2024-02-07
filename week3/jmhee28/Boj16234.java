import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Boj16234 {
    static int n, l, r;
    static int[] dx = {0, 0, 1, -1};
    static int[] dy = {1, -1, 0, 0};

    static int[][] map;
    static int answer;
//    static int[][] visited;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[] input = br.readLine().split(" ");
        n = Integer.parseInt(input[0]);
        l = Integer.parseInt(input[1]);
        r = Integer.parseInt(input[2]);
        map = new int[n][n];

        answer = 0;

        for (int i = 0; i < n; i++) {
            input = br.readLine().split(" ");
            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(input[j]);
            }
        }
        boolean again = true;
        while (again) {
            boolean flag = false;
            int[][] visited = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (visited[i][j] == 0) {
                        boolean repeat = bfs(i, j, visited);
                        if(repeat == true) {
                            flag = true;
                        }
                    }
                }
            }
            again = flag;
            if (flag){
                answer++;
            }
        }
        System.out.println(answer);
    }


    static boolean bfs(int x, int y, int[][] visited) {
        boolean again = false;
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{x, y});
        visited[x][y] = 1;
        int total= 0;
        int cnt = 0;
        Set<int[]> set = new HashSet<>();
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int cx = cur[0];
            int cy = cur[1];
            total += map[cx][cy];
            set.add(new int[]{cx, cy});
            cnt++;
            for (int i = 0; i < 4; i++) {
                int nx = cx + dx[i];
                int ny = cy + dy[i];

                if (nx < 0 || nx >= n || ny < 0 || ny >= n) continue;
                if (visited[nx][ny] != 0) continue;

                int diff = Math.abs(map[cx][cy] - map[nx][ny]);
                if (diff >= l && diff <= r) {
                    visited[nx][ny] = 1;
                    q.add(new int[]{nx, ny});
                }
            }
        }
        if (cnt > 1) {
            again = true;
            for (int[] pos : set) {
                map[pos[0]][pos[1]] = total / cnt;
            }
        }
        return again;
    }


}
