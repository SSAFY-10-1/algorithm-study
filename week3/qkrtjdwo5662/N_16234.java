import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class N_16234 {
    static int n;
    static int min;
    static int max;
    static int[][] board;
    static boolean[][] visited;
    static int[] ry = {1, 0, -1, 0};
    static int[] rx = {0, 1, 0, -1};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        n = Integer.parseInt(st.nextToken()); // nXn 땅
        min = Integer.parseInt(st.nextToken()); // 인구차이 min
        max = Integer.parseInt(st.nextToken()); // 인구차이 max

        board = new int[n][n];


        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j <n ; j++) {
                int num = Integer.parseInt(st.nextToken());
                board[i][j] = num;
            }
        }

        int day = 0;
        while(true){
            boolean flag = false;
            visited = new boolean[n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if(!visited[i][j]){
                        if(run(new int[]{i, j})){
                            flag = true;
                        }
                    }
                }
            }

            if(!flag){
                sb.append(day).append("\n");
                System.out.println(sb);
                return;
            }
            day ++;
        }
    }

    static boolean run(int[] start){
        // min ~ max 인구 차이이면, 인구 이동 시작
        // 좌표 넣고, 개수 +1,
        // 모든 이동 할 수 있는 상황 체크 후 이동
        // 총합/개수 -> (int)
        // 인구 이동 몇일 발생?
        ArrayDeque<int[]> deque = new ArrayDeque<>();
        ArrayList<int[]> list = new ArrayList<>(); // 인구이동 좌표리스트
        deque.addLast(start);
        list.add(start);
        visited[start[0]][start[1]] = true;
        int total = board[start[0]][start[1]];

        while(!deque.isEmpty()){
            int[] now = deque.pollFirst();

            for (int i = 0; i < 4; i++) {
                int r = now[0] + ry[i];
                int c = now[1] + rx[i];

                if(r < 0 || c<0 || r>=n || c>= n) continue; // 범위 벗어나면

                int abs = Math.abs(board[r][c] - board[now[0]][now[1]]);
                if(min <= abs && abs<= max && !visited[r][c]){ // 인구 차이 범위 체크
                    deque.addLast(new int[]{r, c});
                    total += board[r][c];
                    visited[r][c] = true;
                    list.add(new int[]{r, c});
                }
            }

        }

        if(list.size() == 1) return false;


        int average = total/list.size();

        // update
        for (int i = 0; i < list.size(); i++) {
            int[] now =list.get(i);
            board[now[0]][now[1]] = average;
        }
        return true;
    }
}
