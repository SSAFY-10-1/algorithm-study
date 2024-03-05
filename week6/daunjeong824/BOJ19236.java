package baekjoon;

import java.util.*;
import java.io.*;

public class BOJ19236 {
    // DATA
    static class Fish {
        int dir;
        int x, y;
        boolean isActive;

        public Fish(int x, int y, int dir, boolean isActive) {
            this.x = x;
            this.y = y;
            this.dir = dir;
            // 왜 물고기 만드는데 무조건 살아있다고 함?
            this.isActive = isActive;
        }
    }

    static class Shark {
        int x, y, dir, eat;

        public Shark(int x, int y, int dir, int eat) {
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.eat = eat;
        }
    }

    static int[][] initalBoard;
    static Fish[] initalFishList;
    static int result;
    // 방향을 45도 반시계 회전
    static int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
    static int[] dy = {0, -1, -1, -1, 0, 1, 1, 1};

    public static boolean inRange(int x, int y) {
        return (0 <= x && x < 4) && (0 <= y && y < 4);
    }

    // <----------- Method -------------->

    public static void fishMove(int[][] board, Fish[] fishList, int fishCnt) {
        if(!fishList[fishCnt].isActive) return;

        Fish curFish = fishList[fishCnt];
        int x = curFish.x;
        int y = curFish.y;

        for(int i = 0; i < 8; i++) {

            int dir = (curFish.dir + i) % 8;

            if(inRange(x + dx[dir], y + dy[dir]) && board[x + dx[dir]][y + dy[dir]] > -1) {

                board[x][y] = 0;

                if(board[x + dx[dir]][y + dy[dir]] == 0) {
                    fishList[fishCnt].x = x + dx[dir];
                    fishList[fishCnt].y = y + dy[dir];
                } else {
                    Fish temp = fishList[ board[x + dx[dir]][y + dy[dir]] ];
                    temp.x = x;
                    temp.y = y;
                    board[x][y] = board[x + dx[dir]][y + dy[dir]];

                    fishList[fishCnt].x = x + dx[dir];
                    fishList[fishCnt].y = y + dy[dir];
                }

                board[x + dx[dir]][y + dy[dir]] = fishCnt;
                fishList[fishCnt].dir = dir;
                return;
            }
        }
    }

    public static void simulation(int depth, int[][] board, Fish[] fishList, Shark shark) {

//		System.out.println(depth + " 초기 상태");
//		print(board);
//		System.out.println();
        if(result < shark.eat)
            result = shark.eat;

        for(int i = 1; i <= 16; i++)
            fishMove(board, fishList, i);

//		System.out.println(depth + " 물고기 움직인 후");
//		print(board);
//		System.out.println();


        // 반복 -> 물고기 있다면, 해당 물고기 null -> 갱신한 newFishList & 상어위치 옮긴 newBoard -> 재귀
        for(int dist = 1; dist < 4; dist++) {
            int nx = shark.x + dx[shark.dir] * dist;
            int ny = shark.y + dy[shark.dir] * dist;

            // 해당 위치에 물고기가 존재한다면
            if(inRange(nx, ny) && board[nx][ny] > 0) {

                int[][] newBoard = new int[4][4];
                for(int i = 0; i < 4; i++)
                    newBoard[i] = Arrays.copyOf(board[i], 4);
                Fish[] newFishList = new Fish[17];
                for(int i = 1; i <= 16; i++)
                    newFishList[i] = new Fish(fishList[i].x, fishList[i].y, fishList[i].dir, fishList[i].isActive);

                newBoard[shark.x][shark.y] = 0;
                Fish getFish = newFishList[newBoard[nx][ny]];

                Shark newShark = new Shark(getFish.x, getFish.y, getFish.dir, shark.eat + newBoard[nx][ny]);
                getFish.isActive = false;
                newBoard[getFish.x][getFish.y] = -1;

//				System.out.println(depth + " 상어 움직인 후");
//				print(newBoard);
//				System.out.println();

                simulation(depth + 1 , newBoard, newFishList, newShark);

            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        initalBoard = new int[4][4];
        initalFishList = new Fish[17];
        result = 0;

        for(int i = 0; i < 4; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < 4; j++) {
                int fishNum = Integer.parseInt(st.nextToken());
                int fishDir = Integer.parseInt(st.nextToken());

                initalBoard[i][j] = fishNum;
                initalFishList[fishNum] = new Fish(i, j, fishDir-1, true);
            }
        }

        // 초기화
        // 청소년 상어는 (0, 0)에 있는 물고기를 먹고, (0, 0)에 들어가게 된다.
        //상어의 방향은 (0, 0)에 있던 물고기의 방향과 같다.
        int getInitalFish = initalBoard[0][0];
        Shark initalShark = new Shark(0, 0, initalFishList[getInitalFish].dir, getInitalFish);

        initalBoard[0][0] = -1;
        initalFishList[getInitalFish].isActive = false;

        // Simulation -> dfs 구현!
        simulation(0, initalBoard, initalFishList, initalShark);

        System.out.println(result);
    }
}