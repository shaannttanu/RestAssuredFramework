package client;
import files.UtilityFunctions;
import org.testng.annotations.Test;

import java.io.IOException;

public class MainSuite {

    @Test
    public void createLoanApplication() throws IOException {

        //erasing contents of file before each run :
        UtilityFunctions.eraseFileContents();

        CreateAccount.CreateAutomationAccount("AutomationAccount");
        CreateAccount.CreateAutomationAccount("ApproverAccount");

        ReusableAPIs.tokenAuth(GetConfigProperties.getSuperAdminToken());

        CreateAccount.generateAuthToken("adminAuthToken");
        CreateAccount.generateAuthToken("ApproverAdminAuthToken");
        CreateAccount.Login();
        CreateAccount.createNewAccount();

        //Organisaation Creating and Verification :
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
        LoanFlow.addOxyzoSuperAdmin();

    }
}
