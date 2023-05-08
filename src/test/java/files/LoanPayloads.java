package files;

public class LoanPayloads {

    public static String getCreateLoanApplicationPayload(String contactPersonName,String contactPersonEmail,
                                                  String randomMobile,String clientOrganisationId,
                                                  String clientName){
        String json = "{\n" +
                "\"loanAmount\":\"1000000\",\n" +
                "\"contactPersonName\":\""+contactPersonName+"\",\n" +
                "\"contactPersonEmail\":\""+contactPersonEmail+"\",\n" +
                "\"bureauAcceptance\":\"true\",\n" +
                "\"contactPersonNumber\":\""+randomMobile+"\",\n" +
                "\"organisationId\" : \""+clientOrganisationId+"\",\n" +
                "\"clientName\":\""+clientName+"\"\n" +
                "}";
        return json;
    }
}
