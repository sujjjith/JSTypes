package com.js.type.concurrency;

import tern.server.DefaultResponseHandler;
import tern.server.nodejs.NodejsTernServer;
import tern.server.protocol.TernDoc;
import tern.server.protocol.TernQuery;
import tern.server.protocol.outline.TernOutlineQuery;

import java.util.concurrent.Callable;

/**
 * Created by sujithk on 15/2/16.
 */
public class Query implements Callable<String> {
    private NodejsTernServer server;
    private String jsFile;

    public Query(NodejsTernServer server, String jsFile) {
        this.server = server;
        this.jsFile = jsFile;
    }

    @Override
    public String call() throws Exception {
        TernDoc request = createRequest(jsFile);
        DefaultResponseHandler response = createResponseHandler();
        server.request(request,response);
        return response.getJsonString();
    }

    private  TernDoc createRequest(String queryFileLoc) {
        TernQuery query = new TernOutlineQuery(queryFileLoc);
        TernDoc request = new TernDoc(query);
        return request;
    }

    private DefaultResponseHandler createResponseHandler() {
        DefaultResponseHandler response = new DefaultResponseHandler(true);
        return response;
    }

}
