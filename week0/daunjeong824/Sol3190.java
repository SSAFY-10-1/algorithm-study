package baekjoon;

import java.io.*;
import java.util.*;

// Python (nx, ny) in snake => 자바로는 iter 돌려서 일일히 비교해야함! contain 쓰니깐 틀렸음..

public class Sol3190 {

    static int N;
    static int[][] board;

    public static class Position {
        int x, y;

        public Position(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    static Queue<Position> snake;
    static int sx, sy, dir;
    static int[] dx = {-1,0,1,0};
    static int[] dy = {0,1,0,-1};

    public static boolean inRange(int x, int y) {
        return ((0 <= x && x < N) && (0 <= y && y < N));
    }

    static String[] command;
    static int[] timer;

    public static boolean move() {
        int nx = sx + dx[dir];
        int ny = sy + dy[dir];

        if(!inRange(nx , ny)) return false;
        for(Position pos : snake){
            if(pos.x == nx && pos.y == ny ) return false;
        }

        if(board[nx][ny] == 2)
            board[nx][ny] = 0;
        else {
            Position q = snake.poll();
        }
        snake.add(new Position(nx, ny));
        sx = nx;
        sy = ny;
        return true;
    }

    public static void rotate(String c) {
        if (c.equals("L")) {
            dir = dir - 1;
            if(dir == -1) dir = 3;
        }
        else {
            dir = dir + 1;
            if(dir == 4) dir = 0;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        board = new int[N][N];
        // 뱀 = 1, 사과 = 2
        board[0][0] = 1;
        dir = 1;

        int K = Integer.parseInt(br.readLine());

        for(int i = 0; i < K; i++){
            String[] t = br.readLine().split(" ");
            int ax = Integer.parseInt(t[0]);
            int ay = Integer.parseInt(t[1]);

            board[ax-1][ay-1] = 2;
        }

        int L = Integer.parseInt(br.readLine());
        command = new String[L];
        timer = new int[L];

        for(int i = 0; i < L; i++){
            String[] t = br.readLine().split(" ");
            timer[i] = Integer.parseInt(t[0]);
            command[i] = t[1];
        }

        snake = new LinkedList<Position>();
        snake.add(new Position(0,0));

        int timeCnt = 0;
        int time = 0;

        while(true) {
            if(timeCnt < command.length && timer[timeCnt] == time){
                String c = command[timeCnt];
                rotate(c);
                //System.out.println(c + dir);
                timeCnt++;
            }
            boolean isMove = move();
            time += 1;
            if (!isMove) break;
        }

        System.out.println(time);
    }
}
