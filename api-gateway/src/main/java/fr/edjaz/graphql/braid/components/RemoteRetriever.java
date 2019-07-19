package fr.edjaz.graphql.braid.components;

import com.atlassian.braid.source.GraphQLRemoteRetriever;
import com.atlassian.braid.source.Query;
import graphql.ExecutionInput;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RemoteRetriever<C> implements GraphQLRemoteRetriever<C> {

    private final WebClient webClient;

    public RemoteRetriever(String url, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(url).build();
    }

    @Override
    public CompletableFuture<Map<String, Object>> queryGraphQL(Query query, C c) {
        return webClient.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .syncBody(createBody(query))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .toFuture();
    }

    private Map<String, Object> createBody(Query query) {
        ExecutionInput executionInput = query.asExecutionInput();
        Map<String,Object> body = new HashMap<>();
        body.put("query", executionInput.getQuery());
        body.put("variables", executionInput.getVariables());
        return body;
    }
}
