package codeTree;

import java.util.*;
import java.io.*;

public class Kinght_Fight {

    static class Position {
        int x, y;
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    static class Kinght {
        int kx, ky, w, h, k;
        int damaged = 0;
        boolean isAlive;

        public Kinght(int kx, int ky, int h, int w, int k, boolean isAlive) {
            this.kx = kx;
            this.ky = ky;
            this.w = w;
            this.h = h;
            this.k = k;
            this.isAlive = isAlive;
        }

    }

    static int L, N, Q;
    static int damage_count;
    static int[][] board;
    static int[][] kinght_board;
    static Kinght[] kinght_list;
    static Set<Integer> select_move_k;
    static int[][] command;

    static int[] dx = {-1,0,1,0};
    static int[] dy = {0,1,0,-1};

    public static boolean inRange(int x, int y) {
        return (0 <= x && x < L) && (0 <= y && y < L);
    }

    // Method
    public static int[][] draw_kinght(int dir, boolean[] moved_k) {
        int[][] newBoard = new int[L][L];

//        System.out.println("움직일 기사 => " + Arrays.toString(moved_k));
        for(int i = 1; i <= N; i++) {
            if(!kinght_list[i].isAlive)
                continue;
            if(moved_k[i]) { // 움직일 수 있다면, 실제로 해당 좌표로 갱신
                kinght_list[i].kx = kinght_list[i].kx + dx[dir];
                kinght_list[i].ky = kinght_list[i].ky + dy[dir];
            }

            int kx =  kinght_list[i].kx;
            int ky =  kinght_list[i].ky;

            for(int x = kx; x < kx + kinght_list[i].h; x++)
                for(int y = ky; y < ky + kinght_list[i].w; y++)
                    newBoard[x][y] = i;
        }

        return newBoard;
    }

    public static boolean move_kinght(int num, int dir) {
        if(!kinght_list[num].isAlive)
            return false;

        int h = kinght_list[num].h;
        int w = kinght_list[num].w;

        int nx = kinght_list[num].kx + dx[dir];
        int ny = kinght_list[num].ky + dy[dir];

        boolean is_move = true;
        ArrayList<Integer> another_kinght = new ArrayList<>();

        // 이동한 현재 기사의 좌표에 대해
        outer : for(int x = nx; x < nx + h; x++)
                    for(int y = ny; y < ny + w; y++) {
                        if(!inRange(x, y) || board[x][y] == 2) { // 벽이면 이동 불가능!
                            is_move = false;
                            break outer;
                        }
                        if(kinght_board[x][y] != 0 && kinght_board[x][y] != num)
                            another_kinght.add(kinght_board[x][y]);
                    }
        // 움직일 수 없다면 -> 이동 불가능!
        if(!is_move)
            return false;

        // 인접 기사들 체크 ( 밀리는 기사들 중 하나라도 못 밀리면, 아예 못 움직인다 )
//        System.out.println(num + "번 기사 ㄱㄱ, 인접 기사는 다음과 같음 : " + another_kinght);
        for(int an_k : another_kinght) {
            boolean flag = move_kinght(an_k, dir);
//            System.out.println(an_k + "번 기사는 밀릴 수 있는지?" + flag);
            if(!flag)
                return false;
        }

        select_move_k.add(num);
        return true;
        }


    public static void fight(int attack_num, boolean[] moved_k) {
        for(int n = 1; n <= N; n++) {
            // 공격한 기사 또는 살아있지 않은 기사 또는 움직이지 않은 기사
            if(n == attack_num || !kinght_list[n].isAlive || !moved_k[n]) continue;

            int cnt = 0;

            int sx = kinght_list[n].kx;
            int sy = kinght_list[n].ky;
            int h = kinght_list[n].h;
            int w = kinght_list[n].w;

            for(int x = sx; x < sx + h; x++)
                for(int y = sy; y < sy + w; y++)
                    if(board[x][y] == 1) cnt++;
//            System.out.println(n+"번째 기사 데미지 : " + cnt);
            kinght_list[n].k = kinght_list[n].k - cnt;
            kinght_list[n].damaged = kinght_list[n].damaged + cnt;

            if(kinght_list[n].k <= 0 && kinght_list[n].isAlive)
                kinght_list[n].isAlive = false;
        }
    }

    // Debug
    public static void print(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[0].length; j++)
                sb.append(board[i][j]).append(" ");
            sb.append("\n");
        }

        System.out.println(sb);
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        L = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        board = new int[L][L];
        kinght_board = new int[L][L];

        for(int i = 0; i < L; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < L; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        kinght_list = new Kinght[N+1];
        for(int i = 1; i < N+1; i++) {
            st = new StringTokenizer(br.readLine());

            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());

            kinght_list[i] = new Kinght(r-1, c-1, h, w, k, true);

            for(int x = r - 1; x < (r-1)+h; x++)
                for(int y = c - 1; y < (c-1)+w; y++)
                    kinght_board[x][y] = i;
        }

        command = new int[Q][2];
        for(int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            command[i][0] = Integer.parseInt(st.nextToken());
            command[i][1] = Integer.parseInt(st.nextToken());
        }

        // Simulation
        damage_count = 0;
        int tmp = 1;

        for(int[] c : command) {
//            System.out.println(tmp+" 번째 명령어 시작");
            int k_num = c[0];
            int k_dir = c[1];

            select_move_k = new HashSet<>();

            boolean f = move_kinght(k_num, k_dir);

//            System.out.println(k_num + "번 기사의 "+ k_dir +" 방향 으로의 공격 여부는? : " + f);
            boolean[] moved_k = new boolean[N+1];
            if(f) {
                for(Integer num : select_move_k)
                    moved_k[num] = true;

                kinght_board = draw_kinght(k_dir, moved_k);
            }
            fight(k_num, moved_k);

//            System.out.println(tmp+"번째 기사 상황");
//            System.out.println("----");
//            for(int i = 1; i <= N; i++) {
//                System.out.println(i+"번째 기사 (x,y) => " + kinght_list[i].kx + " : " + kinght_list[i].ky);
//                System.out.println(i+"번째 기사 (hp,damaged) =>" + kinght_list[i].k + " : " + kinght_list[i].damaged);
//                System.out.println(i+"번째 기사 생존 여부 : " + kinght_list[i].isAlive);
//                System.out.println();
//            }
//            System.out.println("----");
            tmp++;
        }

        // sol
        for(int i = 1; i < N + 1; i++)
            if(kinght_list[i].isAlive) damage_count += kinght_list[i].damaged;

        System.out.println(damage_count);
    }
}
