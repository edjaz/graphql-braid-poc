package fr.edjaz.graphql.braid.components;

import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import graphql.introspection.IntrospectionResultToSchema;
import graphql.language.Document;
import graphql.schema.idl.SchemaPrinter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;


@Slf4j
public class RemoteIntrospection {

    private final RestTemplate webClient;
    private final String url;

    public RemoteIntrospection(String url) {
        this.webClient = new RestTemplate();
        this.url = url;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Query {
        private String query;
    }

    public Reader get() {
        HttpEntity<Query> request = new HttpEntity<>(new Query(introspectionQuery()));
        Map introspectionResult = webClient.postForObject(url, request, Map.class);
        Document schema = new IntrospectionResultToSchema().createSchemaDefinition((Map<String, Object>) introspectionResult.get("data"));
        String printedSchema = new SchemaPrinter().print(schema);
        return new StringReader(printedSchema);
    }

    private String introspectionQuery() {
        return "\n" +
                "    query IntrospectionQuery {\n" +
                "      __schema {\n" +
                "        queryType { name }\n" +
                "        mutationType { name }\n" +
                "        subscriptionType { name }\n" +
                "        types {\n" +
                "          ...FullType\n" +
                "        }\n" +
                "        directives {\n" +
                "          name\n" +
                "          description\n" +
                "          locations\n" +
                "          args {\n" +
                "            ...InputValue\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "\n" +
                "    fragment FullType on __Type {\n" +
                "      kind\n" +
                "      name\n" +
                "      description\n" +
                "      fields(includeDeprecated: true) {\n" +
                "        name\n" +
                "        description\n" +
                "        args {\n" +
                "          ...InputValue\n" +
                "        }\n" +
                "        type {\n" +
                "          ...TypeRef\n" +
                "        }\n" +
                "        isDeprecated\n" +
                "        deprecationReason\n" +
                "      }\n" +
                "      inputFields {\n" +
                "        ...InputValue\n" +
                "      }\n" +
                "      interfaces {\n" +
                "        ...TypeRef\n" +
                "      }\n" +
                "      enumValues(includeDeprecated: true) {\n" +
                "        name\n" +
                "        description\n" +
                "        isDeprecated\n" +
                "        deprecationReason\n" +
                "      }\n" +
                "      possibleTypes {\n" +
                "        ...TypeRef\n" +
                "      }\n" +
                "    }\n" +
                "\n" +
                "    fragment InputValue on __InputValue {\n" +
                "      name\n" +
                "      description\n" +
                "      type { ...TypeRef }\n" +
                "      defaultValue\n" +
                "    }\n" +
                "\n" +
                "    fragment TypeRef on __Type {\n" +
                "      kind\n" +
                "      name\n" +
                "      ofType {\n" +
                "        kind\n" +
                "        name\n" +
                "        ofType {\n" +
                "          kind\n" +
                "          name\n" +
                "          ofType {\n" +
                "            kind\n" +
                "            name\n" +
                "            ofType {\n" +
                "              kind\n" +
                "              name\n" +
                "              ofType {\n" +
                "                kind\n" +
                "                name\n" +
                "                ofType {\n" +
                "                  kind\n" +
                "                  name\n" +
                "                  ofType {\n" +
                "                    kind\n" +
                "                    name\n" +
                "                  }\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  ";
    }

}
