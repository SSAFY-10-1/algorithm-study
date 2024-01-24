package qkrtjdwo5662;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class N_15686 {
    public static int answer;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        int n = Integer.parseInt(st.nextToken()); // 지도 크기
        int m = Integer.parseInt(st.nextToken()); // 선택할 치킨 집 개수

        int[][] board = new int[n][n];

        // 치킨집의 리스트를 구한다.
        // 집의 리스트를 구한다.

        // 치킨집의 리스트 중 m개씩 선택한다.
        // m개의 치킨집과 집의 거리 최소거리 (치킨거리)의 합을 구한다.
        // 전체 치킨집에서 m개의 치킨을 구하는 방법은 순열이 아닌 조합

        ArrayList<int[]> house = new ArrayList<>();
        ArrayList<int[]> chicken =new ArrayList<>();

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                int num = Integer.parseInt(st.nextToken());
                board[i][j] = num;

                if(num == 1) house.add(new int[]{i, j});
                else if(num == 2) chicken.add(new int[]{i, j});
            }
        }

        boolean[] visited = new boolean[chicken.size()];
        answer = Integer.MAX_VALUE;
        pickChicken(new ArrayList<>(), chicken, house, visited, m, 0);
        sb.append(answer);
        System.out.println(sb);


    }

    public static void pickChicken(ArrayList<int[]>pick, ArrayList<int[]> chicken, ArrayList<int[]> house , boolean[] visited, int m, int index){
        if(pick.size() == m){
            answer = Math.min(answer, chickenDist(pick, house));
            return;
        }


        for (int i = index; i < chicken.size(); i++) {
            if(!visited[i]){
                visited[i] = true;
                pick.add(chicken.get(i));
                pickChicken(pick, chicken, house ,visited, m, i);
                pick.remove(chicken.get(i));
                visited[i] = false;
            }
        }
    }
    public static int chickenDist(ArrayList<int[]> pick, ArrayList<int[]> house){
        int total = 0;

        for (int i = 0; i < house.size(); i++) {
            int[] nowHouse = house.get(i);

            int minDist = Integer.MAX_VALUE;
            for (int j = 0; j < pick.size(); j++) {
                int[] nowChicken = pick.get(j);
                int dist = Math.abs(nowChicken[0] - nowHouse[0]) + Math.abs(nowChicken[1] - nowHouse[1]);

                minDist = Math.min(minDist, dist);
            }
            total += minDist;
        }

        return total;

    }
}
