import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Art {
    static int[][] board;
    static int[][] groupBoard;
    static boolean[][] visited;
    static int n;
    static int[] ry = {0, 0, 1, -1};
    static int[] rx = {1, -1, 0, 0};
    static int groupCount;
    static boolean[] used;
    static ArrayList<int[]>[] groupList;

    static int answer;
    public static void main(String[] args) throws IOException {

        // board 세팅

        // 그룹 나누기 => 같은 숫자 상하좌우 인접하면 같은 그룹 bfs
        // 같은 숫자이지만 다른 그룹 가능성 존재 -> 그룹 파악 후 숫자 구별 될 수 있게 변환 +10, +11, +12...
        // 그룹으로 파악된 그룹의 숫자 값을 arrayList에 보관

        // 조화로움을 구하기 위해서는 그룹 전체 중에 2개를 조합으로 선택
        // 조화로움 => (a의 칸수 + b의 칸수) X a를 이루고 있는 숫자 X b를 이루고 있는 숫자 X a와 b가 맞닿는 변의 수
        // 그룹 2개의 숫자 값 인자로 보냄
        // 맞 닿는 변의 수를 구하는 것이 핵심일 듯
        // 상하좌우 그룹 서로 다른 그룹 되면 count ++

        // 배열 돌리기
        // 전체 왼쪽으로 90도
        // 가운데 십자가 제외 4분할 하여 오른쪽으로 90도

        // 초기, 1회전 후, 2회전 후, 3회전 후 예술 점수 합해서 출력

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        n = Integer.parseInt(st.nextToken());
        board = new int[n][n];


        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                int num = Integer.parseInt(st.nextToken());
                board[i][j] = num;
            }
        }

        groupBoard = new int[n][n];
        groupCount = 0;
        visited = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(!visited[i][j]){
                    divideGroup(new int[]{i, j}, board[i][j]);
                    groupCount ++;
                }
            }
        }
        answer = 0;
        used = new boolean[groupCount];
        groupList = new ArrayList[groupCount];
        for (int i = 0; i < groupCount; i++) {
            groupList[i] = new ArrayList<>();
        }
        setGroupList();
        pickGroup(0, 0);

        rotateFirst();
        rotateSecond();
        groupBoard = new int[n][n];
        groupCount = 0;
        visited = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(!visited[i][j]){
                    divideGroup(new int[]{i, j}, board[i][j]);
                    groupCount ++;
                }
            }
        }
        used = new boolean[groupCount];
        groupList = new ArrayList[groupCount];
        for (int i = 0; i < groupCount; i++) {
            groupList[i] = new ArrayList<>();
        }
        setGroupList();
        pickGroup(0, 0);

        rotateFirst();
        rotateSecond();
        groupBoard = new int[n][n];
        groupCount = 0;
        visited = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(!visited[i][j]){
                    divideGroup(new int[]{i, j}, board[i][j]);
                    groupCount ++;
                }
            }
        }
        used = new boolean[groupCount];
        groupList = new ArrayList[groupCount];
        for (int i = 0; i < groupCount; i++) {
            groupList[i] = new ArrayList<>();
        }
        setGroupList();
        pickGroup(0, 0);


        rotateFirst();
        rotateSecond();
        groupBoard = new int[n][n];
        groupCount = 0;
        visited = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(!visited[i][j]){
                    divideGroup(new int[]{i, j}, board[i][j]);
                    groupCount ++;
                }
            }
        }
        used = new boolean[groupCount];
        groupList = new ArrayList[groupCount];
        for (int i = 0; i < groupCount; i++) {
            groupList[i] = new ArrayList<>();
        }
        setGroupList();
        pickGroup(0, 0);

        System.out.println(answer);

    }
    static void setGroupList(){
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int num = groupBoard[i][j];
                groupList[num].add(new int[] {i, j});
            }
        }
    }

    static void divideGroup(int[] start, int num){
        ArrayDeque<int[]> deque = new ArrayDeque<>();
        visited[start[0]][start[1]] = true;

        deque.addLast(start);

        while(!deque.isEmpty()){
            int[] now = deque.pollFirst();

            for (int i = 0; i < 4; i++) {
                int r = now[0] + ry[i];
                int c = now[1] + rx[i];

                groupBoard[now[0]][now[1]] = groupCount;

                if(r < 0 || c < 0 || r>= n || c>= n) continue;

                if(board[r][c] != num ) continue;

                if(!visited[r][c]){
                    visited[r][c] = true;
                    deque.addLast(new int[]{r, c});
                }
            }
        }


    } // 그룹 나누기

    static void pickGroup(int depth, int index){
        if(depth == 2){
            // group 선택 왼료
            int a = -1;
            int b = -1;
            for (int i = 0; i < groupCount; i++) {
                if(used[i] && a == -1){
                    a = i;
                }else if(used[i] && a != -1){
                    b = i;
                }
            }

            answer += harmony(a, b);
            return;
        }

        for (int i = index; i < groupCount; i++) {
            if(!used[i]){
                used[i] = true;
                pickGroup(depth + 1, i);
                used[i] = false;
            }
        }



    } // 두개 그룹 뽑기

    static int harmony(int a, int b){
        // 그룹 별 조화로움 구하기

        int aSize = groupList[a].size();
        int bSize = groupList[b].size();

        int count = 0;

        // 그룹에 포함된 개수가 작은 것의 맞닿는 변을 찾는다.
        if(aSize > bSize){
            count = touchSide(groupList[b], a);
        }else{
            count = touchSide(groupList[a], b);
        }

        int numA = board[groupList[a].get(0)[0]][groupList[a].get(0)[1]];
        int numB = board[groupList[b].get(0)[0]][groupList[b].get(0)[1]];

        return (aSize + bSize) * numA * numB * count;
    }
    static int touchSide(ArrayList<int[]> list, int other){
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            int[] now = list.get(i);

            for (int j = 0; j < 4; j++) {
                int r = now[0] + ry[j];
                int c = now[1] + rx[j];

                if(r < 0 || c < 0 || r>= n || c>= n) continue;

                if(groupBoard[r][c] == other) count++;
            }
        }
        return count;
    }

    // 배열 돌리기(가운데 십자가) - 왼쪽 90도
    static void rotateFirst(){
        // 0, 2 -> 2, 0 // 1, 2 -> 2, 1
        // 2, 0 -> 4, 2 // 2, 1 -> 3, 2
        // 4, 2 -> 2, 4 // 3, 2 -> 2, 3
        // 2, 4 -> 0, 2 // 2, 3 -> 1, 2



        int[][] newBoard = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newBoard[i][j] = board[j][n- i - 1];
            }
        }

        for (int i = 0; i < n ; i++) {
            board[n/2][i] = newBoard[n/2][i];
        }
        for (int i = 0; i < n ; i++) {
            board[i][n/2] = newBoard[i][n/2];
        }

    }

    // 배열 돌리기(4분할) - 오른쪽 90도
    static void rotateSecond(){
        int len = n/2;

        int[][] newBoard = new int[len][len];

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                newBoard[i][j] = board[len-j-1 ][i];
            }
        }
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                board[i][j] = newBoard[i][j];
                newBoard[i][j] = 0;
            }
        }


        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                newBoard[i][j] = board[len-j-1][i+ (len + 1)];
            }
        }
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                board[i][j + (len + 1)] = newBoard[i][j];
                newBoard[i][j] = 0;
            }
        }

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                newBoard[i][j] = board[len-j-1+ (len + 1)][i];
            }
        }
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                board[i+ (len + 1)][j] = newBoard[i][j];
                newBoard[i][j] = 0;
            }
        }

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                newBoard[i][j] = board[len-j-1+ (len + 1)][i+ (len + 1)];
            }
        }
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                board[i+ (len + 1)][j+(len + 1)] = newBoard[i][j];
                newBoard[i][j] = 0;
            }
        }






    }
}

