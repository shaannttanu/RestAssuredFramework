package client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Base {
    public static Properties prop;
    public static void getConfigProperties(){
        prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream("C:\\Users\\shant\\Desktop\\RestAssuredFramework\\src\\test\\java\\client\\config.properties");
            prop.load(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getBaseUri(){
        getConfigProperties();
        return prop.getProperty("baseUri");
    }

    public static String getRediskey(){
        getConfigProperties();
        return prop.getProperty("redisKey");
    }
    public static String getSuperAdminToken(){
        getConfigProperties();
        return prop.getProperty("superAdminToken");
    }
    public static String getRandomMobile(){
        getConfigProperties();
        return prop.getProperty("randomMobile");
    }
}
