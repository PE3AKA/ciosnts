package com.bn.nook.ios.assistant;

import com.bn.nook.ios.BaseTestRunner;
import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.json.Status;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ConfigParam;
import com.bn.nook.ios.param.ParamsParser;
import com.bn.nook.ios.screen.*;
import com.bn.nook.ios.screen.reader.DrpReaderScreen;
import com.bn.nook.ios.screen.reader.EpubReaderScreen;
import com.bn.nook.ios.utils.NookUtil;
import com.sofment.testhelper.driver.ios.config.IConfig;
import com.sofment.testhelper.driver.ios.config.IWaiterConfig;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.enams.UIAElementType;
import com.sofment.testhelper.driver.ios.helpers.Clicker;
import com.sofment.testhelper.driver.ios.helpers.Getter;
import com.sofment.testhelper.driver.ios.helpers.Scroller;
import com.sofment.testhelper.driver.ios.helpers.Waiter;
import com.sofment.testhelper.driver.ios.interfaces.AlertCallBack;
import com.sofment.testhelper.driver.ios.models.IDevice;
import com.sofment.testhelper.enums.Matcher;
import org.json.Test;

import java.util.ArrayList;

public class Preparer {
    protected IDevice iDevice;
    protected TestManager testManager;
    protected Waiter waiter;
    protected Getter getter;
    protected Clicker clicker;
    protected Scroller scroller;
    protected BaseScreen baseScreen;
    protected LibraryScreen libraryScreen;
    protected SearchScreen searchScreen;
    protected DrpReaderScreen drpReaderScreen;
    protected OobeScreen oobeScreen;
    protected SettingsScreen settingsScreen;
    protected SideMenu sideMenu;
    protected NookUtil nookUtil;
    protected ProductDetailsScreen productDetailsScreen;
    protected ParamsParser paramsParser;
    
    public Preparer(IDevice iDevice, NookUtil nookUtil, ParamsParser paramsParser) {
        this.testManager = TestManager.getInstance();
        this.iDevice = iDevice;
        this.nookUtil = nookUtil;
        this.paramsParser = paramsParser;
        this.waiter = this.iDevice.getWaiter();
        this.getter = this.iDevice.getGetter();
        this.clicker = this.iDevice.getClicker();
        this.scroller = this.iDevice.getScroller();
    }

    public void login() throws TestException {
        baseScreen = nookUtil.getCurrentScreen(true);
        iDevice.i("##################### Detected " + nookUtil.screenModel.name());
        if (nookUtil.screenModel == ScreenModel.OOBE) {
            oobeScreen = (OobeScreen) baseScreen;
            oobeScreen.signIn(Constants.DEFAULT_TIMEOUT);
        }
    }

    public void signOut() throws TestException {
        baseScreen = nookUtil.getCurrentScreen(true);
        iDevice.i("##################### Detected " + nookUtil.screenModel.name());
        if (nookUtil.screenModel == ScreenModel.LIBRARY) {
            libraryScreen = (LibraryScreen) baseScreen;
            libraryScreen.openSettings();
            nookUtil.waitForScreenModel(ScreenModel.SETTINGS, 5000);
            baseScreen = nookUtil.getCurrentScreen(false);
            if (nookUtil.screenModel == ScreenModel.SETTINGS) {
                settingsScreen = (SettingsScreen) baseScreen;
                settingsScreen.clickOnLogOutButton();
                nookUtil.waitForScreenModel(ScreenModel.OOBE, Constants.DEFAULT_TIMEOUT);
            }
        }
    }

    public void initOobeScreen() throws TestException {
        nookUtil.waitForScreenModel(ScreenModel.OOBE, Constants.DEFAULT_TIMEOUT);
        baseScreen = nookUtil.getCurrentScreen(false);
        if(nookUtil.screenModel != ScreenModel.OOBE) testManager.retest("Necessary oobe screen is not found. Expected: " + ScreenModel.OOBE.name() + ", actual : " + nookUtil.screenModel.name());
        oobeScreen = ((OobeScreen) baseScreen);
    }

    public void initLibraryScreen() throws TestException {
        nookUtil.waitForScreenModel(ScreenModel.LIBRARY, Constants.DEFAULT_TIMEOUT);
        baseScreen = nookUtil.getCurrentScreen(false);
        if(nookUtil.screenModel != ScreenModel.LIBRARY) testManager.retest("Necessary library screen is not found. Expected: " + ScreenModel.LIBRARY.name() + ", actual : " + nookUtil.screenModel.name());
        libraryScreen = ((LibraryScreen) baseScreen);
    }


