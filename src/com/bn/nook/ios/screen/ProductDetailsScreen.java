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
 * Created by automation on 5/7/15.
 */
public class ProductDetailsScreen extends BaseScreen {

    public ProductDetailsScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        super(testManager, testHelper, paramsParser, iDevice);
    }

    public boolean pressOnBack() {
        Element element = getter.getElement(new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIANavigationBar, 0)
                .addElement(UIAElementType.UIAButton, Constants.ProductDetails.BACK_BUTTON));
        if(element == null)
            return false;
        TestManager.addStep("Press on button 'back'");
        return clicker.clickOnElement(element);
    }

    public boolean pressOnManage() {
        Element element = getter.getElement(new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIANavigationBar, 0)
                .addElement(UIAElementType.UIAButton, Constants.ProductDetails.MANAGE_BUTTON));
        if(element == null)
            return false;
        TestManager.addStep("Press on button 'Manage'");
        return clicker.clickOnElement(element);
    }

    public boolean pressOnArchive() {
        ElementQuery elementQuery  = new ElementQuery()
                .addElement(UIAElementType.UIAActionSheet, 0)
                .addElement(UIAElementType.UIACollectionView, 0)
                .addElement(UIAElementType.UIACollectionCell, Constants.ProductDetails.ARCHIVE_BUTTON);

        Element element = getter.getElement(elementQuery);
        if(element == null) {
            pressOnManage();
            element = waiter.waitForElementVisible(10000, elementQuery);
        }

        if(element == null)
            return false;

        TestManager.addStep("Press on 'Archive'");
        return clicker.clickByXY(element.getX(), element.getY());
    }

    public boolean pressOnUnArchive() {
        ElementQuery elementQuery  = new ElementQuery()
                .addElement(UIAElementType.UIAActionSheet, 0)
                .addElement(UIAElementType.UIACollectionView, 0)
                .addElement(UIAElementType.UIACollectionCell, Constants.ProductDetails.UNARCHIVE_BUTTON);

        Element element = getter.getElement(elementQuery);
        if(element == null) {
            pressOnManage();
            element = waiter.waitForElementVisible(10000, elementQuery);
        }

        if(element == null)
            return false;

        TestManager.addStep("Press on 'Unarchive'");
        return clicker.clickByXY(element.getX(), element.getY());
    }
}
