import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Main {
	
	static int N, M, answer;
	static int[][] map, dir = {{0,1}, {0,-1}, {1,0}, {-1,0}};
	static boolean[][] visited;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		st = new StringTokenizer(br.readLine(), " ");
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		map = new int[N][M];
		visited = new boolean[N][M];
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			for(int j=0; j<M; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				DFS(i, j, map[i][j], 0);
				calc(i, j);
				visited[i][j] = false;
			}
		}
		
		System.out.println(answer);
		
	}
	
	public static void calc(int x, int y) {
		
		for(int i=0; i<4; i++) {
			
			int sum = map[x][y];
			boolean flag = true;
			
			for(int j=0; j<4; j++) {
				if(i == j) continue;
				int nx = x + dir[j][0];
				int ny = y + dir[j][1];
				
				if(!isInside(nx, ny)) {
					flag = false;
					break;
				} else sum += map[nx][ny];
			}
			
			if(flag) answer = Math.max(answer, sum);
		}
	}
	
	public static void DFS(int x, int y, int sum, int len) {
		
		visited[x][y] = true;
		
		if(len == 3) {
			answer = Math.max(answer, sum);
			return;
		}
		
		for(int i=0; i<4; i++) {
			int nx = x + dir[i][0];
			int ny = y + dir[i][1];
			
			if(isInside(nx, ny) && !visited[nx][ny]) {
				DFS(nx, ny, sum + map[nx][ny], len + 1);
				visited[nx][ny] = false;
			}
		}
	}
	
	public static boolean isInside(int x, int y) {
		return x>=0 && x<N && y>=0 && y<M;
	}
	
}