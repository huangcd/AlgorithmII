import java.util.ArrayList;
import java.util.HashSet;

/**
 * User: huangcd (huangcd.thu@gmail.com)
 * Date: 3/26/13
 * Time: 4:59 PM
 */
public class SAP {
    private Digraph graph;
    private Digraph rev;

    /**
     * constructor takes a digraph (not necessarily a DAG)
     *
     * @param G a digraph
     */
    public SAP(Digraph G) {
        graph = G;
        rev = graph.reverse();
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

    private void validate(int i)
    {
        if (i < 0 || i >= graph.V())
        {
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
        final int INFINITE = Integer.MAX_VALUE;
        // graph has been changed
        if (rev.E() != graph.E())
        {
            rev = graph.reverse();
        }
        int size = graph.V();
        boolean[] marked = new boolean[size * 2];
        int[] dist = new int[size * 2];
        int[] edgeTo = new int[size * 2];
        for (int i = 0; i < size * 2; i++) {
            dist[i] = INFINITE;
        }
        HashSet<Integer> destinations = new HashSet<Integer>();
        for (int w : ws) {
            validate(w);
            destinations.add(w);
        }
        Queue<Integer> queue = new Queue<Integer>();
        for (int v : vs) {
            validate(v);
            dist[v] = 0;
            marked[v] = true;
            queue.enqueue(v);
        }
        while (!queue.isEmpty()) {
            int s = queue.dequeue();
            if (destinations.contains(s % size)) {
                int anc = s;
                while (anc >= size)
                {
                    anc = edgeTo[anc];
                }
                return new int[]{anc, dist[s]};
            }
            // not yet reverse
            if (s < size) {
                for (int d : graph.adj(s)) {
                    // not visited yet
                    if (!marked[d]) {
                        marked[d] = true;
                        dist[d] = dist[s] + 1;
                        edgeTo[d] = s;
                        queue.enqueue(d);
                    }
                }
                // reverse from edge (d -> s)
                for (int revD : rev.adj(s)) {
                    int d = revD + size;
                    if (!marked[d]) {
                        marked[d] = true;
                        dist[d] = dist[s] + 1;
                        edgeTo[d] = s;
                        queue.enqueue(d);
                    }
                }
            }
            // already reversed
            else {
                for (int revD : rev.adj(s - size))
                {
                    int d = revD + size;
                    if (!marked[d]) {
                        marked[d] = true;
                        dist[d] = dist[s] + 1;
                        edgeTo[d] = s;
                        queue.enqueue(d);
                    }
                }
            }
        }
        return new int[]{-1, -1};
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
