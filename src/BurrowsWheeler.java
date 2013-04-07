import java.util.Arrays;

/**
 * User: huangcd (huangcd.thu@gmail.com)
 * Date: 5/13/13
 * Time: 6:37 PM
 */
public class BurrowsWheeler {
    /**
     * apply Burrows-Wheeler encoding,
     * reading from standard input and writing to standard output
     */
    public static void encode() {
        StringBuilder buffer = new StringBuilder();
        int c;
        // read to end
        while (!BinaryStdIn.isEmpty()) {
            c = BinaryStdIn.readChar();
            buffer.append((char) c);
        }

        CircularSuffixArray csa = new CircularSuffixArray(buffer.toString());
        int first = -1;
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                first = i;
                break;
            }
        }

        // output first
        BinaryStdOut.write(first);

        // output suffix
        for (int i = 0, last = csa.length() - 1; i < csa.length(); i++) {
            int index = (csa.index(i) + last) % csa.length();
            BinaryStdOut.write(buffer.charAt(index));
        }

        BinaryStdOut.flush();
    }

    /**
     * apply Burrows-Wheeler decoding,
     * reading from standard input and writing to standard output
     */
    public static void decode() {

        StringBuilder buffer = new StringBuilder();
        int first = 0;
        int c;
        for (int i = 0; i < 4; i++) {
            first = first << 8 | BinaryStdIn.readChar();
        }

        // read to end
        while (!BinaryStdIn.isEmpty()) {
            c = BinaryStdIn.readChar();
            buffer.append((char) c);
        }

        char[] tail = new char[buffer.length()];
        buffer.getChars(0, buffer.length(), tail, 0);

        char[] head = new char[tail.length];
        System.arraycopy(tail, 0, head, 0, tail.length);
        Arrays.sort(head);

        int[] next = next(tail, head);

        int index = first;
        for (char ignored : tail) {
            BinaryStdOut.write(head[index]);
            index = next[index];
        }

        BinaryStdOut.flush();
    }

    private static int[] next(char[] tail, char[] head) {
        int[] searchStart = new int[256];

        int[] next = new int[tail.length];
        for (int i = 0; i < tail.length; i++) {
            char c = tail[i];
            int index = search(head, searchStart[c], c);
            searchStart[c] = index + 1;
            next[index] = i;
        }

        return next;
    }

    private static int search(char[] characters, int from, char c) {
        for (int i = from; i < characters.length; i++) {
            if (characters[i] == c) {
                return i;
            }
        }
        return -1;
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }
        if ("-".equals(args[0])) {
            encode();
        }
        if ("+".equals(args[0])) {
            decode();
        }
    }
}
