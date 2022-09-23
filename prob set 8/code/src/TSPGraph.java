import java.lang.reflect.Array;
import java.util.PriorityQueue;
import java.util.ArrayList;

public class TSPGraph implements IApproximateTSP {

    @Override
    public void MST(TSPMap map) {
        // TODO: implement this method
        // adaptaion of Prim's algorithm
        TreeMapPriorityQueue<Double, Integer> pq = new TreeMapPriorityQueue<>();
        // keeps track of nodes we have already popped
        boolean[] visited = new boolean[map.getCount()];
        // keeps track of the other end of the edge thus far found with the lowest distance for each node
        int[] parent = new int[map.getCount()];
        // add all nodes into the pq using the edge from the source node 0. sets all as unvisited and 0 as their parent
        for (int i = 1; i < map.getCount(); i++) {
            visited[i] = false;
            parent[i] = 0;
        }

        // visited source
        pq.add(0, 0.0);
        parent[0] = 0;
        while  (!pq.isEmpty()) {
            // find minimum distance, travel to the node
            int next = pq.extractMin();
            visited[next] = true;
            map.setLink(next, parent[next] );
            // System.out.println(String.format("linked %s %s", parent[next], next));

            // add nodes into thr pq if they have not been visited and are of a lower distance from the current node
            // than the previous
            for (int i = 0; i < map.getCount(); i++) {
                if (!visited[i] && pq.lookup(i) != null && map.pointDistance(next, i) < pq.lookup(i)) {
                    pq.decreasePriority(i, map.pointDistance(next, i));
                    // sets parent as the current node
                    parent[i] = next;
                } else if(!visited[i] && pq.lookup(i) == null) {
                    pq.add(i, map.pointDistance(next, i));
                }
            }

        }
        map.redraw();
    }

    @Override
    public void TSP(TSPMap map) {
        MST(map);
        // TODO: implement the rest of this method.
        // DFS
        boolean[] visited = new boolean[map.getCount()];
        for (boolean b : visited) {
            b = false;
        }

        // walk will be used to keep track of the path to all nodes with visited nodes removed
        ArrayList<Integer> walk = new ArrayList<>();
        walk.add(0);
        // DFS helps to fill walk
        DFS(map, 0, visited, walk);
        // return back to source
        walk.add(0);

        // erase all links in map
        for (int i = 0; i < map.getCount(); i++) {
            map.eraseLink(i);
        }

        for (int i = 0; i < walk.size() - 1; i++) {
            map.setLink(walk.get(i), walk.get(i + 1));
        }

        map.redraw();
    }

    private void DFS(TSPMap map, int curr, boolean[] visited, ArrayList<Integer> walk) {
        visited[curr] = true;

        // list to collect adjacent nodes
        TreeMapPriorityQueue<Double, Integer> pq = new TreeMapPriorityQueue<>();

        for (int i = 0; i < map.getCount(); i++) {
            int source = map.getLink(i);
            // add nodes that are connected to curr
            if (source == curr && !visited[i]) {
                pq.add(i, map.pointDistance(curr, i));
            }
        }

        // keep walking down min path
        // when a leaf is reached, will return to parent, then add next unvisited adjacent node and recurse
        // so walk will have order of visiting with backtracking, with the visited nodes removed
        while (!pq.isEmpty()) {
            int next = pq.extractMin();
            walk.add(next);
            DFS(map, next, visited, walk);
        }
    }

    @Override
    public boolean isValidTour(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // TODO: implement this method

        // cycle keeps track if path returns back to 0
        boolean cycle = false;
        // keeps track of visited nodes, initialised to all false
        boolean[] visited = new boolean[map.getCount()];
        for (boolean b : visited) {
            b = false;
        }

        // start from source, 0
        int curr = 0;
        visited[curr] = true;
        // trace links, until end or looped
        while (map.getLink(curr) != -1) {
            int next = map.getLink(curr);
            // System.out.println(next);
            // if found already visited node break while loop - path has completed (as a loop)
            if (visited[next]) {
                // if already visited node is source, update cycle to true (returns back to 0)
                if (next == 0) {
                    cycle = true;
                    break;
                }
                break;
            }
            // next has not been visited yet, update to visited and move to next
            visited[next] = true;
            curr = next;
        }

        // check if all nodes have been visited
        boolean visitedall = true;
        for (boolean b : visited) {
            // System.out.println(b);
            visitedall = visitedall && b;
        }

        return visitedall && cycle;
    }

    @Override
    public double tourDistance(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // TODO: implement this method
        if (isValidTour(map)) {
            int curr = 0;
            double dist = 0;
            while (map.getLink(curr) != -1) {
                int next = map.getLink(curr);
                dist += map.pointDistance(curr, next);
                if (next == 0) {
                    break;
                }
                curr = next;
            }
            return dist;
        }
        return -1;
    }

    public static void main(String[] args) {
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "hundredpoints.txt");
        TSPGraph graph = new TSPGraph();

        // graph.MST(map);
        graph.TSP(map);
        System.out.println(graph.isValidTour(map));
        // System.out.println(graph.tourDistance(map));
    }
}
