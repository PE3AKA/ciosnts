package com.bn.nook.ios.screen.library;

import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.bn.nook.ios.screen.BaseLibraryScreen;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.config.IConfig;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.elements.ElementQuery;
import com.sofment.testhelper.driver.ios.enams.UIAElementType;
import com.sofment.testhelper.driver.ios.models.IDevice;
import com.sofment.testhelper.enums.Matcher;

/**
 * Created by avsupport on 5/13/15.
 */
public class DeferredSignInScreen extends BaseLibraryScreen {
    public DeferredSignInScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        super(testManager, testHelper, paramsParser, iDevice);
    }

    public Element getSignInBtn() {
        return waiter.waitForElement(1, new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAButton, Constants.Settings.SIGN_IN));
//        return waiter.waitForElementByNameVisible(Constants.Settings.SIGN_IN, 1, new IConfig().setMatcher(Matcher.ContainsIgnoreCase).setMaxLevelOfElementsTree(2));
    }
}
