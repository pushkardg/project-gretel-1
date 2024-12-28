/*
 * This source file was generated by the Gradle 'init' task
 */
package project;

import org.junit.jupiter.api.Test;

import junit.framework.Assert;
import project.definition.TweetHashTag;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class TweetsFileReaderTest {
    @Test
    void testExtractTweetId() {
        TweetsFileReader tweetsReader = new TweetsFileReader();
        Optional<String> tweetId = tweetsReader
                .extractTweetId("{\"id_str\":\"tweet5\",\"entities\":{\"hashtags\":[{\"text\":\"gretel\"}]}}");
        Assert.assertEquals(tweetId.get(), "tweet5");
    }

    @Test
    void testExtractTweetId_notPresent() {
        TweetsFileReader tweetsReader = new TweetsFileReader();
        Optional<String> tweetId = tweetsReader.extractTweetId("{\"entities\":{\"hashtags\":[{\"text\":\"gretel\"}]}}");
        Assert.assertEquals(tweetId, Optional.empty());
    }

    @Test
    void testExtractHashTagsInTweet() {
        TweetsFileReader tweetsReader = new TweetsFileReader();
        List<String> hashTags = tweetsReader.extractHashTagsInTweetString(
                "{\"id_str\":\"tweet1\",\"entities\":{\"hashtags\":[{\"text\":\"Gretel\"},{\"text\":\"data\"}]}}");
        Assert.assertEquals(hashTags, Arrays.asList("gretel", "data"));
    }

    @Test
    void testExtractHashTagsInTweet_noHashTags() {
        TweetsFileReader tweetsReader = new TweetsFileReader();
        List<String> hashTags = tweetsReader
                .extractHashTagsInTweetString("{\"id_str\":\"tweet1\",\"entities\":{\"hashtags\":[]}}");
        Assert.assertEquals(hashTags, Collections.emptyList());
    }

    @Test
    void testExtractHashTagsInAllTweets() {
        TweetsFileReader tweetsReader = new TweetsFileReader();
        List<TweetHashTag> hashTags = tweetsReader.getHashTagsInTweets(".\\data\\test1.txt");
        List<TweetHashTag> expectedHashTags = Arrays.asList( 
            new TweetHashTag("tweet1", Arrays.asList("gretel", "data")),
            new TweetHashTag("tweet2", Arrays.asList("data", "startup", "privacy")),
            new TweetHashTag("tweet3", Arrays.asList("data")),
            new TweetHashTag("tweet4", Arrays.asList("gretel")),
            new TweetHashTag("tweet5", Arrays.asList("rocketship", "gretel")),
            new TweetHashTag("tweet6", Arrays.asList("cats", "cats", "cats"))
        );

        Assert.assertEquals(hashTags, expectedHashTags);
    }
}