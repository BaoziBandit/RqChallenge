package com.example.rqchallenge.clients;

import com.example.rqchallenge.controllers.EmployeeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public class DummyRestClient {
    private final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);
    private final HttpClient client = HttpClient.newHttpClient();

    private final String BASE_URL = "https://dummy.restapiexample.com/api/v1";

    public String getAll(String objectName){
        try{
            HttpRequest request = HttpRequest.newBuilder().GET()
                    .uri(URI.create(String.format("%s/%ss", BASE_URL, objectName))).build();
            HttpResponse<String> response =  client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get();
            return handleResponse(response);
        }catch (InterruptedException | ExecutionException e){
            LOG.error(e.getLocalizedMessage());
            return null;
        }
    }

    public String getById(String objectName, String id){
        try{
            HttpRequest request = HttpRequest.newBuilder().GET()
                    .uri(URI.create(String.format("%s/%s/%s", BASE_URL, objectName, id))).build();
            HttpResponse<String> response =  client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get();
            return handleResponse(response);
        }catch (InterruptedException | ExecutionException e){
            LOG.error(e.getLocalizedMessage());
            return null;
        }
    }

    public String create(String object){
        try{
            HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(object))
                    .header("Content-Type", "application/json")
                    .uri(URI.create(String.format("%s/create", BASE_URL))).build();
            HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get();
            return handleResponse(response);
        }catch (InterruptedException | ExecutionException e){
            LOG.error(e.getLocalizedMessage());
            return null;
        }
    }

    public String delete(String id){
        try{
            HttpRequest request = HttpRequest.newBuilder().DELETE()
                    .uri(URI.create(String.format("%s/delete/%s", BASE_URL, id))).build();
            HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get();
            return handleResponse(response);
        }catch (InterruptedException | ExecutionException e){
            LOG.error(e.getLocalizedMessage());
            return null;
        }
    }

    private String handleResponse(HttpResponse<String> response){
        if(response.statusCode() >= 200 && response.statusCode() < 300){
            return response.body();
        } else {
            return null;
        }
    }
}
