import java.io.*;
import java.util.*;

public class artistrySample {
	// DATA
	static int N, score;
	static int[][] board;
	
	// 아래 데이터는 변형해서 사용해도 됨
	static ArrayList<ArrayList<Position>> numSetList;

	static boolean[][] visited;
	static int[] dx = {-1,0,1,0};
	static int[] dy = {0,1,0,-1};

	public static boolean inRange(int x, int y) {
		return (0 <= x && x < N) && (0 <= y && y < N);
	}
	
	public static class Position {
		int x, y, num;
		public Position(int x, int y, int num) {
			this.x = x;
			this.y = y;
			this.num = num;
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y, num);
		}

		@Override
		public boolean equals(Object p) {
			if(! (p instanceof Position)) return false;

			Position pos = (Position) p;
			return (this.x == pos.x && this.y == pos.y && this.num == pos.num);
		}
	}
	// <--------------- Method ----------------->
	
	// 1. 숫자에 해당하는 집합을 얻는 메소드
	public static void bfs(int stX, int stY) {
		ArrayList<Position> group = new ArrayList<Position>();

		Queue<Position> queue = new ArrayDeque<Position>();

		visited[stX][stY] = true;
		int number = board[stX][stY];

		queue.add(new Position(stX, stY, number));
		group.add(new Position(stX, stY, number));

		while (!queue.isEmpty()) {
			Position curPos = queue.poll();

			for(int i = 0; i < 4; i++) {
				int nx = curPos.x + dx[i];
				int ny = curPos.y + dy[i];

				if(inRange(nx, ny) && !visited[nx][ny]) {
					if(board[nx][ny] == number) {
						visited[nx][ny] = true;
						queue.add(new Position(nx, ny, number));
						group.add(new Position(nx, ny, number));
					}
				}
			}
		}
		numSetList.add(group);
	}

	// 2. 조화성 계산
	public static int getHarmony(ArrayList<Position> a, ArrayList<Position> b) {

		int cnt = 0;
		int aVal = a.get(0).num;
		int bVal = b.get(0).num;

		for(Position pos : a) {
			int aX = pos.x;
			int aY = pos.y;

			for(int i = 0; i < 4; i++) {
				int nx = aX + dx[i];
				int ny = aY + dy[i];

				if(inRange(nx, ny) && board[nx][ny] != aVal) {
					Position tmpPos = new Position(nx, ny, board[nx][ny]);
					// contains => equals로 포함 여부 확인 => 객체 비교 => equals & hashCode override!
					if(b.contains(tmpPos))
						cnt++;
				}
			}
		}
		// harmony = (groupA cnt + groupB cnt) * groupA num * groupB num * Cnt
		return (a.size() + b.size()) * aVal * bVal * cnt;
	}
	
	public static int[][][] divdeSquare(int leng) {
		
		int[][][] squareSet = new int[4][][];
		// 1사분면
		int[][] firstSquare = new int[leng][leng];
		
		for(int i = 0; i < leng; i++)
			for(int j = 0; j < leng; j++)
				firstSquare[i][j] = board[i][j];
		squareSet[0] = firstSquare;
		// 2사분면
		int[][] secondSquare = new int[leng][leng];
		
		for(int i = 0; i < leng; i++)
			for(int j = 0; j < leng; j++)
				secondSquare[i][j] = board[i][(N / 2) + 1 + j];
		squareSet[1] = secondSquare;
		// 3사분면
		int[][] thirdSquare = new int[leng][leng];
		
		for(int i = 0; i < leng; i++)
			for(int j = 0; j < leng; j++)
				thirdSquare[i][j] = board[(N / 2) + 1 + i][j];
		squareSet[2] = thirdSquare;
		// 4사분면
		int[][] fourthSquare = new int[leng][leng];
		
		for(int i = 0; i < leng; i++)
			for(int j = 0; j < leng; j++)
				fourthSquare[i][j] = board[(N / 2) + 1 + i][(N / 2) + 1 + j];
		squareSet[3] = fourthSquare;
		
		return squareSet;
	}
	
	public static int[][] rotateSquare(int[][] original) {
		int n = original.length;
		int[][] newSquare = new int[n][n];
		
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				newSquare[i][j] = original[n - j - 1][i];
		
		return newSquare;
	}
	
	// 3. 회전하는 메소드
	public static void rotate() {
		// STEP 1. newBoard setting
		int[][] newBoard = new int[N][N];	
	
		// STEP 2. 정사각형 십자모양 통째로  반시계 방향 회전
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)
				newBoard[i][j] = board[j][N - i - 1];
		
//		 System.out.println("반시계 방향으로 돌린 후 : ");
//		 print(newBoard);
		
		// STEP 3. 정사각형 4개 개별적 시계 방향 회전 (메소드로 분리 추천!)
		int[][][] squareSet = divdeSquare(N / 2);
		int[][] tmp = {{0,0}, {0, (N / 2) + 1}, {(N / 2) + 1, 0}, {(N / 2) + 1, (N / 2) + 1}};
		for(int i = 0; i < 4; i++) {
			squareSet[i] = rotateSquare(squareSet[i]);

			int stX = tmp[i][0];
			int stY = tmp[i][1];
			for(int x = stX; x < stX + N / 2; x++)
				for(int y = stY; y < stY + N / 2; y++)
					newBoard[x][y] = squareSet[i][x - stX][y - stY];
		}
		
//		System.out.println("정사각형 시계 방향 돌린 후 : ");
//		print(newBoard);
		board = newBoard;
	}
	
	// <-------------- Debug --------------->
	
	public static void print(int[][] board) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				sb.append(board[i][j]).append(" ");
				}
			sb.append("\n");
			}
		
		System.out.println(sb);
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		// STEP 1. Get Data
		N = Integer.parseInt(br.readLine());
		board = new int[N][N];
		score = 0;
		
		numSetList = new ArrayList<ArrayList<Position>>();

		for(int i = 0; i< N; i++) {
			st = new StringTokenizer(br.readLine());
			
			for(int j = 0; j < N; j++) {
				int data = Integer.parseInt(st.nextToken());
				board[i][j] = data;
			}
		}

		// STEP 2. Main - Simulation ( 1회,2회,3회 )
		for(int cnt = 0; cnt<4; cnt++) {
			// 새로운 단계 -> 모든 방문 격좌 & 그룹 집합 리셋
			visited = new boolean[N][N];
			numSetList = new ArrayList<ArrayList<Position>>();

			for(int i = 0; i < N; i++)
				for(int j = 0; j < N; j++) {
					if(!visited[i][j])
						bfs(i, j);
				}

			for(int i = 0; i < numSetList.size(); i++)
				for(int j = i + 1; j < numSetList.size(); j++) {
					int tmp = getHarmony( numSetList.get(i), numSetList.get(j) );
					//System.out.println(i+1 + "G && "+ (j+1) +"G = "+ " "+tmp);
					score += tmp;
				}
			rotate();
		}

		System.out.println(score);
	}

}