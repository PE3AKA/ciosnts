package com.bn.nook.ios.screen;

import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.config.IConfig;
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
        testManager.addStep("Press on button 'back'");
        return clicker.clickOnElement(element);
    }

    public boolean pressOnManage() {
        Element element = getter.getElement(new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIANavigationBar, 0)
                .addElement(UIAElementType.UIAButton, Constants.ProductDetails.MANAGE_BUTTON));
        if(element == null)
            return false;
        testManager.addStep("Press on button 'Manage'");
        return clicker.clickOnElement(element);
    }

    public boolean pressOnManageItem(String label) {
        ElementQuery elementQuery = null;
        switch (label) {
            case Constants.ProductDetails.CANCEL_BUTTON:
                elementQuery  = new ElementQuery()
                        .addElement(UIAElementType.UIAActionSheet, 0)
                        .addElement(UIAElementType.UIAButton, label);
                break;
            case Constants.ProductDetails.UNARCHIVE_BUTTON:
            case Constants.ProductDetails.ARCHIVE_BUTTON:
                elementQuery  = new ElementQuery()
                        .addElement(UIAElementType.UIAActionSheet, 0)
                        .addElement(UIAElementType.UIACollectionView, 0)
                        .addElement(UIAElementType.UIACollectionCell, label);
                break;
        }

        if(elementQuery == null) {
            iDevice.i("Label '" + label + "' was not found ");
            return false;
        }
        Element element = getter.getElement(elementQuery);
        if(element == null) {
            pressOnManage();
            element = waiter.waitForElementVisible(10000, elementQuery);
        }

        if(element == null)
            return false;

        testManager.addStep("Press on '" + label + "'");
        return clicker.clickByXY(element.getX(), element.getY());
    }

    public boolean openProduct() throws TestException {
        Element scrollView = waiter.waitForElementByClassVisible(UIAElementType.UIAScrollView, 1, new IConfig().setMaxLevelOfElementsTree(2));
        Element button = waiter.waitForElementByClassVisible(UIAElementType.UIAButton, 1, new IConfig().setMaxLevelOfElementsTree(2).setParentElement(scrollView));

        if(button == null || button.getName() == null) return false;

        switch (button.getName()) {
            case Constants.ProductDetails.DOWNLOAD :
            case Constants.ProductDetails.DOWNLOADING :
                return false;
            case Constants.ProductDetails.READ :
                testManager.addStep("click on read button");
                clicker.clickOnElement(button);
                return true;
        }
        return true;
    }

    public boolean downloadProduct() {
        Element scrollView = waiter.waitForElementByClassVisible(UIAElementType.UIAScrollView, 1, new IConfig().setMaxLevelOfElementsTree(2));
        Element button = waiter.waitForElementByClassVisible(UIAElementType.UIAButton, 1, new IConfig().setMaxLevelOfElementsTree(2).setParentElement(scrollView));

        if(button == null || button.getName() == null) return false;

        switch (button.getName()) {
            case Constants.ProductDetails.DOWNLOAD :
                testManager.addStep("click on download button");
                clicker.clickOnElement(button);
                return waiter.waitForElementByNameVisible(Constants.ProductDetails.READ, Constants.DOWNLOAD_PRODUCT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(2).setParentElement(scrollView)) != null;

            case Constants.ProductDetails.READ :
                return true;
            case Constants.ProductDetails.DOWNLOADING :
                return waiter.waitForElementByNameVisible(Constants.ProductDetails.READ, Constants.DOWNLOAD_PRODUCT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(2).setParentElement(scrollView)) != null;
        }
        return true;
    }

    public boolean isDownloading() {
        return waiter.waitForElementByNameVisible(Constants.ProductDetails.DOWNLOADING, 1, new IConfig().setMaxLevelOfElementsTree(3)) != null;
    }
}
