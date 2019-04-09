package pl.zaradny.springApp.api;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import pl.zaradny.springApp.SpringAppApplicationTests;

public class HelloWorldEndpointTest extends SpringAppApplicationTests {

    @Test
    public void shouldReturnGreetings() {
        //given
        final String url = "http://localhost:"+ port + "/hello";
        //when
        ResponseEntity<String> entity = httpClient.getForEntity(url, String.class);
        //then
        Assertions.assertThat(entity.getStatusCodeValue()).isEqualTo(200);
        Assertions.assertThat(entity.getBody()).isEqualTo("Hello Heroku World!");
    }
}
