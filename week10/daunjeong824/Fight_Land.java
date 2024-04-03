package codeTree;

import java.util.*;
import java.io.*;

public class Fight_Land {
    // Data
    static class Player {
        int x, y, dir;
        int status, gun, point;

        public Player(int x, int y, int dir, int status, int gun, int point) {
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.status = status;
            this.gun = gun;
            this.point = point;
        }
    }

    static int N, M, K;
    static ArrayList<Integer>[][] board;
    static int[][] player_board;
    static Player[] playerList;

    static int[] dx = {-1,0,1,0}; // 0<-> 2 && 1<->3
    static int[] dy = {0,1,0,-1};

    public static boolean inRange(int x, int y) {
        return (0 <= x && x < N) && (0 <= y && y < N);
    }

    // Method
    public static void move(int playerNum) {
        Player curPlayer = playerList[playerNum];

        int x = curPlayer.x;;
        int y = curPlayer.y;

        int nx = x + dx[curPlayer.dir];
        int ny = y + dy[curPlayer.dir];

        if(!inRange(nx, ny)) {
            if(curPlayer.dir == 0) curPlayer.dir = 2;
            else if(curPlayer.dir == 1) curPlayer.dir = 3;
            else if(curPlayer.dir == 2) curPlayer.dir = 0;
            else curPlayer.dir = 1;

            playerList[playerNum].dir = curPlayer.dir;
            nx = x + dx[curPlayer.dir];
            ny = y + dy[curPlayer.dir];
        }

        playerList[playerNum].x = nx;
        playerList[playerNum].y = ny;
        player_board[x][y] = 0;
    }

    public static void check(int playerNum) {
        Player curPlayer = playerList[playerNum];

        // 다른 사람이 존재할 때
        if(player_board[curPlayer.x][curPlayer.y] != 0) {
//            System.out.println(playerNum + " VS " + player_board[curPlayer.x][curPlayer.y]);
            int[] get_win_lose_player = fight(playerNum, player_board[curPlayer.x][curPlayer.y]);
            // int[] = [ win, lose ]
//            System.out.println(get_win_lose_player[0] + "번째 선수가 이겼습니다.");
//            System.out.println(get_win_lose_player[1] + "번째 선수가 졌습니다.");

            winPlayer(get_win_lose_player[0], get_win_lose_player[1]);
            losePlayer(get_win_lose_player[1]);

        } else {
            player_board[curPlayer.x][curPlayer.y] = playerNum; // 갱신해 주고

            if(!board[playerList[playerNum].x][playerList[playerNum].y].isEmpty())
                getGun(playerNum);
        }
//        System.out.println(playerNum + "번째 선수 움직인 후");
//        print_playerBoard();
    }
    public static void getGun(int playerNum) {
        Player curPlayer = playerList[playerNum];

        // 총이 있는지 확인
        int maximum_gun = 0;
        int idx = 0;

        for(int i = 0; i < board[curPlayer.x][curPlayer.y].size(); i++) {
                if(maximum_gun < board[curPlayer.x][curPlayer.y].get(i)) {
                    maximum_gun = board[curPlayer.x][curPlayer.y].get(i);
                    idx = i;
                }
        }
            // 총 가지고 있다면
        if(curPlayer.gun != 0) {
                if(maximum_gun > curPlayer.gun) { // 가지고 있는 총보다 더 공격력이 높다면
                    board[curPlayer.x][curPlayer.y].remove(idx); // 해당 총 빼고
                    board[curPlayer.x][curPlayer.y].add(curPlayer.gun); // 원래 총 넣기
                    playerList[playerNum].gun = maximum_gun;
                }
            }
        else { // 총 없다면 -> 그대로 가져 가기
                playerList[playerNum].gun = maximum_gun;
                board[curPlayer.x][curPlayer.y].remove(idx);
            }

//        System.out.println(playerNum + "번째 선수가 " + playerList[playerNum].gun + " 공격력의 총을 획득했습니다.");
    }

