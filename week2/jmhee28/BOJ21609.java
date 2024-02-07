import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
//인접한 칸:|r1-r2|+|c1-c2| =1을 만족하는 두 칸(r1,c1)과(r2,c2)

//블록 그룹:  연결된 블록의 집합
//그룹: 일반 블록이 적어도 하나 있어야 하며
//     일반 블록의 색은 모두 같아야 한다
//      검은색 블록은 포함되면 안 되고,
//      무지개 블록은 얼마나 들어있든 상관없다.
//      블록 그룹에 속한 블록의 개수>= 2
//  임의의 한 블록에서 그룹에 속한 인접한 칸으로 이동해서 그룹에 속한 다른 모든 칸으로 이동할 수 있어야 한다.
//  블록 그룹의 기준 블록: 무지개 블록이 아닌 블록 중에서 행의 번호가 가장 작은 블록, 그러한 블록이 여러개면 열의 번호가 가장 작은 블록이다.

public class BOJ21609 {
    static int[][] map;
    static int n, m;

    static int BLACK = -1;
    static int RAINBOW = 0;
    static int EMPTY = -2;
    static int score = 0;
    static int[] dx = {0, 0, 1, -1};
    static int[] dy = {1, -1, 0, 0};
    static List<Block> rainbowBlocks;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] input = br.readLine().split(" ");
        n = Integer.parseInt(input[0]);
        m = Integer.parseInt(input[1]);
        map = new int[n][n];
        rainbowBlocks = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            input = br.readLine().split(" ");
            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(input[j]);
            }
        }
        while (true) {
            makeRainbowBlock();
            BlockGroup blockGroup = getBlockGroup();
            if (blockGroup.size < 2) break;
            score += blockGroup.size * blockGroup.size;
            removeBlocks(blockGroup);
            gravity();
            rotate();
            gravity();
        }

        System.out.println(score);

    }
    static void makeRainbowBlock() {
        rainbowBlocks = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(map[i][j] == RAINBOW) rainbowBlocks.add(new Block(i, j, RAINBOW));
            }
        }
    }
    // 반시계 방향으로 90도 회전
    static void rotate() {
        int[][] temp = new int[n][n];
        int cnt = 0;
        while(cnt < n){
            int[] tempArr = new int[n];
            for (int i = 0; i < n; i++) {
                tempArr[i] = map[i][cnt];
            }
            temp[n-cnt-1] = tempArr;
            cnt++;
        }
        map = temp;
    }

    static void gravity() {
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] == EMPTY) {
                    for (int k = i - 1; k >= 0; k--) {
                        if (map[k][j] == BLACK) break;
                        if (map[k][j] >= 0) {
                            map[i][j] = map[k][j];
                            map[k][j] = EMPTY;
                            break;
                        }
                    }
                }
            }
        }
    }

    static void removeBlocks(BlockGroup blockGroup) {
        for (Block block : blockGroup.blocks) {
            map[block.x][block.y] = EMPTY;
        }
    }

    static BlockGroup getBlockGroup() {
        int[][] visited = new int[n][n];
        BlockGroup blockGroup = new BlockGroup();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (visited[i][j] == 0 && map[i][j] > 0) {
                    BlockGroup nblockGroup = bfs(i, j, visited);
                    if (nblockGroup.size < 2 || nblockGroup.getRealSize() < 1) continue;

                    if (blockGroup.size < nblockGroup.size) {
                        blockGroup = nblockGroup;
                    } else if (blockGroup.size == nblockGroup.size) {
                        if (blockGroup.rainbowCnt < nblockGroup.rainbowCnt) {
                            blockGroup = nblockGroup;
                        } else if (blockGroup.rainbowCnt == nblockGroup.rainbowCnt) {
                            if (blockGroup.standardBlock.x < nblockGroup.standardBlock.x) {
                                blockGroup = nblockGroup;
                            } else if (blockGroup.standardBlock.x == nblockGroup.standardBlock.x) {
                                if (blockGroup.standardBlock.y < nblockGroup.standardBlock.y) {
                                    blockGroup = nblockGroup;
                                }
                            }
                        }
                    }
                    for(Block rainbow: rainbowBlocks) {
                        visited[rainbow.x][rainbow.y] = 0;
                    }
                }
            }
        }
        return blockGroup;
    }

    static BlockGroup bfs(int x, int y, int[][] visited) {
        BlockGroup blockGroup = new BlockGroup();
        blockGroup.addBlock(new Block(x, y, map[x][y]));
        int color = map[x][y];
        blockGroup.color = color;

        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{x, y});
        visited[x][y] = 1;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int cx = cur[0];
            int cy = cur[1];

            for (int i = 0; i < 4; i++) {
                int nx = cx + dx[i];
                int ny = cy + dy[i];
                if (!isRange(nx, ny)) continue;
                if (visited[nx][ny] != 0) continue;
                if (map[nx][ny] < 0) continue;
                if (map[nx][ny] == RAINBOW || map[nx][ny] == color) {
                    visited[nx][ny] = visited[cx][cy] + 1;
                    q.add(new int[]{nx, ny});
                    blockGroup.addBlock(new Block(nx, ny, map[nx][ny]));
                }
            }
        }
        return blockGroup;
    }

    static boolean isRange(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < n;
    }

    static class Block {
        int x;
        int y;
        int color;

        public Block(int x, int y, int color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }

    static class BlockGroup {
        int rainbowCnt;
        int size;
        List<Block> blocks;
        List<Block> rainbowBlocks;
        Block standardBlock;
        int color;

        public BlockGroup() {
            this.blocks = new LinkedList<>();
            this.rainbowBlocks = new LinkedList<>();
            standardBlock = new Block(n, n, BLACK);
        }

        public void addBlock(Block block) {
            this.blocks.add(block);
            if (block.color == RAINBOW) {
                this.rainbowCnt++;
                this.rainbowBlocks.add(block);
            }
            else {
                if (this.size == 0) {
                    this.standardBlock = block;
                } else {
                    if (this.standardBlock.x > block.x) {
                        this.standardBlock = block;
                    } else if (this.standardBlock.x == block.x) {
                        if (this.standardBlock.y > block.y) {
                            this.standardBlock = block;
                        }
                    }
                }
            }

            this.size++;
        }

        public int getRealSize() {
            return this.size - this.rainbowCnt;
        }
    }
}
