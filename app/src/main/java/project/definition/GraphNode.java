package project.definition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A Pojo which represents the Node in the Graph. Here is an example of a graph and the Node representation of it:
 * 
    *                        tweet1, 
    *                        tweet7                     
    *          (gretel--------------------------- (data)
    *             |                                 
    *             |                                
    *             | tweet5,                         
    *             |                                 
    *             |                                                          
    *          (rocketship)        
    *                 
    * 
    * GraphNode{
    *      hashTag: "gretel",
    *      neighbors: { "data": ["tweet1", "tweet7"] , "rocketship" : ["tweet5"]}
    * }
    * 
    * GraphNode{
    *      hashTag: "data",
    *      neighbors: { "gretel": ["tweet1", "tweet7"] }
    * }
    * 
    * GraphNode{
    *      hashTag: "rocketship",
    *      neighbors: { "gretel": ["tweet5"] }
    * }
    * 
 * 
 */
public class GraphNode {

    private String hashTag;

    //Contains a map of the neighbor nodes associated with the current GraphNode, along with the set of tweetIds
    //associated with the edges. 
    private Map<GraphNode, Set<String>> neighbors;

    public GraphNode(String hashTag) {
        this.hashTag = hashTag;
        this.neighbors = new HashMap<>();
    }

    /**
     * Adds a neighbor to the current node in the graph. If the neighbor existed before, this function returns false. Otherwise it returns true,
     * which indicates that a new edge is being added to the graph. 
     * 
     * @param node
     * @param tweetId
     * @return
     */
    public boolean addNeighbor(GraphNode node, String tweetId) {
        boolean neighborExistedBefore = this.neighbors.containsKey(node);
        this.neighbors.putIfAbsent(node, new HashSet<>());
        this.neighbors.get(node).add(tweetId);
        return !neighborExistedBefore;
    }

    /**
     * Removes the tweetId from the edge between the current nodeand the neighboring node. If removing there was only 1 tweetId associated 
     * with the neighbor & the edge can be removed. It returns true if the edge is removed. Returns false otherwise.
     * 
     * @param node
     * @param tweetId
     * @return
     */
    public boolean removeTweetFromEdge(GraphNode node, String tweetId){
        this.neighbors.get(node).remove(tweetId);
        if( this.neighbors.get(node).isEmpty())
            this.neighbors.remove(node);
        return !this.neighbors.containsKey(node);
    }

    public String getHashTag() {
        return hashTag;
    }

    public Map<GraphNode, Set<String>> getNeighbors() {
        return neighbors;
    }
}
