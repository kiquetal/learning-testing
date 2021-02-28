package com.testing;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
public class CheckApiTest {
    @Test
    public void requestUsZipCode90210_logRequestAndLogResponse()
    {

        given().log().all().
                when().get("http://zippopotam.us/us/90210").
                then().log().body();
     }

     @Test
    public void requestUsZipCode90210_AssertBody()
     {
         given().
                 when().get("http://zippopotam.us/us/90210")
                 .then().assertThat().body("places[0].'place name'",equalTo("Beverly Hills"));

     }


}