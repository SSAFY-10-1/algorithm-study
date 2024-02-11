package baekjoon;

import java.util.*;
import java.io.*;

public class BOJ16236 {

    static int N;
    static int[][] myMap;
    static int[] dx = {-1,0,1,0};
    static int[] dy = {0,1,0,-1};

    public static boolean inRange(int x, int y) {
        return ((0 <= x && x < N) && (0 <= y && y < N));
    }

    static class Position implements Comparable<Position> {
        int x, y, cnt;

        public Position(int x, int y, int cnt) {
            this.x = x;
            this.y = y;
            this.cnt = cnt;
        }

        @Override
        public int compareTo(Position p) {
            if (this.cnt == p.cnt) { // 최단 경로가 같다면
                if (this.x == p.x) // x 좌표가 같다면
                    return this.y - p.y; // y 좌표 오름차순으로
                else
                    return this.x - p.x; // x 좌표 오름차순
            }
            return this.cnt - p.cnt; // 최단 경로 오름차순
        }
    }

    static int sharkX, sharkY;
    static int sharkSize = 2; // 자신의 크기와 같은 수의 물고기를 먹어야 사이즈 + 1
    static int eatFishCnt = 0; // if eatFishCnt == sharkSize -> sharkSize += 1 && FishCnt == 0
    static int timeCnt = 0; // 경로 == 가는데 걸린 시간 ( 먹는 시간은 제외 ), 계속 더하면 된다.

    public static boolean bfs() {
        Queue<Position> queue = new LinkedList<Position>(); // 경로 계산
        boolean[][] visited = new boolean[N][N]; // 방문 여부

        ArrayList<Position> getFish = new ArrayList<Position>(); // 잡아 먹을 물고기 리스트

        queue.add(new Position(sharkX, sharkY, 0));
        visited[sharkX][sharkY] = true;

        while (!(queue.isEmpty())) {
            Position curPos = queue.poll();

            for(int i = 0 ; i < 4; i++) {
                int nx = curPos.x + dx[i];
                int ny = curPos.y + dy[i];

                if(inRange(nx, ny) && !(visited[nx][ny])) {
                    // 상어 사이즈 == 물고기 (빈칸) , 상어 사이즈 > 물고기 (target)
                    if(myMap[nx][ny] < sharkSize && myMap[nx][ny] > 0) {
                        getFish.add(new Position(nx, ny, curPos.cnt + 1));
                        visited[nx][ny] = true;
                    }
                    else if(myMap[nx][ny] == sharkSize || myMap[nx][ny] == 0){
                        queue.add(new Position(nx, ny, curPos.cnt + 1));
                        visited[nx][ny] = true;
                    }
                }
            }
        }

        if(getFish.isEmpty()) return false;

        else {
            Collections.sort(getFish); // 정렬 시킨 뒤 (커스텀 정렬 기준 - CompareTo)
            Position myFish = getFish.get(0); // 먹을 물고기 꺼내서

            sharkX = myFish.x;
            sharkY = myFish.y;
            myMap[sharkX][sharkY] = 0; // 물고기 먹음 -> 빈칸 갱신

            eatFishCnt += 1; // 성장 갱신
            if(eatFishCnt == sharkSize){
                sharkSize += 1;
                eatFishCnt = 0;
            }

            timeCnt += myFish.cnt; // 걸린 시간 갱신

            return true;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // 인풋
        N = Integer.parseInt(br.readLine());
        myMap = new int[N][N];

        for(int i = 0; i < N; i++){
            String[] data = br.readLine().split(" ");

            for(int j = 0; j < N; j++) {
               int tmp = Integer.parseInt(data[j]);

               if(tmp == 9){
                   sharkX = i;
                   sharkY = j;
                   myMap[i][j] = 0;
               }
               else myMap[i][j] = tmp;
            }
        }
        // 메인 로직
        while (true) { // 메인 함수 -> 물고기 더 이상 못 먹게 될 때 까지
            boolean f = bfs();
            if(!f) break;
        }

        System.out.println(timeCnt);
    }
}
