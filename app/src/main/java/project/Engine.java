package project;

import project.definition.TweetHashTag;
import java.util.*;

public class Engine {

    private TweetsFileReader tweetReader;
    private TweetHashTagGraph graph;

    private static final String TWEET_FILE_PATH = ".\\app\\data\\test1.txt";
    //private static final String TWEET_FILE_PATH = ".\\app\\data\\tweets.txt";
    // private static final String TWEET_FILE_PATH = ".\\data\\tweets.txt";

    public Engine() {
        this.tweetReader = new TweetsFileReader();
        graph = new TweetHashTagGraph();
        List<TweetHashTag> hashTagsInAllTweets = tweetReader.getHashTagsInTweets(TWEET_FILE_PATH);
        graph.createGraph(hashTagsInAllTweets);
    }

    public double test() {
        return calculateAverage();
    }

    public double calculateAverage() {
        System.out.println("Number of edges : " + this.graph.getNumberOfEdges());
        System.out.println("Number of nodes : " + this.graph.getNumberOfNodesInGraph());
        double averageDegree =  (double) this.graph.getNumberOfEdges() / this.graph.getNumberOfNodesInGraph();
        System.out.println("Average Degree: " +averageDegree);
        return averageDegree;
    }

    public void addTweet(String tweet) {
        List<String> hashTags = tweetReader.extractHashTagsInTweetString(tweet);
        Optional<String> tweetId = tweetReader.extractTweetId(tweet);
        if (!tweetId.isPresent())
            return;
        this.graph.addTweetToGraph(new TweetHashTag(tweetId.get(), hashTags));
    }

    public void removeTweet(String tweet) {
        List<String> hashTags = tweetReader.extractHashTagsInTweetString(tweet);
        Optional<String> tweetId = tweetReader.extractTweetId(tweet);
        if (!tweetId.isPresent())
            return;
        this.graph.removeTweetFromGraph(new TweetHashTag(tweetId.get(), hashTags));
    }
}
