package com.hibicode.kafkatwitterproducer.twitter;

import com.twitter.hbc.core.Client;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class TwitterPolling implements Runnable {

    private Logger logger = LoggerFactory.getLogger(TwitterPolling.class);

    private final BlockingQueue<String> blockingQueue;
    private final Client twitterClient;
    private final KafkaProducer<String, String> kafkaProducer;

    private boolean stop = false;

    public TwitterPolling(BlockingQueue<String> blockingQueue, Client twitterClient, KafkaProducer<String, String> kafkaProducer) {
        this.blockingQueue = blockingQueue;
        this.twitterClient = twitterClient;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void run() {
        stop = false;

        while (!twitterClient.isDone() && !stop) {
            final String message = pollMessage();
            sendMessage(message);
        }
    }

    private String pollMessage() {
        String message = null;
        try {
            message = blockingQueue.poll(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Error polling tweets", e);
            twitterClient.stop();
        }
        return message;
    }

    private void sendMessage(String message) {
        if (message != null) {
//            logger.info(message);
            kafkaProducer.send(new ProducerRecord<>("twitter_tweets", null, message), (recordMetadata, e) -> {
                if (e != null) {
                    logger.error("Something bad happened sending the tweet to twitter", e);
                }
            });
        }
    }

    public void stop() {
        stop = true;
    }
}
