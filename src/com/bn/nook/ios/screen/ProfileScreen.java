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
import com.sofment.testhelper.enums.Matcher;

import java.util.ArrayList;

/**
 * Created by automation on 5/12/15.
 */
public class ProfileScreen extends BaseScreen {

    public ProfileScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        super(testManager, testHelper, paramsParser, iDevice);
    }

    public boolean pressOnNavigationBarButton(String label, boolean isStrict) throws TestException {
        Element element = getter.getElementByName(label, new IConfig().setMaxLevelOfElementsTree(4).setMatcher(Matcher.EqualsIgnoreCase));
//        Element element = getter.getElement(new ElementQuery()
//                .addElement(UIAElementType.UIAWindow, 0)
//                .addElement(UIAElementType.UIANavigationBar, 0)
//                .addElement(UIAElementType.UIAButton, label).setOnlyVisible(false));
        if(element == null) {
            if(isStrict)
                testManager.retest("Can not click on " + label);
            else return false;
        }
        testManager.addStep("Press on button '" + label + "'");
        if(!clicker.clickByXY(element.getX() + element.getWidth()/2, element.getY() + element.getHeight()/2)) {
            if(isStrict)
                testManager.retest("Can not click on " + label);
            else return false;
        }
        return true;
    }

    public Element getBtnProfile(Element profile, int instance) throws TestException {
        ArrayList<Element> buttons = getter.getElementChildrenByType(profile, UIAElementType.UIAButton);
        if(buttons.size() == 0 || buttons.size() <= instance)
            testManager.retest("Profile Button was not found");

        return buttons.get(instance);
    }

    public ArrayList<Element> getProfiles() throws TestException {
        Element tableItems = waiter.waitForElementByClassVisible(UIAElementType.UIATableView, Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(3));
        if (tableItems == null)
            testManager.retest("TableView with profiles was not found");
        return getter.getElementChildrenByType(tableItems, UIAElementType.UIATableCell);
    }

    public Element getProfile(String profileName) throws TestException {
        ArrayList<Element> profiles = getProfiles();
        for(Element element : profiles) {
            if(element.getName().toLowerCase().contains(profileName.toLowerCase())){
                return element;
            }
        }
        return null;
    }

    public Element waitForElement(ElementQuery elementQuery, long timeout, boolean isStrict) throws TestException {
        Element element = waiter.waitForElementVisible(timeout, elementQuery);
        if(element == null) {
            if(isStrict)
                testManager.retest("Can not find element " + elementQuery.toString());
            else return null;
        }

        return element;
    }

    public boolean selectSwitch(int instance, boolean isStrict) throws TestException {
        Element element = getter.getElement(new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIASwitch, instance));
        if(element == null) {
            if(isStrict)
                testManager.retest("Switch was not found");
            else return false;
        }

        if(!clicker.clickByXY(element.getX() + element.getWidth() / 2, element.getY() + element.getHeight() / 2)){
            if(isStrict)
                testManager.retest("");
            else return false;
        }
        return true;
    }

    public void inputTextToProfile(String profileName) throws TestException {
        Element element = getter.getElement(new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIATextField, 0)
                .addElement(UIAElementType.UIATextField, Constants.ProfileScreen.FIRST_NAME_EDIT_TEXT));
        if(element == null){
            testManager.retest("TextField was not found");
        }
        if(!inputText(profileName, element)) {
            testManager.retest("Can not enter text to '" +Constants.ProfileScreen.FIRST_NAME_EDIT_TEXT +"'");
        }
    }

    public boolean isProfileNameFromHamburgerMenu(String profileName) throws TestException {
        Element profile = getter.getElementByName(Constants.ProfileScreen.PROFILE_LABEL,
                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase));
        if(profile == null)
            testManager.retest("Profile was not found");

        if(profile.getName() == null) {
            iDevice.i("Profile name =" + profile.toString());
            testManager.retest("Profile Name is null");
        }

        if(profile.getName().toLowerCase().contains(profileName.toLowerCase()))
            return true;
        return false;
    }

    public void openProfile() throws TestException {
        Element profile = getter.getElementByName(Constants.ProfileScreen.PROFILE_LABEL,
                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase));
        if(profile == null)
            testManager.retest("Profile was not found");

        clicker.clickByXY(profile.getX() + profile.getWidth() / 2, profile.getY() + profile.getHeight() / 2);
    }
}
