package com.bn.nook.ios.exception;

import com.bn.nook.ios.json.TestCaseInfo;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.sofment.testhelper.driver.ios.models.IDevice;

/**
 * Created by avsupport on 4/13/15.
 */
public class TestException extends Exception{

    public TestException(String errorMessage) {
        super(errorMessage);
    }

    public TestException failTest() {
        stopApplication(true);
        StackTraceElement[] stackTrace = this.getStackTrace();
        System.out.println("Exception in thread \"main\" " + TestException.class.getName() + ": " + this.getMessage());
        for(StackTraceElement stackTraceElement : stackTrace) {
            System.out.println(stackTraceElement.toString());
        }
        return this;
    }

    private void stopApplication(boolean isFail) {
        IDevice iDevice = TestManager.getInstance().getIDevice(ParamsParser.getInstance().getDeviceUuid());
        if((iDevice) != null) {
            iDevice.takeScreenShot(isFail ? "fail" : "retest");
            stopApplication();
        }
        TestCaseInfo testCaseInfo = TestManager.getInstance().getTestCaseInfo();
        if(testCaseInfo != null)
            testCaseInfo.writeResult(ParamsParser.getInstance().getPathToResultsFolder() + "/result.json");
    }

    public TestException retest() {
        stopApplication(false);
        StackTraceElement[] stackTrace = this.getStackTrace();
        System.out.println("Exception in thread \"main\" " + TestException.class.getName() + ": " + this.getMessage());
        for(StackTraceElement stackTraceElement : stackTrace) {
            System.out.println(stackTraceElement.toString());
        }
        return this;
    }

    private void stopApplication() {
        IDevice iDevice = TestManager.getInstance().getIDevice(ParamsParser.getInstance().getDeviceUuid());
        if((iDevice) != null) {
            iDevice.stopApplication();
        }
    }
}
