
import java.util.*;
import java.io.*;

public class DeleteTree {
    // N = 격자 크기, M = 박멸 진행되는 수, K = 제초제 확산 범위, C = 제초제가 남아있는 년 수
    static int N, M, K, C;
    // Graph = 나무를 담을 2차원 격자
    static int[][] graph;
    // treeKillerMaps = 제초제가 남아있음을 표시하는 2차원 격자
    static int[][] treeKillerMaps;
    // M년 동안 박멸한 나무의 수를 저장하는 변수 = 정답을 출력 하는 변수
    static int result = 0;

    // dx & dy + inRange Func
    static int[] dx = {-1,0,1,0};
    static int[] dy = {0,1,0,-1};
    public static boolean inRange(int x, int y) {
        return ((0 <= x && x < N) && (0 <= y && y < N));
    }

    public static class Position {
        int x, y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // <--------------- 구현 해야 할 메소드 ---------------->
    public static void growTree() {
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                if (graph[x][y] > 0) {
                    int treeToAdd = 0;

                    for (int d = 0; d < 4; d++) {
                        int nx = x + dx[d];
                        int ny = y + dy[d];

                        if (inRange(nx, ny) && graph[nx][ny] > 0) {
                            treeToAdd++;
                        }
                    }

                    graph[x][y] += treeToAdd;
                }
            }
        }
    }

    public static int[][] spreadTree() {
        return new int[N][N];
    }

    // (st_x, st_y)를 시작으로 제초제를 뿌려보는 함수
    public List<Position> treeKiller(int st_x, int st_y) {

        return new ArrayList<Position>();
    }

    public static void compareTreeKiller() {

    }
    // <--------------- 구현 해야 할 메소드 ---------------->

    public static void main(String[] args) throws IOException {
        // STEP 1. INPUT
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());

        graph = new int[N][N];
        treeKillerMaps = new int[N][N];

        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++)
                graph[i][j] = Integer.parseInt(st.nextToken());
        }

        // STEP 2. Simulation
        for(int year = 0; year < M; year++) {
            growTree();
            graph = spreadTree();
            compareTreeKiller();
        }

        System.out.println(result);
    }
}