    // int[] = [ win, lose ]
    public static int[] fight(int num1, int num2) {
        int num1Status = playerList[num1].status + playerList[num1].gun;
        int num2Status = playerList[num2].status + playerList[num2].gun;

        if(num1Status == num2Status) {
            if(playerList[num1].status > playerList[num2].status)
                return new int[] {num1, num2};

            else
                return new int[] {num2, num1};
        }

        else if(num1Status > num2Status)
            return new int[] {num1, num2};
        else
            return new int[] {num2, num1};
    }

    public static void losePlayer(int num) {
        Player losePlayer = playerList[num];

        int dir = losePlayer.dir;
        int nx = losePlayer.x + dx[dir];
        int ny = losePlayer.y + dy[dir];

        // 이동하려는 칸에 다른 플레이어가 있거나 격자 범위 밖인 경우
        if(!inRange(nx, ny) || player_board[nx][ny] != 0) {

            for(int i = 0; i < 4; i++) {
                dir = (dir + 1) % 4;

                nx = losePlayer.x + dx[dir];
                ny = losePlayer.y + dy[dir];

                if(inRange(nx, ny) && player_board[nx][ny] == 0){
//                    System.out.println(num + " 선수는 진 선수");
//                    System.out.println(losePlayer.x + " : " + losePlayer.y + " => ");
//                    System.out.println(nx + " : " + ny);
                    break;
                }

            }
        }
        // 위치 갱신
        playerList[num].x = nx;
        playerList[num].y = ny;
        playerList[num].dir = dir;
        player_board[nx][ny] = num;

        if(!board[nx][ny].isEmpty())
            getGun(num);
    }
    //이긴 플레이어는 각 플레이어의 초기 능력치와 가지고 있는 총의 공격력의 합의 차이만큼을 포인트로 획득
    public static void winPlayer(int win, int lose) {

        Player losePlayer = playerList[lose];
        int loserGun = losePlayer.gun;

        if(losePlayer.gun != 0) {
            board[losePlayer.x][losePlayer.y].add(losePlayer.gun);
            playerList[lose].gun = 0;
        }

        int calcPoint = 0;
        int winPlayerStatus = playerList[win].status + playerList[win].gun;
        int losePlayerStatus = playerList[lose].status + loserGun;

        calcPoint = winPlayerStatus - losePlayerStatus;
        playerList[win].point += calcPoint;
//        System.out.println(win + "번째 선수는 " + calcPoint + "포인트를 획득합니다.");

        // 위치 갱신 & 총 얻기
        if(!board[playerList[win].x][playerList[win].y].isEmpty())
            getGun(win);
        player_board[playerList[win].x][playerList[win].y] = win;
    }
    // Debug
    public static void print_gunBoard() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++)
                sb.append(board[i][j]).append(" ");
            sb.append("\n");
        }
        System.out.println(sb);
    }

    public static void print_playerBoard() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++)
                sb.append(player_board[i][j]).append(" ");
            sb.append("\n");
        }
        System.out.println(sb);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        board = new ArrayList[N][N];
        player_board = new int[N][N];

        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++)
                board[i][j] = new ArrayList<Integer>();

        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                int g = Integer.parseInt(st.nextToken());
                if (g != 0) board[i][j].add(g);
            }
        }

        playerList = new Player[M+1];
        for(int i = 1; i <= M; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());

            playerList[i] = new Player(x - 1, y - 1, d, s, 0, 0);
            player_board[x-1][y-1] = i;
        }

        // Simulation
        for(int i = 0; i < K; i++) {
//            System.out.println((i+1) + "번째 라운드 시작 ==========");
            for(int num = 1; num <= M; num++) {
                // move
                move(num);
                check(num);
            }
        }
        // Score
        StringBuilder sb = new StringBuilder();
        for(int i = 1 ; i < M + 1; i++)
            sb.append(playerList[i].point).append(" ");
        System.out.println(sb);
    }
}
