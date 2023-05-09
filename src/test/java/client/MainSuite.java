package client;
import files.UtilityFunctions;
import org.testng.annotations.Test;

public class MainSuite {

    @Test
    public void createLoanApplication() throws Exception {

        CreateAccount.CreateAutomationAccount("AutomationAccount");
        CreateAccount.CreateAutomationAccount("ApproverAccount");

        ReusableAPIs.tokenAuth(GetConfigProperties.getSuperAdminToken());

        CreateAccount.generateAuthToken("adminAuthToken");
        CreateAccount.generateAuthToken("ApproverAdminAuthToken");
        CreateAccount.Login();
        CreateAccount.createNewAccount();

        //Organisation Creating and Verification :
        CreateVerifiedOrg.createNewOrganisation();
        CreateVerifiedOrg.addAddress();
        CreateVerifiedOrg.addGst();
        CreateVerifiedOrg.stateLevelInfo();
        CreateVerifiedOrg.addAddressWithGst();

        ReusableAPIs.tempUplaod();
        CreateVerifiedOrg.verifyAddress();

        ReusableAPIs.tempUplaod();
        CreateVerifiedOrg.verifyGst();

        ReusableAPIs.tempUplaod();
        CreateVerifiedOrg.verifyPan();
        CreateVerifiedOrg.verifyBranchRegion();

        //creating oxyzoLoanApplication :
        LoanFlow.CreateLoanApplication();
//        LoanFlow.addOxyzoSuperAdmin();

    }
}
