package com.bn.nook.ios.assistant;

import com.bn.nook.ios.BaseTestRunner;
import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.json.Status;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ConfigParam;
import com.bn.nook.ios.param.ParamsParser;
import com.bn.nook.ios.screen.*;
import com.bn.nook.ios.screen.library.LibraryScreen;
import com.bn.nook.ios.screen.reader.DrpReaderScreen;
import com.bn.nook.ios.screen.reader.EpubReaderScreen;
import com.bn.nook.ios.utils.NookUtil;
import com.sofment.testhelper.driver.ios.config.IConfig;
import com.sofment.testhelper.driver.ios.config.IWaiterConfig;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.elements.ElementQuery;
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
    protected ProfileScreen profileScreen;
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

    public boolean signOut() throws TestException {
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
                return nookUtil.waitForScreenModel(ScreenModel.OOBE, Constants.DEFAULT_TIMEOUT);
            }
        }
        return false;
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

    public void initDRPScreen() throws TestException {
        nookUtil.waitForScreenModel(ScreenModel.DRP_READER, Constants.DEFAULT_TIMEOUT);
        baseScreen = nookUtil.getCurrentScreen(false);
        if(nookUtil.screenModel != ScreenModel.DRP_READER) testManager.retest("Necessary DRP_READER screen is not found. Expected: " + ScreenModel.DRP_READER.name() + ", actual : " + nookUtil.screenModel.name());
        drpReaderScreen = ((DrpReaderScreen) baseScreen);
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
            testManager.retest("Necessary product details screen is not found. Expected: " +
                    ScreenModel.PRODUCT_DETAILS.name() + ", actual : " +
                    nookUtil.screenModel.name());
        productDetailsScreen = ((ProductDetailsScreen) baseScreen);
    }

    public void initProfileScreen() throws TestException {
        nookUtil.waitForScreenModel(ScreenModel.PROFILES, Constants.DEFAULT_TIMEOUT);
        baseScreen = nookUtil.getCurrentScreen(false);
        if(nookUtil.screenModel != ScreenModel.PROFILES)
            testManager.retest("Necessary profile screen is not found. Expected: " +
                    ScreenModel.PROFILES.name() + ", actual : " + nookUtil.screenModel.name());
        profileScreen = ((ProfileScreen) baseScreen);
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

        testManager.addStep("Long click on product '" + productName + "'");
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
            testManager.addStep("Archive button doesn't exist");
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

        testManager.addStep("Long click on product '" + productName + "'");
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

    public void addProfile(String profileName, boolean isChild) throws TestException {
//        if (nookUtil.waitForScreenModel(ScreenModel.LIBRARY, 1, false)) {
//            iDevice.i(ScreenModel.LIBRARY.name() + " already opened");
//            return;
//        }
        openHamburgerMenuFromAnyScreen();
        if(!waitWhileHamburgerMenuOpened(Constants.DEFAULT_TIMEOUT))
            testManager.retest("Hamburger menu not opened");
        sideMenu = new SideMenu(testManager, testManager.getTestHelper(), paramsParser, iDevice);
        sideMenu.openProfile();

        if(!nookUtil.waitForScreenModel(ScreenModel.PROFILES, Constants.DEFAULT_TIMEOUT)) {
            testManager.retest("Profiles Screen was not loaded");
        }
        initProfileScreen();

        Element profile = profileScreen.getProfile(profileName);
        if(profile != null) {
            testManager.addStep("Profile '" + profileName + "' exists");
            return;
        }

        profileScreen.pressOnNavigationBarButton(Constants.ProfileScreen.ADD_BUTTON, true);

        profileScreen.waitForElement(new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIANavigationBar, 0)
                .addElement(UIAElementType.UIAStaticText, Constants.ProfileScreen.TITLE_NEW_PROFILE), Constants.DEFAULT_TIMEOUT, true);

        profileScreen.inputTextToProfile(profileName);

        profileScreen.pressOnNavigationBarButton(Constants.ProfileScreen.DONE_BUTTON, true);

        profileScreen.waitForElement(new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIANavigationBar, 0)
                .addElement(UIAElementType.UIAStaticText, Constants.ProfileScreen.TITLE_SET_PROFILE), Constants.DEFAULT_TIMEOUT, true);

        profile = profileScreen.getProfile(profileName);
        if(profile != null) {
            testManager.retest("Profile '" + profileName + "' doesn't exists");
        }

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
//        libraryScreen.changeFilter(LibraryScreen.FilterItems.ALL_ITEMS);
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
                testManager.addStep("Click to open on " + product);
                clicker.clickOnElement(element);
                if(!nookUtil.waitForScreenModel(productType, Constants.DEFAULT_TIMEOUT, false)) {
                    iDevice.i("CURRENT SCREEN MODEL: " + nookUtil.screenModel.name());
                    testManager.retest("product " + product + " was not opened");
                }
                if (productType.equals(ScreenModel.DRP_READER)){
                    if (waiter.waitForElementByNameVisible(Constants.Reader.Drp.COMICS_TIP, 5000, new IConfig().setMaxLevelOfElementsTree(2))!= null){
                        int[] screenSize = iDevice.getScreenSize();
                        clicker.clickByXY(screenSize[0] / 2, screenSize[1] / 2);
                        if (waiter.waitForElementByNameGone(Constants.Reader.Drp.COMICS_TIP, 5000,
                                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase)))
                            testManager.retest("Comics tip was not gone");
                        initDRPScreen();
                        drpReaderScreen.hideReaderMenu();
                    }
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
                testManager.addStep("Click on Done");
                break;
            case Constants.Screens.Classes.LIBRARY_SCREEN:
                Element menu = waiter.waitForElementByNames(new IWaiterConfig()
                                .setMaxLevelOfElementsTree(3).addMatcher(Matcher.ContainsIgnoreCase),
                        10000, Constants.CommonElements.MENU_BTN, new String[] {Constants.CommonElements.MENU_BTN_2});
                if(menu != null) {
                    testManager.addStep("Click on menu button");
                    clicker.clickByXY(menu.getX() + menu.getWidth()/2, menu.getY() + menu.getHeight()/2);
                    return;
                }
                iDevice.i("Library Menu is not found");
                break;
            case Constants.Screens.Classes.SEARCH_SCREEN:
                Element cancelBtn = getter.getElementByName(Constants.CommonElements.CANCEL_BTN, new IConfig().setMaxLevelOfElementsTree(3));
                clicker.clickOnElement(cancelBtn);
                testManager.addStep("Click on Cancel");
                break;
            case Constants.Screens.Classes.DRP_READER_SCREEN:
                drpReaderScreen = (DrpReaderScreen) baseScreen;
                if (!drpReaderScreen.isReaderMenuOpened()){
                    clicker.clickOnScreenCenter(1);
                    testManager.addStep("Click on Center screen");
                }
                drpReaderScreen.openHamburgerMenu();
                return;
            case Constants.Screens.Classes.EPUB_READER_SCREEN:
                //todo logic for epub
                break;
            case Constants.Screens.Classes.MY_SHELVES_SCREEN:
                menu = waiter.waitForElementByNames(new IWaiterConfig()
                                .setMaxLevelOfElementsTree(3).addMatcher(Matcher.ContainsIgnoreCase),
                        10000, Constants.CommonElements.MENU_BTN, new String[] {Constants.CommonElements.MENU_BTN_2});
                if(menu != null) {
                    testManager.addStep("Click on menu button");
                    clicker.clickByXY(menu.getX(), menu.getY());
                    return;
                }
                break;
        }
        if(waiter.waitForElementByNameExists(Constants.CommonElements.MENU_BTN, 10000,
                new IConfig().setMaxLevelOfElementsTree(3).setMatcher(Matcher.ContainsIgnoreCase)) == null)
            testManager.retest("Menu button was not found");
        menuBtn = getter.getElementByName(Constants.CommonElements.MENU_BTN,
                new IConfig().setMaxLevelOfElementsTree(3).setMatcher(Matcher.ContainsIgnoreCase));
        testManager.addStep("Click on menu button");
        clicker.clickOnElement(menuBtn);
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

    public void exploreApp() throws TestException {
        baseScreen = nookUtil.getCurrentScreen(true);
        if(nookUtil.screenModel == ScreenModel.SEARCH) {
            ((SearchScreen)baseScreen).closeSearch();
            baseScreen = nookUtil.getCurrentScreen(true);
        }
        if(nookUtil.screenModel == ScreenModel.LIBRARY ||
                nookUtil.screenModel == ScreenModel.MY_SHELVES) {
            signOut();
            baseScreen = nookUtil.getCurrentScreen(true);
        }
        if(nookUtil.screenModel == ScreenModel.OOBE) {
            ((OobeScreen)baseScreen).clickOnExploreAppButton();
            baseScreen = nookUtil.getCurrentScreen(true);
        }
    }
}

