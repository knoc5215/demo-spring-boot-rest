package me.jumen.demospringbootrest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("/hello")
    public String hello() throws InterruptedException {
        Thread.sleep(5000l);
        return "hello";
    }

    @GetMapping("world")
    public String word() throws InterruptedException {
        Thread.sleep(3000l);
        return "world";
    }
}

