package fr.edjaz.graphql.braid.controllers;

import java.io.IOException;
import java.util.Map;

import com.atlassian.braid.BraidGraphQL;
import graphql.ExecutionInput;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GraphQLController {

    private final BraidGraphQL graphQL;

    @Data
    public static class GraphQlQuery {
        private String query;
        private Map<String, Object> variables;
    }

    @PostMapping("/graphql")
    public Object graphQLPost(@RequestBody GraphQlQuery body) throws IOException {
        ExecutionInput build = ExecutionInput.newExecutionInput()
                .query(body.query)
                .variables(body.variables)
                .build();
        return graphQL.execute(build);
    }

}
