import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BOJ15686 {
    static int chickenCnt = 0;
    static int houseCnt = 0;

    static int[][] dist;
    static int answer = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] nm = br.readLine().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);
        dist = new int[n * n][n * n]; // [집][치킨집] 거리

        Pos[] chickens = new Pos[n * n];
        Pos[] houses = new Pos[n * n];


        int[][] map = new int[n][n];

        for (int i = 0; i < n; i++) {
            String[] row = br.readLine().split(" ");
            for (int j = 0; j < n; j++) {
                int num = Integer.parseInt(row[j]);
                map[i][j] = num;
                if (num == 1) {
                    houses[houseCnt] = new Pos(i, j, houseCnt++, "house");
                } else if (num == 2) {
                    chickens[chickenCnt] = new Pos(i, j, chickenCnt++, "chicken");
                }
            }
        }
        for (int i = 0; i < houseCnt; i++) {
            for (int j = 0; j < chickenCnt; j++) {
                dist[i][j] = getDist(houses[i], chickens[j]);
            }
        }
        findCombinations(new ArrayList<>(), 0, chickenCnt - 1, m);
        System.out.println(answer);
    }

    public static void calcDist(int[] comb, int[][] dist, int houseCnt, int m) {
        int sum = 0;
        int[] minDist = new int[houseCnt];
        // minDist 초기화
        for (int i = 0; i < houseCnt; i++) {
            minDist[i] = Integer.MAX_VALUE;
        }

        for (int i = 0; i < m; i++) {
            int selectedChicken = comb[i];
            for (int j = 0; j < houseCnt; j++) {
                minDist[j] = Math.min(minDist[j], dist[j][selectedChicken]);
            }
        }
        for (int i = 0; i < houseCnt; i++) {
            sum += minDist[i];
        }
        answer = Math.min(answer, sum);
    }

    public static void findCombinations(List<Integer> combination, int start, int k, int m) {
        // m개를 모두 선택한 경우, 결과 출력
        if (combination.size() == m) {
            int[] comb = new int[m];
            for (int i = 0; i < m; i++) {
                comb[i] = combination.get(i);
            }
            calcDist(comb, dist, houseCnt, m);
            return;
        }

        // 가능한 모든 요소에 대해 반복
        for (int i = start; i <= k; i++) {
            // 현재 요소를 추가
            combination.add(i);

            // 다음 요소를 위한 재귀 호출
            findCombinations(combination, i + 1, k, m);

            // 마지막 요소를 제거하여 다른 조합을 찾음
            combination.remove(combination.size() - 1);
        }
    }

    public static int getDist(Pos p1, Pos p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    public static class Pos {
        int x;
        int y;

        int index;
        String property; // 치킨집 or 집

        Pos(int x, int y, int index, String property) {
            this.x = x;
            this.y = y;
            this.property = property;
            this.index = index;
        }
    }


}
