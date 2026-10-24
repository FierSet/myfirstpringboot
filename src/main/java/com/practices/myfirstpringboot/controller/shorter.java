package com.practices.myfirstpringboot.controller;

import java.net.URI;
import java.io.IOException;
import java.net.http.HttpClient;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Controller
public class shorter {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/{shortenedUrl}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortenedUrl) 
    {
        HttpClient client = HttpClient.newHttpClient();

        String baseUrl = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .build()
            .toUriString();
        String originalUrl = baseUrl;

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + "/dba/getlink/" + shortenedUrl))
            .GET() // Optional, GET is default
            .build();
        HttpResponse<String> response;
        
        try 
        {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(!response.body().isEmpty() || response.body() != null)
                originalUrl = response.body();
        } 
        catch (IOException e) 
        {
            System.err.println("Error during HTTP request: " + e.getMessage());
        } 
        catch (InterruptedException e) 
        {
            System.err.println("Error during HTTP request: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(originalUrl))
            .build();
    }
    
}
