package com.lianyitech.common.config;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;

public class SolrServerConfigurer {
    public static HttpSolrServer solrServer (String solrUrl, int solrMaxRetries, int solrConnectionTimeout) {
        HttpSolrServer solrServer = new HttpSolrServer(solrUrl);
        solrServer.setParser(new XMLResponseParser());
        solrServer.setMaxRetries(solrMaxRetries);
        solrServer.setConnectionTimeout(solrConnectionTimeout);
        return solrServer;
    }
}
