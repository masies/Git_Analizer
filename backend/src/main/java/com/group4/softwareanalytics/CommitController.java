package com.group4.softwareanalytics;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommitController {

  private final AtomicLong counter = new AtomicLong();

  @GetMapping("/commit")
  @ResponseBody
  public Commit getCommit(@RequestParam(name="developer", required=false, defaultValue="default dev") String developer) {
    return new Commit(counter.incrementAndGet(),"hello world", developer);
  }

  @PostMapping("/commit")
  @ResponseBody
  public Object setCommit(@RequestBody Map<String,Object> objectJson) {
    System.out.println(objectJson.get("developer").toString());
    return objectJson.get("developer").toString();
//    return objectJson;
//    return new Commit(counter.incrementAndGet(),"hello world", developer);
  }

  @PostMapping(path = "/members")
  public void addMemberV1(@RequestBody Object commit) {
    System.out.println(commit);
  }


}



