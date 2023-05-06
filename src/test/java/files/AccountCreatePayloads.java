package files;

public class AccountCreatePayloads {

    public static String createAutomationAccountPayload(){
        String json= "{\n" +
                "  \"name\": \"Automation Bot\",\n" +
                "  \"mobile\":\"1000000000\",\n" +
                "  \"password\":\"##ofbdevautomation@2016##\",\n" +
                "  \"email\":\"\"\n" +
                "}";
        return json;
    }

    public static String createApproverAccountPayload(){
        String json= "{\n" +
                "  \"name\": \"Approver Bot\",\n" +
                "  \"mobile\":\"1000000001\",\n" +
                "  \"password\":\"##ofbdevautomation@2016##\",\n" +
                "  \"email\":\"\"\n" +
                "}";
        return json;
    }

    public static String sendLoginOtpPayload(String randomMobile){

        String json="{\"mobile\":\""+randomMobile+"\"}";
        return json;
    }

    public static String accountCreatePayload(String randomMobile,String clientOtp){
        String json = "{\n" +
                "\t\"profiles\": {\n" +
                "\t\t\"BUYER\": {\n" +
                "\t\t\t\"type\":\"buyer\"\n" +
                "\t\t},\n" +
                "\t\t\"OXYZOAPPLICANT\": {\n" +
                "\t\t\t\"type\":\"oxyzoapplicant\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"mobile\":\""+randomMobile+"\",\n" +
                "\t\"password\":\"Password@123\",\n" +
                "\t\"otp\":\""+clientOtp+"\"\n" +
                "}";

        return json;
    }
}
