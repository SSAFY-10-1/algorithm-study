import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.IOException;

public class N_15683 {
    static int n;
    static int m;
    static int[][] board;
    static ArrayList<int[]> cctvList;

    static int[] ry = {0, -1, 0, 1};
    static int[] rx = {1, 0, -1, 0};

    static int answer;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        board = new int[n][m];
        cctvList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                int num = Integer.parseInt(st.nextToken());
                board[i][j] = num;

                if(num >= 1 && num <= 5) cctvList.add(new int[] {i, j});
            }
        }
        answer = 987654321;
        run(0);
        System.out.println(answer);
    }

    static int countSafeArea() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(board[i][j] == 0) count++;
            }
        }

        return count;
    }
    static void printBoard() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    static void run(int index) {
        if(index == cctvList.size()) {
            answer= Math.min(answer, countSafeArea());
            return;
        }

        int[] now = cctvList.get(index);
        int num = board[now[0]][now[1]];

        if(num == 1) { // 우
            for (int i = 0; i < 4; i++) {
                int r = now[0];
                int c = now[1];

                ArrayList<int[]> checkList = new ArrayList<>();
                while(true) {
                    r += ry[i];
                    c += rx[i];

                    if(r < 0 || c< 0 || r>=n || c>= m ) break;
                    // 맵 밖 벗어나면 break;

                    if(board[r][c] == 6) break;
                    // 벽이면 멈춤

                    if(board[r][c] == -1 || (board[r][c] >= 1 && board[r][c] <=6 )) continue;
                    // 이미 감시영역이거나 cctc면 넘어감

                    if(board[r][c] == 0) {
                        board[r][c] = -1;
                        // 빈칸이면 영역표시

                        checkList.add(new int[] {r, c});
                    }
                }

                run(index+1);
                back(checkList);
            }
        }else if(num == 2) { // 좌우
            for (int i = 0; i < 2; i++) {
                int r = now[0];
                int c = now[1];

                ArrayList<int[]> checkList = new ArrayList<>();
                while(true) {
                    r += ry[i];
                    c += rx[i];

                    if(r < 0 || c< 0 || r>=n || c>= m ) break;
                    // 맵 밖 벗어나면 break;

                    if(board[r][c] == 6) break;
                    // 벽이면 멈춤

                    if(board[r][c] == -1 || (board[r][c] >= 1 && board[r][c] <=6 )) continue;
                    // 이미 감시영역이거나 cctc면 넘어감

                    if(board[r][c] == 0) {
                        board[r][c] = -1;
                        // 빈칸이면 영역표시

                        checkList.add(new int[] {r, c});
                    }
                }

                r = now[0];
                c = now[1];
                while(true) {
                    r += ry[i + 2];
                    c += rx[i + 2];

                    if(r < 0 || c< 0 || r>=n || c>= m ) break;
                    // 맵 밖 벗어나면 break;

                    if(board[r][c] == 6) break;
                    // 벽이면 멈춤

                    if(board[r][c] == -1 || (board[r][c] >= 1 && board[r][c] <=6 )) continue;
                    // 이미 감시영역이거나 cctc면 넘어감

                    if(board[r][c] == 0) {
                        board[r][c] = -1;
                        // 빈칸이면 영역표시

                        checkList.add(new int[] {r, c});
                    }
                }

                run(index+1);
                back(checkList);
            }
        }else if(num == 3) { // 상우
            for (int i = 0; i < 4; i++) {
                int r = now[0];
                int c = now[1];

                ArrayList<int[]> checkList = new ArrayList<>();
                while(true) {
                    r += ry[i];
                    c += rx[i];

                    if(r < 0 || c< 0 || r>=n || c>= m ) break;
                    // 맵 밖 벗어나면 break;

                    if(board[r][c] == 6) break;
                    // 벽이면 멈춤

                    if(board[r][c] == -1 || (board[r][c] >= 1 && board[r][c] <=6 )) continue;
                    // 이미 감시영역이거나 cctc면 넘어감

                    if(board[r][c] == 0) {
                        board[r][c] = -1;
                        // 빈칸이면 영역표시

                        checkList.add(new int[] {r, c});
                    }
                }

                r = now[0];
                c = now[1];
                while(true) {
                    r += ry[(i+1) % 4];
                    c += rx[(i+1) % 4];

                    if(r < 0 || c< 0 || r>=n || c>= m ) break;
                    // 맵 밖 벗어나면 break;

                    if(board[r][c] == 6) break;
                    // 벽이면 멈춤

                    if(board[r][c] == -1 || (board[r][c] >= 1 && board[r][c] <=6 )) continue;
                    // 이미 감시영역이거나 cctc면 넘어감

                    if(board[r][c] == 0) {
                        board[r][c] = -1;
                        // 빈칸이면 영역표시

                        checkList.add(new int[] {r, c});
                    }
                }

                run(index+1);
                back(checkList);
            }
        }else if(num == 4) { // 좌상우
            for (int i = 0; i < 4; i++) {
                int r = now[0];
                int c = now[1];

                ArrayList<int[]> checkList = new ArrayList<>();
                while(true) {
                    r += ry[i];
                    c += rx[i];

                    if(r < 0 || c< 0 || r>=n || c>= m ) break;
                    // 맵 밖 벗어나면 break;

                    if(board[r][c] == 6) break;
                    // 벽이면 멈춤

                    if(board[r][c] == -1 || (board[r][c] >= 1 && board[r][c] <=6 )) continue;
                    // 이미 감시영역이거나 cctc면 넘어감

                    if(board[r][c] == 0) {
                        board[r][c] = -1;
                        // 빈칸이면 영역표시

                        checkList.add(new int[] {r, c});
                    }
                }

                r = now[0];
                c = now[1];
                while(true) {
                    r += ry[(i+1) % 4];
                    c += rx[(i+1) % 4];

                    if(r < 0 || c< 0 || r>=n || c>= m ) break;
                    // 맵 밖 벗어나면 break;

                    if(board[r][c] == 6) break;
                    // 벽이면 멈춤

                    if(board[r][c] == -1 || (board[r][c] >= 1 && board[r][c] <=6 )) continue;
                    // 이미 감시영역이거나 cctc면 넘어감

                    if(board[r][c] == 0) {
                        board[r][c] = -1;
                        // 빈칸이면 영역표시

                        checkList.add(new int[] {r, c});
                    }
                }

                r = now[0];
                c = now[1];
                while(true) {
                    r += ry[(i+2) % 4];
                    c += rx[(i+2) % 4];

                    if(r < 0 || c< 0 || r>=n || c>= m ) break;
                    // 맵 밖 벗어나면 break;

                    if(board[r][c] == 6) break;
                    // 벽이면 멈춤

                    if(board[r][c] == -1 || (board[r][c] >= 1 && board[r][c] <=6 )) continue;
                    // 이미 감시영역이거나 cctc면 넘어감

                    if(board[r][c] == 0) {
                        board[r][c] = -1;
                        // 빈칸이면 영역표시

                        checkList.add(new int[] {r, c});
                    }
                }

                run(index+1);
                back(checkList);
            }
        }else if(num == 5) { // 좌상우하
            ArrayList<int[]> checkList = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                int r = now[0];
                int c = now[1];

                while(true) {
                    r += ry[i];
                    c += rx[i];

                    if(r < 0 || c< 0 || r>=n || c>= m ) break;
                    // 맵 밖 벗어나면 break;

                    if(board[r][c] == 6) break;
                    // 벽이면 멈춤

                    if(board[r][c] == -1 || (board[r][c] >= 1 && board[r][c] <=6 )) continue;
                    // 이미 감시영역이거나 cctc면 넘어감

                    if(board[r][c] == 0) {
                        board[r][c] = -1;
                        // 빈칸이면 영역표시

                        checkList.add(new int[] {r, c});
                    }
                }
            }

            run(index+1);
            back(checkList);
        }
    }

    static void back(ArrayList<int[]> checkList) {
        for (int i = 0; i < checkList.size(); i++) {
            int[] now = checkList.get(i);

            board[now[0]][now[1]] = 0;
        }
    }
}