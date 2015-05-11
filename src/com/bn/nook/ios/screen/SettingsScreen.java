package com.bn.nook.ios.screen;

import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.config.IConfig;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.models.IDevice;

/**
 * Created by avsupport on 4/24/15.
 */
public class SettingsScreen extends BaseScreen {
    public SettingsScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        super(testManager, testHelper, paramsParser, iDevice);
    }

    public boolean clickOnSettingsItem(int menuIndex) throws TestException {
        String name = "";
        switch (menuIndex) {
            case SettingsItems.ACCOUNTS:
                name = Constants.Settings.ACCOUNTS;
                break;
            case SettingsItems.DICTIONARY_OPTIONS:
                name = Constants.Settings.DICTIONARY_OPTIONS;
                break;
            case SettingsItems.SET_PROFILE:
                name = Constants.Settings.SET_PROFILE;
                break;
            case SettingsItems.CHILD_PASSCODE:
                name = Constants.Settings.CHILD_PASSCODE;
                break;
            case SettingsItems.FREQUENTLY_ASKED_QUESTIONS:
                name = Constants.Settings.FREQUENTLY_ASKED_QUESTIONS;
                break;
            case SettingsItems.SEND_FEEDBACK:
                name = Constants.Settings.SEND_FEEDBACK;
                break;
            case SettingsItems.LEGAL:
                name = Constants.Settings.LEGAL;
                break;
            case SettingsItems.ABOUT:
                name = Constants.Settings.ABOUT;
                break;
            case SettingsItems.LOG_OUT:
                name = Constants.Settings.LOG_OUT;
                break;
        }
        Element element = waiter.waitForElementByNameExists(name, 1000, new IConfig().setMaxLevelOfElementsTree(3));
        if(element == null) {
            testManager.retest("Menu item " + name + " is not found");
        }
        return clicker.clickOnElement(element);
    }

    public boolean clickOnLogOutButton() throws TestException {
        boolean result = clickOnSettingsItem(SettingsItems.LOG_OUT);
        iDevice.sleep(1000);
        return result;
    }



    public static class SettingsItems {
        public static final int ACCOUNTS = 0;
        public static final int DICTIONARY_OPTIONS = 1;
        public static final int SET_PROFILE = 2;
        public static final int CHILD_PASSCODE = 3;
        public static final int FREQUENTLY_ASKED_QUESTIONS = 4;
        public static final int SEND_FEEDBACK = 5;
        public static final int LEGAL = 6;
        public static final int ABOUT = 7;
        public static final int LOG_OUT = 8;
    }
}
