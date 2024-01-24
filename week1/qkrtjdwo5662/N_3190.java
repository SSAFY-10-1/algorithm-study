package qkrtjdwo5662;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class N_3190 {
    public static int answer;
    public static int[] ry = {0, 1, 0, -1};
    public static int[] rx = {1, 0, -1, 0};
    public static void main(String[] args) throws IOException {
        // 몸 길이를 늘려 머리를 다음칸에 위치 -> 한칸씩 이동
        // 벽이나 자기자신 몸에 부딪히면 게임 끝 -> 게임 종료 조건
        // 이동한 칸에 사과가 있다면 -> 머리 한칸 움직이고 꼬리 그대로(몸길이가 늘어남)
        // 이동한 칸에 사과가 없다면 -> 몸길이를 줄여서 꼬리가 위치한 칸을 비워준다. (머리 ~ 꼬리 한칸씩 이동)

        // 한칸 이동시마다 1초씩 증가
        // 해당 초마다 왼쪽, 오른쪽 방향으로 90도 회전

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        int n = Integer.parseInt(st.nextToken()); // 보드 크기
        int[][] board = new int[n][n];

        st = new StringTokenizer(br.readLine());
        int k = Integer.parseInt(st.nextToken()); // 사과의 개수
        // 행, 열 형태로 주어짐
        // 행 : 1 ~ n // 열 : 1 ~ n

        for (int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine());
            int y = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken());

            y = y-1;
            x = x-1;
            board[y][x] = 1; //사과 1로 표시
        }

        st = new StringTokenizer(br.readLine());
        int l = Integer.parseInt(st.nextToken()); // 방향 전환 횟수
        HashMap<Integer, String> map = new HashMap<>();

        for (int i = 0; i < l; i++) {
            st = new StringTokenizer(br.readLine());
            int minute = Integer.parseInt(st.nextToken());
            String opr = st.nextToken();

            map.put(minute, opr);
        }
        answer = 0;
        run(n, board, map);
        sb.append(answer);
        System.out.println(sb);
    }

    public static void run(int n, int[][] board, HashMap<Integer, String> map){
        ArrayList<int[]> list = new ArrayList<>();
        list.add(new int[]{0, 0});
        int[] now = new int[]{0, 0};
        int second = 0;
        int d = 0;

        while(true){
            int r = now[0] + ry[d];
            int c = now[1] + rx[d];
            second += 1;

            if(r < 0 || r >= n || c < 0 || c >= n) {
                answer = second;
                return;
            } // 벽

            for (int i = 0; i < list.size(); i++) {
                int[] el = list.get(i);

                if(el[0] == r && el[1] == c) {
                    answer = second;
                    return; // 자기 자신
                }
            }

            if(board[r][c] == 1){
                list.add(new int[]{r, c}); // 길이 증가
                board[r][c] = 0; // 사과 없애고
            }else{
                list.add(new int[]{r, c});
                list.remove(0);
            }

            if(map.containsKey(second)){
                String opr = map.get(second);

                if(opr.equals("D")){
                    d = (d + 1) % 4;
                }else if(opr.equals("L")){
                    if(d == 0) d = 3;
                    else d -= 1;
                }
            }

            now[0] = r;
            now[1] = c;


        }

    }
}
