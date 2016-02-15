package com.js.type.concurrency;

import tern.server.nodejs.NodejsTernServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by sujithk on 15/2/16.
 */
public class Result {
    public List<String> getResults(List<String> jsFiles, NodejsTernServer server) throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newCachedThreadPool();
        List<Future<String>> futures = new ArrayList<>();
        for(String jsFile : jsFiles){
            Query query = new Query(server, jsFile);
            Future<String> result = pool.submit(query);
            futures.add(result);
        }

        List<String> results = new ArrayList<>();

        for(Future<String> f : futures){
            results.add(f.get());
        }
        return results;
    }
}
