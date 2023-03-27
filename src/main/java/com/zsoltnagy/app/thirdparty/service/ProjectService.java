package com.zsoltnagy.app.thirdparty.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.json.*;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    public static final String REPO_OWNER = "Chezo";
    public static final String GITHUB_GRAPHQL_URL = "https://api.github.com/graphql";

    private final String authorization;

    public ProjectService(@Value("${github-authorization-token}") String authorization) {
        this.authorization = authorization;
    }

    public List<String> getRepoNames(int limit) {
        List<String> repoNames;

        String query = getRepoNameQuery(limit);

        try {
            String repoData = getResponse(query, authorization);
            StringReader reader = new StringReader(repoData);
            JsonReader jsonReader = Json.createReader(reader);
            JsonObject result = jsonReader.readObject();
            JsonArray repos = result.get("data").asJsonObject().get("user").asJsonObject().get("repositories").asJsonObject().get("edges").asJsonArray();
            repoNames = repos.stream().map(this::getRepoName).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return repoNames;
    }

    private String getRepoName(JsonValue jsonValue) {
        String repoOwner = jsonValue.asJsonObject().get("node").asJsonObject().get("name").toString();
        return repoOwner.replaceAll("\"", "");
    }


    private String getResponse(String query, String authorizationToken) throws IOException {
        CloseableHttpResponse response;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = buildRequest(authorizationToken, query);
            response = client.execute(httpPost);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    private HttpPost buildRequest(String authorizationToken, String query) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(GITHUB_GRAPHQL_URL);
        httpPost.addHeader("Authorization", authorizationToken);
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-Type", "application/json");
        StringEntity entity = new StringEntity("{\"query\":\"query " + query + "\"}");
        httpPost.setEntity(entity);
        return httpPost;
    }

    public JsonNode getRepoData(String repoName) {
        JsonNode jsonNode = null;
        String response;

        String query = getRepoQuery(repoName);
        try {
            response = getResponse(query, authorization).replaceAll("\\\\n", "");
            jsonNode = new ObjectMapper().readTree(response);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonNode;
    }

    private static String getRepoQuery(String repoName) {
        return "{  repository(owner: \\\"" + REPO_OWNER + "\\\", name: \\\"" + repoName + "\\\") {collaborators {totalCount edges {node{login url}}}createdAt description url techStack:object(expression: \\\"main:tech-stack\\\") {... on Blob {text}} longDescription:object(expression: \\\"main:long-description\\\") {... on Blob { text}} images:object(expression: \\\"main:imgs/\\\") { ... on Tree { entries {name path extension}}} imageURL:object(expression: \\\"main:img-URL\\\") { ... on Blob { text}}}}";
    }

    private static String getRepoNameQuery(int limit) {
        return "{user(login: \\\"" + REPO_OWNER + "\\\") {email repositories(orderBy: {field: PUSHED_AT, direction: DESC} first: " + limit + " privacy: PUBLIC ownerAffiliations: OWNER) {edges { node { name }}}}}";
    }
}
