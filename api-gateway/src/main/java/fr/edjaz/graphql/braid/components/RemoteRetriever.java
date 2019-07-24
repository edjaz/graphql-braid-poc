package fr.edjaz.graphql.braid.components;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.atlassian.braid.source.GraphQLRemoteRetriever;
import com.atlassian.braid.source.Query;
import graphql.ExecutionInput;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class RemoteRetriever<C> implements GraphQLRemoteRetriever<C> {

    private final RestTemplate webClient;
    private final String url;


    public RemoteRetriever(String url) {
        this.webClient = new RestTemplate();
        this.url = url;
    }

    @Override
    public CompletableFuture<Map<String, Object>> queryGraphQL(Query query, C c) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> request = new HttpEntity<>(createBody(query));
        Map results = webClient.postForObject(url, request, Map.class);
        return CompletableFuture.completedFuture(results);
    }

    private Map<String, Object> createBody(Query query) {
        ExecutionInput executionInput = query.asExecutionInput();
        Map<String,Object> body = new HashMap<>();
        body.put("query", executionInput.getQuery());
        body.put("variables", executionInput.getVariables());
        return body;
    }
}
