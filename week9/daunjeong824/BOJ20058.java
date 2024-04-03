package baekjoon;

import java.util.*;
import java.io.*;

public class BOJ20058 {

    static int N, leng, Q;
    static int[][] board;
    static boolean[][] visited;
    static int[] L_stage;

    static class Position {
        int x, y;
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static int[] dx = {-1,0,1,0};
    static int[] dy = {0,1,0,-1};
    public static boolean inRange(int x, int y) {
        return (0 <= x && x < leng) && (0 <= y && y < leng);
    }

    // Method (Simulation)

    public static void rotate(int stX, int stY, int R) {

        int[][] newBoard = new int[R][R];

        for(int i = stX; i < stX + R; i++)
            for(int j = stY; j < stY + R; j++) {
                int tx = i - stX;
                int ty = j - stY;

                int rx = R - ty - 1;
                int ry = tx;

                newBoard[i-stX][j-stY] = board[rx + stX][ry + stY];
            }

        for(int i = stX; i < stX + R; i++)
            for(int j = stY; j < stY + R; j++)
                board[i][j] = newBoard[i - stX][j - stY];
    }

    public static int[][] melt_down() {
        // 완탐 -> 인접 3개 미만 -> 멜트 다운
        int[][] newBoard = new int[leng][leng];

        for(int i = 0; i < leng; i++)
            for(int j = 0; j < leng; j++) {

                if(board[i][j] == 0) continue;

                int cnt = 0;
                for(int k = 0; k < 4; k++) {
                    if(inRange(i + dx[k], j + dy[k]) && board[i + dx[k]][j + dy[k]] != 0)
                        cnt++;
                }

                if(cnt < 3)
                    newBoard[i][j] = board[i][j] > 0 ? board[i][j] - 1 : 0;
                else
                    newBoard[i][j] = board[i][j];
            }
        return newBoard;
    }

    // Method (Score)
    // bfs -> 단, 1개는 제외한다.
    public static int bfs(int x, int y) {
        int cnt = 1;
        Queue<Position> queue = new ArrayDeque<>();
        queue.offer(new Position(x, y));
        visited[x][y] = true;

        while (!queue.isEmpty()) {
            Position curPos = queue.poll();

            for(int i = 0; i < 4; i++) {
                int nx = curPos.x + dx[i];
                int ny = curPos.y + dy[i];

                if(inRange(nx, ny) && board[nx][ny] > 0 && !visited[nx][ny]) {
                    cnt++;
                    visited[nx][ny] = true;
                    queue.offer(new Position(nx, ny));
                }
            }
        }
        return cnt;
    }

    // Debug
    public static void print() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < leng; i++) {
            for (int j = 0; j < leng; j++)
                sb.append(board[i][j]).append(" ");
            sb.append("\n");
        }
        System.out.println(sb);
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        leng = (int)Math.pow(2, N);
        Q = Integer.parseInt(st.nextToken());

        board = new int[leng][leng];
        visited = new boolean[leng][leng];

        for(int i = 0; i < leng; i++){
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < leng; j++)
                board[i][j] = Integer.parseInt(st.nextToken());
        }

        L_stage = new int[Q];
        st = new StringTokenizer(br.readLine());

        for(int i = 0; i < Q; i++)
            L_stage[i] = Integer.parseInt(st.nextToken());

        // Simulation
        for(int L : L_stage) {
            int Range = (int)Math.pow(2, L);

            // STEP 1. rotate Square
            for(int stX = 0; stX < leng; stX += Range)
                for(int stY = 0; stY < leng; stY += Range) {
                    rotate(stX, stY, Range);
                }

            // STEP 2. melt_down
            board = melt_down();
        }

        // get Score
        int sum_of_ice = 0;
        int maximum_ice = 0;

        for(int i = 0; i < leng; i++)
            for(int j = 0; j < leng; j++) {
                if(board[i][j] > 0) {
                    sum_of_ice += board[i][j];
                    if(!visited[i][j]) {
                        maximum_ice = Math.max(maximum_ice, bfs(i, j)); // 단, 1개는 제외한다.
                    }
                }
            }

        System.out.println(sum_of_ice);
        if(maximum_ice == 1) maximum_ice = 0;
        System.out.println(maximum_ice);
    }
}
