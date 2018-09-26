package com.hibicode.kafkatwitterproducer.config;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.hibicode.kafkatwitterproducer.config.TwitterCredentialsConfig.TwitterCredential;

@Configuration
public class TwitterConfig {

    @Value("#{'${terms:kakfa}'.split(',')}")
    private List<String> terms;

    @Bean
    public BlockingQueue<String> blockingQueue() {
        return  new LinkedBlockingQueue<>(1000);
    }

    @Bean
    public Client twitterClient(BlockingQueue<String> blockingQueue, TwitterCredential twitterCredential) {
        Hosts hosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint filter = new StatusesFilterEndpoint();

        filter.trackTerms(terms);

        final String consumerKey = twitterCredential.getApiKey();
        final String consumerSecret = twitterCredential.getApiSecretKey();
        final String token = twitterCredential.getAccessToken();
        final String secret = twitterCredential.getAccessTokenSecret();
        Authentication authentication = new OAuth1(consumerKey, consumerSecret, token, secret);

        ClientBuilder builder = new ClientBuilder()
                .name("client-01")
                .hosts(hosts)
                .authentication(authentication)
                .endpoint(filter)
                .processor(new StringDelimitedProcessor(blockingQueue));

        return builder.build();
    }

}
