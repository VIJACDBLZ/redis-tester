package com.vijacdblz.redistester.app;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

@Component
public class RedisTester {

    @Autowired
    Partitioner partitioner;

    @EventListener(ApplicationReadyEvent.class)
    public void redisTesterStartup() {
        System.out.println("Application started");

        int batchSize = 1000;

        RandomStringGenerator rsg = new RandomStringGenerator(50);

        RedisClient redisClient = RedisClient.create("redis://localhost:6379/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisAsyncCommands<String, String> asyncCmd = connection.async();

        asyncCmd.flushall();

        UniqueSet<String> uniqueItemsP1 = new UniqueSet<>();
        UniqueSet<String> uniqueItemsP2 = new UniqueSet<>();
        UniqueSet<String> uniqueItemsP3 = new UniqueSet<>();
        UniqueSet<String> uniqueItemsP4 = new UniqueSet<>();

        Instant startTime = Instant.now();
        for (int i = 0; i < 1000000; i++) {
            // hash the input unique key

            String randString = rsg.nextString();
            // use partitioner to identify the set name
            try {
                switch (partitioner.getPartition(randString)) {

                    case 0:
                        uniqueItemsP1.add(randString, "P1");
                    case 1:
                        uniqueItemsP2.add(randString, "P2");
                    case 2:
                        uniqueItemsP3.add(randString, "P3");
                    case 3:
                        uniqueItemsP4.add(randString, "P4");
                    default:
                        throw new IllegalArgumentException("Unable to partition the message :" + randString);

                }
            } catch (Exception e) {
                System.out.println("Duplicate message found!");
                // TODO:code to handle duplicate
            }

            // code to check if individual batches are filled up and check redis for
            // uniqueness

            if (uniqueItemsP1.size() >= batchSize) {
                RedisFuture<List<Boolean>> p1CheckExistanceFuture = asyncCmd.smismember("P1",
                        uniqueItemsP1.stream().toArray(String[]::new));

            }

            if (uniqueItemsP2.size() >= batchSize) {
                RedisFuture<List<Boolean>> p2CheckExistanceFuture = asyncCmd.smismember("P2",
                        uniqueItemsP2.stream().toArray(String[]::new));

            }

            if (uniqueItemsP3.size() >= batchSize) {
                RedisFuture<List<Boolean>> p3CheckExistanceFuture = asyncCmd.smismember("P3",
                        uniqueItemsP3.stream().toArray(String[]::new));

            }

            if (uniqueItemsP4.size() >= batchSize) {
                RedisFuture<List<Boolean>> p4CheckExistanceFuture = asyncCmd.smismember("P4",
                        uniqueItemsP4.stream().toArray(String[]::new));

            }

        }

        Instant endTime = Instant.now();

        System.out.println("TIME TAKEN : " + Duration.between(startTime, endTime));
    }
}
