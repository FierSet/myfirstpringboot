package com.practices.myfirstpringboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practices.myfirstpringboot.models.Links;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/dba")
public class dbapi {

    private final JdbcTemplate jdbcTemplate;
    private String base62characters;

    public dbapi(JdbcTemplate jdbcTemplate) 
    {
        this.jdbcTemplate = jdbcTemplate;
        base62characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    }

    @GetMapping("/connection")
    public String testConnection() 
    {
        try
        {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        }
        catch (Exception e)
        {
            return "SQLite connection failed: " + e.getMessage();
        }

        return "SQLite connection works";
    }

    @PostMapping("/addlink")
    public String addLink(@RequestBody Links request)
    {
        String url = request.getUrl();
        
        String findLinkSql = "SELECT ShortenedUrl FROM Links WHERE OriginalUrl = ?;";
        List<String> existingLinks = jdbcTemplate.queryForList(findLinkSql, String.class, url);
        
        if (!existingLinks.isEmpty()) 
            return existingLinks.get(0);
        
        String insertLinkSql = "INSERT INTO Links (OriginalUrl, ShortenedUrl) VALUES (?, ?) RETURNING Id;";
        Integer id = jdbcTemplate.queryForObject(insertLinkSql, Integer.class, url, url);

        String AddedShortenedUrl = "UPDATE Links SET ShortenedUrl = ? WHERE Id = ? RETURNING ShortenedUrl;";
        List<String> shortenedUrl = jdbcTemplate.queryForList(
            AddedShortenedUrl, String.class, EncodeBase62(id), id);
        
        return shortenedUrl.get(0);
    }

    @GetMapping("/getlink/{shortenedUrl}")
    public String getLink(@PathVariable String shortenedUrl) 
    {
        String findLinkSql = "SELECT OriginalUrl FROM Links WHERE ShortenedUrl = ?;";
        List<String> originalUrls = jdbcTemplate.queryForList(findLinkSql, String.class, shortenedUrl);
        
        return originalUrls.get(0);
    }
    
    private String EncodeBase62(int id)
    {
        if(id == 0) 
            return base62characters.substring(0, 1);

        String buffer = "";
        while(id > 0)
        {
            int remainder = (int)(id % 62);
            buffer = base62characters.substring(remainder, remainder + 1) + buffer;
            id /= 62;
        }

        return buffer;
    }
}
