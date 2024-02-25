
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class BOJ15683 {
	
	
	// 상 우 하 좌
	static int[] dr = {-1, 0, 1, 0};
	static int[] dc = {0, 1, 0, -1};
	static int N;
	static int M;
	static int min = Integer.MAX_VALUE;
	static int cctvNumber = 0;
	
	static StringBuilder pr;
	
	
	static class CCTV{
		int type;
		int r;
		int c;
		public CCTV(int type, int r, int c) {
			this.type = type;
			this.r = r;
			this.c = c;
		}
		
	}
	
	static List<CCTV> cctvList = new ArrayList<>();
	static int[] combi;
	private static int[][] firstRoom;
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		firstRoom = new int[N][M];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				firstRoom[i][j] = Integer.parseInt(st.nextToken());
				if(firstRoom[i][j]>0 && firstRoom[i][j]<6) {
					cctvList.add(new CCTV(firstRoom[i][j], i, j));
				}
			}
		}
		
		cctvNumber = cctvList.size();
		
		combi = new int[cctvNumber];
		
		DFS(0);
		
		System.out.println(min);
		
	}
	
	public static void DFS(int cnt) {
		if(cnt == cctvNumber) {
			int[][] newRoom = new int[N][M];
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < M; j++) {
					newRoom[i][j] = firstRoom[i][j];
				}
			}
			
			for (int i = 0; i < combi.length; i++) {
				switch (cctvList.get(i).type) {
				case 1:
					newRoom = one(newRoom, cctvList.get(i).r, cctvList.get(i).c, combi[i]);
					break;
				case 2:
					newRoom = two(newRoom, cctvList.get(i).r, cctvList.get(i).c, combi[i]);
					break;
				case 3:
					newRoom = three(newRoom, cctvList.get(i).r, cctvList.get(i).c, combi[i]);
					break;
				case 4:
					newRoom = four(newRoom, cctvList.get(i).r, cctvList.get(i).c, combi[i]);
					break;
				case 5:
					newRoom = five(newRoom, cctvList.get(i).r, cctvList.get(i).c);
					break;
				}
			}
			countRoom(newRoom);
			return;
		}
		switch (cctvList.get(cnt).type) {
		case 1:
			for (int i = 0; i < 4; i++) {
				combi[cnt] = i;
				DFS(cnt+1);
			}
			break;
		case 2:
			for (int i = 0; i < 2; i++) {
				combi[cnt] = i;
				DFS(cnt+1);
			}
			break;
		case 3:
			for (int i = 0; i < 4; i++) {
				combi[cnt] = i;
				DFS(cnt+1);
			}
			break;
		case 4:
			for (int i = 0; i < 4; i++) {
				combi[cnt] = i;
				DFS(cnt+1);
			}
			break;
		case 5:
			combi[cnt] = -1;
			DFS(cnt+1);
			break;

		}
		
	}

	
	// 1번 : 1 방향
	public static int[][] one(int[][] room, int r, int c, int dir) {
		int nr = r + dr[dir];
		int nc = c + dc[dir];
		while(inBound(nr, nc) && room[nr][nc]!=6) {
			room[nr][nc] = 7;
			nr += dr[dir];
			nc += dc[dir];
		}
		return room;
	}
	
	// 2번 : 180도 양방향
	public static int[][] two(int[][] room, int r, int c, int dir) {
		for (int i = dir; i < dir+3; i+=2) {
			int nr = r + dr[i%4];
			int nc = c + dc[i%4];
			while(inBound(nr, nc) && room[nr][nc]!=6) {
				room[nr][nc] = 7;
				nr += dr[i%4];
				nc += dc[i%4];
			}
		}
		return room;
	}
	
	// 3번 : 90도 양방향
	public static int[][] three(int[][] room, int r, int c, int dir) {
		for (int i = dir; i < dir+2; i++) {
			int nr = r + dr[i%4];
			int nc = c + dc[i%4];
			while(inBound(nr, nc) && room[nr][nc]!=6) {
				room[nr][nc] = 7;
				nr += dr[i%4];
				nc += dc[i%4];
			}
		}
		return room;
	}
	
	// 4번 : 3방향
	public static int[][] four(int[][] room, int r, int c, int dir) {
		for (int i = dir; i < dir+3; i++) {
			int nr = r + dr[i%4];
			int nc = c + dc[i%4];
			while(inBound(nr, nc) && room[nr][nc]!=6) {
				room[nr][nc] = 7;
				nr += dr[i%4];
				nc += dc[i%4];
			}
		}
		return room;
	}
	
	// 5번 : 모든 방향 (상우하좌)
	public static int[][] five(int[][] room, int r, int c) {
		for (int i = 0; i < 4; i++) {
			int nr = r + dr[i];
			int nc = c + dc[i];
			while(inBound(nr, nc) && room[nr][nc]!=6) {
				room[nr][nc] = 7;
				nr += dr[i];
				nc += dc[i];
			}
		}
		return room;
	}
	
	public static boolean inBound(int r, int c) {
		if(r >= 0 && r < N && c >= 0 && c < M) return true;
		else return false;
	}
	
	public static void printRoom(int[][] room) {
		pr = new StringBuilder();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				pr.append(room[i][j]).append(" ");
			}
			pr.append("\n");
		}
		pr.append("\n");
		System.out.println(pr);
	}
	
	public static void countRoom(int[][] room) {
		int count = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if(room[i][j] == 0) count++;
			}
		}
		min = Math.min(min, count);
	}
}
