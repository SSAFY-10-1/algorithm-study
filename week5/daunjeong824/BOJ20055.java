import java.io.*;
import java.util.*;

public class Main {
	
	static int N, K, time;
	static List<Node> belt; // [ 1,2,3,...2N ]
	
	static int robotNum = 1;
	static int[] robotPos = new int[201];
	
	public static class Node {
		int hp;
		int robot;
		
		public Node(int hp, int robot) {
			this.hp = hp;
			this.robot = robot;
		}
	}
	
	public static void rotateBelt() {
		// 돌린다.
		Node last = belt.remove(belt.size() - 1);
		belt.add(0, last);
		removeRobot();
	}
	
	public static void moveRobot() {
		// belt 꼬리 -> 머리 순으로 탐색
		for(int idx = 2*N - 1; idx > -1; idx--) {
			if(belt.get(idx).robot == 0) continue;
			
			// queue 꼬리 -> 머리
			if(idx == 2*N - 1) {
				Node nextNode = belt.get(0);
				// Node.robot == 0 && 해당 되는 다음 Node.hp >=1 이상일 때
				if(nextNode.robot == 0 && nextNode.hp >= 1) {
					int robot_num = belt.get(idx).robot;
					belt.get(idx).robot = 0;
					
					belt.get(0).robot = robot_num;
					belt.get(0).hp--;
				}
			}
			// else -> 바로 앞 노드로 이동
			else {
				Node nextNode = belt.get(idx + 1);
				// Node.robot == 0 && 해당 되는 다음 Node.hp >=1 이상일 때
				if(nextNode.robot == 0 && nextNode.hp >= 1) {
					int robot_num = belt.get(idx).robot;
					belt.get(idx).robot = 0;
					
					belt.get(idx + 1).robot = robot_num;
					belt.get(idx + 1).hp--;
				}
				// 이동한 위치가 내리는 위치면 그 즉시 내린다. (내구도 감소 없이)
				if(idx + 1 == N - 1) removeRobot();
			}
			
		}
	}
	
	public static void removeRobot() {
		//queue[N-1] == 내리는 위치
		if(belt.get(N - 1).robot != 0)
			belt.get(N-1).robot = 0;
	}
	
	public static void putRobot() {
		// queue[0] = 올리는 위치 
		if(belt.get(0).hp != 0) {
			belt.get(0).robot = robotNum++;
			belt.get(0).hp--;
		}
	}
	
	 public static boolean check() {
		 int cnt = 0;
		 
		 Iterator<Node> iter = belt.iterator();
		 
		 while(iter.hasNext()) {
			 Node node = iter.next();
			 if(node.hp == 0) cnt++;
		 }
		 
		 if(cnt >= K)
			 return false;
		 
		 return true;
	 }
	
	public static void print() {
		StringBuilder sb = new StringBuilder();
		
		Iterator<Node> iter = belt.iterator();
		while(iter.hasNext()) {
			 Node node = iter.next();
			 sb.append("(hp : ").append(node.hp).append(", Robot : ").append(node.robot).append(") ");
		 }
		
		System.out.println(sb);
	}
	 
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		belt = new ArrayList<Node>();
		
		st = new StringTokenizer(br.readLine());
		for(int i = 0; i < 2*N; i++) {
			int initalHp = Integer.parseInt(st.nextToken());
			belt.add(new Node(initalHp, 0));
		}
		
		// Simulation
		time = 0;
		while(check()) {
			
//			System.out.println(time + " 번째 벨트 상태 (갱신 전)");
//			print();
			
			rotateBelt();
			moveRobot();
			putRobot();
			
//			System.out.println(time + " 번째 벨트 상태 (갱신 후)");
//			print();
//			System.out.println();
			time++;
		}
		
		System.out.println(time);
	}
}