import java.util.Arrays;
import java.util.Comparator;

/**
 * User: huangcd (huangcd.thu@gmail.com)
 * Date: 5/13/13
 * Time: 6:16 PM
 */
public class CircularSuffixArray {
    private int length;
    private int[] indices;

    public CircularSuffixArray(String s) {
        this.length = s.length();
        final char[] characters = s.toCharArray();
        Integer[] _indices = new Integer[this.length];
        indices = new int[this.length];
        for (int i = 0; i < this.length; i++) {
            _indices[i] = i;
        }

        Arrays.sort(_indices, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                for (int i = 0; i < length; i++) {
                    char ca = characters[(i + a) % length];
                    char cb = characters[(i + b) % length];
                    if (ca != cb) {
                        return ca - cb;
                    }
                }
                return 0;
            }
        });

        for (int i = 0; i < this.length; i++) {
            indices[i] = _indices[i];
        }
    }

    public int length() {
        return length;
    }

    public int index(int i) {
        return indices[i];
    }

    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < csa.length(); i++) {
            System.out.println(csa.index(i) + " ");
        }
    }
}
