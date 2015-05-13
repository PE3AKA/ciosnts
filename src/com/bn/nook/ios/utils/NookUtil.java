package com.bn.nook.ios.utils;

import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.bn.nook.ios.screen.*;
import com.bn.nook.ios.screen.library.DeferredSignInScreen;
import com.bn.nook.ios.screen.library.LibraryScreen;
import com.bn.nook.ios.screen.reader.DrpReaderScreen;
import com.bn.nook.ios.screen.reader.EpubReaderScreen;
import com.bn.nook.ios.screen.reader.MyShelvesScreen;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.elements.ElementQuery;
import com.sofment.testhelper.driver.ios.enams.UIAElementType;
import com.sofment.testhelper.driver.ios.models.IDevice;
//import com.sofment.testhelper.driver.ios.helpers.Clicker;
//import com.sofment.testhelper.driver.ios.helpers.Getter;
//import com.sofment.testhelper.driver.ios.helpers.Scroller;
//import com.sofment.testhelper.driver.ios.helpers.Waiter;

/**
 * Created by avsupport on 4/24/15.
 */
public class NookUtil {

    private IDevice iDevice;
    private TestHelper testHelper;
    private TestManager testManager;
    private ParamsParser paramsParser;
    public ScreenModel screenModel;
    private BaseScreen baseScreen;
//    private Waiter waiter;
//    private Getter getter;
//    private Clicker clicker;
//    private Scroller scroller;

    public NookUtil(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        this.testManager = testManager;
        this.testHelper = testHelper;
        this.paramsParser = paramsParser;
        this.iDevice = iDevice;
//        this.waiter = this.iDevice.getWaiter();
//        this.getter = this.iDevice.getGetter();
//        this.clicker = this.iDevice.getClicker();
//        this.scroller = this.iDevice.getScroller();
    }

    public BaseScreen getCurrentScreen(boolean isUpdate) {
        if(baseScreen != null && !isUpdate) {
            iDevice.i("CurrentScreen is " + screenModel.name());
            return baseScreen;
        }
        //todo make detect Readers via invisible elements!!!
        Element element = iDevice.getWaiter().waitForElement(Constants.DEFAULT_TIMEOUT * 2,
                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAButton, Constants.Screens.SEARCH_SCREEN),
                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAStaticText, Constants.Screens.DeferredSignIn.BUILD_YOUR_OWN_LIBRARY),
                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAButton, Constants.Screens.OOBE_SCREEN),
                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAButton, Constants.Screens.EPUB_READER).setOnlyVisible(false),
                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAToolBar, 0).addElement(UIAElementType.UIAButton, Constants.Reader.Drp.LIBRARY_BTN).setOnlyVisible(false),
                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAStaticText, Constants.My_Shelves.TITLE_ACTION_BAR),
                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAStaticText, Constants.SideMenu.LIBRARY),
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAToolBar, 0).addElement(UIAElementType.UIAButton, Constants.Screens.LIBRARY_SCREEN),
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAToolBar, 0).addElement(UIAElementType.UIAButton, Constants.Screens.LIBRARY_SCREEN2),
                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIANavigationBar, 0).addElement(UIAElementType.UIAStaticText, Constants.Screens.SETTINGS_SCREEN),
                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIANavigationBar, 0).addElement(UIAElementType.UIAButton, Constants.ProductDetails.MANAGE_BUTTON)
        );

        if(element == null) {
            screenModel = ScreenModel.NON;
            return null;
        }

        switch (element.getName()) {
            case Constants.Screens.OOBE_SCREEN:
                screenModel= ScreenModel.OOBE;
                iDevice.i("####### CurrentScreen is " + screenModel.name());
                baseScreen = new OobeScreen(testManager, testHelper, paramsParser, iDevice);
                return baseScreen;
//            case Constants.Screens.LIBRARY_SCREEN:
//            case Constants.Screens.LIBRARY_SCREEN2:
            case Constants.SideMenu.LIBRARY:
                screenModel = ScreenModel.LIBRARY;
                iDevice.i("####### CurrentScreen is " + screenModel.name());
                baseScreen = new LibraryScreen(testManager, testHelper, paramsParser, iDevice);
                return baseScreen;
            case Constants.Screens.SETTINGS_SCREEN:
                screenModel = ScreenModel.SETTINGS;
                iDevice.i("####### CurrentScreen is " + screenModel.name());
                baseScreen = new SettingsScreen(testManager, testHelper, paramsParser, iDevice);
                return baseScreen;
            case Constants.Screens.SEARCH_SCREEN:
                screenModel = ScreenModel.SEARCH;
                iDevice.i("####### CurrentScreen is " + screenModel.name());
                baseScreen = new SearchScreen(testManager, testHelper, paramsParser, iDevice);
                return baseScreen;
            case Constants.Screens.EPUB_READER:
                screenModel = ScreenModel.EPUB_READER;
                iDevice.i("####### CurrentScreen is " + screenModel.name());
                baseScreen = new EpubReaderScreen(testManager, testHelper, paramsParser, iDevice);
                return baseScreen;
            case Constants.ProductDetails.MANAGE_BUTTON:
                screenModel = ScreenModel.PRODUCT_DETAILS;
                iDevice.i("####### CurrentScreen is " + screenModel.name());
                baseScreen = new ProductDetailsScreen(testManager, testHelper, paramsParser, iDevice);
                return baseScreen;
            case Constants.Reader.Drp.LIBRARY_BTN:
                screenModel = ScreenModel.DRP_READER;
                iDevice.i("####### CurrentScreen is " + screenModel.name());
                baseScreen = new DrpReaderScreen(testManager, testHelper, paramsParser, iDevice);
                return baseScreen;
            case Constants.My_Shelves.TITLE_ACTION_BAR:
                screenModel = ScreenModel.MY_SHELVES;
                iDevice.i("####### CurrentScreen is " + screenModel.name());
                baseScreen = new MyShelvesScreen(testManager, testHelper, paramsParser, iDevice);
                return baseScreen;
            case Constants.Screens.DeferredSignIn.BUILD_YOUR_OWN_LIBRARY:
                screenModel = ScreenModel.DEFERRED_SIGN_IN;
                iDevice.i("####### CurrentScreen is " + screenModel.name());
                baseScreen = new DeferredSignInScreen(testManager, testHelper, paramsParser, iDevice);
                return baseScreen;
        }
        return null;
    }

    public boolean waitForScreenModel(ScreenModel screenModel, long timeOut) throws TestException {
        return waitForScreenModel(screenModel, timeOut, true);
    }

    public boolean waitForScreenModel(ScreenModel screenModel, long timeOut, boolean isStrict) throws TestException {
        long start = System.currentTimeMillis();
        iDevice.i("####### Wait for " + screenModel.name());
        while (true) {
            if(System.currentTimeMillis() - start > timeOut) {
                if(isStrict) {
                    testManager.retest("necessary model is not found. \n" +
                            "Expected: " + screenModel.name() + "\n" +
                            "Actual: " + this.screenModel.name());
                }
                return false;
            }
            getCurrentScreen(true);
            if(screenModel != ScreenModel.SEARCH
                    && screenModel != ScreenModel.EPUB_READER
                    && screenModel != ScreenModel.DRP_READER
                    && screenModel != ScreenModel.READER) {
                if(this.screenModel == ScreenModel.SEARCH) {
                    ((SearchScreen)baseScreen).closeSearch();
                }
            }
            if(this.screenModel == screenModel) break;
            if(screenModel == ScreenModel.READER && baseScreen instanceof ReaderScreen) {
                return true;
            }
        }
        return true;
    }
}
