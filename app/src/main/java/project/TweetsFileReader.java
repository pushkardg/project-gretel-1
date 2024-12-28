package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Optional;

import project.definition.TweetHashTag;

/**
 * THis class is responsible for reading the tweets.txt file and parsing the json content to extract 
 * the hashtags from the tweets
 */
public class TweetsFileReader {
    final JSONParser parser = new JSONParser();


    public boolean checkIfFileExists(String filePath){
        final File file = new File(filePath);
        return file.exists();
    }

    /**
     * Reads the file tweets file, extracts the tweet hash tags and returns them.
     * @param filePath
     * @return
     */
    public List<TweetHashTag> getHashTagsInTweets(String filePath) {

        final List<TweetHashTag> hashTagsList = new ArrayList<>();
        final File file = new File(filePath);

        try {
            final BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = reader.readLine()) != null) {
                List<String> hashTagsInTweet = extractHashTagsInTweetString(line);
                if (!hashTagsInTweet.isEmpty()) {
                    String tweetId = extractTweetId(line).get();
                    hashTagsList.add(new TweetHashTag(tweetId, hashTagsInTweet));
                }
            }
            reader.close();
        } catch (IOException exception) {
            System.out.println("Exception while reading & extracting tweet File : " + filePath);
            exception.printStackTrace();
        }

        return hashTagsList;
    }

    /**
     * Given an individual tweet Json String, this function extracts the tweetId from it
     * @param tweet
     * @return
     */
    protected Optional<String> extractTweetId(String tweet) {
        try {
            JSONObject tweetJson = (JSONObject) parser.parse(tweet);
            if (tweetJson.containsKey("id_str"))
                return Optional.of((String) tweetJson.get("id_str"));
        } catch (ParseException parseException) {
            System.out.println("Exception while extracting tweetId");
            parseException.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Given an individual tweet json string, this function extracts the hash tags from it. 
     * @param tweet
     * @return
     */
    protected List<String> extractHashTagsInTweetString(String tweet) {
        final List<String> hashTagsInTweet = new ArrayList<>();
        try {
            JSONObject tweetJson = (JSONObject) parser.parse(tweet);
            if (!tweetJson.containsKey("entities")
                    || !((JSONObject) tweetJson.get("entities")).containsKey("hashtags")) {
                return Collections.emptyList();
            }
            JSONArray hashTags = (JSONArray) ((JSONObject) tweetJson.get("entities")).get("hashtags");
            if (!hashTags.isEmpty()) {
                for (int idx = 0; idx < hashTags.size(); idx++) {
                    String tag = (String) ((JSONObject) hashTags.get(idx)).get("text");
                    hashTagsInTweet.add(tag.toLowerCase());
                }
            }
        } catch (ParseException exception) {
            System.out.println("Exception while parsing string in the tweets file");
            exception.printStackTrace();
            return Collections.emptyList();
        }
        return hashTagsInTweet;
    }

}
