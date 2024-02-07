package baekjoon;

import java.util.*;
import java.io.*;

public class BOJ16234 {

    static int N;
    static int L, R;
    static int[][] graph;
    static int[][] newGraph;
    static boolean[][] visited;

    public static class Position {
        int x, y;
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static int[] dx = {-1,0,1,0};
    static int[] dy = {0,1,0,-1};

    public static boolean inRange(int x, int y){
        return ((0 <= x && x < N) && (0 <= y && y < N));
    }

    public static boolean bfs(int stX, int stY) {
        Queue<Position> queue = new ArrayDeque<Position>();
        ArrayList<Position> posSet = new ArrayList<Position>();

        Position stP = new Position(stX, stY);

        queue.add(stP);
        posSet.add(stP);

        visited[stX][stY] = true;

        int people = graph[stX][stY];
        int cnt = 1;
        newGraph[stX][stY] = people;

        while (!queue.isEmpty()) {
            Position curPos = queue.poll();

            for(int i = 0; i < 4; i++) {
                int nx = curPos.x + dx[i];
                int ny = curPos.y + dy[i];

                if(inRange(nx, ny) && !visited[nx][ny]) {
                    int chai = Math.abs( graph[curPos.x][curPos.y] - graph[nx][ny] );

                    if(L <= chai && chai <= R){
                        Position nP = new Position(nx, ny);

                        queue.add(nP);
                        posSet.add(nP);

                        visited[nx][ny] = true;
                        people += graph[nx][ny];
                        cnt++;
                    }
                }
            }
        }

        if(posSet.size() > 1) {
            int newPeople = people / cnt;
            for(Position P : posSet) {
//                System.out.println(P.x + " : "+ P.y + " = " + newPeople);
                newGraph[P.x][P.y] = newPeople;
            }

            return true;
        }

        return false;
    }

    public static void debug() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                sb.append(graph[i][j]).append(" ");
            sb.append("\n");
        }
        sb.append("\n");
        System.out.println(sb);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        L = Integer.parseInt(st.nextToken());
        R = Integer.parseInt(st.nextToken());

        graph = new int[N][N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++)
                graph[i][j] = Integer.parseInt(st.nextToken());
        }

        int res = 0;

        while(true) {
            boolean flag = false;
            visited = new boolean[N][N];
            newGraph = new int[N][N];

            for(int i = 0; i < N; i++)
                for(int j = 0; j < N; j++) {
                    if(!visited[i][j])
                        if(bfs(i, j) && !flag)
                            flag = true;
                    }

            if(!flag) break;

            for(int i = 0; i < N; i++)
                graph[i] = Arrays.copyOf(newGraph[i], N);

//            debug();
            res++;
        }

        System.out.println(res);
    }
}