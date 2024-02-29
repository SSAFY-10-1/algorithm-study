package baekjoon;

import java.io.*;
import java.util.StringTokenizer;

public class BOJ20057 {

    static int N;
    static int[][] board;

    static int[] dx = {0,1,0,-1};
    static int[] dy = {-1,0,1,0};
    static boolean inRange(int x, int y) {
        return (0 <= x && x < N) && (0 <= y && y < N);
    }

    static int outOfSand;

    static int[][][] spreadFilter = {
            {
                    {0, 0, 2, 0, 0},
                    {0, 10, 7, 1, 0},
                    {5, 11, 1, 0, 0},
                    {0, 10, 7, 1, 0},
                    {0, 0, 2, 0, 0}
            },
            {
                    {0,0,0,0,0},
                    {0,1,0,1,0},
                    {2,7,1,7,2},
                    {0,10,11,10,0},
                    {0,0,5,0,0}
            },
            {
                    {0,0,2,0,0},
                    {0,1,7,10,0},
                    {0,0,1,11,5},
                    {0,1,7,10,0},
                    {0,0,2,0,0}
            },
            {
                    {0,0,5,0,0},
                    {0,10,11,10,0},
                    {2,7,1,7,2},
                    {0,1,0,1,0},
                    {0,0,0,0,0}
            }
    };

    public static void spread(int x, int y, int dir) {
        int sand = board[x][y];
        board[x][y] = 0;

        int ten = (int) (sand * (0.1));
        int seven = (int) (sand * (0.07));
        int five = (int) (sand * (0.05));
        int two = (int) (sand * (0.02));
        int one = (int) (sand * (0.01));

        int remainder = sand - (2*(ten + seven + two + one) + five);
        int[] hash = {0,one, two, 0, 0, five, 0, seven, 0, 0, ten, remainder};

        int stX = x - 2;
        int stY = y - 2;
        int[][] myFilter = spreadFilter[dir];

        for(int nx = stX; nx < stX + 5; nx++)
            for(int ny = stY; ny < stY + 5; ny++) {
                if(inRange(nx, ny))
                    board[nx][ny] += hash[myFilter[nx - stX][ny - stY]];
                else
                    outOfSand += hash[myFilter[nx - stX][ny - stY]];
            }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        N = Integer.parseInt(br.readLine());
        outOfSand = 0;

        board = new int[N][N];
        for(int i  = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++)
                board[i][j] = Integer.parseInt(st.nextToken());
        }

        int x = N / 2;
        int y = N / 2;
        int dir = 0;

        int cnt = 0;
        int dist = 1;

        outer : while (true) {
            cnt++; // 2번마다 dist+1 하기위한 flag
            // 이동
            for(int i = 0; i < dist; i++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];

                if(nx == 0 && ny == -1)
                    break outer;

                spread(nx, ny, dir);

                x = nx;
                y = ny;
            }

            dir = (dir + 1) % 4;
            if(cnt == 2) {
                cnt = 0;
                dist++;
            }
        }

        System.out.println(outOfSand);
    }
}