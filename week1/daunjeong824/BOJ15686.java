package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BOJ15686 {

    static int N, M;
    static int[][] myMap;
    static int cityChick = Integer.MAX_VALUE;
    public static class Position {
        int x, y;
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static ArrayList<Position> house = new ArrayList<Position>();
    static ArrayList<Position> chick = new ArrayList<Position>();

    // 치킨 거리는 집과 가장 가까운 치킨집 사이의 거리
    // 집을 기준, 각각의 집은 치킨 거리를 가짐
    // 도시의 치킨 거리는 모든 집의 치킨 거리
    static int calcChick(ArrayList<Position> chickCombi) {
        int getCityChick = 0;

        for(Position h : house) {
            int range = 101;

            for(Position c : chickCombi)
                range = Math.min(range, (Math.abs(h.x - c.x) + Math.abs(h.y - c.y)) );

            getCityChick += range;
        }
        return getCityChick;
    }

    // 치킨집 중 최대 M개 선택하기
    static void dfs(int depth, ArrayList<Position> combi) {
        // combi 조합되면
        if(combi.size() == M) {
            // calcChick(combi) 계산
            int tmp = calcChick(combi);
            cityChick = Math.min(tmp, cityChick);
            return;
        }
        else if (depth == chick.size())
            return;

        // combination 수행
        combi.add(chick.get(depth));
        dfs(depth+1, combi);

        combi.remove(combi.size()-1);
        dfs(depth+1, combi);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[] d = br.readLine().split(" ");
        N = Integer.parseInt(d[0]);
        M = Integer.parseInt(d[1]);

        myMap = new int[N][N];
        for(int i = 0; i < N; i++) {
            String[] row = br.readLine().split(" ");

            for(int j = 0; j < N; j++) {
                int rowData = Integer.parseInt(row[j]);
                myMap[i][j] = rowData;

                if(rowData == 1)  house.add(new Position(i, j));
                if(rowData == 2) chick.add(new Position(i,j));
            }
        }

        dfs(0, new ArrayList<Position>());

        System.out.println(cityChick);
    }
}