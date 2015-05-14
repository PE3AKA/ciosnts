package com.bn.nook.ios.screen.library;

import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.bn.nook.ios.screen.BaseLibraryScreen;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.models.IDevice;

/**
 * Created by avsupport on 5/13/15.
 */
public class DeferredSignInScreen extends BaseLibraryScreen {
    public DeferredSignInScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        super(testManager, testHelper, paramsParser, iDevice);
    }
}
