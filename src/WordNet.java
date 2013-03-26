import java.util.*;

/**
 * User: huangcd (huangcd.thu@gmail.com)
 * Date: 13-3-26
 * Time: 下午2:30
 */
public class WordNet {
    private HashMap<String, ArrayList<Integer>> synsetMap;
    private HashMap<Integer, String> synsetArray;
    private Digraph wordNet;
    private SAP _sap;

    /**
     * constructor takes the name of the two input files
     *
     * @param synsets   synsets file path
     * @param hypernyms hypernyms file path
     */
    public WordNet(String synsets, String hypernyms) {
        synsetMap = new HashMap<String, ArrayList<Integer>>();
        synsetArray = new HashMap<Integer, String>();
        In synsetIn = new In(synsets);
        int maxIndex = 0;
        String line;
        while ((line = synsetIn.readLine()) != null) {
            String[] inputArray = line.split(",");
            if (inputArray.length >= 3) {
                int index = Integer.parseInt(inputArray[0]);
                synsetArray.put(index, inputArray[1]);
                String[] synsetWords = inputArray[1].split(" ");
                for (String synset : synsetWords) {
                    if (!synsetMap.containsKey(synset)) {
                        synsetMap.put(synset, new ArrayList<Integer>());
                    }
                    synsetMap.get(synset).add(index);
                }
                maxIndex = Math.max(maxIndex, index);
            } else {
                // something wrong happen...
            }
        }
        wordNet = new Digraph(maxIndex + 1);
        In hypernymIn = new In(hypernyms);
        while ((line = hypernymIn.readLine()) != null) {
            String[] inputArray = line.split(",");
            if (inputArray.length > 1) {
                int s = Integer.parseInt(inputArray[0]);
                for (int i = 1; i < inputArray.length; i++) {
                    int v = Integer.parseInt(inputArray[i]);
                    wordNet.addEdge(s, v);
                }
            } else {
                // something wrong happen...
            }
        }

        if (!isRootedDirectedAcylicGraph()) {
            throw new IllegalArgumentException("Input not a rooted directed acylic graph");
        }
        _sap = new SAP(wordNet);
    }

    public int wordCount()
    {
        return synsetArray.size();
    }

    private boolean isRootedDirectedAcylicGraph() {
        int root = 0;
        // get the root element
        int count = 1;
        while (true) {
            Iterator<Integer> adj = wordNet.adj(root).iterator();
            if (!adj.hasNext()) {
                break;
            }
            root = adj.next();
            count ++;
            if (count > wordNet.V())
            {
                return false;
            }
        }
        Digraph reverseGraph = wordNet.reverse();
        AcylicDepthFirstSearch search = new AcylicDepthFirstSearch(reverseGraph);
        search.init();
        return search.depthFirstSearchWithAcylicDetect(root) && search.count == reverseGraph.V();
    }

    private class AcylicDepthFirstSearch {
        private boolean[] marked;
        private boolean[] inStack;
        private Digraph graph;
        private int count;

        public AcylicDepthFirstSearch(Digraph graph) {
            this.graph = graph;
        }

        public void init() {
            marked = new boolean[graph.V()];
            inStack = new boolean[graph.V()];
            count = 0;
        }

        public boolean depthFirstSearchWithAcylicDetect(int s) {
            count++;
            marked[s] = true;
            inStack[s] = true;
            for (int v : graph.adj(s)) {
                if (inStack[v]) {
                    return false;
                }
                depthFirstSearchWithAcylicDetect(v);
            }
            inStack[s] = false;
            return true;
        }
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(".\\data\\wordnet\\synsets.txt",
                ".\\data\\wordnet\\hypernymsInvalidTwoRoots.txt");
        System.out.println(((Set<String>) wordnet.nouns()).size());
    }

    /**
     * @return returns all WordNet nouns
     */
    public Iterable<String> nouns() {
        return synsetMap.keySet();
    }

    /**
     * Is the word a WordNet noun?
     *
     * @param word word to check
     * @return Is the word a WordNet noun?
     */
    public boolean isNoun(String word) {
        return synsetMap.containsKey(word);
    }

    /**
     * distance between nounA and nounB
     *
     * @param nounA nounA
     * @param nounB nounB
     * @return sap distance
     */
    public int distance(String nounA, String nounB) {
        validate(nounA);
        validate(nounB);
        return _sap.length(synsetMap.get(nounA), synsetMap.get(nounB));
    }

    /**
     * a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
     * in a shortest ancestral path (defined below)
     *
     * @param nounA nounA
     * @param nounB nounB
     * @return common ancestor represented by synset
     */
    public String sap(String nounA, String nounB) {
        validate(nounA);
        validate(nounB);
        return synsetArray.get(_sap.ancestor(synsetMap.get(nounA), synsetMap.get(nounB)));
    }

    private void validate(String noun) {
        if (!isNoun(noun)) {
            throw new IllegalArgumentException(noun + " is not a WordNet nouns");
        }
    }
}
