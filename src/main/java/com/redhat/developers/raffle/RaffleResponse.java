package com.redhat.developers.raffle;

import java.util.Objects;

public class RaffleResponse {

    private final String username;

    private final String tweet;

    private RaffleResponse(String username, String tweet) {
        this.username = username;
        this.tweet = tweet;
    }

    public static RaffleResponse of(String username, long id) {
        return new RaffleResponse(username, String.format("https://twitter.com/%s/status/%d", username, id));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RaffleResponse) {
            RaffleResponse other = (RaffleResponse) obj;
            return Objects.equals(this.username, other.username);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username);
    }

    public String getUsername() {
        return username;
    }

    public String getTweet() {
        return tweet;
    }

}
