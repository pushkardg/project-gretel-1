package project;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import project.definition.GraphNode;
import project.definition.TweetHashTag;

public class TweetHashTagGraph {

    private Map<String, GraphNode> hashTagToNodeMap;
    private int numberOfEdges;

    public TweetHashTagGraph() {
        this.hashTagToNodeMap = new HashMap<>();
        numberOfEdges = 0;
    }

    public void createGraph(List<TweetHashTag> hashTagsInAllTweets) {
        for (TweetHashTag hashTagsInTweet : hashTagsInAllTweets) {
            Set<String> uniqueHashTags = new HashSet<>(hashTagsInTweet.getHashTags());
            for (String hashTag1 : uniqueHashTags) {
                for (String hashTag2 : uniqueHashTags) {
                    hashTagToNodeMap.putIfAbsent(hashTag1, new GraphNode(hashTag1));
                    hashTagToNodeMap.putIfAbsent(hashTag2, new GraphNode(hashTag2));
                    if (!hashTag1.equals(hashTag2)) {
                        GraphNode node1 = hashTagToNodeMap.get(hashTag1);
                        GraphNode node2 = hashTagToNodeMap.get(hashTag2);
                        node1.addNeighbor(node2);
                        node2.addNeighbor(node1);
                    }
                }
            }
        }
        for (Map.Entry<String, GraphNode> entry : this.hashTagToNodeMap.entrySet()) {
            if (!entry.getValue().getNeighbors().isEmpty()) {
                numberOfEdges += entry.getValue().getNeighbors().size();
            }
        }
    }

    public int getNumberOfEdges() {
        return numberOfEdges;
    }

    public int getNumberOfNodesInGraph(){
        return this.hashTagToNodeMap.size();
    }
}
