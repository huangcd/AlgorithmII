import java.util.Arrays;

/**
 * User: huangcd (huangcd.thu@gmail.com)
 * Date: 4/7/13
 * Time: 1:51 PM
 */
public class Exercises {
    public static void main(String[] args) {
        String[] a = new String[]{"6265","6428","6616","1284","2315","5667","7282","1762","8351","8511","2264","7214"};
        int lo = 0;
        int hi = a.length - 1;
        int d = 0;
        if (hi <= lo) return;
        int lt = lo, gt = hi;
        int v = charAt(a[lo], d);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(a[i], d);
            if (t < v) exch(a, lt++, i++);
            else if (t > v) exch(a, i, gt--);
            else i++;
        }
        System.out.println(Arrays.toString(a));
    }

    private static int charAt(String str, int d) {
        return str.charAt(d) - '0';
    }

    private static void exch(String[] a, int i, int j)
    {
        String tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
}