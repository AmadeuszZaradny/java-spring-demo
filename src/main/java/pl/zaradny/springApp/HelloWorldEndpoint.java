package pl.zaradny.springApp;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldEndpoint {

    //localhost:8080/hello
    //HelloWorld

    @RequestMapping(method = RequestMethod.GET, path = "/hello")
    String hello(){
        return "Hello World!";
    }
}
