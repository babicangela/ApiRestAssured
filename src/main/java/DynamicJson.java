import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

public class DynamicJson {

    @Test(dataProvider = "booksData")
    public void addBook(String name, String isbn, String aisle, String author) {

        baseURI="http://216.10.245.166";
        String response = given().log().all().header("Content-Type", "application/json").body(Payload.addBook(name, isbn, aisle, author))
                .when().post("/Library/Addbook.php").then().log().all().assertThat().statusCode(200)
                .extract().response().asString();
        JsonPath js = ReUsableMethods.rawToJason(response);
        String id = js.get("ID");
        System.out.println(id);

//        deleteBook



    }

    @DataProvider(name = "booksData")
    public Object[][] getData(){

//      Array - collection of elements
//      Multidimensional array - collection of arrays
        return new Object[][] {
                {"The Great Gatsby", "978-0-7475-8294-0", "1234", "F. Scott Fitzgerald"},
                {"To Kill a Mockingbird", "978-0-449-31481-3", "3333", "Harper Lee"},
                {"Pride and Prejudice", "978-0-140-37941-0","2233", "Jane Austen"}};

    }
}
