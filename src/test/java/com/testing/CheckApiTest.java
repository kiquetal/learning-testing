package com.testing;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.httpclient.HttpStatus;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

/*
   you should declare Per_class to be able to use parameterized test.

   if you have a different class when you provide values, the method to be provided must be static.

 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CheckApiTest {

    private static RequestSpecification requestSpecification;

    private static ResponseSpecification responseSpecification;



    @BeforeAll
    public static void setUrl()
    {
        requestSpecification= new RequestSpecBuilder()
                .setBaseUri("http://zippopotam.us")
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_ACCEPTED)
                .expectContentType(ContentType.JSON)
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
    public void extractData()
    {
        String placeName= given().spec(requestSpecification).
                when().get("/us/90210")
                .then().extract().path("places[0].'place name'");

        Assert.assertEquals(placeName,"Beverly Hill");
    }

    @Test
    public void requestUsZipCode90210_logRequestAndLogResponse()
    {

        given().spec(requestSpecification).log().all().
                when().get("/us/90210").
                then().spec(responseSpecification).log().body();
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
                 .then().assertThat().body("places.'place name'", not(hasItem("Beverly Hills")));
     }


    @ParameterizedTest
    @MethodSource(value = "com.testing.ProviderMethod#sourceStream_StringDouble")
    void method_sourcePackage(String param1, double param2)
    {
        System.out.println("Value "+ param1 + "-"+ param2);

    }

     @ParameterizedTest
     @MethodSource(value = "argumentsStream")
     void method_source(String param1, double param2)
     {
         System.out.println("Value "+ param1 + "-"+ param2);

     }
     List<Arguments> sourceListDouble()
     {
        return Arrays.asList(Arguments.arguments("tomate",1.2),Arguments.arguments("carrot",4.5));
     }

    Stream<Arguments> argumentsStream()
    {

        return Stream.of(Arguments.arguments("tomate",2.3),Arguments.arguments("apple",4.5));
    }

    @ParameterizedTest(name = "Run:{index} - value:{arguments}")
    @ValueSource(ints = {1,2,3,4})
    void intValues(int Param)
    {
        System.out.println("Value "+ Param);
    }
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"word","firstWord"})
    void stringValues(String param)
    {
        System.out.println("value is:"+param);
    }
}