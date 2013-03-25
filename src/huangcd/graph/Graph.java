package huangcd.graph;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * User: huangcd (huangcd.thu@gmail.com)
 * Date: 3/25/13
 * Time: 9:49 PM
 */
public class Graph {
    private List<ArrayList<Integer>> edges;

    public Graph(int V) {
        edges = new ArrayList<ArrayList<Integer>>(V);
    }

    public Graph(InputStream in) {
    }

    public void addEdge(int v, int w) {
        edges.get(v).add(w);
        edges.get(w).add(v);
    }

    public Iterable<Integer> adj(int v) {
        return edges.get(v);
    }

    public int V() {
        return edges.size();
    }

    public int E() {
        int count = 0;
        for (ArrayList<Integer> adj : edges) {
            count += adj.size();
        }
        return count / 2;
    }
}
