package project;

import project.definition.TweetHashTag;
import java.util.*;

public class Engine {

    private TweetsFileReader tweetReader;
    private TweetHashTagGraph graph;

    private static final String TWEET_FILE_PATH = ".\\app\\data\\tweets.txt";
    //private static final String TWEET_FILE_PATH = ".\\data\\tweets.txt";

    public Engine() {
        this.tweetReader = new TweetsFileReader();
        graph = new TweetHashTagGraph();
        List<TweetHashTag> hashTagsInAllTweets = tweetReader.getHashTagsInTweets(TWEET_FILE_PATH);
        graph.createGraph(hashTagsInAllTweets);
    }

    public double test() {
        return calculateAverage(TWEET_FILE_PATH);
    }

    public double calculateAverage(String fileName){
        System.out.println("Number of edges : " + this.graph.getNumberOfEdges());
        System.out.println("Number of nodes : " + this.graph.getNumberOfNodesInGraph());
        System.out.println("Avg : " + (double) this.graph.getNumberOfEdges() / this.graph.getNumberOfNodesInGraph());
        return (double) this.graph.getNumberOfEdges() / this.graph.getNumberOfNodesInGraph();
    }

    public void 

}
