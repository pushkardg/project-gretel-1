package project;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import project.definition.GraphNode;
import project.definition.TweetHashTag;

public class TweetHashTagGraph {

    private Map<String, GraphNode> hashTagToNodeMap;
    private Map<String, Set<String>> singleHashTagToTweetsMap;
    private Set<String> tweetsInGraph;
    private int numberOfEdges;

    public TweetHashTagGraph() {
        this.hashTagToNodeMap = new HashMap<>();
        singleHashTagToTweetsMap = new HashMap<>();
        this.tweetsInGraph = new HashSet<>();
        numberOfEdges = 0;
    }

    public void createGraph(List<TweetHashTag> hashTagsInAllTweets) {
        for (TweetHashTag hashTagsInTweet : hashTagsInAllTweets) {
            addTweetToGraph(hashTagsInTweet);
        }
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

    public boolean addTweetToGraph(TweetHashTag tweetHashTag) {
        if (this.tweetsInGraph.contains(tweetHashTag.getTweetId()) || tweetHashTag.getHashTags().isEmpty()) {
            System.out.println("The graph already contains the tweet or the hash tags are empty. Returning");
            return false;
        }

        this.tweetsInGraph.add(tweetHashTag.getTweetId());
        Set<String> uniqueHashTags = new HashSet<>(tweetHashTag.getHashTags());
        String tweetId = tweetHashTag.getTweetId();
        if( uniqueHashTags.size() == 1){
            singleHashTagToTweetsMap.putIfAbsent(tweetHashTag.getHashTags().get(0), new HashSet<>());
            singleHashTagToTweetsMap.get(tweetHashTag.getHashTags().get(0)).add(tweetId);
        }
        
        for (String hashTag1 : uniqueHashTags) {
            for (String hashTag2 : uniqueHashTags) {
                hashTagToNodeMap.putIfAbsent(hashTag1, new GraphNode(hashTag1));
                hashTagToNodeMap.putIfAbsent(hashTag2, new GraphNode(hashTag2));
                if (!hashTag1.equals(hashTag2)) {
                    GraphNode node1 = hashTagToNodeMap.get(hashTag1);
                    GraphNode node2 = hashTagToNodeMap.get(hashTag2);
                    if (node1.addNeighbor(node2, tweetId))
                        numberOfEdges++;
                    if (node2.addNeighbor(node1, tweetId))
                        numberOfEdges++;
                }
            }
        }
        return true;
    }

    public boolean removeTweetFromGraph(TweetHashTag tweetHashTag) {
        if (!this.tweetsInGraph.contains(tweetHashTag.getTweetId()) || tweetHashTag.getHashTags().isEmpty()) {
            System.out.println("The graph does not contains the tweet or the hash tags are empty. Returning");
            return false;
        }
        String tweetId = tweetHashTag.getTweetId();
        this.tweetsInGraph.remove(tweetId);

        List<String> uniqueHashTags = new HashSet<>(tweetHashTag.getHashTags()).stream().collect(Collectors.toList());
        if( uniqueHashTags.size() == 1){
            String hashTagToRemove = uniqueHashTags.get(0);
            singleHashTagToTweetsMap.get(hashTagToRemove).remove(tweetId);
            if( singleHashTagToTweetsMap.get(hashTagToRemove).isEmpty()){
                singleHashTagToTweetsMap.remove(hashTagToRemove);
                if( !hashTagToNodeMap.containsKey(hashTagToRemove) || hashTagToNodeMap.get(hashTagToRemove).getNeighbors().isEmpty()){
                    hashTagToNodeMap.remove(hashTagToRemove);
                }
            }
            return true;
        }

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

                if (node1.getNeighbors().isEmpty() && !singleHashTagToTweetsMap.containsKey(hashTag1))
                    hashTagToNodeMap.remove(hashTag1);
                if (node2.getNeighbors().isEmpty() && !singleHashTagToTweetsMap.containsKey(hashTag2))
                    hashTagToNodeMap.remove(hashTag2);

            }
        }

        return true;
    }
}
