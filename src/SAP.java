import java.util.ArrayList;

/**
 * User: huangcd (huangcd.thu@gmail.com)
 * Date: 3/26/13
 * Time: 4:59 PM
 */
public class SAP {
    private Digraph graph;

    /**
     * constructor takes a digraph (not necessarily a DAG)
     *
     * @param G a digraph
     */
    public SAP(Digraph G) {
        graph = new Digraph(G);
    }

    public static void main(String[] args) {
        In in = new In("./data/wordnet/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    private void validate(int i) {
        if (i < 0 || i >= graph.V()) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * @param v first vertex
     * @param w second vertex
     * @return length of shortest ancestral path between v and w; -1 if no such path
     */
    public int length(int v, int w) {
        ArrayList<Integer> vv = new ArrayList<Integer>();
        vv.add(v);
        ArrayList<Integer> ww = new ArrayList<Integer>();
        ww.add(w);
        return internalVisit(vv, ww)[1];
    }

    private int[] internalVisit(Iterable<Integer> vs, Iterable<Integer> ws) {
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(graph, vs);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(graph, ws);
        int minDistance = Integer.MAX_VALUE;
        int minIndex = -1;
        for (int i = 0, size = graph.V(); i < size; i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                int distance = bfsv.distTo(i) + bfsw.distTo(i);
                if (distance < minDistance) {
                    minIndex = i;
                    minDistance = distance;
                }
            }
        }
        if (minDistance == Integer.MAX_VALUE) {
            return new int[]{-1, -1};
        }
        return new int[]{minIndex, minDistance};
    }

    /**
     * a common ancestor of v and w that participates in a shortest ancestral path;
     * -1 if no such path
     */
    public int ancestor(int v, int w) {
        ArrayList<Integer> vv = new ArrayList<Integer>();
        vv.add(v);
        ArrayList<Integer> ww = new ArrayList<Integer>();
        ww.add(w);
        return internalVisit(vv, ww)[0];
    }

    /**
     * length of shortest ancestral path between any vertex in v and any vertex in w;
     * -1 if no such path
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return internalVisit(v, w)[1];
    }

    /**
     * a common ancestor that participates in shortest ancestral path;
     * -1 if no such path
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return internalVisit(v, w)[0];
    }
}
