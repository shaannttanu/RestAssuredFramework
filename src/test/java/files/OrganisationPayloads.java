package files;

public class OrganisationPayloads {
    public static String createOrganisationPayload(String randomMobile,String contactName,String organisationName){

        String json= "{\n" +
                "    \"contactMobile\": \""+randomMobile+"\",\n" +
                "    \"contactName\": \""+contactName+"\",\n" +
                "    \"orgName\": \""+organisationName+"\",\n" +
                "    \"industry\": \"MANUFACTURING\",\n" +
                "    \"industryType\": \"AUTO_AND_AUTO_ANCILLARIES\",\n" +
                "    \"pinCode\": \"121001\",\n" +
                "    \"potential\": \"500\",\n" +
                "    \"organisationType\": \"BUYER\"\n" +
                "}";
        return  json;
    }
}
