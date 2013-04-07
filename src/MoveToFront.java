import java.util.LinkedList;

/**
 * User: huangcd (huangcd.thu@gmail.com)
 * Date: 5/13/13
 * Time: 5:04 PM
 */
public class MoveToFront {

    /**
     * apply move-to-front encoding,
     * reading from standard input and writing to standard output
     */
    public static void encode() {
        LinkedList<Character> alphabet = new LinkedList<Character>();
        for (char c = 0; c < 256; c++) {
            alphabet.addLast(c);
        }

        char c;
        while (!BinaryStdIn.isEmpty()) {
            c = BinaryStdIn.readChar();
            // Get index
            int index = alphabet.indexOf(c);
            alphabet.remove(index);

            // Output
            BinaryStdOut.write((char) index);

            // Adjust alphabet
            alphabet.addFirst(c);
        }

        BinaryStdOut.flush();
    }

    /**
     * apply move-to-front decoding,
     * reading from standard input and writing to standard output
     */
    public static void decode() {
        LinkedList<Character> alphabet = new LinkedList<Character>();
        for (char c = 0; c < 256; c++) {
            alphabet.addLast(c);
        }

        int index;
        while (!BinaryStdIn.isEmpty()) {
            index = BinaryStdIn.readChar();
            char c = alphabet.remove(index);
            BinaryStdOut.write(c);

            // Adjust alphabet
            alphabet.addFirst(c);
        }

        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
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
