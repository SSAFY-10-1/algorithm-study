import java.util.*;
import java.io.*;

public class DeleteTree {
	// N = 격자 크기, M = 박멸 진행되는 수, K = 제초제 확산 범위, C = 제초제가 남아있는 년 수
	static int N, M, K, C;
	// Graph = 나무를 담을 2차원 격자
	static int[][] graph;
	// treeKillerMaps = 제초제가 남아있음을 표시하는 2차원 격자
	static int[][] treeKillerMaps;
	// M년 동안 박멸한 나무의 수를 저장하는 변수 = 정답을 출력 하는 변수
	static int result = 0;

	// dx & dy + inRange Func
	static int[] dx = {-1,0,1,0};
	static int[] dy = {0,1,0,-1};

	public static boolean inRange(int x, int y) {
		return ((0 <= x && x < N) && (0 <= y && y < N));
	}

	public static class Position {
		int x, y;

		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public static class KillerPos implements Comparable<KillerPos> {
		int st_x, st_y;
		List<Position> posSet;
		int killTree;

		public KillerPos(int x, int y, List<Position> posSet, int killTree) {
			this.st_x = x;
			this.st_y = y;
			this.posSet = posSet;
			this.killTree = killTree;
		}

		@Override
		public int compareTo(KillerPos o) {
			if(this.killTree == o.killTree) {
				if(this.st_x == o.st_x)
					return this.st_y - o.st_y;

				return this.st_x - o.st_x;
			}
			return o.killTree - this.killTree;
		}
	}

	// <--------------- 구현 해야 할 메소드 ---------------->
	public static void growTree() {

		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++) {
				if(graph[i][j] > 0) {
					int cnt = 0;

					for(int k = 0; k < 4; k++) {
						int nx = i + dx[k];
						int ny = j + dy[k];

						if(inRange(nx, ny) && graph[nx][ny] > 0)
							cnt++;
					}

					graph[i][j] += cnt;
				}
			}
	}

	public static int[][] spreadTree() {

		int[][] newGraph = new int[N][N];
		for(int i = 0; i < N; i++)
			newGraph[i] = Arrays.copyOf(graph[i], graph[i].length);

		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++) {

				if(graph[i][j] > 0) {

					// 인접한 4개의 칸 중 벽, 다른 나무, 제초제 모두 없는 칸
					ArrayList<Position> posSet = new ArrayList<Position>();
					int isPossible = 0;

					for(int k = 0; k < 4; k++) {
						int nx = i + dx[k];
						int ny = j + dy[k];

						if(inRange(nx, ny)) {
							if(graph[nx][ny] == 0 && treeKillerMaps[nx][ny] == 0) {
								posSet.add(new Position(nx, ny));
								isPossible++;
							}
						}
					}

					if(isPossible > 0) {
						int newTree = graph[i][j] / isPossible;

						for(Position newTreePos : posSet)
							newGraph[newTreePos.x][newTreePos.y] += newTree;
					}
				}
			}
		return newGraph;
	}

	// (st_x, st_y)를 시작으로 제초제를 뿌려보는 함수
	public static KillerPos treeKiller(int st_x, int st_y) {
		int[] rx = {-1,-1,1,1};
		int[] ry = {-1,1,-1,1};

		List<Position> posSet = new ArrayList<Position>();
		posSet.add(new Position(st_x, st_y));
		int countTree = graph[st_x][st_y];

		for(int i = 0; i < 4; i++) {

			int x = st_x + rx[i];
			int y = st_y + ry[i];

			for(int tmp = 0; tmp < K; tmp++) {
				if(inRange(x, y)) {
					if(graph[x][y] < 1) {
						posSet.add(new Position(x, y));
						break;
					}
					posSet.add(new Position(x, y));
					countTree += graph[x][y];
					x = x + rx[i];
					y = y + ry[i];
				}
			}
		}

		return new KillerPos(st_x, st_y, posSet, countTree);
	}

	public static void compareTreeKiller() {

		List<KillerPos> killerSet = new ArrayList<KillerPos>();

		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++) {
				if(graph[i][j] > 0)
					killerSet.add(treeKiller(i, j));
			}

		if(killerSet.isEmpty())
			return;
		Collections.sort(killerSet);

		KillerPos getBiggestKiller = killerSet.get(0);

		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++) {
				if(treeKillerMaps[i][j] >= 1)
					treeKillerMaps[i][j]--;
			}

		for(Position pos : getBiggestKiller.posSet) {
			if (graph[pos.x][pos.y] >= 0) {
				graph[pos.x][pos.y] = 0;
				treeKillerMaps[pos.x][pos.y] = C;
			}
		}

		result += getBiggestKiller.killTree;
	}
	// <--------------- 구현 해야 할 메소드 ---------------->

	public static void main(String[] args) throws IOException {
		// STEP 1. INPUT
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());

		graph = new int[N][N];
		treeKillerMaps = new int[N][N];

		for(int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++)
				graph[i][j] = Integer.parseInt(st.nextToken());
		}

		// STEP 2. Simulation
		for(int year = 0; year < M; year++) {

			growTree();

			graph = spreadTree();

			compareTreeKiller();
		}

		System.out.println(result);
	}
}