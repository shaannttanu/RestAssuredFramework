package files;

import io.restassured.path.json.JsonPath;

public class ReusableMethods {
    public static JsonPath rawToJson(String response){
        JsonPath js=new JsonPath(response);
        return js;
    }

    public static String key(){
        return "qaclick123";
    }
    public static String headerOption(){
        return "application/json";
    }

}
