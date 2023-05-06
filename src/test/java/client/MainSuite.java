package client;

import org.testng.annotations.Test;

public class MainSuite {

    @Test
    public void createOrganisation(){
        CreateAccount.CreateAutomationAccount("AutomationAccount");
        CreateAccount.CreateAutomationAccount("ApproverAccount");
        TokenAuth.tokenAuth();
        GenerateAuthToken.generateAuthToken("adminAuthToken");
        GenerateAuthToken.generateAuthToken("ApproverAdminAuthToken");
        CreateAccount.Login();
        CreateAccount.createNewAccount();
        CreateOrganisation.createNewOrganisation();
    }
}
