import java.io.*;
import java.util.*;

public class BOJ1561 {
    static long n;
    static int m;
    static long maxTime = 0L;
    static long[] amusementsTime;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] nm = br.readLine().split(" ");
        n = Long.parseLong(nm[0]);
        m = Integer.parseInt(nm[1]);
        amusementsTime = new long[m];

        String[] amusementStr = br.readLine().split(" ");
        for (int i = 0; i < m; i++) {
            long t = Long.parseLong(amusementStr[i]);
            maxTime = Math.max(maxTime, t);
            amusementsTime[i] = t;
        }
        if (n <= m) {
            System.out.println(n);
        } else
            solve();
    }

    static long binarySearch() {
        long left = 1;
        long right = maxTime * n;
        long result = maxTime * n;
        while (left < right) {
            long mid = (left + right) / 2;
            long cnt = 0L;
            for (int i = 0; i < m; i++) {
                cnt += (mid / amusementsTime[i]) + 1;
            }
            if (cnt >= n) {
                result = Math.min(mid, result);
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return result;
    }

    static void solve() {
        long requiredTime = binarySearch();
        long temp = 0L;
        for (int i = 0; i < m; i++) {
            temp += (requiredTime - 1) / amusementsTime[i] + 1;
        }
        for (int i = 0; i < m; i++) {
            if (requiredTime % amusementsTime[i] == 0) {
                temp += 1;
                if (temp == n) {
                    System.out.println(i + 1);
                    return;
                }
            }
        }
    }
}
