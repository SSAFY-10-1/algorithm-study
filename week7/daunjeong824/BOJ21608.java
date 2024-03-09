package baekjoon;

import java.io.*;
import java.util.*;

public class BOJ21608 {

    static class Position { // 일단 Compareable 구현은 보류한다.
        int x, y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    static int N;
    static int[][] student_data;
    static int[] happiness = {0, 1, 10, 100, 1000};
    static Position[] table;
    static int[][] likeStudent;

    static int[][] board;
    static int[] dx = {-1,0,1,0};
    static int[] dy = {0,1,0,-1};

    public static boolean inRange(int x, int y) {
        return (0 <= x && x < N) && (0 <= y && y < N);
    }

    // Method
    public static ArrayList<Position> firstCondition(int studentNum) {

        ArrayList<Position>[] posSet = new ArrayList[5];
        for(int i = 0; i < 5; i++)
            posSet[i] = new ArrayList<Position>();

        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++) {
                if(board[i][j] != 0) continue;

                int cnt = 0;
                // 인접한 좌표에 대해
                for(int p = 0; p < 4; p++) {
                    int nx = i + dx[p];
                    int ny = j + dy[p];

                    if(inRange(nx, ny)) {
                        // 인접한 좌표값이 좋아하는 학생이라면
                        for(int like : likeStudent[studentNum]){
                            if(like == board[nx][ny]){
                                cnt++;
                                break;
                            }
                        }
                    }
                }

                posSet[cnt].add(new Position(i,j));
            }

        for(int getMax = 4; getMax > 0; getMax--) {
            if(!posSet[getMax].isEmpty())
                return posSet[getMax];
        }
        // 조건 부합하지 않으면, 그냥 빈칸 리스트 반환
        return posSet[0];
    }

    public static ArrayList<Position> secondCondition(ArrayList<Position> posList) {
        ArrayList<Position>[] posSet = new ArrayList[5];
        for(int i = 0; i < 5; i++)
            posSet[i] = new ArrayList<Position>();

        for(Position pos : posList) {
            int blank_cnt = 0;
            // 인접한 좌표에 대해
            for (int p = 0; p < 4; p++) {
                int nx = pos.x + dx[p];
                int ny = pos.y + dy[p];

                // 인접한 좌표값이 빈칸이라면
                if (inRange(nx, ny) && board[nx][ny] == 0)
                    blank_cnt++;
            }
            posSet[blank_cnt].add(new Position(pos.x, pos.y));
        }
        // 조건 부합하지 않으면, 그냥 빈칸 리스트 반환
        for(int getMax = 4; getMax > 0; getMax--) {
            if(!posSet[getMax].isEmpty())
                return posSet[getMax];
        }
        return posSet[0];
    }

    public static Position finalCondition(ArrayList<Position> posList) {
            Collections.sort(posList, new Comparator<Position>() {
                @Override
                public int compare(Position a, Position b) {
                    if(a.x == b.x)
                        return a.y - b.y;
                    return a.x - b.x;
                }
            });

            return posList.get(0);
    }

    public static int calcHappiness() {
        int result = 0;

        for(int stdNum = 1; stdNum <= N*N; stdNum++) {
            Position pos = table[stdNum];

            int cnt = 0;
            for(int i = 0; i < 4; i++) {
                int nx = pos.x + dx[i];
                int ny = pos.y + dy[i];

                if(inRange(nx, ny)) {
                    for(int likeStd : likeStudent[stdNum]) {
                        if(board[nx][ny] == likeStd) {
                            cnt++;
                            break;
                        }
                    }
                }
            }

            result += happiness[cnt];
        }
        return result;
        }


    // debug
    public static void print() {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                sb.append(board[i][j]).append(" ");
            }
            sb.append("\n");
        }

        System.out.println(sb);
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        N = Integer.parseInt(br.readLine());
        student_data = new int[N*N][5];
        likeStudent = new int[N*N+1][4];

        for(int i = 0; i < N*N; i++) {
            st = new StringTokenizer(br.readLine());
            int studentNum = Integer.parseInt(st.nextToken());
            student_data[i][0] = studentNum;

            for(int j = 1; j < 5; j++) {
                student_data[i][j] = Integer.parseInt(st.nextToken());
                likeStudent[studentNum][j-1] = student_data[i][j];
            }
        }

        board = new int[N][N];
        table = new Position[N*N+1];

        // Simulation
        for(int i = 0; i < N*N; i++) {

            int stuNum = student_data[i][0];

            ArrayList<Position> getFirst = firstCondition(stuNum);

            if(getFirst.size() == 1) {
                Position pos = getFirst.get(0);

                if(table[stuNum] == null) table[stuNum] = new Position(pos.x, pos.y);
                board[pos.x][pos.y] = stuNum;
                continue;
            }

            ArrayList<Position> getSec = secondCondition(getFirst);

            if(getSec.size() == 1) {
                Position pos = getSec.get(0);

                if(table[stuNum] == null) table[stuNum] = new Position(pos.x, pos.y);
                board[pos.x][pos.y] = stuNum;
                continue;
            }

            Position finalPos = finalCondition(getSec);

            if(table[stuNum] == null) table[stuNum] = new Position(finalPos.x, finalPos.y);
            board[finalPos.x][finalPos.y] = stuNum;
        }

        System.out.println(calcHappiness());
    }
}
