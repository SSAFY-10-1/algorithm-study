import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.StringTokenizer;

public class Main {

	public static void main(String[] args) throws NumberFormatException, IOException {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		int n = Integer.parseInt(in.readLine());
		int[][] board = new int[n][n];
		board[0][0] = 1;
//		System.out.println("new board");
//		printList(board);

		int[] snakeHead = { 0, 0 };
		int[] snakeTail = { 0, 0 };
		int[] dx = { 1, 0, -1, 0 };
		int[] dy = { 0, 1, 0, -1 };
		int dir = 0;
		
		int totalTime = 0;
		boolean game = true;
		
		int k = Integer.parseInt(in.readLine());

		for (int i = 0; i < k; i++) {
			int[] tempArr = new int[2];
			StringTokenizer st = new StringTokenizer(in.readLine());
			for (int j = 0; j < 2; j++) {
				tempArr[j] = Integer.parseInt(st.nextToken());
			}
			board[tempArr[0] - 1][tempArr[1] - 1] = 4;
		}
		
//		System.out.println("apple board");
//		printList(board);
		
		int l = Integer.parseInt(in.readLine());
		
		int prevTime = 0;
		
		ArrayDeque<int[]> body = new ArrayDeque();
		body.addFirst(new int[] {0, 0});
		
		
		
		loop: for (int i = 0; i < l; i++) {
			StringTokenizer st = new StringTokenizer(in.readLine());
			int nextTime= Integer.parseInt(st.nextToken());
			int time = nextTime-prevTime;
			prevTime = nextTime;
//			System.out.println(nextTime);
			
			String direction = st.nextToken();

			for (int j = 0; j < time; j++) {
				int nx = snakeHead[0] + dx[dir];
				int ny = snakeHead[1] + dy[dir];

				if (inBoard(n, nx, ny) && board[ny][nx] == 4) {
					board[ny][nx] = 1;
					snakeHead[0] = nx;
					snakeHead[1] = ny;
					body.addFirst(new int[] {nx, ny});
					totalTime++;
//					System.out.println("4" + totalTime);
//					printList(board);
				} else if (inBoard(n, nx, ny) && board[ny][nx] == 0) {
					board[ny][nx] = 1;
					snakeHead[0] = nx;
					snakeHead[1] = ny;
					body.addFirst(new int[] {nx, ny});
					snakeTail = body.pollLast();
					board[snakeTail[1]][snakeTail[0]] = 0;
					totalTime++;
//					System.out.println("0" + totalTime);
//					printList(board);
				} else {
//					System.out.println("1" + totalTime);
					game = false;
					break loop;
				}
			}
			
			if (direction.equals("L")) {
				dir += 3;
			} else if (direction.equals("D")) {
				dir += 1;
			}
			dir %= 4;
//			System.out.println("dir"+dir);
		}
		

		
		int lx = snakeHead[0];
		int ly = snakeHead[1];
		
		while(game) {
			lx += dx[dir];
			ly += dy[dir];
			if(!inBoard(n, lx, ly)) {
//				System.out.println("break" + totalTime);
				break;
			}
			if ( board[ly][lx] == 4) {
				board[ly][lx] = 1;
				snakeHead[0] = lx;
				snakeHead[1] = ly;
				body.addFirst(new int[] {lx, ly});
				totalTime++;
//				System.out.println("4" + totalTime);
//				printList(board);
			} else if (board[ly][lx] == 0) {
				board[ly][lx] = 1;
				snakeHead[0] = lx;
				snakeHead[1] = ly;
				body.addFirst(new int[] {lx, ly});
				snakeTail = body.pollLast();
				board[snakeTail[1]][snakeTail[0]] = 0;
				totalTime++;
//				System.out.println("0" + totalTime);
//				printList(board);
			} else {
//				System.out.println("break" + totalTime);
				break;
			}
		}
		System.out.println(totalTime+1);

	}

	private static void printHeadTail(int[] snakeHead, int[] snakeTail) {
		System.out.println("snaketail[0]:"+snakeTail[0]+"snaketail[1]:"+snakeTail[1]);
		System.out.println("snakeHead[0]:"+snakeHead[0]+"snakeHead[1]:"+snakeHead[1]);
	}

	private static boolean inBoard(int n, int nx, int ny) {
		return nx >= 0 && nx < n && ny >= 0 && ny < n;
	}

	static void printList(int[][] arr) {
		for (int[] i : arr) {
			System.out.println(Arrays.toString(i));
		}
		System.out.println();
	}

}