package client;
import org.testng.annotations.Test;

public class MainSuite {

    @Test
    public void createLoanApplication(){

        CreateAccount.CreateAutomationAccount("AutomationAccount");
        CreateAccount.CreateAutomationAccount("ApproverAccount");

        TokenAuth.tokenAuth(GetConfigProperties.getSuperAdminToken());

        GenerateAuthToken.generateAuthToken("adminAuthToken");
        GenerateAuthToken.generateAuthToken("ApproverAdminAuthToken");
        CreateAccount.Login();
        CreateAccount.createNewAccount();
        CreateVerifiedOrg.createNewOrganisation();

        CreateVerifiedOrg.addAddress();
        CreateVerifiedOrg.addGst();
        CreateVerifiedOrg.stateLevelInfo();
        CreateVerifiedOrg.addAddressWithGst();

        FileUpload.tempUplaod();
        CreateVerifiedOrg.verifyAddress();

        FileUpload.tempUplaod();
        CreateVerifiedOrg.verifyGst();

        FileUpload.tempUplaod();
        CreateVerifiedOrg.verifyPan();
        CreateVerifiedOrg.verifyBranchRegion();

        //creating oxyzoLoanApplication :
        LoanFlow.CreateLoanApplication();
    }
}
