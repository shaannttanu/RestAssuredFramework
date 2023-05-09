package client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class GetConfigProperties {
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

    public static String getStgAPI(){
        getConfigProperties();
        return prop.getProperty("stgAPI");
    }

    public static String getStgfs(){
        getConfigProperties();
        return prop.getProperty("stgfs");
    }

    public static String getOxyzoAPI(){
        getConfigProperties();
        return prop.getProperty("oxyzoAPI");
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
    public static String getAddressId(){
        getConfigProperties();
        return prop.getProperty("addressId");
    }

    public static String getCIN(){
        getConfigProperties();
        return prop.getProperty("CIN");
    }
}