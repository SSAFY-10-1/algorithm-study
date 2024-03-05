package WS0227;

import java.util.*;
import java.io.*;

public class BOJ17143 {
	
	static int R, C, M;
	static int[][] board;
	
	static class Shark {
		int r, c;
		int speed, dir, size;
		
		public Shark(int r, int c, int s, int d, int z) {
			this.r = r;
			this.c = c;
			this.speed = s;
			this.dir = d;
			this.size = z;
		}
	}
	
	static int[] dx = {-1,1,0,0};
	static int[] dy = {0,0,1,-1};
	
	public static boolean inRange(int x, int y) {
		return (0 <= x && x < R) && (0 <= y && y < C);
	}
	
	public static int reverseDir(int dir) {
		if(0 <= dir && dir < 2) return 1 - dir;
		else return 5 - dir;
	}
	
	static int fishKingPos = -1; // 0 ~ C 까지 이동하며, 모든 [0 ~ R][fishKingPos] 탐색
	static int getFish;
	
	static Shark[] sharkList; // 1번 상어 ~ M번 상어
	static int[][] nextSharkPos; // 다음 맵에 갱신할 상어 위치
	
	// <------------- Method ------------------>
	
	public static boolean fishKing() {
		if(fishKingPos + 1 >= C)
			return false;
		
		fishKingPos = fishKingPos + 1;

		for(int i = 0; i < R; i++) {
			int check = board[i][fishKingPos]; 
			
			if(check != 0) {
				getFish += sharkList[check].size;
				
				sharkList[check] = null;
				board[i][fishKingPos] = 0;
				
				break;
			}
		}
		
		return true;
	}
	// 상어는 순차적으로 움직이는 게 아니라, 모두 한꺼번에 움직여야 한다. ( 아직 자기 턴이 오지 않았는데 잡아먹히면 모순된다. )
	public static void sharkMove(int sharkNum) {
		
		// STEP 1. 해당 상어의 방향과 속력을 구한다.
		int sharkDir = sharkList[sharkNum].dir;
		int sharkSpeed = sharkList[sharkNum].speed;
		int sharkSize = sharkList[sharkNum].size;
		
		// STEP 2. 속력만큼 이동한 좌표를 구한다. ( 단, 격좌 범위를 넘어가면 방향을 반대로 바꾼다.)
		int sx = sharkList[sharkNum].r;
		int sy = sharkList[sharkNum].c;
		
		for(int i = 0; i < sharkSpeed; i++) {
			if(!inRange(sx + dx[sharkDir], sy + dy[sharkDir]))
				sharkDir = reverseDir(sharkDir);
			
			sx = sx + dx[sharkDir];
			sy = sy + dy[sharkDir];
		}
		// STEP 3.상어 객체를 갱신한다.
		sharkList[sharkNum] = new Shark(sx, sy, sharkSpeed, sharkDir, sharkSize);
		if(nextSharkPos[sx][sy] != 0) {
			int alreadySharkNum = nextSharkPos[sx][sy];
			if(sharkList[alreadySharkNum].size > sharkList[sharkNum].size) {
				sharkList[sharkNum] = null;
			}
			else  {
				sharkList[alreadySharkNum] = null;
				nextSharkPos[sx][sy] = sharkNum;
			}
		}
		else nextSharkPos[sx][sy] = sharkNum;
	}
	
	// <--- debug --->
	public static void print() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < R; i++) {
			for(int j = 0; j < C; j++) {
				sb.append(board[i][j]).append(" ");
			}
			sb.append("\n");
		}
		System.out.println(sb);
		System.out.println();
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		R = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		board = new int[R][C];
		
		sharkList = new Shark[M+1];
		for(int i = 1; i <= M; i++) {
			st = new StringTokenizer(br.readLine());
			
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int s = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			int z = Integer.parseInt(st.nextToken());
			
			// 최적화 고려 안하면 14%에서 시간 초과..
			//규칙이 있다! (메모장으로 설명한다)
			
			if(d <= 2) s = s%((R-1)*2);
			else s = s%((C-1)*2); 
			
			sharkList[i] = new Shark(r-1, c-1, s, d-1, z);
			board[r-1][c-1] = i;
		}
		
		getFish = 0;
		
		// Simulation
		while(fishKing()) {
			// 다음 맵 세팅
			nextSharkPos = new int[R][C];
			
			for(int i = 1; i <= M; i++)
				if(sharkList[i] != null) sharkMove(i);
			
			board = nextSharkPos;
		}
		
		System.out.println(getFish);
	}
}
