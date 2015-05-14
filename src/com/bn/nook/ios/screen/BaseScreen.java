package com.bn.nook.ios.screen;

import com.bn.nook.ios.Base;
import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.config.IConfig;
import com.sofment.testhelper.driver.ios.config.IWaiterConfig;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.elements.ElementQuery;
import com.sofment.testhelper.driver.ios.enams.SearchCondition;
import com.sofment.testhelper.driver.ios.enams.UIAElementType;
import com.sofment.testhelper.driver.ios.helpers.*;
import com.sofment.testhelper.driver.ios.models.IDevice;

import java.util.ArrayList;

/**
 * Created by avsupport on 4/24/15.
 */
public class BaseScreen extends Base {
    protected IDevice iDevice;
    protected TestHelper testHelper;
    protected TestManager testManager;
    protected ParamsParser paramsParser;
    protected Waiter waiter;
    protected Drager drager;
    protected Getter getter;
    protected Clicker clicker;
    protected Scroller scroller;

    public BaseScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        this.testManager = testManager;
        this.testHelper = testHelper;
        this.paramsParser = paramsParser;
        this.iDevice = iDevice;
        this.waiter = this.iDevice.getWaiter();
        this.getter = this.iDevice.getGetter();
        this.clicker = this.iDevice.getClicker();
        this.scroller = this.iDevice.getScroller();
        this.drager = this.iDevice.getDrager();
    }

    public ArrayList<Element> getProducts(int minSearchResultProducts, long timeout) throws TestException {
        Element collectionView = waiter.waitForElementByClassVisible(UIAElementType.UIACollectionView,
                Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(2));
        if(collectionView == null) {
            testManager.retest("Collection view with search results is not found");
        }
        ArrayList<Element> products = getter.getElementChildren(collectionView);
        long start = System.currentTimeMillis();
        while (products == null || products.size() < minSearchResultProducts) {
            if(System.currentTimeMillis() - start > timeout) {
                return products == null ? new ArrayList<Element>() : products;
            }
        }
        return products;
    }

    public Element waitForTextField(int instance) throws TestException {
        return waiter.waitForElementByClassExists(UIAElementType.UIATextField, 1, new IConfig().setMaxLevelOfElementsTree(2).setInstance(instance));
    }

    public Element waitForMenuButton(long timeout) {
        return waiter.waitForElementByNames(new IWaiterConfig().setMaxLevelOfElementsTree(3).setSearchCondition(SearchCondition.NAME), timeout, Constants.Library.MENU_BUTTON, Constants.Library.MENU_BUTTON2);
//        return waiter.waitForElementByNameVisible(Constants.Library.MENU_BUTTON, timeout, new IConfig().setMaxLevelOfElementsTree(3));
    }

    public Element waitForSecureTextField(int instance) throws TestException {
        Element textField = waiter.waitForElementByClassExists(UIAElementType.UIASecureTextField, 1, new IConfig().setMaxLevelOfElementsTree(2).setInstance(instance));
        if(textField == null) {
            testManager.failTest("login text field is not found");
        }
        return textField;
    }

    public boolean clearTextField(Element textField) {
        clicker.clickOnElement(textField);
        Element clearText = iDevice.getWaiter().waitForElementByNameVisible("Clear text", 1, new IConfig().setMaxLevelOfElementsTree(3));
        return clearText != null && iDevice.getClicker().clickOnElement(clearText);
    }

    public boolean inputText(String text, Element textField) {
        testManager.addStep("Input text " + text);
        return iDevice.inputText(text, textField);
    }

    public Element waitForCollection() throws TestException {
        Element collection = waiter.waitForElementByClassExists(UIAElementType.UIACollectionView, Constants.DEFAULT_TIMEOUT*4, new IConfig().setMaxLevelOfElementsTree(2));
        if(collection == null) {
            testManager.failTest("UIACollection view is not found");
        }
        return collection;
    }

    public boolean waitForLogin(long timeout){
        long startTime = System.currentTimeMillis();
        while (true){
            if (waiter.waitForElementByClassExists(UIAElementType.UIACollectionView, 1, new IConfig().setMaxLevelOfElementsTree(2))!= null &&
                    waiter.waitForElement(1, new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAButton, Constants.Settings.SIGN_IN)) == null){
                return true;
            }
            if (System.currentTimeMillis() - startTime > timeout)
                break;
        }
         return false;
    }
}
