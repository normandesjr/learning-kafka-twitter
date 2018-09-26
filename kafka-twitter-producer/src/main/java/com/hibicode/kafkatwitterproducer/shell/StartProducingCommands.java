package com.hibicode.kafkatwitterproducer.shell;

import com.hibicode.kafkatwitterproducer.twitter.TwitterPolling;
import com.twitter.hbc.core.Client;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.concurrent.BlockingQueue;

@ShellComponent
public class StartProducingCommands {

    private Logger logger = LoggerFactory.getLogger(StartProducingCommands.class);

    @Autowired
    private BlockingQueue<String> blockingQueue;

    @Autowired
    private Client twitterClient;

    @Autowired
    private KafkaProducer<String, String> kafkaProducer;

    private TwitterPolling twitterPolling;
    private boolean connected = false;

    @ShellMethod("Connect to twitter API. Should be run first")
    public void connect() {
        twitterClient.connect();
        connected = true;
    }

    @ShellMethod("Start produce Kafka messages from tweets")
    public void start() {
        twitterPolling = new TwitterPolling(blockingQueue, twitterClient, kafkaProducer);
        new Thread(twitterPolling).start();
    }

    @ShellMethod("Stop produce Kafka messages from tweets")
    public void stop() {
        twitterPolling.stop();
    }

    @ShellMethod("Shutdown application")
    public void shutdown() {
        logger.info("Stopping application...");
        logger.info("shutting down client from twitter...");
        twitterClient.stop();
        logger.info("closing producer...");
        kafkaProducer.close();
        logger.info("done!");
        System.exit(0);
    }

    public Availability startAvailability() {
        return connected
                ? Availability.available()
                : Availability.unavailable("you are not connected");
    }

}
