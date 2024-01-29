package qkrtjdwo5662;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class N_16236 {
    public static int n;

    public static int[] ry = {1, -1, 0, 0};
    public static int[] rx = {0, 0, 1, -1};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st =new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        n = Integer.parseInt(st.nextToken());

        int[][] board = new int[n][n];

        int[] start = new int[2];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                int num = Integer.parseInt(st.nextToken());
                board[i][j] = num;
                if(num == 9){
                    start = new int[]{i, j};
                    board[i][j] = 0;
                }
            }
        }

        int answer = bfs(start, board);
        System.out.println(answer);
    }

    public static int bfs(int[] start, int[][] board){
        // 상어 처음 크기 2
        // 상어는 상하좌우 한칸씩 이동

        // 자신보다 큰 물고기있는 칸은 못지나감
        // 자신보다 같거나 작은 물고기 칸은 지나감
        // 작은 물고기는 먹음

        // 자신의 무게 만큼 물고기를 먹으면 크기 1증가
        // 물고기를 먹기 위해 최단거리로 이동하는데

        // 위 > 왼쪽 순

        int size = 2; // 상어 초기 사이즈
        int move = 0; // 움직인 거리
        int eat = 0; // 먹은 수

        int[] now = start;
        while(true){
            boolean[][] visited = new boolean[n][n]; // 최단거리 위한 방문배열
            PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
                @Override
                public int compare(int[] o1, int[] o2) {
                    if(o1[2] == o2[2]){
                        if(o1[0] == o2[0]){
                            return Integer.compare(o1[1], o2[1]);
                        }else{
                            return Integer.compare(o1[0], o2[0]);
                        }
                    }else{
                        return Integer.compare(o1[2], o2[2]);
                    }
                }
            });
            visited[now[0]][now[1]] = true;
            pq.add(new int[]{now[0], now[1], 0});
            boolean flag = false; // 잡아먹었는지 체크

            while(!pq.isEmpty()){
                now = pq.poll();

                // 잡아먹을 물고기 존재
                if(0 < board[now[0]][now[1]] && board[now[0]][now[1]] < size){
                    board[now[0]][now[1]] = 0;
                    eat ++;
                    move += now[2]; // 이동 누적
                    flag = true; // 잡아먹음 체크

                    break;
                }

                // 잡아먹을 물고기 존재하지 않음 이동 ㄱㄱ
                for (int i = 0; i < 4; i++) {
                    int r = now[0] + ry[i];
                    int c = now[1] + rx[i];

                    if(r < 0 || c< 0 || r>= n || c>= n) continue;

                    if(board[r][c] > size) continue;

                    if(!visited[r][c]){
                        visited[r][c] = true;
                        pq.add(new int[]{r, c, now[2] + 1});
                    }
                }
            }

            // 하나 먹었으면 여기로
            if(!flag) break;

            if(size == eat) {
                size += 1;
                eat = 0;
            }

        }
        return move;
    }
}

