package com.bn.nook.ios;

import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.json.TestCaseInfo;
import com.bn.nook.ios.manager.AlertManager;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.model.TestCase;
import com.bn.nook.ios.param.ParamsParser;
import com.bn.nook.ios.tests.acceptance.AcceptanceTests;

/**
 * Created by avsupport on 4/9/15.
 */
public class TestLauncher {

    private static ParamsParser paramsParser = null;
    private static AcceptanceTests acceptanceTests = null;

    public static void main(String[] args) throws TestException {

        paramsParser = ParamsParser.getInstance();

        if(!paramsParser.parse(args)) return;

        launchTest();
    }

    public static TestCase.TestCaseConfig testCaseConfig = new TestCase.TestCaseConfig() {
        @Override

        public boolean isReinstallApp() {
            return false;
        }

        @Override
        public String getPathToApp() {
            return TestManager.getInstance().getPathToBuild();
        }

        @Override
        public void mainLogic() throws TestException {
            executeTest();
        }
    };

    private static void launchTest() throws TestException {

        TestCase testCase = new TestCase(testCaseConfig);
        testCase.start();
    }


//    var target = UIATarget.localTarget();
//
//    target.frontMostApp().mainWindow().collectionViews()[0].cells()["Judy Moody Predicts the Future (Judy Moody Series #4) by Megan McDonald"].elements()["Judy Moody Predicts the Future (Judy Moody Series #4) by Megan McDonald"].scrollToVisible();

    private static void executeTest() throws TestException {
        TestCaseInfo testCaseInfo = new TestCaseInfo();
        AlertManager alertManager = new AlertManager();
        switch (paramsParser.getTestId()) {
            case "434245":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(434245L);
                testCaseInfo.setTitle("Sign in: Country list Menu [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase434245();
                break;
            case "435985":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(435985L);
                testCaseInfo.setTitle("Sign in: Nook Account Login [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase435985();
                break;
            case "435986":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(435986L);
                testCaseInfo.setTitle("Sign in: Profile message [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase435986();
                break;
            case "435993":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(435993L);
                testCaseInfo.setTitle("Left Nook app menu: Profile Name [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase435993();
                break;
            case "435990":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(435990L);
                testCaseInfo.setTitle("Left Nook app menu: Currently Reading [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase435990();
                break;
            case "435987":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(435987L);
                testCaseInfo.setTitle("Left Nook app menu: Library [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase435987();
                break;
            case "435988":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(435988L);
                testCaseInfo.setTitle("Left Nook app menu: My Shelves [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase435988();
                break;
            case "435989":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(435989L);
                testCaseInfo.setTitle("Left Nook app menu: Settings [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase435989();
                break;
            case "435992":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(435992L);
                testCaseInfo.setTitle("Left Nook app menu: Messages [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase435992();
                break;
            case "435994":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(435994L);
                testCaseInfo.setTitle("Global Navigation/filtering: Library filters (content type) [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase435994();
                break;
            case "435995":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(435995L);
                testCaseInfo.setTitle("Global Navigation/filtering: Library filters (content type) [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase435995();
                break;
            case "435997":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(435997L);
                testCaseInfo.setTitle("Download sample content [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase435997();
                break;
            case "435998":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(435998L);
                testCaseInfo.setTitle("ePUB: Download [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase435998();
                break;
            case "436026":
                alertManager.archiveHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436026L);
                testCaseInfo.setTitle("Archive a book [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436026();
                break;
            case "436027":
                alertManager.archiveHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436027L);
                testCaseInfo.setTitle("Unarchive a book [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436027();
                break;
            case "436028":
                alertManager.createShelfHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436028L);
                testCaseInfo.setTitle("Create a Shelf [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436028();
                break;
            case "436029":
                alertManager.renameAndDeleteShelfHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436029L);
                testCaseInfo.setTitle("Edit/remove a Stack [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436029();
                break;
            case "436012":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436012L);
                testCaseInfo.setTitle("DRP: download [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436012();
                break;
            case "436013":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436013L);
                testCaseInfo.setTitle("DRP: open [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436013();
                break;
            case "435999":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(435999L);
                testCaseInfo.setTitle("ePUB: Open [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase435999();
                break;
            case "436000":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436000L);
                testCaseInfo.setTitle("ePUB: Table of content [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436000();
                break;
            case "436014":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436014L);
                testCaseInfo.setTitle("DRP:Table of Contents [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436014();
                break;
            case "436015":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436015L);
                testCaseInfo.setTitle("DRP: Article view [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436015();
                break;
            case "439472":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(439472L);
                testCaseInfo.setTitle("DRP: Article view -- Change font style [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase439472();
                break;
            case "436016":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436016L);
                testCaseInfo.setTitle("DRP:Page view [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436016();
                break;
            case "436018":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436018L);
                testCaseInfo.setTitle("DRP:double tap zoom in zoom out [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436018();
                break;
            case "436019":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436019L);
                testCaseInfo.setTitle("DRP:scrubber navigation [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436019();
                break;
            case "436022":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436022L);
                testCaseInfo.setTitle("DRP:swipe forward a few pages [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436022();
                break;

            case "123456":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(123456L);
                testCaseInfo.setTitle("demo");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase123456();
                break;
            case "436002":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436002L);
                testCaseInfo.setTitle("ePUB:bookmark [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436002();
                break;
            case "436003":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436003L);
                testCaseInfo.setTitle("ePUB:text options [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436003();
                break;
            case "436004":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436004L);
                testCaseInfo.setTitle("ePUB:more options [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436004();
                break;
            case "436005":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436005L);
                testCaseInfo.setTitle("ePUB:go to page [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436005();
                break;
            case "436007":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436007L);
                testCaseInfo.setTitle("ePUB:View details [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436007();
            case "436021":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436021L);
                testCaseInfo.setTitle("DRP: bookmark [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436021();
                break;
            case "436008":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436008L);
                testCaseInfo.setTitle("ePUB:navigation bar [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436008();
                break;
            case "436010":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436010L);
                testCaseInfo.setTitle("ePUB:go back to page [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436010();
                break;
            case "436032":
                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(436032L);
                testCaseInfo.setTitle("Sign Out of Account [bnauto]");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase436032();
                break;
        }
    }
}
