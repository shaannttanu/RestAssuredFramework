package files;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;

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

    public static String saveAddressPayload(String clientOrganisationId,String contactName,String randomMobile){
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

    public static String saveGstPayload(String gst){
        String json = "{\n" +
                "\"vat\":null,\n" +
                "\"cst\":null,\n" +
                "\"gst\":\""+gst+"\",\n" +
                "\"gstIssueDate\":1545046231865\n" +
                "}";
        return json;
    }

    public static String addAddressWithGstPayload(String gst,String clientOrganisationId){
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

    public static String verifyAddressPayload(String fingerPrint,String tempFileLoaction){
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

    public static String verifyGstPayload(String fingerPrint,String tempFileLocation){
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

    public static String verifyPanPayload(String fingerPrint,String tempFileLocation,String panNumber){
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

    public static String verifyBranchRegionPayload(){

        String json = "{}";
        return json;
    }
}
