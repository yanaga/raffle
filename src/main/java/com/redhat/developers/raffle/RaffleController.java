package com.redhat.developers.raffle;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class RaffleController {

    private static final int MAX_SIZE = 150;

    private final Twitter twitter = new TwitterFactory().getInstance();

    @GetMapping(value = "/{csvHashtags}/{csvUsernames}")
    public List<RaffleResponse> hashtagsAndUsernames(@PathVariable("csvUsernames") String[] csvUsernames, @PathVariable("csvHashtags") String[] csvHashtags, @RequestParam(value = "limit", required = false, defaultValue = "20") int limit) throws Exception {
        QueryResult queryResult = twitter.search(QueryBuilder.of(csvUsernames).addHashtags(csvHashtags).build());
        return queryResult.getTweets().stream()
                .map(s -> RaffleResponse.of(s.getUser().getScreenName(), s.getId()))
                .distinct()
                .limit(MAX_SIZE)
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(ArrayList::new),
                        list -> {
                            Collections.shuffle(list);
                            return list.stream();
                        }
                ))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{csvHashtags}")
    public List<RaffleResponse> hashtags(@PathVariable("csvHashtags") String[] csvHashtags, @RequestParam(value = "limit", required = false, defaultValue = "20") int limit) throws Exception {
        QueryResult queryResult = twitter.search(QueryBuilder.of().addHashtags(csvHashtags).build());
        return queryResult.getTweets().stream()
                .map(s -> RaffleResponse.of(s.getUser().getScreenName(), s.getId()))
                .distinct()
                .limit(MAX_SIZE)
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(ArrayList::new),
                        list -> {
                            Collections.shuffle(list);
                            return list.stream();
                        }
                ))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private boolean checkIfUserIsFollowerOf(String source, String target) {
        try {
            return twitter.showFriendship(source, target).isSourceFollowedByTarget();
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String rootMessage() {
        return "Usage:" +
                "\n" +
                "/{hashtags}: You can provide a comma-separated list of hashtags to filter" +
                "\n" +
                "/{hashtags}/{usernames}: You can provide a comma-separated list of hashtags *AND* a comma-separated list of usernames to filter" +
                "\n" +
                "You can also add the 'limit' request parameter to limit the results. Example: ?limit=1 will give you only the 1st result";
    }

}
