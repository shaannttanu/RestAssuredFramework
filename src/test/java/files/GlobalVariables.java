package files;
import client.GetConfigProperties;
import com.github.javafaker.Faker;

import java.security.PublicKey;

public class GlobalVariables {

    //CreateAccount :
    public static String automationId;
    public static String approverId;
    public static String contactName = UtilityFunctions.getRandomName();
    public static String contactPersonEmail = UtilityFunctions.getRandomEmail();
    public static String currentTime ;
    public static String otp;
    public static String randomMobile= GetConfigProperties.getRandomMobile();
    public static String panNumber;
    public static String gstNumber;
    public static String buyerAccountId;
    public static String clientAuthToken;

    //CreateVerifiedOrganisation:
    public static String organisationName;
    public static String clientOrganisationId;
    public static String stateTaxInfoId;
    public static String clientAddressId;

    //FileUpload :
    public static String tempFileLocation;
    public static String fingerPrint;

    //GenerateAuthToken :
    public static String adminAuthToken;
    public static String approverAdminAuthToken;

    //CreateLoanAppication :
    public static String loanAmount;
    public static String clientAccountId;
    public static String clientAppId;

}