    public void initSearchScreen() throws TestException {
        nookUtil.waitForScreenModel(ScreenModel.SEARCH, Constants.DEFAULT_TIMEOUT);
        baseScreen = nookUtil.getCurrentScreen(false);
        if(nookUtil.screenModel != ScreenModel.SEARCH) testManager.retest("Necessary search screen is not found. Expected: " + ScreenModel.SEARCH.name() + ", actual : " + nookUtil.screenModel.name());
        searchScreen = ((SearchScreen) baseScreen);
    }

    public void initProductDetailsScreen() throws TestException {
        nookUtil.waitForScreenModel(ScreenModel.PRODUCT_DETAILS, Constants.DEFAULT_TIMEOUT);
        baseScreen = nookUtil.getCurrentScreen(false);
        if(nookUtil.screenModel != ScreenModel.PRODUCT_DETAILS)
            testManager.retest("Necessary search screen is not found. Expected: " +
                    ScreenModel.PRODUCT_DETAILS.name() + ", actual : " +
                    nookUtil.screenModel.name());
        productDetailsScreen = ((ProductDetailsScreen) baseScreen);
    }

    public void archiveProduct(NookUtil nookUtil, String productName) throws TestException {
        initLibraryScreen();
        libraryScreen.openMenu(LibraryScreen.MenuItems.LIBRARY);
        libraryScreen.changeFilter(LibraryScreen.FilterItems.ALL_ITEMS);
        libraryScreen.searchProduct(productName);

        if(!nookUtil.waitForScreenModel(ScreenModel.SEARCH, Constants.DEFAULT_TIMEOUT)) {
            testManager.retest("Search screen was not loaded");
        }
        initSearchScreen();

        ArrayList<Element> searchResults = searchScreen.getSearchResults(1, Constants.DEFAULT_TIMEOUT);
        if(searchResults.size() == 0)
            testManager.retest("The list results of the search isn't consist needed product");
        Element product = null;
        for(Element element : searchResults) {
            iDevice.i("###########################Product:" + element.toString());
            if(element != null && element.getName().toLowerCase().contains(productName.toLowerCase())){
                product = element;
                break;
            }
        }

        if(product == null)
            testManager.retest("Product '" + productName + "' was not found");

        if(!scroller.scrollToVisible(product)) {
            testManager.retest("Can not scrollable to product '" + productName + "'");
        }

        TestManager.addStep("Long click on product '" + productName + "'");
        clicker.longClickOnElement(product, 5);

        if(!nookUtil.waitForScreenModel(ScreenModel.PRODUCT_DETAILS, Constants.DEFAULT_TIMEOUT)){
            testManager.retest("Product details screen was not loaded");
        }
        initProductDetailsScreen();

        final int[] alertState = new int[1];
        alertState[0] = 0;
        iDevice.setAlertHandler(new AlertCallBack() {
            @Override
            public void handleAlert(ArrayList<Element> arrayList) {
                iDevice.i("############Handle Alert");
                for (Element element : arrayList) {
                    iDevice.i("Alert Element name:" + element.getName());
                    if(element.getName().toLowerCase().equals(Constants.ProductDetails.ARCHIVE_TITLE.toLowerCase())){
                        iDevice.i("Alert Element exist");
                        alertState[0] = 1;
                        break;
                    }
                }
            }
        });

        if(!productDetailsScreen.pressOnManageItem(Constants.ProductDetails.ARCHIVE_BUTTON)) {
            TestManager.addStep("Archive button doesn't exist");
            iDevice.takeScreenShot("product_details_not_archive_btn");
            productDetailsScreen.pressOnManageItem(Constants.ProductDetails.CANCEL_BUTTON);
            return;
        }

        iDevice.i("waiting for alert Archive title");
        long startTimer = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTimer < 20000) {
            if(alertState[0] != 0) {
                iDevice.i("Alert is visible");
                break;
            }
        }
        iDevice.sleep(1000);
        if(alertState[0] == 0)
            testManager.retest("Alert '" + Constants.ProductDetails.ARCHIVE_TITLE + "' was not found");

