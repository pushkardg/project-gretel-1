/*
 * This source file was generated by the Gradle 'init' task
 */
package project; 
import org.junit.jupiter.api.Test;

import junit.framework.Assert;
import project.definition.GraphNode;
import project.definition.TweetHashTag;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class TweetHashTagGraphTest {
    
    /**
     *      Initial Graph Used for test case
     * 
     *                        tweet1, 
     *                        tweet7                        tweet2
     *          (gretel--------------------------- (data)---------- (privacy)
     *             |                                  |            /
     *             |                                  |           /
     *             | tweet5,                          |tweet2    /
     *             |                                  |         /tweet2             
     *             |                                  |        /
     *             |                                  |       /
     *          (rocketship)                        (startup)/
     * 
     * 
     *          (cat)
     */
    @Test
    void testExtractHashTagsInAllTweets() {

        List<TweetHashTag> tweetHashTags = Arrays.asList(
                new TweetHashTag("tweet1", Arrays.asList("gretel", "data")),
                new TweetHashTag("tweet2", Arrays.asList("data", "startup", "privacy")),
                new TweetHashTag("tweet3", Arrays.asList("data")),
                new TweetHashTag("tweet4", Arrays.asList("gretel")),
                new TweetHashTag("tweet5", Arrays.asList("rocketship", "gretel")),
                new TweetHashTag("tweet6", Arrays.asList("cats", "cats", "cats")),
                new TweetHashTag("tweet7", Arrays.asList("gretel", "data")));
        TweetHashTagGraph graph = new TweetHashTagGraph();
        graph.createGraph(tweetHashTags);
        Assert.assertEquals(graph.getNumberOfEdges(), 10);
        Assert.assertEquals(graph.getNumberOfNodesInGraph(), 6);

        // Verify node - gretel
        Assert.assertEquals(graph.getHashTagToNodeMap().get("gretel").getHashTag(), "gretel");      
        Assert.assertEquals(getNeighborHashTags(graph, "gretel"), Set.of("data", "rocketship"));
        Assert.assertEquals(getTweetsOnEdge(graph, "gretel", "data") , Set.of("tweet1", "tweet7"));
        Assert.assertEquals(getTweetsOnEdge(graph, "gretel", "rocketship") , Set.of("tweet5"));

        // Verify node - data
        Assert.assertEquals(graph.getHashTagToNodeMap().get("data").getHashTag(), "data");
        Assert.assertEquals(getNeighborHashTags(graph, "data"),Set.of("gretel", "privacy", "startup"));
        Assert.assertEquals(getTweetsOnEdge(graph, "data", "gretel") , Set.of("tweet1", "tweet7"));
        Assert.assertEquals(getTweetsOnEdge(graph, "data", "startup") ,Set.of("tweet2"));
        Assert.assertEquals(getTweetsOnEdge(graph, "data", "privacy") ,Set.of("tweet2"));


        // Verify node - rocketship
        Assert.assertEquals(graph.getHashTagToNodeMap().get("rocketship").getHashTag(), "rocketship");
        Assert.assertEquals(getNeighborHashTags(graph, "rocketship"), Set.of("gretel"));
        Assert.assertEquals(getTweetsOnEdge(graph, "rocketship", "gretel") , Set.of("tweet5"));

        // Verify node - startup
        Assert.assertEquals(graph.getHashTagToNodeMap().get("startup").getHashTag(), "startup");
        Assert.assertEquals(getNeighborHashTags(graph, "startup"),  Set.of("data", "privacy"));
        Assert.assertEquals(getTweetsOnEdge(graph, "startup", "data") , Set.of("tweet2"));
        Assert.assertEquals(getTweetsOnEdge(graph, "startup", "privacy") , Set.of("tweet2"));



        // Verify node - cats
        Assert.assertEquals(graph.getHashTagToNodeMap().get("cats").getHashTag(), "cats");
        Assert.assertEquals(graph.getHashTagToNodeMap().get("cats").getNeighbors().isEmpty(), true);

        /// ----------------------///
        // Add new Tweet
        /// ----------------------///
        boolean tweetAdded = graph.addTweetToGraph(new TweetHashTag("tweet10", Arrays.asList("gretel", "rocketship")));
        Assert.assertEquals(tweetAdded,true);
        Assert.assertEquals(graph.getNumberOfEdges(), 10);
        Assert.assertEquals(graph.getNumberOfNodesInGraph(), 6);

        
        // Verify node - gretel
        Assert.assertEquals(graph.getHashTagToNodeMap().get("gretel").getHashTag(), "gretel");
        Assert.assertEquals(getNeighborHashTags(graph, "gretel"), Set.of("data", "rocketship"));
        Assert.assertEquals(getTweetsOnEdge(graph, "gretel", "data") , Set.of("tweet1", "tweet7"));
        Assert.assertEquals(getTweetsOnEdge(graph, "gretel", "rocketship") , Set.of("tweet5", "tweet10"));


        // Verify node - rocketship
        Assert.assertEquals(graph.getHashTagToNodeMap().get("rocketship").getHashTag(), "rocketship");
        Assert.assertEquals(getNeighborHashTags(graph, "rocketship"), Set.of("gretel"));
        Assert.assertEquals(getTweetsOnEdge(graph, "rocketship", "gretel") , Set.of("tweet5", "tweet10"));


        // Try adding the same tweet again and it should return false
        tweetAdded = graph.addTweetToGraph(new TweetHashTag("tweet10", Arrays.asList("gretel", "rocketship")));
        Assert.assertEquals(tweetAdded,false);

        tweetAdded = graph.addTweetToGraph(new TweetHashTag("tweet11", Arrays.asList("cats", "rocketship")));
        Assert.assertEquals(tweetAdded,true);
        Assert.assertEquals(graph.getNumberOfEdges(), 12);
        Assert.assertEquals(graph.getNumberOfNodesInGraph(), 6);


        //Add a tweet that results in the addition of a new node in the graph, but no new edges
        tweetAdded = graph.addTweetToGraph(new TweetHashTag("tweet12", Arrays.asList("dogs")));
        Assert.assertEquals(tweetAdded,true);
        Assert.assertEquals(graph.getNumberOfEdges(), 12);
        Assert.assertEquals(graph.getNumberOfNodesInGraph(), 7);
 
    }

    /**
     *      Initial Graph Used for the test case
     * 
     *                        tweet1, 
     *                        tweet7                        tweet2
     *          (gretel--------------------------- (data)---------- (privacy)
     *             |                                  |            /
     *             |                                  |           /
     *             | tweet5,                          |tweet2    /
     *             | tweet8                           |         /tweet2             
     *             |                                  |        /
     *             |                                  |       /
     *          (rocketship)                        (startup)/
     * 
     * 
     *          (cat)
     */
    @Test
    void testHashTagRemoval() {
        List<TweetHashTag> tweetHashTags = Arrays.asList(
                new TweetHashTag("tweet1", Arrays.asList("gretel", "data")),
                new TweetHashTag("tweet2", Arrays.asList("data", "startup", "privacy")),
                new TweetHashTag("tweet3", Arrays.asList("data")),
                new TweetHashTag("tweet4", Arrays.asList("gretel")),
                new TweetHashTag("tweet5", Arrays.asList("rocketship", "gretel")),
                new TweetHashTag("tweet6", Arrays.asList("cats", "cats", "cats")),
                new TweetHashTag("tweet7", Arrays.asList("gretel", "data")),
                new TweetHashTag("tweet8", Arrays.asList("gretel", "rocketship")));
        TweetHashTagGraph graph = new TweetHashTagGraph();
        graph.createGraph(tweetHashTags);
        Assert.assertEquals(graph.getNumberOfEdges(), 10);
        Assert.assertEquals(graph.getNumberOfNodesInGraph(), 6);

        //Verify Node - gretel
        Assert.assertEquals(getNeighborHashTags(graph, "gretel"), Set.of("data", "rocketship"));
        Assert.assertEquals(getTweetsOnEdge(graph, "gretel", "data") , Set.of("tweet1", "tweet7"));
        Assert.assertEquals(getTweetsOnEdge(graph, "gretel", "rocketship") , Set.of("tweet5", "tweet8"));

        //Verify Node - data
        Assert.assertEquals(getNeighborHashTags(graph, "data"), Set.of("gretel", "privacy", "startup"));
        Assert.assertEquals(getTweetsOnEdge(graph, "data", "gretel") , Set.of("tweet1", "tweet7"));
        Assert.assertEquals(getTweetsOnEdge(graph, "data", "startup") , Set.of("tweet2"));
        Assert.assertEquals(getTweetsOnEdge(graph, "data", "privacy") , Set.of("tweet2"));
 

        //-------------------------
        // Remove tweet 7
        //-------------------------
        graph.removeTweetFromGraph( new TweetHashTag("tweet7", Arrays.asList("gretel", "data")));
        Assert.assertEquals(graph.getNumberOfEdges(), 10);
        Assert.assertEquals(graph.getNumberOfNodesInGraph(), 6);

        //Verify Node - gretel
        Assert.assertEquals(getNeighborHashTags(graph, "gretel"), Set.of("data", "rocketship"));
        Assert.assertEquals(getTweetsOnEdge(graph, "gretel", "data") , Set.of("tweet1"));
        Assert.assertEquals(getTweetsOnEdge(graph, "gretel", "rocketship") , Set.of("tweet5", "tweet8"));

        // Verify Node - data
        Assert.assertEquals(getNeighborHashTags(graph, "data"), Set.of("gretel", "privacy", "startup"));
        Assert.assertEquals(getTweetsOnEdge(graph, "data", "gretel"), Set.of("tweet1"));
        Assert.assertEquals(getTweetsOnEdge(graph, "data", "startup"), Set.of("tweet2"));
        Assert.assertEquals(getTweetsOnEdge(graph, "data", "privacy"), Set.of("tweet2"));        

        //-------------------------
        // Remove tweet 1
        //-------------------------
        graph.removeTweetFromGraph(  new TweetHashTag("tweet1", Arrays.asList("gretel", "data")) );
        Assert.assertEquals(graph.getNumberOfEdges(), 8);
        Assert.assertEquals(graph.getNumberOfNodesInGraph(), 6);

        //Verify Node - gretel
        Assert.assertEquals(getNeighborHashTags(graph, "gretel"), Set.of("rocketship"));
        Assert.assertEquals(getTweetsOnEdge(graph, "gretel", "rocketship") , Set.of("tweet5", "tweet8"));

        // Verify Node - data
        Assert.assertEquals(getNeighborHashTags(graph, "data"), Set.of("privacy", "startup"));
        Assert.assertEquals(getTweetsOnEdge(graph, "data", "startup"), Set.of("tweet2"));
        Assert.assertEquals(getTweetsOnEdge(graph, "data", "privacy"), Set.of("tweet2"));     

        //-------------------------
        // Remove tweet 2
        //-------------------------
        Assert.assertEquals( graph.getHashTagToNodeMap().containsKey("data"), true);
        graph.removeTweetFromGraph(  new TweetHashTag("tweet2", Arrays.asList("data", "startup", "privacy")) );
        Assert.assertEquals(graph.getNumberOfEdges(), 2);
        Assert.assertEquals(graph.getNumberOfNodesInGraph(), 4);
        Assert.assertEquals( graph.getHashTagToNodeMap().containsKey("data"), true);

        //-------------------------
        // Remove tweet 3
        //-------------------------
        Assert.assertEquals( graph.getHashTagToNodeMap().containsKey("data"), true);
        graph.removeTweetFromGraph(  new TweetHashTag("tweet3", Arrays.asList("data")) );
        Assert.assertEquals(graph.getNumberOfEdges(), 2);
        Assert.assertEquals(graph.getNumberOfNodesInGraph(), 3);
        Assert.assertEquals( graph.getHashTagToNodeMap().containsKey("data"), false);

        //-------------------------
        // Remove tweet 4
        //-------------------------
        Assert.assertEquals( graph.getHashTagToNodeMap().containsKey("gretel"), true);
        graph.removeTweetFromGraph(  new TweetHashTag("tweet4", Arrays.asList("gretel")) );
        Assert.assertEquals(graph.getNumberOfEdges(), 2);
        Assert.assertEquals(graph.getNumberOfNodesInGraph(), 3);
        Assert.assertEquals( graph.getHashTagToNodeMap().containsKey("gretel"), true);

        //-------------------------
        // Remove tweet 5
        //-------------------------
        graph.removeTweetFromGraph(  new TweetHashTag("tweet5", Arrays.asList("rocketship", "gretel")) );
        Assert.assertEquals(graph.getNumberOfEdges(), 2);
        Assert.assertEquals(graph.getNumberOfNodesInGraph(), 3);

        //-------------------------
        // Remove tweet 8
        //-------------------------
        graph.removeTweetFromGraph(  new TweetHashTag("tweet8", Arrays.asList("rocketship", "gretel")) );
        Assert.assertEquals(graph.getNumberOfEdges(), 0);
        Assert.assertEquals(graph.getNumberOfNodesInGraph(), 1);

        //-------------------------
        // Remove tweet 6
        //-------------------------
        graph.removeTweetFromGraph(  new TweetHashTag("tweet6", Arrays.asList("cats", "cats", "cats")) );
        Assert.assertEquals(graph.getNumberOfEdges(), 0);
        Assert.assertEquals(graph.getNumberOfNodesInGraph(), 0);
    }


    //--------------------------
    // Helper methods used in the test cases. 
    //--------------------------

    private Map<GraphNode, Set<String>> getNeighbors(TweetHashTagGraph graph, String node){
        return graph.getHashTagToNodeMap().get(node).getNeighbors();
    }

    private Set<String> getNeighborHashTags(TweetHashTagGraph graph, String node){
        return getNeighbors(graph, node).keySet().stream()
                .map(n -> n.getHashTag())
                .collect(Collectors.toSet());
    }

    private Set<String> getTweetsOnEdge(TweetHashTagGraph graph, String node1, String node2){
        Map<GraphNode, Set<String>> node1Neighbors = getNeighbors(graph, node1);
        for (Map.Entry<GraphNode, Set<String>> node : node1Neighbors.entrySet()) {
            if (node.getKey().getHashTag().equals(node2)) {
                return node.getValue();
            }
        }
        return Collections.emptySet();
    }
}
