package com.bn.nook.ios.model;

import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.sofment.testhelper.driver.ios.models.IDevice;

/**
 * Created by avsupport on 4/13/15.
 */
public class TestCase {

    private TestCaseConfig testCaseConfig;
    public TestCase (TestCaseConfig testCaseConfig) {
        this.testCaseConfig = testCaseConfig;
    }

    public interface TestCaseConfig {
        public boolean isReinstallApp();
        public String getPathToApp();
        public void mainLogic() throws TestException;
    }

    public void start() throws TestException {
        IDevice iDevice = TestManager.getInstance().getIDevice(ParamsParser.getInstance().getDeviceUuid());
        if(iDevice == null) {
//            TestManager.getInstance().getTestCaseInfo().setMessage("device with uuid is not found");
            throw new TestException("device with uuid: " + ParamsParser.getInstance().getDeviceUuid() + " is not found").retest();
        }
//        String pathToBuild = testCaseConfig.getPathToApp();
//        if(testCaseConfig.isReinstallApp()) {
//            iDevice.uninstallApplication(pathToBuild);
//            iDevice.installApplication(pathToBuild);
//        }
//        iDevice.launchApplication(pathToBuild);
        testCaseConfig.mainLogic();
        iDevice.stopApplication();
        TestManager.writeResult();
//        TestManager.getInstance().getTestCaseInfo().writeResult(ParamsParser.getInstance().getPathToResultsFolder() + "/result.json");
    }
}
