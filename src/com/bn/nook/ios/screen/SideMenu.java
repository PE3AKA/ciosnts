package com.bn.nook.ios.screen;

import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.config.IConfig;
import com.sofment.testhelper.driver.ios.config.IWaiterConfig;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.elements.ElementQuery;
import com.sofment.testhelper.driver.ios.enams.UIAElementType;
import com.sofment.testhelper.driver.ios.models.IDevice;
import com.sofment.testhelper.enums.Matcher;

import java.util.ArrayList;

public class SideMenu extends BaseScreen {

    public SideMenu(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        super(testManager, testHelper, paramsParser, iDevice);
    }

    public boolean isHamburgerMenuOpened() {
        Element element = waiter.waitForElementByNameVisible(Constants.SideMenu.NOOK_LOGO, 1, new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase));
        if (element == null) {
            iDevice.i("Menu not opened");
            return false;
        }
        iDevice.i("Menu opened");
        return true;
    }

    public void openItem(String itemName) throws TestException {
        if(!isHamburgerMenuOpened())
            testManager.retest("Menu not opened");
        Element tableItems = waiter.waitForElementByClassVisible(UIAElementType.UIATableView, Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(3));
        if (tableItems == null)
            testManager.retest("Table menu not found");
        ArrayList<Element> items = getter.getElementChildrenByType(tableItems, UIAElementType.UIATableCell);
        for (Element item : items) {
            if (item.getName().isEmpty())
                continue;
            iDevice.i("name: " + item.getName());
            if (item.getName().toLowerCase().equals(itemName.toLowerCase())) {
                clicker.clickOnElement(item);
                return;
            }
        }
        testManager.retest(itemName + " item was not found in Side Menu");
    }

    public void openProfile() throws TestException {
        if(!isHamburgerMenuOpened())
            testManager.retest("Menu not opened");

        Element profile = getter.getElementByName(Constants.ProfileScreen.PROFILE_LABEL, new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase));
//        Element profile = getter.getElement(new ElementQuery().addElement(UIAElementType.UIAWindow, 0)
//                .addElement(UIAElementType.UIAStaticText, Constants.ProfileScreen.PROFILE_LABEL).setOnlyVisible(false));
        if(profile == null)
            testManager.retest("Profile was not found");

        clicker.clickByXY(profile.getX() + profile.getWidth() / 2, profile.getY() + profile.getHeight() / 2);
    }
}