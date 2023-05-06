package files;

public class AdminAuthTokenPayloads {
    public static String adminAuthTokenPayload(){
        String json="{\n" +
                "  \"mobile\":\"1000000000\",\n" +
                "  \"password\":\"##ofbdevautomation@2016##\",\n" +
                "  \"userProfileType\":\"OXYZOAPPLICANT\"\n" +
                "}";
        return json;
    }
    public static String approverAdminAuthTokenPayload(){
        String json = "{\n" +
                "  \"mobile\":\"1000000001\",\n" +
                "  \"password\":\"##ofbdevautomation@2016##\",\n" +
                "  \"userProfileType\":\"OXYZOAPPLICANT\"\n" +
                "}";
        return json;
    }
}
