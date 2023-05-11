package files;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Payload {

    //AccountCreate Payloads :
    @Contract(pure = true)
    public static @NotNull String automationApproverAccountPayload(String name , String mobile){
        String json= "{\n" +
                "  \"name\": \""+name+"\",\n" +
                "  \"mobile\":\""+mobile+"\",\n" +
                "  \"password\":\"##ofbdevautomation@2016##\",\n" +
                "  \"email\":\"\"\n" +
                "}";
        return json;
    }

    @Contract(pure = true)
    public static @NotNull String sendLoginOtpPayload(String randomMobile){

        String json="{\"mobile\":\""+randomMobile+"\"}";
        return json;
    }

    @Contract(pure = true)
    public static @NotNull String accountCreatePayload(String randomMobile, String clientOtp){
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

    //AdminAuthToken Payloads :
    @Contract(pure = true)
    public static @NotNull String adminAndApproverAdminAuthTokenPayload(String mobile){
        String json="{\n" +
                "  \"mobile\":\""+mobile+"\",\n" +
                "  \"password\":\"##ofbdevautomation@2016##\",\n" +
                "  \"userProfileType\":\"OXYZOAPPLICANT\"\n" +
                "}";
        return json;
    }

    //CreateVerifiedOrganisation Payloads :
    @Contract(pure = true)
    public static @NotNull String createOrganisationPayload(String randomMobile, String contactName, String organisationName){

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

    @Contract(pure = true)
    public static @NotNull String saveAddressPayload(String clientOrganisationId, String contactName, String randomMobile){
        String json = "{\n" +
                "\"isBillingAddress\":true,\n" +
                "\"isShippingAddress\":true,\n" +
                "\"isCourrierAddress\":false,\n" +
                "\"isConsigneeAddress\":true,\n" +
                "\"isCorporateAddress\":true,\n" +
                "\"isOxyzoOfficeAddress\":true,\n" +
                "\"partyId\":\""+clientOrganisationId+"\",\n" +
                "\"contactPersonName\":\""+contactName+"\",\n" +
                "\"contactPersonNumber\":\""+randomMobile+"\",\n" +
                "\"partyType\":\"BUYER_ORGANISATION\",\n" +
                "\"addressLine1\":\"test-addressLine1\",\n" +
                "\"addressLine2\":\"test-addressLine2\",\n" +
                "\"city\":\"Faridabad\",\n" +
                "\"state\":\"Haryana\",\n" +
                "\"pinCode\":\"121001\",\n" +
                "\"addressTypes\":[\n" +
                "\"BILLING_ADDRESS\",\n" +
                "\"OXYZO_OFFICE_ADDRESS\"\n" +
                ",\n" +
                "\"SHIPPING_ADDRESS\"\n" +
                ",\n" +
                "\"CORPORATE_ADDRESS\"\n" +
                "],\n" +
                "\"partyTypes\":[\n" +
                "\"BUYER_ORGANISATION\"\n" +
                "]\n" +
                "}";
        return json;
    }

    @Contract(pure = true)
    public static @NotNull String saveGstPayload(String gst){
        String json = "{\n" +
                "\"vat\":null,\n" +
                "\"cst\":null,\n" +
                "\"gst\":\""+gst+"\",\n" +
                "\"gstIssueDate\":1545046231865\n" +
                "}";
        return json;
    }

    @Contract(pure = true)
    public static @NotNull String addAddressWithGstPayload(String gst, String clientOrganisationId){
        String json= "{\n" +
                "    \"addressLine1\": \"Somewhere\",\n" +
                "    \"addressLine2\": \"in Faridabad\",\n" +
                "    \"pinCode\": \"121001\",\n" +
                "    \"city\": \"Faridabad\",\n" +
                "    \"state\": \"HARYANA\",\n" +
                "    \"landmark\": null,\n" +
                "    \"addressTypes\": [\n" +
                "        \"BILLING_ADDRESS\",\n" +
                "        \"SHIPPING_ADDRESS\"\n" +
                "    ],\n" +
                "    \"linkedContactPersonIds\": [\n" +
                "        \"6238628696362588160\"\n" +
                "    ],\n" +
                "    \"linkedGst\": \""+gst+"\",\n" +
                "    \"partyId\": \""+clientOrganisationId+"\"\n" +
                "}";
        return json;
    }

    @Contract(pure = true)
    public static @NotNull String verifyAddressPayload(String fingerPrint, String tempFileLoaction){
        String json = "{\n" +
                "    \"minCreateDocumentDto\": {\n" +
                "        \"comment\": \"\",\n" +
                "        \"documentGroup\": \"COMPANY_ADDRESS_PROOF\",\n" +
                "        \"documentTypeId\": \"6255234674453387231\",\n" +
                "        \"formAnswerCreateDto\": {},\n" +
                "        \"parentDocumentId\": \"\",\n" +
                "        \"fileFingerPrint\": \""+fingerPrint+"\",\n" +
                "        \"documentName\": \"download.jpeg\",\n" +
                "        \"keepOriginal\": true,\n" +
                "        \"size\": 4652,\n" +
                "        \"status\": \"VERIFIED\",\n" +
                "        \"storagePath\": \""+tempFileLoaction+"\"\n" +
                "    },\n" +
                "    \"verificationOption\": \"CHOOSE_EXISTING\"\n" +
                "}";
        return json;
    }

    @Contract(pure = true)
    public static @NotNull String verifyGstPayload(String fingerPrint, String tempFileLocation){
        String json = "{\n" +
                "    \"minCreateDocumentDto\": {\n" +
                "        \"fileFingerPrint\": \""+fingerPrint+"\",\n" +
                "        \"documentName\": \"download.jpeg\",\n" +
                "        \"size\": 67975,\n" +
                "        \"storagePath\": \""+tempFileLocation+"\",\n" +
                "        \"status\": \"VERIFIED\",\n" +
                "        \"documentTypeId\": \"6307081014124484786\",\n" +
                "        \"documentGroup\": \"COMPANY_GST\"\n" +
                "    },\n" +
                "    \"verificationOption\": \"CHOOSE_EXISTING\"\n" +
                "}";

        return  json;
    }

    @Contract(pure = true)
    public static @NotNull String verifyPanPayload(String fingerPrint, String tempFileLocation, String panNumber){
        String json = "{\n" +
                "    \"minCreateDocumentDto\": {\n" +
                "        \"fileFingerPrint\": \""+fingerPrint+"\",\n" +
                "        \"documentName\": \"download.jpeg\",\n" +
                "        \"size\": 67975,\n" +
                "        \"storagePath\": \""+tempFileLocation+"\",\n" +
                "        \"status\": \"VERIFIED\",\n" +
                "        \"documentTypeId\": \"6254601419865724783\",\n" +
                "        \"documentGroup\": \"COMPANY_PAN\"\n" +
                "    },\n" +
                "    \"pan\": \""+panNumber+"\",\n" +
                "    \"oldPan\": \"\",\n" +
                "    \"changePan\": false,\n" +
                "    \"verificationOption\": \"CHOOSE_EXISTING\"\n" +
                "}";
        return json;
    }

    @Contract(pure = true)
    public static @NotNull String verifyBranchRegionPayload(){

        String json = "{}";
        return json;
    }

    //Loan Payloads :
    @Contract(pure = true)
    public static @NotNull String getCreateLoaAnApplicationPayload(String contactPersonName, String contactPersonEmail,
                                                                   String randomMobile, String clientOrganisationId,
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

    @Contract(pure = true)
    public static String @NotNull [] addOxyzoSuperAdminPayload(String automationId, String approverId){

        String [] array = new String[2];
        array[0]=automationId;
        array[1]=approverId;

        return array;
    }

    @Contract(pure = true)
    public static @NotNull String updateLoanApplicationPayload(String clientAppId, String loanType, String clientId){

        String json = "{\n" +
                "\"applicationId\":\""+clientAppId+"\",\n" +
                "\"loanAmount\":1000000,\n" +
                "\"loanType\":\""+loanType+"\",\n" +
                "\"loanTenure\":3,\n" +
                "\"purpose\":null,\n" +
                "\"version\":\"2\",\n" +
                "\"evaluation\":{\n" +
                "\"overallEvaluation\":null\n" +
                "},\n" +
                "\"offer\":{\n" +
                "\"limitOffer\":null\n" +
                "},\n" +
                "\"clientId\":\""+clientId+"\",\n" +
                "\"contactPersonName\":null,\n" +
                "\"contactPersonMobile\":\"\",\n" +
                "\"contactPersonEmail\":\"\",\n" +
                "\"processingOfficerAccountId\":\"\",\n" +
                "\"processingOfficerAccount\":null,\n" +
                "\"salesPersonAccountId\":\"6130569898436333872\",\n" +
                "\"mode\":\"\",\n" +
                "\"documentCollectorAccountId\":\"6130569898436333872\",\n" +
                "\"underwriterAccountId\":\"\",\n" +
                "\"loanApplicationStatus\":\"VERIFIED\",\n" +
                "\"contactPersonDesignation\":\"\",\n" +
                "\"remarks\":\"\"\n" +
                "}";
        return json;
    }

    public static String updateSalesAgentPayload(String clientOrganisationId,String accountId){

        String json = "{\n" +
                "    \"leadId\": \""+clientOrganisationId+"\",\n" +
                "    \"othersIds\": [\n" +
                "        \""+accountId+"\"\n" +
                "    ],\n" +
                "    \"crmAssigneeId\": \""+accountId+"\",\n" +
                "    \"callingAgentId\": \""+accountId+"\",\n" +
                "    \"salesAgentId\": \""+accountId+"\",\n" +
                "    \"oxyzoSalesAgentId\": \""+accountId+"\"\n" +
                "}";

        return json;
    }
}
