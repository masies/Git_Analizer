package com.group4.softwareanalytics;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import jdk.nashorn.internal.parser.JSONParser;
import net.minidev.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class HelloWorldController {

  private static final String template = "Hello, %s!";
  private final AtomicLong counter = new AtomicLong();

  @GetMapping("/hello-world")
  @ResponseBody
  public Greeting sayHello(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {
    return new Greeting(counter.incrementAndGet(), String.format(template, name));
  }
//
//  @PostMapping(path = "/hello-worlds",  produces = MediaType.APPLICATION_XML_VALUE)
//  @ResponseBody
//  public Greeting sayHello2(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {
//    return new Greeting(counter.incrementAndGet(), String.format(template, name));
//  }

}


