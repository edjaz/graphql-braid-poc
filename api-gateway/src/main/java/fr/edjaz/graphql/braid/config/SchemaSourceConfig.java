package fr.edjaz.graphql.braid.config;

import java.util.Collections;
import java.util.List;

import com.atlassian.braid.Link;
import com.atlassian.braid.LinkArgument;
import com.atlassian.braid.SchemaNamespace;
import com.atlassian.braid.SchemaSource;
import com.atlassian.braid.source.QueryExecutorSchemaSource;
import fr.edjaz.graphql.braid.components.RemoteIntrospection;
import fr.edjaz.graphql.braid.components.RemoteRetriever;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Data
public class SchemaSourceConfig {

    private static final String USERS_SCHEMA_URL = "http://localhost:8080/graphql";
    private static final String BOOKS_SCHEMA_URL = "http://localhost:8081/graphql";


    @Bean
    public SchemaSource userSchemaSource(WebClient.Builder webClientConfigBuilder) {

        LinkArgument argument = LinkArgument.newLinkArgument()
                .sourceName("bookId")
                .argumentSource(LinkArgument.ArgumentSource.OBJECT_FIELD)
                .queryArgumentName("id")
                .build();

        return querySchemaSourceBuilder("users", USERS_SCHEMA_URL,
                Collections.singletonList(Link.newComplexLink()
                        .sourceNamespace(SchemaNamespace.of("users"))
                        .sourceType("User")
                        .newFieldName("book")
                        .targetNamespace(SchemaNamespace.of("books"))
                        .topLevelQueryField("bookById")
                        .targetType("Book")
                        .linkArgument(argument)
                        .build()),

                webClientConfigBuilder);
    }


    @Bean
    public SchemaSource bookSchemaSource(WebClient.Builder webClientConfigBuilder) {
        return querySchemaSourceBuilder("books", BOOKS_SCHEMA_URL, Collections.emptyList(), webClientConfigBuilder);
    }


    private SchemaSource querySchemaSourceBuilder(String namespace, String schemaUrl, List<Link> links, WebClient.Builder webClientConfigBuilder) {
        return QueryExecutorSchemaSource.builder().namespace(SchemaNamespace.of(namespace))
                .schemaProvider(() -> new RemoteIntrospection(schemaUrl, webClientConfigBuilder).get())
                .remoteRetriever(new RemoteRetriever<>(schemaUrl, webClientConfigBuilder))
                .links(links)
                .build();
    }

}

