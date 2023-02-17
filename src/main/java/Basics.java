import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
// The import statement that allows usage of static methods and fields of the RestAssured classwithout having to qualify them with the class name
import static io.restassured.RestAssured.*;
// Iz nekog razloga nije htelo da prihvati sa zvezdicom nego je importovalo ovu static biblioteku
import static org.hamcrest.Matchers.equalTo;

public class Basics {

    public static void main(String[] args){

// validate if Add_Place API is working as expected
//        Add place --> Update place with New Address --> Get place to validate if New address is present in response
//        In this task we need 3 API (1.Add new place, 2.PUT to update existing place we added, and 3.GET to retrieve the place we ADDed and verify that the new address is returned)

//         given - take all input details we need to submit
//         when - Submit the API
//         Then - validate the response
//         Content od the Json file to String -> content of file into Byte -> Byte data to String u body-u se doda path to Json
        RestAssured.baseURI = "https://rahulshettyacademy.com/";
        String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(Payload.AddPlace()).when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();

        System.out.println(response);
        JsonPath js = new JsonPath(response);
        String placeId = js.getString("place_id");

        System.out.println(placeId);


//        Update Place
        String newAddress = "70 winter walk, USA";
        given().log().all().queryParam("key","qaclick123").header("Content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\"" + placeId + "\",\n" +
                        "\"address\":\"" + newAddress + "\",\n" +
                        "\"key\":\"" + "qaclick123" + "\"\n" +
                        "}").when().put("maps/api/place/update/json")
                .then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));


//        Get Place and Verify that the address is changed
        String getPlaceResponse = given().log().all().queryParam("key", "qaclick123")
                .queryParam("place_id", placeId).when().get("maps/api/place/get/json")
                .then().assertThat().log().all().statusCode(200).extract().response().asString();

        JsonPath js1 = ReUsableMethods.rawToJason(getPlaceResponse);
        String actualAddress = js1.getString("address");
        System.out.println(actualAddress);
//        Cucumber Junit, Testng
        Assert.assertEquals(actualAddress, newAddress);














    }
}
