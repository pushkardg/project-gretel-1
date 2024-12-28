package project.definition;

import java.util.HashSet;
import java.util.Set;

public class GraphNode {
    private String hashTag;
    private Set<GraphNode> neighbors;

    public GraphNode(String hashTag) {
        this.hashTag = hashTag;
        this.neighbors = new HashSet<>();
    }

    public void addNeighbor(GraphNode node) {
        this.neighbors.add(node);
    }

    public String getHashTag() {
        return hashTag;
    }

    public Set<GraphNode> getNeighbors() {
        return neighbors;
    }
}
