package baekjoon;

import java.util.*;
import java.io.*;

public class BOJ15683 {
    // DATA
    static int[][][] CCTV = {
            {},
            {{0,1},{0,-1},{-1,0},{1,0}}, // 1번 (x,y)
            {{0,1, 0,-1}, {1,0, -1,0}}, // 2번 (x,y, x2,y2)
            {{0,1, -1,0}, {-1,0, 0,-1}, {0,-1, 1,0}, {1,0, 0,1}}, // 3번 (x,y, x2,y2)
            {{0,1, -1,0, 0,-1}, {-1,0, 0,-1, 1,0}, {0,-1, 1,0, 0,1}, {1,0, 0,1, -1,0}}, // 4번, 3개
            {{0,1, 0,-1, 1,0, -1,0}} // 5번, 4개
    };

    static int N, M;
    static int[][] board;

    static ArrayList<Position> posList = new ArrayList<Position>();
    static int result = 64;

    public static boolean inRange(int x, int y) {
        return (0 <= x && x < N) && (0 <= y && y < M);
    }

    public static class Position {
        int x, y, cctv;

        public Position(int x, int y, int cctv) {
            this.x = x;
            this.y = y;
            this.cctv = cctv;
        }
    }
    // <--------------------- Method --------------------->

    public static int[][] paint(int[][] board, int x, int y, int[] cctvData) {
        int[][] newBoard = new int[N][M];
        for(int i = 0; i < N; i++)
            newBoard[i] = Arrays.copyOf(board[i], board[0].length);

        for(int i = 0; i < cctvData.length; i = i + 2) {
            int nx = x;
            int ny = y;

            int cx = cctvData[i];
            int cy = cctvData[i+1];

            while(inRange(nx + cx , ny + cy)) {
                if(newBoard[nx + cx][ny + cy] == 6)
                    break;

                newBoard[nx + cx][ny + cy] = -1;
                nx = nx + cx;
                ny = ny + cy;
            }
        }

        return newBoard;
    }

    public static void dfs(int depth, int[][] board) {
        if(depth == posList.size()) {
//          print(board);
            int cnt = 0;
            for(int i = 0; i < N; i++)
                for(int j = 0; j < M; j++) {
                    if(board[i][j] == 0) cnt++;
                }

            result = Math.min(result, cnt);
            return;
        }

        Position curPos = posList.get(depth);
        int cctvNum = curPos.cctv;
        int cctvX = curPos.x;
        int cctvY = curPos.y;

        for(int[] dir : CCTV[cctvNum]) {
            int[][] nextBoard = paint(board, cctvX, cctvY, dir);
            dfs(depth + 1, nextBoard);
        }
    }
    // Debug

    public static void print(int[][] board) {
        StringBuilder br = new StringBuilder();

        for(int i = 0; i < N; i++) {
            for(int j = 0; j < M; j++)
                br.append(board[i][j]).append(" ");
            br.append("\n");
        }

        System.out.println(br);
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        board = new int[N][M];
        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < M; j++) {
                int data = Integer.parseInt(st.nextToken());
                if(data > 0 && data < 6)
                    posList.add(new Position(i, j, data));

                board[i][j] = data;
            }
        }

        dfs(0, board);
        System.out.println(result);
    }
}