        productDetailsScreen.pressOnBack();
        if(!nookUtil.waitForScreenModel(ScreenModel.SEARCH, Constants.DEFAULT_TIMEOUT)) {
            testManager.retest("Search screen was not loaded");
        }
        initSearchScreen();
        searchScreen.closeSearch();
    }

    public void unArchiveProduct(NookUtil nookUtil, String productName) throws TestException {
        baseScreen = nookUtil.getCurrentScreen(true);
        iDevice.i("##################### Detected " + nookUtil.screenModel.name());
        openScreen(Constants.SideMenu.LIBRARY, ScreenModel.LIBRARY);
        if(!nookUtil.waitForScreenModel(ScreenModel.LIBRARY, Constants.DEFAULT_TIMEOUT))
            testManager.retest("Library not opened");
        libraryScreen = (LibraryScreen) nookUtil.getCurrentScreen(false);
        libraryScreen.changeFilter(LibraryScreen.FilterItems.ARCHIVED);
        ArrayList<Element> products = libraryScreen.getProducts(1, Constants.DEFAULT_TIMEOUT);
        Element archiveProduct = null;
        for(Element element : products) {
            iDevice.i("###########################Product:" + element.toString());
            if(element != null && element.getName().toLowerCase().contains(productName.toLowerCase())){
                archiveProduct = element;
                break;
            }
        }

        if(archiveProduct == null)
            return;

        if(!scroller.scrollToVisible(archiveProduct)) {
            throw new TestException("Can not scrollable to product '" + productName + "'").retest();
        }

        TestManager.addStep("Long click on product '" + productName + "'");
        clicker.longClickOnElement(archiveProduct, 5);

        if(!nookUtil.waitForScreenModel(ScreenModel.PRODUCT_DETAILS, Constants.DEFAULT_TIMEOUT)){
            testManager.retest("Product details screen was not loaded");
        }
        baseScreen = nookUtil.getCurrentScreen(false);
        if(nookUtil.screenModel != ScreenModel.PRODUCT_DETAILS)
            testManager.retest("Necessary search screen is not found. Expected: " +
                    ScreenModel.PRODUCT_DETAILS.name() + ", actual : " +
                    nookUtil.screenModel.name());
        productDetailsScreen = ((ProductDetailsScreen) baseScreen);

        if(!productDetailsScreen.pressOnManageItem(Constants.ProductDetails.UNARCHIVE_BUTTON))
            throw new TestException("Unarchive button doesn't exist").retest();
    }

    public void deferredSignIn()throws TestException{

    }

    public void openScreen(String screen, ScreenModel screenModel) throws TestException {
        if (nookUtil.waitForScreenModel(screenModel, 1, false)) {
            iDevice.i(screenModel.name() + " already opened");
            return;
        }
        openHamburgerMenuFromAnyScreen();
        if(!waitWhileHamburgerMenuOpened(Constants.DEFAULT_TIMEOUT))
            testManager.retest("Hamburger menu not opened");
        sideMenu = new SideMenu(testManager, testManager.getTestHelper(), paramsParser, iDevice);
        sideMenu.openItem(screen);
    }

    public void openScreen(String screen) throws TestException {
        openHamburgerMenuFromAnyScreen();
        if(!waitWhileHamburgerMenuOpened(Constants.DEFAULT_TIMEOUT))
            testManager.retest("Hamburger menu not opened");
        sideMenu = (SideMenu) nookUtil.getCurrentScreen(true);
        sideMenu.openItem(screen);
    }

    public void openProduct(String product, ScreenModel productType) throws TestException {
        if (nookUtil.waitForScreenModel(productType, 1, false)) {
            iDevice.i(product + " already opened");
            return;
        }
        openScreen(Constants.SideMenu.LIBRARY, ScreenModel.LIBRARY);
        if(!nookUtil.waitForScreenModel(ScreenModel.LIBRARY, Constants.DEFAULT_TIMEOUT))
            testManager.retest("Library not opened");
        libraryScreen = (LibraryScreen) nookUtil.getCurrentScreen(false);
        libraryScreen.changeFilter(LibraryScreen.FilterItems.ALL_ITEMS);
        libraryScreen.searchProduct(product);
        if(!nookUtil.waitForScreenModel(ScreenModel.SEARCH, Constants.DEFAULT_TIMEOUT))
            testManager.retest("Search not opened");
        searchScreen = (SearchScreen) nookUtil.getCurrentScreen(false);
        ArrayList<Element> searchProducts = searchScreen.getSearchResults(1, Constants.DEFAULT_TIMEOUT);
        iDevice.i("searchProducts: " + searchProducts.size());
        //todo fix with download product (add logic to load product)
        for (Element element : searchProducts) {
            if(element.getName().toLowerCase().contains(product.toLowerCase()) &&
                    !element.getName().toLowerCase().contains(Constants.Search.DOWNLOADING)) {
                TestManager.addStep("Click to open on " + product);
                clicker.clickOnElement(element);
                if(!nookUtil.waitForScreenModel(productType, Constants.DEFAULT_TIMEOUT, false)) {
                    iDevice.i("CURRENT SCREEN MODEL: " + nookUtil.screenModel.name());
                    testManager.retest("product " + product + " was not opened");
                }
                if (productType.equals(ScreenModel.DRP_READER)) {
                    iDevice.sleep(5000);
                }
                return;
            }
        }
        testManager.retest("necessary downloaded product " + testManager.getDrpMagazine() + " was not found");
    }

    public void openHamburgerMenuFromAnyScreen() throws TestException {
        Element menuBtn;
        if (waitWhileHamburgerMenuOpened(1))
            return;
        if (nookUtil.getCurrentScreen(true) == null)
            testManager.retest("Base screen is null");
        String currentScreen = nookUtil.getCurrentScreen(false).getClass().getSimpleName();
        baseScreen = nookUtil.getCurrentScreen(false);
        switch (currentScreen){
            case Constants.Screens.Classes.OOBE_SCREEN:
                testManager.retest("User is not logged");
                break;
            case Constants.Screens.Classes.SETTINGS_SCREEN:
                settingsScreen = (SettingsScreen) baseScreen;
                Element doneBtn = getter.getElementByName(Constants.CommonElements.DONE_BTN, new IConfig().setMaxLevelOfElementsTree(3));
                clicker.clickOnElement(doneBtn);
                TestManager.addStep("Click on Done");
                break;
            case Constants.Screens.Classes.LIBRARY_SCREEN:
                Element menu = waiter.waitForElementByNames(new IWaiterConfig()
                                .setMaxLevelOfElementsTree(3).addMatcher(Matcher.ContainsIgnoreCase),
                        10000, Constants.CommonElements.MENU_BTN, new String[] {Constants.CommonElements.MENU_BTN_2});
                if(menu != null) {
                    TestManager.addStep("Click on menu button");
                    clicker.clickByXY(menu.getX(), menu.getY());
                    return;
                }
                iDevice.i("Library Menu is not found");
                break;
            case Constants.Screens.Classes.SEARCH_SCREEN:
                Element cancelBtn = getter.getElementByName(Constants.CommonElements.CANCEL_BTN, new IConfig().setMaxLevelOfElementsTree(3));
                clicker.clickOnElement(cancelBtn);
                TestManager.addStep("Click on Cancel");
                break;
            case Constants.Screens.Classes.DRP_READER_SCREEN:
                drpReaderScreen = (DrpReaderScreen) baseScreen;
                if (!drpReaderScreen.isReaderMenuOpened()){
                    clicker.clickOnScreenCenter(1);
                    TestManager.addStep("Click on Center screen");
                }
                drpReaderScreen.openHamburgerMenu();
                return;
            case Constants.Screens.Classes.EPUB_READER_SCREEN:
                //todo logic for epub
                break;
            case Constants.Screens.Classes.MY_SHELVES_SCREEN:
                break;
        }
        if(waiter.waitForElementByNameExists(Constants.CommonElements.MENU_BTN, 10000,
                new IConfig().setMaxLevelOfElementsTree(3).setMatcher(Matcher.ContainsIgnoreCase)) == null)
            testManager.retest("Menu button was not found");
        menuBtn = getter.getElementByName(Constants.CommonElements.MENU_BTN,
                new IConfig().setMaxLevelOfElementsTree(3).setMatcher(Matcher.ContainsIgnoreCase));
        clicker.clickOnElement(menuBtn);
        TestManager.addStep("Click on menu button");
    }

    public void removeAllBookmarks(ScreenModel productType) throws TestException {
        switch (productType){
            case EPUB_READER:
                //todo logic
                break;
            case DRP_READER:
                new DrpReaderScreen(testManager, testManager.getTestHelper(), paramsParser, iDevice).removeAllBookmarks();
                break;
        }

    }

    public boolean waitWhileHamburgerMenuOpened(long timeout) {
        Element element = waiter.waitForElementByNameVisible(Constants.SideMenu.NOOK_LOGO, timeout,
                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase));
        if (element == null) {
            iDevice.i("Menu not opened");
            return false;
        }
        iDevice.i("Menu opened");
        return true;
    }
}

