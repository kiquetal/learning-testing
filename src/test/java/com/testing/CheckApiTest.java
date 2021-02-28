package com.testing;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class CheckApiTest {

    private static RequestSpecification requestSpecification;


    @BeforeAll
    public static void setUrl()
    {
        requestSpecification= new RequestSpecBuilder()
                .setBaseUri("http://zippopotam.us")
                .build();
    }

    public static Object [] [] zipCodesAndPlaces()
    {
        return new Object[][] {
                { "us","90210","Beverly Hills" },
                { "us","12345","Schenectady"},
                {"ca","B2R","Waverley"}
        };

    }



    @Test
    public void requestUsZipCode90210_logRequestAndLogResponse()
    {

        given().spec(requestSpecification).log().all().
                when().get("/us/90210").
                then().log().body();
     }

     @ParameterizedTest
     @MethodSource("zipCodesAndPlaces")
     public void testFromCollection(String country,String zip, String name)
     {
         given().spec(requestSpecification).
                 pathParam("countryCode",country).
                 pathParam("zip",zip).
                 when().get("/{countryCode}/{zip}")
                 .then().assertThat().body("places[0].'place name'",equalTo(name));

     }
     @Test
    public void requestUsZipCode90210_AssertJSONBody()
     {
         given().spec(requestSpecification).
                 when().get("/us/90210")
                 .then().assertThat().body("places[0].'place name'",equalTo("Beverly Hills"));

     }
     @Test
    public void notIncludeNameInCollections()
     {
         given().spec(requestSpecification).
                 when().get("/us/90210")
                 .then().assertThat().body("places.'place name'",not(hasItem("Beverly Hills")));
     }




}