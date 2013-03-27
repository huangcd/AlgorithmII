/**
 * User: huangcd (huangcd.thu@gmail.com)
 * Date: 3/26/13
 * Time: 10:08 PM
 */
public class Outcast {
    private WordNet wordNet;

    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(".\\data\\wordnet\\synsets.txt",
                ".\\data\\wordnet\\hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        while (!StdIn.isEmpty()) {
            System.out.println(outcast.outcast(StdIn.readLine().trim().split(" ")));
        }
    }

    public String outcast(String[] nouns) {
        int maxValue = 1000000;
        int length = nouns.length;
        int[][] distances = new int[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                int distance = wordNet.distance(nouns[i], nouns[j]);
                if (distance == -1) {
                    distances[i][j] = maxValue;
                    distances[j][i] = maxValue;
                } else {
                    distances[i][j] = distance;
                    distances[j][i] = distance;
                }
            }
        }
        int maxDistance = Integer.MIN_VALUE;
        String maxNoun = null;
        for (int i = 0; i < length; i++) {
            int sum = 0;
            for (int j = 0; j < length; j++) {
                sum += distances[i][j];
            }
            if (sum > maxDistance) {
                maxDistance = sum;
                maxNoun = nouns[i];
            }
        }
        return maxNoun;
    }
}
