package fr.edjaz.graphql.braid.config;

import java.util.List;

import com.atlassian.braid.Braid;
import com.atlassian.braid.BraidGraphQL;
import com.atlassian.braid.SchemaSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BraidConfigBuilder {

    @Bean
    public BraidGraphQL graphQL(List<SchemaSource> schemaSourceList) {
        return Braid.builder().schemaSources(schemaSourceList).build().newGraphQL();
    }

}
