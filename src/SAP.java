import java.util.ArrayList;

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
        rev = G.reverse();
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

    /**
     * @param v first vertex
     * @param w second vertex
     * @return length of shortest ancestral path between v and w; -1 if no such path
     */
    public int length(int v, int w) {
        return internalVisit(v, w)[1];
    }

    private int[] internalVisit(int v, int w) {
        boolean[] visited = new boolean[graph.V()];
        ArrayList<QueueNode> queue = new ArrayList<QueueNode>();
        int pointer = 0;
        queue.add(new QueueNode(v, true, 0, -1));
        while (pointer != queue.size()) {
            QueueNode node = queue.get(pointer);
            visited[node.vertex] = true;
            if (w == node.vertex) {
                QueueNode n = node;
                while (!n.up) {
                    n = queue.get(n.parent);
                }
                return new int[]{n.vertex, node.distance};
            }
            if (node.up) {
                for (int n : graph.adj(node.vertex)) {
                    if (!visited[n]) {
                        queue.add(new QueueNode(n, true, node.distance + 1, pointer));
                    }
                }
                for (int n : rev.adj(node.vertex)) {
                    if (!visited[n]) {
                        queue.add(new QueueNode(n, false, node.distance + 1, pointer));
                    }
                }
            } else {
                for (int n : rev.adj(node.vertex)) {
                    if (!visited[n]) {
                        queue.add(new QueueNode(n, false, node.distance + 1, pointer));
                    }
                }
            }
            pointer++;
        }
        return new int[]{-1, -1};

    }

    /**
     * a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
     */
    public int ancestor(int v, int w) {
        return internalVisit(v, w)[0];
    }

    /**
     * length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int min = Integer.MAX_VALUE;
        for (int vv : v) {
            for (int ww : w) {
                int length = length(vv, ww);
                if (length == -1) {
                    continue;
                }
                min = Math.min(length, min);
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    /**
     * a common ancestor that participates in shortest ancestral path; -1 if no such path
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int min = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int vv : v) {
            for (int ww : w) {
                int[] result = internalVisit(vv, ww);
                if (result[0] == -1) {
                    continue;
                }
                if (result[1] < min) {
                    min = result[1];
                    ancestor = result[0];
                }
            }
        }
        return ancestor;
    }

    private class QueueNode {
        int vertex;
        // true for out edge, false for in edge
        boolean up;
        int distance;
        int parent;

        QueueNode(int vertex, boolean up, int distance, int parent) {
            this.vertex = vertex;
            this.up = up;
            this.distance = distance;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return "QueueNode{" +
                    "vertex=" + vertex +
                    ", up=" + up +
                    ", distance=" + distance +
                    ", parent=" + parent +
                    '}';
        }
    }
}
