package com.bn.nook.ios.screen;

import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.json.Status;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.config.IConfig;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.models.IDevice;

/**
 * Created by avsupport on 4/24/15.
 */
public class OobeScreen extends BaseScreen {
    public OobeScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        super(testManager, testHelper, paramsParser, iDevice);
    }

    public Element waitForSignInButton(long timeoutMs) throws TestException {
        Element signIn = iDevice.getWaiter().waitForElementByNameVisible(Constants.Oobe.SIGN_IN, timeoutMs, new IConfig().setMaxLevelOfElementsTree(2));
        if(signIn == null)
            testManager.retest("sign in button is not found");
        return signIn;
    }

    public Element waitForExplorerAppButton(long timeoutMs) throws TestException {
        Element signIn = iDevice.getWaiter().waitForElementByNameVisible(Constants.Oobe.EXPLORER_APP, timeoutMs, new IConfig().setMaxLevelOfElementsTree(2));
        if(signIn == null)
            testManager.retest("sign in button is not found");
        return signIn;
    }

    public boolean inputLogin() throws TestException {
        return inputCredentials(true);
    }

    public boolean inputPassword() throws TestException {
        return inputCredentials(false);
    }

    public boolean inputCredentials() throws TestException {
        return inputLogin() && inputPassword();
    }

    private boolean inputCredentials(boolean isLogin) throws TestException {
        Element textField = isLogin ? super.waitForTextField(0) : super.waitForSecureTextField(0);
        if(textField == null) {
            testManager.failTest((isLogin ? "login" : "secure") + " text field is not found");
        }
        super.clearTextField(textField);
        return inputText(isLogin ? testManager.getLogin() : (testManager.getPass() + "\n"), textField);
    }

    public void clickOnSignInButton(long timeoutMs) throws TestException {
        Element signIn = waitForSignInButton(timeoutMs);

        if(!signIn.isVisible() || !signIn.isEnabled() || !signIn.isValid()) {
            iDevice.i("Sign in button is not available now");
            testManager.retest("Sign in button is not available now");
        }

        testManager.addStep("click on the sign in button");
        clicker.clickOnElement(signIn);
    }

    public void signIn(long timeoutMs) throws TestException {
        waitForSignInButton(timeoutMs);
        iDevice.sleep(3000);
        clickOnSignInButton(timeoutMs);
        inputCredentials();
        clicker.clickOnElement(waitForSignInButton(timeoutMs));
        waitForCollection();
    }

    public boolean clickOnExploreAppButton() throws TestException {
        Element explore = waitForExplorerAppButton(Constants.DEFAULT_TIMEOUT);
        if(explore == null) {
            testManager.retest("Explorer app button is not found");
        }
        testManager.addStep(String.format("click to Explorer app button [%s]", Constants.Oobe.EXPLORER_APP));
        return clicker.clickOnElement(explore);
    }
}
