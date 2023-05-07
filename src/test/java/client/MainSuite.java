package client;
import org.testng.annotations.Test;

public class MainSuite {

    @Test
    public void createOrganisation(){
        CreateAccount.CreateAutomationAccount("AutomationAccount");
        CreateAccount.CreateAutomationAccount("ApproverAccount");

        TokenAuth.tokenAuth(GetConfigProperties.getSuperAdminToken());

        GenerateAuthToken.generateAuthToken("adminAuthToken");
        GenerateAuthToken.generateAuthToken("ApproverAdminAuthToken");
        CreateAccount.Login();
        CreateAccount.createNewAccount();
        CreateOrganisation.createNewOrganisation();
        VerifyOrganisation.addAddress();
        VerifyOrganisation.addGst();
        VerifyOrganisation.stateLevelInfo();
        VerifyOrganisation.addAddressWithGst();

        FileUpload.tempUplaod();
        VerifyOrganisation.verifyAddress();

        FileUpload.tempUplaod();
        VerifyOrganisation.verifyGst();

        FileUpload.tempUplaod();
        VerifyOrganisation.verifyPan();
        VerifyOrganisation.verifyBranchRegion();
    }
}
