package project;

import project.definition.TweetHashTag;

import java.io.IOException;
import java.util.*;

public class DegreeCalculator {

    private final TweetsFileReader tweetReader;
    private final TweetHashTagGraph graph;

    //private static final String TWEET_FILE_PATH = ".\\app\\data\\tweets.txt";
    private static final String TWEET_FILE_PATH = ".\\data\\tweets.txt";

    public DegreeCalculator() throws IOException {
        this.tweetReader = new TweetsFileReader();
        this.graph = new TweetHashTagGraph();

        if( !tweetReader.checkIfFileExists(TWEET_FILE_PATH)){
            System.out.println( "The Tweet file does not exist at the path : " +TWEET_FILE_PATH+ ". Cannot continue...");
            throw new IOException("Tweet file does not exist : " +TWEET_FILE_PATH);
        }

        //Read all the tweets and initialize the graph wih the tweet hash tags.
        List<TweetHashTag> hashTagsInAllTweets = tweetReader.getHashTagsInTweets(TWEET_FILE_PATH);
        graph.createGraph(hashTagsInAllTweets);
    }

    public double calculateAverage() {
        double averageDegree =  (double) this.graph.getNumberOfEdges() / this.graph.getNumberOfNodesInGraph();
        System.out.print("-- Number of edges : " + this.graph.getNumberOfEdges());
        System.out.println(". Number of nodes : " + this.graph.getNumberOfNodesInGraph()+" --");
        return averageDegree;
    }

    public void addTweet(String tweet) {
        List<String> hashTags = tweetReader.extractHashTagsInTweetString(tweet);
        Optional<String> tweetId = tweetReader.extractTweetId(tweet);
        if (tweetId.isPresent()){
            this.graph.addTweetToGraph(new TweetHashTag(tweetId.get(), hashTags));
        }        
    }

    public void removeTweet(String tweet) {
        List<String> hashTags = tweetReader.extractHashTagsInTweetString(tweet);
        Optional<String> tweetId = tweetReader.extractTweetId(tweet);
        if (tweetId.isPresent()){
            this.graph.removeTweetFromGraph(new TweetHashTag(tweetId.get(), hashTags));
        }        
    }
}
