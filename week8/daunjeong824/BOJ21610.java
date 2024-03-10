package baekjoon;

import java.util.*;
import java.io.*;


public class BOJ21610 {

    static class Position {
        int x, y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Cloud {
        ArrayList<Position> posSet;

        public Cloud() {
            posSet = new ArrayList<>();
        }

        void add(Position p) {
            posSet.add(p);
        }

        void print() {
            StringBuilder sb = new StringBuilder();

            System.out.println("Cloud Pos ==");
            for(Position p : posSet) sb.append("( ").append(p.x).append(" : ").append(p.y).append(" )").append(" ");
            System.out.println(sb);
            System.out.println();
        }
    }

    static int N, M;
    static int[][] board;
    static int[][] command;

    static int[] dx = {0,-1,-1,-1, 0,1,1,1};
    static int[] dy = {-1,-1,0,1, 1,1,0,-1};

    static int[] rx = {-1,-1,1,1};
    static int[] ry = {-1,1,-1,1};

    public static boolean inRange(int x, int y) {
        return (0 <= x && x < N) && (0 <= y && y < N);
    }

    // Method
    public static void move(Cloud cloud, int dir, int range) {

        for(int cnt = 0; cnt < range; cnt++) {
            for(Position p : cloud.posSet) {

                int x = p.x;
                int y = p.y;

                if(x + dx[dir] == -1) x = N - 1;
                else if (x + dx[dir] == N) x = 0;
                else x = x + dx[dir];

                if(y + dy[dir] == -1) y = N - 1;
                else if (y + dy[dir] == N) y = 0;
                else y = y + dy[dir];

                p.x = x;
                p.y = y;
            }
        }

    }

    public static void rain(Cloud cloud) {
        for(Position p : cloud.posSet) {
            board[p.x][p.y] += 1;
        }
    }

    public static void copyWater(Cloud cloud) {
        for(Position p : cloud.posSet) {
            int cnt = 0;

            for(int i = 0; i < 4; i++) {
                int nx = p.x + rx[i];
                int ny = p.y + ry[i];

                if(inRange(nx, ny) && board[nx][ny] != 0)
                    cnt++;
            }
            board[p.x][p.y] += cnt;
        }
//        print();
    }

    public static Cloud makeCloud(Cloud cloud) {

        ArrayList<Position> posSet = new ArrayList<>();

        boolean[][] visited = new boolean[N][N];
        for(Position p : cloud.posSet)
            visited[p.x][p.y] = true;

        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++) {
                if(board[i][j] >= 2 && !visited[i][j]) {
                    posSet.add(new Position(i , j));
                    board[i][j] -= 2;
                }
            }

//        System.out.println("after making cloud");
//        print();

        Cloud newCloud = new Cloud();
        for(Position p : posSet)
            newCloud.add(p);

        return newCloud;
    }

    public static int calcWater() {
        int result = 0;
        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++) {
                result += board[i][j];
            }
        return result;
    }

    // Debug
    public static void print() {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++)
                sb.append(board[i][j]).append(" ");
            sb.append("\n");
        }
        System.out.println(sb);
    }


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        board = new int[N][N];
        command = new int[M][2];

        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++)
                board[i][j] = Integer.parseInt(st.nextToken());
        }

        for(int i = 0 ; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int d = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());

            command[i][0] = d-1;
            command[i][1] = s;
        }

        // Simulation
        Cloud cloud = new Cloud();
        Position[] inital = {new Position(N-1, 0), new Position(N-1, 1),
                             new Position(N-2, 0), new Position(N-2, 1)};
        for(Position p : inital)
            cloud.add(p);

        for(int[] c : command) {
            move(cloud, c[0], c[1]);
            rain(cloud);
            copyWater(cloud);

            cloud = makeCloud(cloud);
//            cloud.print();
        }

        System.out.println(calcWater());
    }
}
