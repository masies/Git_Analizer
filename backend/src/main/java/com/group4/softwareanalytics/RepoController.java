package com.group4.softwareanalytics;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RepoController {
    @Autowired
    private RepoRepository repository;

  @PostMapping("/repo")
  @ResponseBody
  public Repo fetchRepo(@RequestBody String repoURL) {
      try {
          URL url = new URL(repoURL);
          URLConnection request = url.openConnection();
          request.connect();
          Gson gson = new Gson();
          // Convert to a JSON object to print data

          JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
          JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.

//          System.out.println(root.toString());

          Long id = rootobj.get("id").getAsLong();
          String name = rootobj.get("name").getAsString();
          String full_name = rootobj.get("full_name").getAsString();
          String html_url = rootobj.get("html_url").getAsString();
          String description = rootobj.get("description").getAsString();
          Date created_at = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rootobj.get("created_at").getAsString());
          Date updated_at = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(rootobj.get("updated_at").getAsString());
          String homepage = rootobj.get("homepage").getAsString();
          Long size = rootobj.get("size").getAsLong();
          Long watchers_count = rootobj.get("watchers_count").getAsLong();
          String language = rootobj.get("language").getAsString();
          Long forks_count = rootobj.get("forks_count").getAsLong();
          Long open_issues_count = rootobj.get("open_issues_count").getAsLong();
          Long forks = rootobj.get("forks").getAsLong();
          Long subscribers_count = rootobj.get("subscribers_count").getAsLong();

          Repo repo = new Repo(id,name,full_name,html_url,description,created_at,updated_at,homepage,size,watchers_count,language,forks_count,open_issues_count,forks,subscribers_count);

          repository.save(repo);

          return repo;

      }catch (Exception e){
          System.out.println(e);
      }

      return null;
  }




}







