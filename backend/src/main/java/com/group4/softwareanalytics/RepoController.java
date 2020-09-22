package com.group4.softwareanalytics;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RepoController {
    @Autowired
    private RepoRepository repository;

  @PostMapping("/repo")
  @ResponseBody
  public Repo fetchRepo(@RequestBody Map<String,Object> body) {
      try {
          String repoURL = body.get("url").toString();
          URL url = new URL(repoURL);
          URLConnection request = url.openConnection();
          request.connect();
          Gson gson = new Gson();
          // Convert to a JSON object to print data

          JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element

          // get current dateTime in ISO format
          TimeZone tz = TimeZone.getTimeZone("UTC");
          DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
          df.setTimeZone(tz);
          String nowAsISO = df.format(new Date());
          // add those to the repo object
          root.getAsJsonObject().addProperty("my_created_at", nowAsISO);
          root.getAsJsonObject().addProperty("my_updated_at", nowAsISO);

          Repo repo = gson.fromJson(root,Repo.class);
          repository.save(repo);

          return repo;

      }catch (Exception e){
          System.out.println(e);
      }

      return null;
  }
}







