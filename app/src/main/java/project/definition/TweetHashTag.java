package project.definition;

import java.util.List;

/**
 * A Pojo for holding the tweetId and the hash tags associated with the tweet
 */
public class TweetHashTag {
    private String tweetId;
    private List<String> hashTags;

    public TweetHashTag(String tweetId, List<String> hashTags) {
        this.hashTags = hashTags;
        this.tweetId = tweetId;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public String getTweetId() {
        return tweetId;
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashTags.equals(((TweetHashTag) obj).getHashTags()) &&
                this.tweetId.equals(((TweetHashTag) obj).getTweetId());
    }
}
