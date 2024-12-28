package project;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import project.definition.GraphNode;
import project.definition.TweetHashTag;

/**
 * This class has all the grpah related logic, which initializes the graph based on the tweet data, adds & removes tweets
 * from the graph
 */
public class TweetHashTagGraph {

    //Mapping of all the hash tags to their nodes in the graph
    private Map<String, GraphNode> hashTagToNodeMap;
    
    //Mapping of all the hash tags to their nodes, which dont have a neighbor node associated with them
    //This is useful for cases where there are tweets with a single hash tag in it. 
    private Map<String, Set<String>> singleHashTagToTweetsMap;

    //Set of all the tweetIds in the graph, which can be used to quickly look up the tweets before adding/removing 
    //a new tweet from the graph.
    private Set<String> tweetsInGraph;

    //Number of edges in the graph. 
    private int numberOfEdges;

    public TweetHashTagGraph() {
        this.hashTagToNodeMap = new HashMap<>();
        singleHashTagToTweetsMap = new HashMap<>();
        this.tweetsInGraph = new HashSet<>();
        numberOfEdges = 0;
    }

    /**
     * Creates the graph by adding tweets to the it.
     * 
     * @param hashTagsInAllTweets
     */
    public void createGraph(List<TweetHashTag> hashTagsInAllTweets) {
        for (TweetHashTag hashTagsInTweet : hashTagsInAllTweets) {
            addTweetToGraph(hashTagsInTweet);
        }
    }

    /**
     * Adds the tweet hash tags to the graph. It adds new nodes and edges to the graph if necessary.
     * Additionally, it calculates the number of edges in the graph and updates the `numberOfEdges` variable.
     * This can be used to make the average degree calculations fast. 
     * 
     * @param tweetHashTag
     * @return
     */
    public boolean addTweetToGraph(TweetHashTag tweetHashTag) {
        if (this.tweetsInGraph.contains(tweetHashTag.getTweetId()) || tweetHashTag.getHashTags().isEmpty()) {
            System.out.println("The graph already contains the tweet or the hash tags are empty. Returning");
            return false;
        }

        this.tweetsInGraph.add(tweetHashTag.getTweetId());
        Set<String> uniqueHashTags = new HashSet<>(tweetHashTag.getHashTags());
        String tweetId = tweetHashTag.getTweetId();

        if( uniqueHashTags.size() == 1){
            //For the case where the tweet has only 1 unique hash tag, the singleHashTagToTweetsMap is updated.
            singleHashTagToTweetsMap.putIfAbsent(tweetHashTag.getHashTags().get(0), new HashSet<>());
            singleHashTagToTweetsMap.get(tweetHashTag.getHashTags().get(0)).add(tweetId);
        }
        
        for (String hashTag1 : uniqueHashTags) {
            for (String hashTag2 : uniqueHashTags) {

                //Add nodes to the graph if needed.
                hashTagToNodeMap.putIfAbsent(hashTag1, new GraphNode(hashTag1));
                hashTagToNodeMap.putIfAbsent(hashTag2, new GraphNode(hashTag2));

                if (!hashTag1.equals(hashTag2)) {
                    GraphNode node1 = hashTagToNodeMap.get(hashTag1);
                    GraphNode node2 = hashTagToNodeMap.get(hashTag2);

                    //Add edges to the graph.
                    if (node1.addNeighbor(node2, tweetId))
                        numberOfEdges++;
                    if (node2.addNeighbor(node1, tweetId))
                        numberOfEdges++;
                }
            }
        }
        return true;
    }

    /**
     * Removes the tweet hash tags to the graph. It removes existing nodes and/or edges from the graph if necessary.
     * Additionally, it calculates the number of edges in the graph and updates the `numberOfEdges` variable.
     * This can be used to make the average degree calculations fast. 
     * 
     * @param tweetHashTag
     * @return
     */
    public boolean removeTweetFromGraph(TweetHashTag tweetHashTag) {
        if (!this.tweetsInGraph.contains(tweetHashTag.getTweetId()) || tweetHashTag.getHashTags().isEmpty()) {
            System.out.println("The graph does not contains the tweet or the hash tags are empty. Returning");
            return false;
        }
        String tweetId = tweetHashTag.getTweetId();
        this.tweetsInGraph.remove(tweetId);

        List<String> uniqueHashTags = new HashSet<>(tweetHashTag.getHashTags()).stream().collect(Collectors.toList());
        if( uniqueHashTags.size() == 1){
            //If tweets with a single unique hash tag are being removed, we'll have to remove the node only
            //if there are not other edges associated with that node
            String hashTagToRemove = uniqueHashTags.get(0);
            singleHashTagToTweetsMap.get(hashTagToRemove).remove(tweetId);
            if( singleHashTagToTweetsMap.get(hashTagToRemove).isEmpty()){
                singleHashTagToTweetsMap.remove(hashTagToRemove);

                if( hashTagToNodeMap.get(hashTagToRemove).getNeighbors().isEmpty()){
                    hashTagToNodeMap.remove(hashTagToRemove);
                }
            }
            return true;
        }

        //For all the hashtag pairs in the graph, remove the nodes and edges if necessary
        for (int index1 = 0; index1 < uniqueHashTags.size(); index1++) {
            for (int index2 = index1 + 1; index2 < uniqueHashTags.size(); index2++) {
                String hashTag1 = uniqueHashTags.get(index1);
                String hashTag2 = uniqueHashTags.get(index2);

                if (!hashTagToNodeMap.containsKey(hashTag1) || !hashTagToNodeMap.containsKey(hashTag2))
                    continue;

                GraphNode node1 = hashTagToNodeMap.get(hashTag1);
                GraphNode node2 = hashTagToNodeMap.get(hashTag2);
                if (node1.removeTweetFromEdge(node2, tweetId))
                    numberOfEdges--;
                if (node2.removeTweetFromEdge(node1, tweetId))
                    numberOfEdges--;

                //Remove the nodes from the graph only if there are no tweets associated with a single hashTag
                if (node1.getNeighbors().isEmpty() && !singleHashTagToTweetsMap.containsKey(hashTag1))
                    hashTagToNodeMap.remove(hashTag1);
                if (node2.getNeighbors().isEmpty() && !singleHashTagToTweetsMap.containsKey(hashTag2))
                    hashTagToNodeMap.remove(hashTag2);

            }
        }

        return true;
    }

    protected Map<String, GraphNode> getHashTagToNodeMap() {
        return hashTagToNodeMap;
    }

    public int getNumberOfEdges() {
        return numberOfEdges;
    }

    public int getNumberOfNodesInGraph() {
        return this.hashTagToNodeMap.size();
    }
}
