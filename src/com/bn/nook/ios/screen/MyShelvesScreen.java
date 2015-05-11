package com.bn.nook.ios.screen;

import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.elements.ElementQuery;
import com.sofment.testhelper.driver.ios.enams.UIAElementType;
import com.sofment.testhelper.driver.ios.models.IDevice;

/**
 * Created by automation on 5/11/15.
 */
public class MyShelvesScreen extends BaseScreen {

    public MyShelvesScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        super(testManager, testHelper, paramsParser, iDevice);
    }

    public boolean pressOnAdd() {
        Element element = getter.getElement(new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIAToolBar, 0)
                .addElement(UIAElementType.UIAButton, Constants.My_Shelves.ADD_BUTTON));
        if(element == null)
            return false;
        TestManager.addStep("Press on button 'Add'");
        return clicker.clickOnElement(element);
    }

    public boolean pressOnNext() {
        Element element = getter.getElement(new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIANavigationBar, 0)
                .addElement(UIAElementType.UIAButton, Constants.My_Shelves.NEXT_BUTTON));
        if(element == null)
            return false;
        TestManager.addStep("Press on button 'Next'");
        return clicker.clickOnElement(element);
    }

    public boolean pressOnCancel() {
        Element element = getter.getElement(new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIANavigationBar, 0)
                .addElement(UIAElementType.UIAButton, Constants.My_Shelves.CANCEL_BUTTON));
        if(element == null)
            return false;
        TestManager.addStep("Press on button 'Cancel'");
        return clicker.clickOnElement(element);
    }

}
