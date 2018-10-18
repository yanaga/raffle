package com.redhat.developers.raffle;

import org.junit.Test;
import twitter4j.Query;

import static org.junit.Assert.*;

public class QueryBuilderTest {

    @Test
    public void testBuild() {
        assertEquals(new Query("@yanaga filter:media -filter:retweets"), QueryBuilder.of("yanaga").build());
        assertEquals(new Query("@yanaga @burrsutter filter:media -filter:retweets"), QueryBuilder.of("yanaga", "burrsutter").build());
        assertEquals(new Query("@rafabene @burrsutter filter:media -filter:retweets"), QueryBuilder.of("rafabene", "burrsutter").build());
        assertEquals(new Query("@rafabene @burrsutter #openshift filter:media -filter:retweets"), QueryBuilder.of("rafabene", "burrsutter").addHashtag("openshift").build());
        assertEquals(new Query("@rafabene @burrsutter #openshift -filter:retweets"), QueryBuilder.of("rafabene", "burrsutter").addHashtag("openshift").anyTweet().build());
    }

}