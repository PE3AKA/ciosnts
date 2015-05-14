package com.bn.nook.ios.tests.acceptance;

import com.bn.nook.ios.BaseTestRunner;
import com.bn.nook.ios.annotation.Condition;
import com.bn.nook.ios.annotation.PreCondition;
import com.bn.nook.ios.assistant.Preparer;
import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.json.Status;
import com.bn.nook.ios.param.ConfigParam;
import com.bn.nook.ios.param.ParamsParser;
import com.bn.nook.ios.screen.*;
import com.bn.nook.ios.screen.library.LibraryScreen;
import com.bn.nook.ios.screen.reader.DrpReaderScreen;
import com.bn.nook.ios.screen.reader.EpubReaderScreen;
import com.sofment.testhelper.driver.ios.config.IConfig;
import com.sofment.testhelper.driver.ios.config.IWaiterConfig;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.elements.ElementQuery;
import com.sofment.testhelper.driver.ios.enams.SearchCondition;
import com.sofment.testhelper.driver.ios.enams.UIAElementType;
import com.sofment.testhelper.driver.ios.interfaces.AlertCallBack;
import com.sofment.testhelper.enums.Matcher;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by avsupport on 4/9/15.
 */
public class AcceptanceTests extends BaseTestRunner{

    private ArrayList<String> availableCountries;
    private ArrayList<String> expected;
    private int[] expectedResultIndex;
    private ArrayList<String> alertTitles;

    @PreCondition(preConditions = {Condition.SIGN_OUT},
            testId = 434245,
            testTitle = "Sign in Country list Menu [bnauto]")
    public void testCase434245() throws TestException{
        initOobeScreen();
        availableCountries = new ArrayList<String>();

        clickChoseCountryAndScroll(true);
        clickChoseCountryAndScroll(false);

        if(availableCountries.size() == 2 &&
                Arrays.toString(availableCountries.toArray()).toLowerCase().contains("united states") &&
                Arrays.toString(availableCountries.toArray()).toLowerCase().contains("united kingdom"))
            testManager.testCaseInfo.setStatusId(1);
        else {
            testManager.failTest("wrong country list");
        }
    }

    @PreCondition(preConditions = {Condition.SIGN_OUT},
            testId = 435985,
            testTitle = "Sign in Nook Account Login [bnauto]")
    public void testCase435985() throws TestException {
        initOobeScreen();
        oobeScreen.waitForSignInButton(Constants.DEFAULT_TIMEOUT);
        iDevice.sleep(3000);
        oobeScreen.clickOnSignInButton(Constants.DEFAULT_TIMEOUT);
        testCase435985ExpectedResult1();
        oobeScreen.inputCredentials();
        oobeScreen.waitForSignInButton(Constants.DEFAULT_TIMEOUT);
        oobeScreen.waitForCollection();
        takeScreenShot("UIACollection_appeared");
        testManager.testCaseInfo.setStatusId(1);
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 435990,
            testTitle = "Left Nook app menu: Currently Reading [bnauto]")
    public void testCase435990() throws TestException {
        setupAlertCalBack435990();
//        initOobeScreen();
//        oobeScreen.signIn(Constants.DEFAULT_TIMEOUT);

        nookUtil.waitForScreenModel(ScreenModel.LIBRARY, Constants.DEFAULT_TIMEOUT);
        libraryScreen = (LibraryScreen) nookUtil.getCurrentScreen(false);
        libraryScreen.openMenu();
        takeScreenShot("hamburger_menu_opened");
        checkCurrentReadButton();
        testManager.testCaseInfo.setStatusId(1);
    }

    private void checkCurrentReadButton() throws TestException {
        Element tableView = waiter.waitForElementByNames(new IWaiterConfig().setMaxLevelOfElementsTree(2).setSearchCondition(SearchCondition.VALUE).addMatcher(Matcher.ContainsIgnoreCase).setOnlyVisible(true), 1, "to 5 of 5", "to 6 of 6");
        if(tableView == null) {
            testManager.retest("table view of menu ia not found");
        }

        ArrayList<Element> elements = getter.getElementChildren(tableView);
        if(elements.size() < 2) {
            testManager.failTest("Current Read is not found");
        }
        Element currentRead = elements.get(1);
        if(!currentRead.getName().equals(Constants.Library.Menu.CURRENT_READ)) {
            testManager.failTest("Current Read is not found");
        }
        testManager.addStep("click on " + Constants.Library.Menu.CURRENT_READ);
        clicker.clickOnElement(currentRead);

        long startTime = System.currentTimeMillis();
        while (true) {
            if(System.currentTimeMillis() - startTime > Constants.DEFAULT_TIMEOUT) {
                testManager.failTest("After click on \"current read\" alert is not appeared");
            }
            if(expectedResultIndex[0] > 0) break;
        }
    }

    private void setupAlertCalBack435990() {
        expected = new ArrayList<String>();
        expectedResultIndex = new int[]{0};
        expected.add("Tapping here will normally take you".toLowerCase());
//                    Tapping here will normally take you to your
        alertTitles = new ArrayList<String>();
        iDevice.setAlertHandler(new AlertCallBack() {
            @Override
            public void handleAlert(ArrayList<Element> arrayList) {
                String name;
                for (Element element : arrayList) {
                    name = element.getName().toLowerCase();
                    if (!name.equals("null") && !name.equals("empty list")) alertTitles.add(name);
                    if (name.contains(expected.get(0))) {
                        expectedResultIndex[0]++;
                    }
                }
            }
        });
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 435993,
            testTitle = "Left Nook app menu: Profile Name [bnauto]")
    public void testCase435993() throws TestException {
        initLibraryScreen();
        libraryScreen.openMenu();
        takeScreenShot("hamburger_menu_opened");
        expectedResult435993();
        testManager.testCaseInfo.setStatusId(1);
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 435986,
            testTitle = "Sign in: Profile message [bnauto]")
    public void testCase435986() throws TestException {
        setupAlertCalBack();

        takeScreenShot("UIACollection_appeared");
        if(expectedResultFor435986())
            testManager.testCaseInfo.setStatusId(1);
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 435987,
            testTitle = "Left Nook app menu: Library [bnauto]")
    public void testCase435987() throws TestException {
        initLibraryScreen();
        libraryScreen.openMenu(LibraryScreen.MenuItems.LIBRARY);
        Element libraryTitle = waiter.waitForElementByNameVisible(Constants.Library.Menu.LIBRARY, Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(2).setSearchCondition(SearchCondition.VALUE));
        if(libraryTitle == null) testManager.failTest(Constants.Library.Menu.LIBRARY + "library screen is not loaded");
        testManager.testCaseInfo.setStatusId(1);
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 435994,
            testTitle = "Global Navigation/filtering: Library filters (content type) [bnauto]")
    public void testCase435994() throws TestException {
        testCase435987();
        initLibraryScreen();
        int currentFilter = libraryScreen.getCurrentFilter();
        if(currentFilter != LibraryScreen.FilterItems.ALL_ITEMS) {
            testManager.failTest("expected filter by default " + libraryScreen.getFilterNameByIndex(LibraryScreen.FilterItems.ALL_ITEMS) + ",\n" +
                    "actually found filter " + libraryScreen.getFilterNameByIndex(currentFilter));
        }
        for(int currentFilterIndex = 1; currentFilterIndex < 7; currentFilterIndex ++) {
            libraryScreen.changeFilter(currentFilterIndex);
            currentFilter = libraryScreen.getCurrentFilter();
            if(currentFilter == -1) {
                iDevice.i("can not detect current filter.");
                takeScreenShot("should be filter by " + libraryScreen.getFilterNameByIndex(currentFilterIndex));
            } else if(currentFilter != currentFilterIndex) {
                testManager.failTest("expected: " + libraryScreen.getFilterNameByIndex(currentFilterIndex) + ", \n" +
                        "actual: " + libraryScreen.getFilterNameByIndex(currentFilter));
            }
        }
        libraryScreen.changeFilter(LibraryScreen.FilterItems.ALL_ITEMS);
        currentFilter = libraryScreen.getCurrentFilter();

        if(currentFilter != LibraryScreen.FilterItems.ALL_ITEMS) {
            testManager.failTest("expected filter by " + libraryScreen.getFilterNameByIndex(LibraryScreen.FilterItems.ALL_ITEMS) + ",\n" +
                    "actually found filter by " + libraryScreen.getFilterNameByIndex(currentFilter));
        }
        testManager.testCaseInfo.setStatusId(1);
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 435995,
            testTitle = "Global Navigation/filtering: Library filters (content type) [bnauto]")
    public void testCase435995() throws TestException {
        testCase435987();
        initLibraryScreen();
        libraryScreen.changeFilter(LibraryScreen.FilterItems.ALL_ITEMS);
        int currentSort = libraryScreen.getCurrentSort();
        if(currentSort != LibraryScreen.SortItems.RECENT) {
            testManager.failTest("expected sort by default " + libraryScreen.getSortNameByIndex(LibraryScreen.SortItems.RECENT) + ",\n" +
                    "actually found sort " + libraryScreen.getSortNameByIndex(currentSort));
        }
        for(int currentSortIndex = 1; currentSortIndex < 3; currentSortIndex ++) {
            libraryScreen.changeSort(currentSortIndex);
            currentSort = libraryScreen.getCurrentSort();
            if(currentSort == -1) {
                iDevice.i("can not detect current sort.");
                takeScreenShot("should be sort by " + libraryScreen.getSortNameByIndex(currentSortIndex));
            } else if(currentSort != currentSortIndex) {
                testManager.failTest("expected: " + libraryScreen.getSortNameByIndex(currentSortIndex) + ", \n" +
                        "actual: " + libraryScreen.getSortNameByIndex(currentSort));
            }
        }
        libraryScreen.changeSort(LibraryScreen.SortItems.RECENT);
        currentSort = libraryScreen.getCurrentSort();

        if(currentSort != LibraryScreen.FilterItems.ALL_ITEMS) {
            testManager.failTest("expected sort by " + libraryScreen.getSortNameByIndex(LibraryScreen.SortItems.RECENT) + ",\n" +
                    "actually found sort " + libraryScreen.getSortNameByIndex(currentSort));
        }
        testManager.testCaseInfo.setStatusId(1);
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 435997,
            testTitle = "Download sample content [bnauto]")
    public void testCase435997() throws TestException {
        testCase435987();
        initLibraryScreen();
        libraryScreen.changeFilter(LibraryScreen.FilterItems.ALL_ITEMS);
        libraryScreen.searchProduct(testManager.getSearchProduct());

        nookUtil.waitForScreenModel(ScreenModel.SEARCH, Constants.DEFAULT_TIMEOUT);

        initSearchScreen();

        Element element = searchScreen.findSample();

        Element sampleButton = waiter.waitForElementByNameExists(Constants.Search.SAMPLE, Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(2).setParentElement(element));

        testManager.addStep("click on sample button");
        if(!clicker.clickOnElement(sampleButton)) {
            testManager.retest("can not click on sample button");
        }

        Element read = waiter.waitForElementByNameExists(Constants.Search.READ, Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(2).setParentElement(element));
        if(read == null) {
            testManager.retest("read button is not found");
        }

        testManager.addStep("click on read button");
        if(!clicker.clickOnElement(read)) {
            testManager.retest("can not click on read button");
        }

        initReaderScreen();

        if(readerScreen == null) {
            testManager.failTest("sample was not opened");
        }

        testManager.testCaseInfo.setStatusId(1);
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 435998,
            testTitle = "ePUB: Download [bnauto]")
    public void testCase435998() throws TestException {

        testCase435987();
        initLibraryScreen();
        libraryScreen.changeFilter(LibraryScreen.FilterItems.ALL_ITEMS);
        libraryScreen.searchProduct(testManager.getEpubProduct());

        nookUtil.waitForScreenModel(ScreenModel.SEARCH, Constants.DEFAULT_TIMEOUT);

        initSearchScreen();

        Element product = searchScreen.findNotDownloadedProduct();
        if(product == null) {
            testManager.retest("not downloaded product is not found");
        }

        if(!searchScreen.downloadProduct(product)) {
            testManager.failTest(testManager.getEpubProduct() + " product is not downloaded");
        }

        testManager.testCaseInfo.setStatusId(1);
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 435999,
            testTitle = "ePUB: Open [bnauto]")
    public void testCase435999() throws TestException {

        testCase435987();
        initLibraryScreen();
        libraryScreen.changeFilter(LibraryScreen.FilterItems.ALL_ITEMS);
        libraryScreen.searchProduct(testManager.getEpubProduct());

        nookUtil.waitForScreenModel(ScreenModel.SEARCH, Constants.DEFAULT_TIMEOUT);

        initSearchScreen();

        ArrayList<Element> searchProducts = searchScreen.getSearchResults(1, Constants.DEFAULT_TIMEOUT);

        iDevice.i("searchProducts: " + searchProducts.size());
        for (Element element : searchProducts) {
            if(element.getName().toLowerCase().contains(testManager.getEpubProduct().toLowerCase()) &&
                    !element.getName().toLowerCase().contains(Constants.Search.SAMPLE.toLowerCase()) &&
                    waiter.waitForElementByNameExists(Constants.Search.SAMPLE, 1,
                            new IConfig().setMatcher(Matcher.ContainsIgnoreCase).setParentElement(element).setMaxLevelOfElementsTree(2)) == null) {

                if(element.getName().toLowerCase().contains(Constants.Search.NOT_DOWNLOADED.toLowerCase())) {
                    if(!searchScreen.downloadProduct(element)) {
                        testManager.retest("can not download necessary product " + testManager.getEpubProduct());
                    }
                } else if(element.getName().toLowerCase().contains(Constants.Search.DOWNLOADING.toLowerCase())) {
                    testManager.retest("necessary product " + testManager.getEpubProduct() + " is downloading...");
                } else {
                    scroller.scrollToVisible(element);
                }

                testManager.addStep("Click to open on " + testManager.getEpubProduct() + " by xy: [" + (element.getX() + element.getWidth() / 2) + ", " + element.getY() + element.getHeight() / 2 + "]");
                clicker.clickByXY(element.getX() + element.getWidth() / 2, element.getY() + element.getHeight() / 2);
                if(!nookUtil.waitForScreenModel(ScreenModel.EPUB_READER, Constants.DEFAULT_TIMEOUT, false)) {
                    testManager.failTest("product " + testManager.getEpubProduct() + " was not opened");
                }

                testManager.testCaseInfo.setStatusId(1);
                return;
            }
        }
        testManager.retest("necessary downloaded product " + testManager.getEpubProduct() + " was not found");
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 436000,
            testTitle = "ePUB: Table of content")
    public void testCase436000() throws TestException {

        testCase435999();
        initEbubReaderScreen();

        if(!readerScreen.openContents()){
            testManager.failTest("contents is not opened");
        }

        readerScreen.closeContents();

        iDevice.sleep(2000);
        testManager.testCaseInfo.setStatusId(1);
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 436002,
            testTitle = "ePUB:bookmark [bnauto]")
    public void testCase436002() throws TestException {

        testCase435999();
        initEbubReaderScreen();

        readerScreen.openReaderMenu();

        readerScreen.removeBookmark();
        readerScreen.addBookmark();

        int[] pageInfo = readerScreen.getCurrentPageInfo();

        readerScreen.openBookmarkTab();

        Element tableView = waiter.waitForElementByClassVisible(UIAElementType.UIATableView, 1, new IConfig());
        if(tableView == null) testManager.retest("bookmarks list is not found");

        ArrayList<Element> bookmarks = getter.getElementChildren(tableView);

        Element necessaryElement = null;
        for(Element bookmark : bookmarks) {
            if(bookmark.getName().toLowerCase().trim().contains("page " + pageInfo[0] + ":")) {
                necessaryElement = bookmark;
                break;
            }
        }

        if(necessaryElement == null) {
            testManager.failTest("bookmarked page is not found");
            return;
        }

        testManager.addStep("click on page: " + necessaryElement.getName());
        clicker.clickOnElement(necessaryElement);

        iDevice.sleep(3000);

        int[] pageInfo1 = readerScreen.getCurrentPageInfo();

        if(pageInfo[0] != pageInfo1[0]) {
            testManager.failTest("looks like loaded wrong page:\n" +
                    "expected: " + Arrays.toString(pageInfo) + "\n" +
                    "actual loaded: " + Arrays.toString(pageInfo1));
        }

        testManager.testCaseInfo.setStatusId(1);
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 436003,
            testTitle = "ePUB:text options [bnauto]")
    public void testCase436003() throws TestException {

        testCase435999();
        initEbubReaderScreen();

        prepareTextOptions();

        String before = takeScreenShot("before_changing_font_size");
        readerScreen.changeFontSize(EpubReaderScreen.FontSize.EXTRA_LARGE_FONT);
        iDevice.sleep(2000);
        String after = takeScreenShot("after_changing_font_size");

        if(testManager.compareTwoImages(before, after)) {
            testManager.failTest("the images before and after font size changing are equals: \n" +
                    "screen shot before: " + before + "\n" +
                    "screen shot after: " + after);
        }

        before = after;
        readerScreen.changeFont(EpubReaderScreen.Font.TREBUCHET);
        iDevice.sleep(2000);
        after = takeScreenShot("after_changing_font");

        if(testManager.compareTwoImages(before, after)) {
            testManager.failTest("the images before and after font changing are equals: \n" +
                    "screen shot before: " + before + "\n" +
                    "screen shot after: " + after);
        }

        before = after;
        readerScreen.changeTheme(EpubReaderScreen.Theme.MOCHA);
        iDevice.sleep(2000);
        after = takeScreenShot("after_changing_theme");

        if(testManager.compareTwoImages(before, after)) {
            testManager.failTest("the images before and after theme changing are equals: \n" +
                    "screen shot before: " + before + "\n" +
                    "screen shot after: " + after);
        }

        before = after;
        readerScreen.changeLineSpacing(EpubReaderScreen.LineSpacing.SINGLE_LINE_SPACING);
        iDevice.sleep(2000);
        after = takeScreenShot("after_changing_line_spacing");

        if(testManager.compareTwoImages(before, after)) {
            testManager.failTest("the images before and after line spacing changing are equals: \n" +
                    "screen shot before: " + before + "\n" +
                    "screen shot after: " + after);
        }

        before = after;
        readerScreen.changeMargin(EpubReaderScreen.Margin.LARGE_MARGIN);
        iDevice.sleep(2000);
        after = takeScreenShot("after_changing_margin");

        if(testManager.compareTwoImages(before, after)) {
            testManager.failTest("the images before and after margin changing are equals: \n" +
                    "screen shot before: " + before + "\n" +
                    "screen shot after: " + after);
        }

        testManager.testCaseInfo.setStatusId(1);
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 436004,
            testTitle = "ePUB:more options [bnauto]")
    public void testCase436004() throws TestException {

        testCase435999();
        initEbubReaderScreen();

        readerScreen.openTextOptions();

        Element moreOptions = waiter.waitForElementByNameVisible(Constants.Reader.Epub.TextOptions.MORE_OPTIONS, 1, new IConfig().setMaxLevelOfElementsTree(2));

        if(moreOptions == null) testManager.failTest(String.format("the button to open more options is not found [%s]", Constants.Reader.Epub.TextOptions.MORE_OPTIONS));

        clicker.clickOnElement(moreOptions);

        moreOptions = waiter.waitForElementByNameVisible(Constants.Reader.Epub.TextOptions.MoreOptions.StaticText.JUSTIFICATION, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(moreOptions == null) testManager.failTest(String.format("%s is not found", Constants.Reader.Epub.TextOptions.MoreOptions.StaticText.JUSTIFICATION));
        moreOptions = waiter.waitForElementByNameVisible(Constants.Reader.Epub.TextOptions.MoreOptions.StaticText.PUBLISHER_DEFAULTS, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(moreOptions == null) testManager.failTest(String.format("%s is not found", Constants.Reader.Epub.TextOptions.MoreOptions.StaticText.PUBLISHER_DEFAULTS));
        moreOptions = waiter.waitForElementByNameVisible(Constants.Reader.Epub.TextOptions.MoreOptions.StaticText.LOCK_ROTATION, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(moreOptions == null) testManager.failTest(String.format("%s is not found", Constants.Reader.Epub.TextOptions.MoreOptions.StaticText.LOCK_ROTATION));
        moreOptions = waiter.waitForElementByNameVisible(Constants.Reader.Epub.TextOptions.MoreOptions.StaticText.ANIMATE_PAGE_TURNS, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(moreOptions == null) testManager.failTest(String.format("%s is not found", Constants.Reader.Epub.TextOptions.MoreOptions.StaticText.ANIMATE_PAGE_TURNS));

        testManager.testCaseInfo.setStatusId(1);
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 436005,
            testTitle = "ePUB:go to page [bnauto]")
    public void testCase436005() throws TestException {

        testCase435999();
        initEbubReaderScreen();

        int[] pageInfo = readerScreen.getCurrentPageInfo();

        readerScreen.openContents();
        Element textField = waiter.waitForElementByClassExists(UIAElementType.UIATextField, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(textField == null) {
            testManager.failTest("text field go to page is not found");
        }

        testManager.addStep("click to text field go to page");
        int random_page = getRandomInt(1, pageInfo[1]);
        testManager.addStep("input text " + random_page);
        iDevice.inputText(random_page + "", textField);

        Element goToPage = waiter.waitForElementByNameVisible(Constants.Reader.Epub.Contents.GO_TO_PAGE, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(goToPage == null) testManager.retest(String.format("go to page button is not found [%s]", Constants.Reader.Epub.Contents.GO_TO_PAGE));
        testManager.addStep(String.format("click on button to go to page [%s]", Constants.Reader.Epub.Contents.GO_TO_PAGE));
        clicker.clickOnElement(goToPage);

        iDevice.sleep(3000);
        pageInfo = readerScreen.getCurrentPageInfo();
        if(pageInfo[0] != random_page) {
            testManager.failTest("loaded wrong page:\n" +
                    "expected: " + random_page + "\n" +
                    "loaded: " + pageInfo[0]);
        }
        testManager.testCaseInfo.setStatusId(1);
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 436007,
            testTitle = "ePUB:View details [bnauto]")
    public void testCase436007() throws TestException {

        testCase435999();
        initEbubReaderScreen();

        readerScreen.openInformation();

        readerScreen.closeInformation();

        if(!nookUtil.waitForScreenModel(ScreenModel.EPUB_READER, 10000, false)) {
            testManager.failTest("after click back on product information screen the reader wa not loaded");
        }

        testManager.testCaseInfo.setStatusId(1);
    }

    /**
     *
     * @return ArrayList with two objects.
     * 1-st object - int (page number before drag to reader control)
     * 2-s - instanse of Element class (back button);
     * @throws TestException
     */

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 436008,
            testTitle = "ePUB:navigation bar [bnauto]")
    public ArrayList<Object> testCase436008() throws TestException {

        testCase435999();
        initEbubReaderScreen();

        ArrayList<Object> results = new ArrayList<>();

        int[] pageInfoBefore = readerScreen.getCurrentPageInfo();
        double random = (double)getRandomInt(0, 100) / (double) 100;
        if(!readerScreen.dragToValue(random)) {
            testManager.retest("can not complete drag to value");
        }

        iDevice.sleep(3000);

        int[] pageInfoAfter = readerScreen.getCurrentPageInfo();

        if(pageInfoAfter[0] == pageInfoBefore[0]) {
            testManager.failTest("page number is not changed after drag the navigation bar: \n" +
                    "before drag: " + pageInfoBefore[0] + "\n" +
                    "after drag: " + pageInfoAfter[0]);
        }

        Element back = waiter.waitForElementByNameVisible(Constants.Reader.Epub.BACK, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(back == null) {
            testManager.failTest(String.format("back button is not found [%s]", Constants.Reader.Epub.BACK));
        }

        testManager.testCaseInfo.setStatusId(1);
        results.add(pageInfoBefore[0]);
        results.add(back);
        return results;
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 436010,
            testTitle = "ePUB:go back to page [bnauto]")
    public void testCase436010() throws TestException {
        ArrayList<Object> objects = testCase436008();
        testManager.testCaseInfo.setStatusId(Status.FAILED);

        int pageNumberBeforeDrag = -1;
        Element backButton = null;
        if(objects.get(0) instanceof Integer) {
            pageNumberBeforeDrag = (Integer) objects.get(0);
        }
        if(objects.get(1) instanceof Element) {
            backButton = (Element) objects.get(1);
        }

        if(backButton == null) {
            testManager.retest(String.format("back button is not found [%s]", Constants.Reader.Epub.BACK));
        }

        testManager.addStep("click on back button");
        if(!clicker.clickOnElement(backButton)) {
            testManager.failTest(String.format("can not click on back button [%s]", Constants.Reader.Epub.BACK));
        }

        iDevice.sleep(3000);

        int[] pageInfoAfter = readerScreen.getCurrentPageInfo();

        if(pageInfoAfter[0] != pageNumberBeforeDrag) {
            testManager.failTest("the page number before drag the navigation bar is not equals the page number after click the back button: \n" +
                    "before drag: " + pageNumberBeforeDrag + "\n" +
                    "after click the back button: " + pageInfoAfter[0]);
        }

        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    private void prepareTextOptions() throws TestException {
        readerScreen.changeFontSize(EpubReaderScreen.FontSize.SMALL_FONT);
        readerScreen.changeFont(EpubReaderScreen.Font.GEORGIA);
        readerScreen.changeTheme(EpubReaderScreen.Theme.DAY);
        readerScreen.changeLineSpacing(EpubReaderScreen.LineSpacing.ONE_AND_HALF_LINES_SPACING);
        readerScreen.changeMargin(EpubReaderScreen.Margin.MEDIUM_MARGIN);
        readerScreen.closeTextOptions();
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 435988,
            testTitle = "Left Nook app menu: My Shelves [bnauto]")
    public void testCase435988() throws TestException {
        initLibraryScreen();
        libraryScreen.openMenu(LibraryScreen.MenuItems.MY_SHELVES);
        Element libraryTitle = waiter.waitForElementByNameVisible(Constants.Library.Menu.MY_SHELVES, Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(2).setSearchCondition(SearchCondition.VALUE));
        if(libraryTitle == null) testManager.failTest(Constants.Library.Menu.MY_SHELVES + "screen is not loaded");
        testManager.testCaseInfo.setStatusId(1);
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 435992,
            testTitle = "Left Nook app menu: Messages [bnauto]")
    public void testCase435992() throws TestException {
        String inboxTitle = "Inbox";
        initLibraryScreen();
        libraryScreen.openMenu(LibraryScreen.MenuItems.MESSAGES);
        Element libraryTitle = waiter.waitForElementByNameVisible(inboxTitle, Constants.DEFAULT_TIMEOUT, new IConfig().setMatcher(Matcher.ContainsIgnoreCase).setMaxLevelOfElementsTree(2).setSearchCondition(SearchCondition.VALUE));
        if(libraryTitle == null) testManager.failTest(inboxTitle + "screen is not loaded");
        testManager.testCaseInfo.setStatusId(1);
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 435989,
            testTitle = "Left Nook app menu: Settings [bnauto]")
    public void testCase435989() throws TestException {
        initLibraryScreen();
        libraryScreen.openMenu(LibraryScreen.MenuItems.SETTINGS);
        Element libraryTitle = waiter.waitForElementByNameVisible(Constants.Library.Menu.SETTINGS, Constants.DEFAULT_TIMEOUT, new IConfig().setMatcher(Matcher.ContainsIgnoreCase).setMaxLevelOfElementsTree(2).setSearchCondition(SearchCondition.VALUE));
        if(libraryTitle == null) testManager.failTest(Constants.Library.Menu.SETTINGS + "screen is not loaded");

        checkSettingsScreen();
        testManager.testCaseInfo.setStatusId(1);
    }

    @PreCondition(preConditions = {Condition.LOGIN},
            testId = 436032,
            testTitle = "Sign Out of Account [bnauto]")
    public void testCase436032() throws TestException {
        initLibraryScreen();
        if(!preparer.signOut()) {
            testManager.failTest("Oobe first screen is not appeared");
        }
        testManager.testCaseInfo.setStatusId(1);
    }

    @PreCondition(preConditions = {Condition.DEFERRED_SIGN_IN},
            testId = 436034,
            testTitle = "Deferred sign in:library search [bnauto]")
    public Element testCase436034() throws TestException {
        initDeferredSignInScreen();

        deferredSignInScreen.searchProduct(testManager.getDeferredSignInSearchProduct());

        initSearchScreen();

        Element sample = searchScreen.findSample();
        if(sample == null) {
            testManager.failTest("sample is not found");
        }
        testManager.testCaseInfo.setStatusId(Status.PASSED);
        return sample;
    }

    @PreCondition(preConditions = {Condition.DEFERRED_SIGN_IN},
            testId = 436035,
            testTitle = "Deferred sign in:download sample [bnauto]")
    public void testCase436035() throws TestException {
        Element sampleButton = testCase436034();
        testManager.testCaseInfo.setStatusId(Status.FAILED);

        Element parent = getter.getParent(sampleButton);

        iDevice.i("Sample button: " + sampleButton.getName());
        iDevice.i("parent of sample button: " + parent.getName());

        initSearchScreen();
        if(!searchScreen.downloadProduct(parent)) {
            testManager.failTest(String.format("the sample %s is not downloaded", parent.getName()));
        }

        searchScreen.closeSearch();

        initDeferredSignInScreen();

        ArrayList<Element> elements = deferredSignInScreen.getProducts();

        ArrayList<String> availableNames =new ArrayList<String>();

        for(Element element : elements) {
            availableNames.add(element.getName());
            if(element.getName() != null && element.getName().toLowerCase().replaceAll(",", "").contains(parent.getName().toLowerCase().replaceAll(",", ""))) {
                testManager.testCaseInfo.setStatusId(Status.PASSED);
                return;
            }
        }

//        Carly (Women of Ivy Manor Series #4), by Lyn Cote, SAMPLE
//        Carly (Women of Ivy Manor Series #4) by Lyn Cote

        testManager.failTest("the sample which was downloaded is not found in library screen.\n" +
                "expected: " + parent.getName() + "\n" +
                "actual: " + Arrays.toString(availableNames.toArray()));
    }

    private void checkSettingsScreen() throws TestException {
        Element necessaryElement = waiter.waitForElementByNameExists(Constants.Settings.DONE, 1, new IConfig().setMaxLevelOfElementsTree(3));
        if(necessaryElement == null)
            testManager.failTest("button \"" + Constants.Settings.DONE + "\" is not found");

        necessaryElement = waiter.waitForElementByNameExists(Constants.Settings.ACCOUNT_SETTINGS, 1, new IConfig().setMaxLevelOfElementsTree(3));
        if(necessaryElement == null)
            testManager.failTest("button \"" + Constants.Settings.ACCOUNT_SETTINGS + "\" is not found");

        necessaryElement = waiter.waitForElementByNameExists(Constants.Settings.PROFILES, 1, new IConfig().setMaxLevelOfElementsTree(3));
        if(necessaryElement == null)
            testManager.failTest("button \"" + Constants.Settings.PROFILES + "\" is not found");

        necessaryElement = waiter.waitForElementByNameExists(Constants.Settings.SUPPORT, 1, new IConfig().setMaxLevelOfElementsTree(3));
        if(necessaryElement == null)
            testManager.failTest("button \"" + Constants.Settings.SUPPORT + "\" is not found");

        necessaryElement = waiter.waitForElementByNameExists(Constants.Settings.LOG_OUT, 1, new IConfig().setMaxLevelOfElementsTree(3));
        if(necessaryElement == null)
            testManager.failTest("button \"" + Constants.Settings.LOG_OUT + "\" is not found");
    }

    @PreCondition(preConditions = {Condition.LOGIN, Condition.UNARCHIVE_PRODUCT},
            productName = ConfigParam.ARCHIVE_PRODUCT,
            testId = 436026,
            testTitle = "Archive a book [bnauto]")
    public void testCase436026() throws TestException {
        initLibraryScreen();
        libraryScreen.openMenu(LibraryScreen.MenuItems.LIBRARY);
        libraryScreen.changeFilter(LibraryScreen.FilterItems.ALL_ITEMS);
        String productName = testManager.getTestProperty(ConfigParam.ARCHIVE_PRODUCT.name());
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

        if(!productDetailsScreen.pressOnManageItem(Constants.ProductDetails.ARCHIVE_BUTTON))
            testManager.retest("Archive button doesn't exist");

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

        if(!nookUtil.waitForScreenModel(ScreenModel.LIBRARY, Constants.DEFAULT_TIMEOUT)) {
            testManager.retest("Library screen was not loaded");
        }
        initLibraryScreen();
        if(!libraryScreen.changeFilter(LibraryScreen.FilterItems.ARCHIVED)){
            testManager.retest("Current filter is not " + LibraryScreen.FilterItems.ARCHIVED);
        }

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
            testManager.failTest("Product '" + productName + "' was not archivated");
        else testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    @PreCondition(preConditions = {Condition.LOGIN, Condition.ARCHIVE_PRODUCT},
            productName = ConfigParam.ARCHIVE_PRODUCT,
            testId = 436027,
            testTitle = "Unarchive a book [bnauto]")
    public void testCase436027() throws TestException {
        String productName = testManager.getTestProperty(ConfigParam.ARCHIVE_PRODUCT.name());

        if(!nookUtil.waitForScreenModel(ScreenModel.PRODUCT_DETAILS, 5000, false)) {
            initLibraryScreen();
            libraryScreen.openMenu(LibraryScreen.MenuItems.LIBRARY);
            libraryScreen.changeFilter(LibraryScreen.FilterItems.ARCHIVED);

            ArrayList<Element> products = libraryScreen.getProducts(1, Constants.DEFAULT_TIMEOUT);
            Element archiveProduct = null;
            for (Element element : products) {
                iDevice.i("###########################Product:" + element.toString());
                if (element != null && element.getName().toLowerCase().contains(productName.toLowerCase())) {
                    archiveProduct = element;
                    break;
                }
            }

            if (archiveProduct == null)
                testManager.retest("Product '" + productName + "' was not archivated");

            if (!scroller.scrollToVisible(archiveProduct)) {
                throw new TestException("Can not scrollable to product '" + productName + "'").retest();
            }

            testManager.addStep("Long click on product '" + productName + "'");
            clicker.longClickOnElement(archiveProduct, 5);

            if (!nookUtil.waitForScreenModel(ScreenModel.PRODUCT_DETAILS, Constants.DEFAULT_TIMEOUT)) {
                testManager.retest("Product details screen was not loaded");
            }
        }
        initProductDetailsScreen();

        if(!productDetailsScreen.pressOnManageItem(Constants.ProductDetails.UNARCHIVE_BUTTON))
            testManager.retest("Unarchive button doesn't exist");

        if(!nookUtil.waitForScreenModel(ScreenModel.LIBRARY, Constants.DEFAULT_TIMEOUT)){
            testManager.retest("Library screen was not loaded");
        }
        initLibraryScreen();

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
            testManager.failTest("Product '" + productName + "' was not found");
        else if(!product.getName().toLowerCase().contains("not downloaded"))
            testManager.failTest("Product '" + productName + "' don't consist download icon");
        else testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    @PreCondition(preConditions = {Condition.LOGIN, Condition.OPEN_SCREEN},
            screenModel = ScreenModel.MY_SHELVES,
            screenTitle = Constants.Library.Menu.MY_SHELVES,
            testId = 436028,
            testTitle = "Create a Shelf [bnauto] ")
    public void testCase436028() throws TestException {
        if(!nookUtil.waitForScreenModel(ScreenModel.MY_SHELVES, Constants.DEFAULT_TIMEOUT)) {
            testManager.retest("My Shelves screen was not loaded");
        }
        initMyShelvesScreen();
        myShelvesScreen.pressOnToolBarButton(Constants.My_Shelves.ADD_BUTTON, true);

        Element nextBtn = waiter.waitForElementVisible(Constants.DEFAULT_TIMEOUT, new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIANavigationBar, 0)
                .addElement(UIAElementType.UIAStaticText, Constants.My_Shelves.TITLE_CREATE_NEW_SHELF));
        if(nextBtn == null)
            testManager.retest("Cannot detect screen " + Constants.My_Shelves.TITLE_CREATE_NEW_SHELF);

        ArrayList<Element> products = myShelvesScreen.getProducts(1, 5000);
        if(products == null || products.size() == 0 )
            testManager.retest("Products were not found");
        Element firstElement = products.get(0);
        if(!scroller.scrollToVisible(firstElement))
            testManager.retest("Can not scrollable to product '" + firstElement.getName() + "'");

        iDevice.i("Select product " + firstElement.getName());
        testManager.addStep("Select product " + firstElement.getName());
        clicker.clickOnElement(firstElement);

        final int[] alertState = new int[1];
        alertState[0] = 0;
        iDevice.setAlertHandler(new AlertCallBack() {
            @Override
            public void handleAlert(ArrayList<Element> arrayList) {
                iDevice.i("############Handle Alert");
                for (Element element : arrayList) {
                    iDevice.i("Alert Element name:" + element.getName());
                    if (element.getName().toLowerCase().equals(Constants.My_Shelves.ALERT_TITLE_CREATE_SHELF.toLowerCase())) {
                        iDevice.i("Alert Element exist");
                        alertState[0] = 1;
                        break;
                    }
                }
            }
        });

        myShelvesScreen.pressOnNavigationBarButton(Constants.My_Shelves.NEXT_BUTTON, true);

        iDevice.i("waiting for alert Create Shelf");
        long startTimer = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTimer < 20000) {
            if(alertState[0] != 0) {
                iDevice.i("Alert is visible");
                break;
            }
        }
        iDevice.sleep(1000);
        if(alertState[0] == 0)
            testManager.retest("Alert '" + Constants.My_Shelves.ALERT_TITLE_CREATE_SHELF + "' was not found");

        if(!nookUtil.waitForScreenModel(ScreenModel.MY_SHELVES, Constants.DEFAULT_TIMEOUT)) {
            testManager.retest("My Shelves screen was not loaded");
        }

        String newShelfName = testManager.getRandomShelfName();
        initMyShelvesScreen();

        ArrayList<Element> shelves = myShelvesScreen.getProducts(1, Constants.DEFAULT_TIMEOUT);
        if(shelves == null || shelves.size() == 0 )
            testManager.retest("Shelf '" + newShelfName + "' was not found");
        Element shelf = null;
        for(Element element : shelves) {
            iDevice.i("Element: " + element.getName());
            if(element.getName().toLowerCase().contains(newShelfName.toLowerCase())) {
                shelf = element;
                break;
            }
        }

        if(shelf == null)
            testManager.failTest("New Shelf with name '" + newShelfName + "' is not created");

        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    @PreCondition(preConditions = {Condition.LOGIN, Condition.OPEN_SCREEN}, // todo CREATE_SHELF
            screenModel = ScreenModel.MY_SHELVES,
            screenTitle = Constants.Library.Menu.MY_SHELVES,
            testId = 436029,
            testTitle = "Edit/remove a Stack [bnauto]")
    public void testCase436029() throws TestException {
        if(!nookUtil.waitForScreenModel(ScreenModel.MY_SHELVES, Constants.DEFAULT_TIMEOUT)) {
            testManager.retest("My Shelves screen was not loaded");
        }
        initMyShelvesScreen();

        ArrayList<Element> products = myShelvesScreen.getProducts(1, 5000);
        if(products == null || products.size() == 0 )
            testManager.retest("Shelf was not found");
        Element firstElement = products.get(0);
        if(!scroller.scrollToVisible(firstElement))
            testManager.retest("Can not scrollable to shelf '" + firstElement.getName() + "'");

        iDevice.i("Select shelf " + firstElement.getName());
        testManager.addStep("Select shelf " + firstElement.getName());
        clicker.clickOnElement(firstElement);

        myShelvesScreen.editShelf(true);
        myShelvesScreen.renameShelf(true);
        myShelvesScreen.removeShelf(true);
        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    @PreCondition(preConditions = {Condition.LOGIN, Condition.ADD_PROFILE},
            testId = 436030,
            profileName = Constants.ProfileScreen.PROFILE_NAME,
            isChildProfile = false,
            testTitle = "Profiles - swap between profiles [bnauto]")
    public void testCase436030() throws TestException {
        initProfileScreen();
        String nameOfChildProfile = Constants.ProfileScreen.PROFILE_NAME;

        //profile B
        String oldNameOfProfile = preparer.changeProfile(nameOfChildProfile);

        preparer.openHamburgerMenuFromAnyScreen();
        if(!preparer.waitWhileHamburgerMenuOpened(Constants.DEFAULT_TIMEOUT))
            testManager.retest("Hamburger menu not opened");

        if(!profileScreen.isProfileNameFromHamburgerMenu(nameOfChildProfile)) {
            testManager.failTest(nameOfChildProfile + " profile was not shown in hamburger menu");
        }

        //Main profile
        preparer.changeProfile(oldNameOfProfile);

        preparer.openHamburgerMenuFromAnyScreen();
        if(!preparer.waitWhileHamburgerMenuOpened(Constants.DEFAULT_TIMEOUT))
            testManager.retest("Hamburger menu not opened");

        if(!profileScreen.isProfileNameFromHamburgerMenu(oldNameOfProfile)) {
            testManager.failTest(oldNameOfProfile + " profile was not shown in hamburger menu");
        }

        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    private void expectedResult435993() throws TestException {
        Element profiles = waiter.waitForElementByNameVisible("PROFILES:", Constants.DEFAULT_TIMEOUT, new IConfig().setMatcher(Matcher.ContainsIgnoreCase).setMaxLevelOfElementsTree(3));
        if(profiles == null) {
            testManager.failTest("profile not found");
        }
    }

    private boolean expectedResultFor435986() throws TestException {
        long startTime = System.currentTimeMillis();
        long start = 0;
        while (alertTitles.size() == 0) {
            if(System.currentTimeMillis() - start > 1000) {
                iDevice.i("alert elements size: " + alertTitles.size());
                start = System.currentTimeMillis();
            }
            if(System.currentTimeMillis() - startTime > Constants.DEFAULT_TIMEOUT * 2) {
                testManager.failTest("profile alert is not found");
                return false;
            }
        }

        iDevice.sleep(1000);

        if(expectedResultIndex[0] < 4) {
            testManager.failTest("Expected:" + Arrays.toString(expected.toArray()) + "\n" +
                    "actual: " + Arrays.toString(alertTitles.toArray()));
            return false;
        }
        return true;
    }

    private void setupAlertCalBack() {
        expected = new ArrayList<String>();
        expectedResultIndex = new int[]{0};
        expected.add("NOOK Now Supports Profiles".toLowerCase());
        expected.add("Since you have a child profile on your account, do you want to set a passcode to prevent him/her from switching to adult profiles?".toLowerCase());
        expected.add("Later".toLowerCase());
        expected.add("Yes".toLowerCase());

        alertTitles = new ArrayList<String>();
        iDevice.setAlertHandler(new AlertCallBack() {
            @Override
            public void handleAlert(ArrayList<Element> arrayList) {
                String name;
                for (Element element : arrayList) {
                    name = element.getName().toLowerCase();
                    if (!name.equals("null") && !name.equals("empty list")) alertTitles.add(name);
                    if (expected.contains(name)) {
                        expectedResultIndex[0]++;
                    }
                }
            }
        });
    }

    private void testCase435985ExpectedResult1() throws TestException {
        Element textField = waiter.waitForElementByClassExists(UIAElementType.UIATextField, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(textField == null) {
            testManager.failTest("login text field is not found");
        }
        Element secureTexField = waiter.waitForElementByClassExists(UIAElementType.UIASecureTextField, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(secureTexField == null) {
            testManager.failTest("password secure text field is not found");
        }
        Element forgotPassword = waiter.waitForElementByNameExists("forgot your password", 1, new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase));
        if(forgotPassword == null) {
            testManager.failTest("forgot password is not found");
        }
        Element signIn = waiter.waitForElementByNameExists("Sign In", 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(signIn == null) {
            testManager.failTest("Sign In is not found");
        }
        Element signInFb = waiter.waitForElementByNameVisible("Sign In With Facebook", 1, new IConfig().setMaxLevelOfElementsTree(3));
        if(signInFb == null) {
            testManager.failTest("Sign In With Facebook is not found");
        }
        Element signInGp = waiter.waitForElementByNameVisible("Sign In With Google+", 1, new IConfig().setMaxLevelOfElementsTree(3));
        if(signInGp == null) {
            testManager.failTest("Sign In With Google+ is not found");
        }
        testManager.testCaseInfo.setStatusId(1);
    }

    private void clickChoseCountryAndScroll(boolean isScrollDown) throws TestException {
        Element chooseCountry;
        Element picker;
        String prevValue = "";
        while (true) {
            chooseCountry = waiter.waitForElementByNameVisible("My Country is", Constants.DEFAULT_TIMEOUT, new IConfig().setMatcher(Matcher.ContainsIgnoreCase).setMaxLevelOfElementsTree(2));

            if(chooseCountry == null) {
                testManager.retest("can not find button choose country");
            }

            testManager.addStep("click choose country");

            if(!clicker.clickOnElement(chooseCountry)) {
                testManager.retest("can not click to choose country");
            }

            picker = waiter.waitForElementByClassExists(UIAElementType.UIAPickerWheel, Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(3));
            iDevice.i("picker name: " + picker.getValue());


            testManager.addStep("scroll " + (isScrollDown ? " down" : "up") + "inside picker");
            if(isScrollDown)
                scroller.scrollDownInsideElement(picker, 0.1, 1);
            else
                scroller.scrollUpInsideElement(picker, 0.1, 1);

            if(!availableCountries.contains(picker.getValue()))
                availableCountries.add(picker.getValue());
            if(prevValue.equals(picker.getValue()))
                break;
            prevValue = picker.getValue();
        }
    }

    //C436012	DRP: download
    @PreCondition(preConditions = {Condition.LOGIN, Condition.OPEN_SCREEN},
            testId = 436012,
            testTitle = "DRP: download [bnauto]")
    public void testCase436012() throws TestException {
        initLibraryScreen();
        libraryScreen.changeFilter(LibraryScreen.FilterItems.ALL_ITEMS);
        libraryScreen.searchProduct(testManager.getDrpMagazine());
        nookUtil.waitForScreenModel(ScreenModel.SEARCH, Constants.DEFAULT_TIMEOUT);
        initSearchScreen();
        Element product = searchScreen.findNotDownloadedProduct();
        if(product == null) {
            testManager.retest("not downloaded product is not found");
        }
        if(!searchScreen.downloadProduct(product)) {
            testManager.failTest(testManager.getDrpMagazine() + " product is not downloaded");
        }
        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    /*
    C436013	DRP:open
     */
    @PreCondition(preConditions = {Condition.LOGIN, Condition.OPEN_SCREEN},
            testId = 436013,
            testTitle = "DRP:open [bnauto]")
    public void testCase436013() throws TestException {
        initLibraryScreen();
        libraryScreen.changeFilter(LibraryScreen.FilterItems.ALL_ITEMS);
        libraryScreen.searchProduct(testManager.getDrpMagazine());
        nookUtil.waitForScreenModel(ScreenModel.SEARCH, Constants.DEFAULT_TIMEOUT);
        initSearchScreen();
        ArrayList<Element> searchProducts = searchScreen.getSearchResults(1, Constants.DEFAULT_TIMEOUT);
        iDevice.i("searchProducts: " + searchProducts.size());
        for (Element element : searchProducts) {
            if(element.getName().toLowerCase().contains(testManager.getDrpMagazine().toLowerCase()) &&
                    !element.getName().toLowerCase().contains(Constants.Search.DOWNLOADING)) {
                testManager.addStep("Click to open on " + testManager.getDrpMagazine());
                clicker.clickOnElement(element);
                if(!nookUtil.waitForScreenModel(ScreenModel.DRP_READER, Constants.DEFAULT_TIMEOUT, false)) {
                    iDevice.i("CURRENT SCREEN MODEL: " + nookUtil.screenModel.name());
                    testManager.failTest("product " + testManager.getDrpMagazine() + " was not opened");
                }
                testManager.testCaseInfo.setStatusId(Status.PASSED);
                return;
            }
        }
        testManager.retest("necessary downloaded product " + testManager.getDrpMagazine() + " was not found");
        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    /*
    C436014	DRP:Table of Contents
     */
    @PreCondition(preConditions = {Condition.LOGIN, Condition.OPEN_PRODUCT},
            productName = ConfigParam.DRP_MAGAZINE,
            productType = ScreenModel.DRP_READER,
            testId = 436014,
            testTitle = "DRP:Table of Contents")
    public void testCase436014() throws TestException {
//        iDevice.sleep(5000);
        drpReaderScreen = new DrpReaderScreen(testManager, testHelper, paramsParser, iDevice);
        if(!drpReaderScreen.openReaderMenu())
            testManager.retest("Reader menu was not opened");
        takeScreenShot("Drp menu opened");
        testManager.addStep("Check if tabs present : \n1. Contents \n2Brightness");
        String sliderPercentBeforeOpenNewPage = drpReaderScreen.getSliderPercent();
        if (waiter.waitForElementByNameVisible(Constants.Reader.Drp.CONTENTS_BOOKMARKS, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase)) == null){
            testManager.failTest("There is not Contents element");
        }
        if(waiter.waitForElementByNameVisible(Constants.Reader.Drp.BRIGHTNESS, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase)) == null){
            testManager.failTest("There is not Brightness element");
        }
        testManager.addStep("Contents and Brightness elements present");
        takeScreenShot("Contents and Brightness elements present");
        if(!drpReaderScreen.openContents())
            testManager.retest("Contents was not opened");
        testManager.addStep("Check if Table of Contents shows");
        drpReaderScreen.openContentByNumber(1);
        takeScreenShot("After click on second page in contents");
        drpReaderScreen.openReaderMenu();
        String sliderPercentAfterOpenNewPage = drpReaderScreen.getSliderPercent();
        testManager.addStep("Check if page changed after click");
        if (sliderPercentBeforeOpenNewPage.equals(sliderPercentAfterOpenNewPage))
            testManager.retest("Page was not changed");
        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    /*
    C436015	DRP: Article view
     */
    @PreCondition(preConditions = {Condition.LOGIN, Condition.OPEN_PRODUCT},
            productName = ConfigParam.DRP_MAGAZINE,
            productType = ScreenModel.DRP_READER,
            testId = 436015,
            testTitle = "DRP: Article view")
    public void testCase436015() throws TestException {
        drpReaderScreen = new DrpReaderScreen(testManager, testHelper, paramsParser, iDevice);
        if(!drpReaderScreen.openReaderMenu())
            testManager.retest("Reader menu was not opened");
        takeScreenShot("Drp menu opened");
        if(!drpReaderScreen.openContents())
            testManager.retest("Contents was not opened");
        drpReaderScreen.openContentByNumber(2);
        Element articleViewBtn = waiter.waitForElementByNameVisible(Constants.Reader.Drp.ARTICLE_VIEW, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase));
        if(articleViewBtn == null){
            testManager.retest("Article View button was not found");
        }
        clicker.clickOnElement(articleViewBtn);
        testManager.addStep("Click on article view button");
        if (waiter.waitForElementByNameVisible(Constants.Reader.Drp.ARTICLE_VIEW_PAGE, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMaxLevelOfElementsTree(3).setMatcher(Matcher.ContainsIgnoreCase)) == null)
            testManager.failTest("Article View page was not opened");
        testManager.addStep("Article view page opened");
        takeScreenShot("Article view page opened");
        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    /*
    C439472	DRP: Article view -- Change font style
     */
    @PreCondition(preConditions = {Condition.LOGIN, Condition.OPEN_PRODUCT},
            productName = ConfigParam.DRP_MAGAZINE,
            productType = ScreenModel.DRP_READER,
            testId = 439472,
            testTitle = "DRP: Article view -- Change font style")
    public void testCase439472() throws TestException {
        iDevice.i("PATH: " + ParamsParser.getInstance().getPathToResultsFolder());
        drpReaderScreen = new DrpReaderScreen(testManager, testHelper, paramsParser, iDevice);
        if(!drpReaderScreen.openArticleView())
            testManager.retest("Article View page was not opened");
        if (!drpReaderScreen.openReaderMenu())
            testManager.retest("Reader menu was not opened");

        testManager.addStep("Check if tabs present : \n1. Contents \n2 \n3Brightness");
        if (waiter.waitForElementByNameVisible(Constants.Reader.Drp.CONTENTS_BOOKMARKS, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase)) == null){
            testManager.failTest("There is not Contents element");
        }
        if(waiter.waitForElementByNameVisible(Constants.Reader.Drp.BRIGHTNESS, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase)) == null){
            testManager.failTest("There is not Brightness element");
        }
        if(waiter.waitForElementByNameVisible(Constants.Reader.Drp.TEXT, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase)) == null){
            testManager.failTest("There is not Text element");
        }
        //change text
        String nameBeforeChangeSize = "text_before_change_text";
        String nameAfterChangeSize = "text_after_change_text";
        String nameAfterChangeSize2 = "text_after_change_text_2";
        String image1 = takeScreenShot(nameBeforeChangeSize);
        if(!drpReaderScreen.openReaderMenu())
            testManager.retest("Reader menu not opened");
        drpReaderScreen.chooseSize(Constants.Reader.TextOptions.Size.LARGE_FONT_BUTTON);
        iDevice.takeScreenShot("LARGE_FONT_BUTTON selected");
        iDevice.sleep(3000);
        String image2 = takeScreenShot(nameAfterChangeSize);
        testManager.addStep("Check screenshots before and after text size changes");
//        String image1 = ParamsParser.getInstance().getPathToResultsFolder() + nameBeforeChangeSize +".png";
//        String image2 = ParamsParser.getInstance().getPathToResultsFolder() + nameAfterChangeSize +".png";
        if (testManager.compareTwoImages(image1, image2))
            testManager.failTest("Text size was not changed to extra large");
        testManager.addStep("Text Size changed");
        //repeat
        if(!drpReaderScreen.openReaderMenu())
            testManager.retest("Reader menu not opened");
        drpReaderScreen.chooseSize(Constants.Reader.TextOptions.Size.EXTRA_SMALL_FONT_BUTTON);
        iDevice.takeScreenShot("LARGE_FONT_BUTTON selected");
        iDevice.sleep(3000);
        String image3 = takeScreenShot(nameAfterChangeSize2);
        testManager.addStep("Check screenshots before and after text size changes");
//        String image3 = ParamsParser.getInstance().getPathToResultsFolder() + nameAfterChangeSize2 +".png";
        if (testManager.compareTwoImages(image2, image3))
            testManager.failTest("Text size was not changed to extra small");
        testManager.addStep("Text Size changed");
        //font changes
        String imageBeforeChangeFont = takeScreenShot("before_change_font");
        if(!drpReaderScreen.openReaderMenu())
            testManager.retest("Reader menu not opened");
        drpReaderScreen.chooseFont(Constants.Reader.TextOptions.Font.GILL_SANS);
        String imageAfterChangeFont = takeScreenShot("after_change_font");
        testManager.addStep("Check screenshots before and after font was changed");
        if (testManager.compareTwoImages(imageBeforeChangeFont, imageAfterChangeFont))
            testManager.failTest("Font was not changed");
        testManager.addStep("Text font changed");
        //theme changes
        String imageBeforeChangeTheme = takeScreenShot("before_change_theme");
        if(!drpReaderScreen.openReaderMenu())
            testManager.retest("Reader menu not opened");
        drpReaderScreen.chooseTheme(Constants.Reader.TextOptions.Theme.GRAY);
        String imageAfterChangeTheme = takeScreenShot("after_change_theme");
        testManager.addStep("Check screenshots before and after theme was changed");
        if (testManager.compareTwoImages(imageBeforeChangeTheme, imageAfterChangeTheme))
            testManager.failTest("Theme was not changed");
        testManager.addStep("Text Theme changed");
        // change line spacing
        String imageBeforeChangeSpacing = takeScreenShot("before_change_spacing");
        if(!drpReaderScreen.openReaderMenu())
            testManager.retest("Reader menu not opened");
        drpReaderScreen.changeLineSpacing(EpubReaderScreen.LineSpacing.SINGLE_LINE_SPACING);
        String imageAfterChangeSpacing = takeScreenShot("after_change_spacing");
        testManager.addStep("Check screenshots before and after spacing was changed");
        if (testManager.compareTwoImages(imageBeforeChangeSpacing, imageAfterChangeSpacing))
            testManager.failTest("Spacing was not changed");
        testManager.addStep("Text Spacing changed");
        //change margin
        String imageBeforeChangeMargin = takeScreenShot("before_change_margin");
        if(!drpReaderScreen.openReaderMenu())
            testManager.retest("Reader menu not opened");
        drpReaderScreen.chooseMargin(Constants.Reader.TextOptions.Margins.MARGIN_2_BUTTON);
        String imageAfterChangeMargin = takeScreenShot("after_change_margin");
        testManager.addStep("Check screenshots before and after margin was changed");
        if (testManager.compareTwoImages(imageBeforeChangeMargin, imageAfterChangeMargin))
            testManager.failTest("Margin was not changed");
        testManager.addStep("Text Margin changed");
        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }


    /*
    C436016	DRP:Page view
     */
    @PreCondition(preConditions = {Condition.LOGIN, Condition.OPEN_PRODUCT},
            productName = ConfigParam.DRP_MAGAZINE,
            productType = ScreenModel.DRP_READER,
            testId = 436016,
            testTitle = "DRP:Page view")
    public void testCase436016() throws TestException {
        drpReaderScreen = new DrpReaderScreen(testManager, testHelper, paramsParser, iDevice);
        if(!drpReaderScreen.openArticleView())
            testManager.retest("Article View page was not opened");
        Element closeBtn = waiter.waitForElementByNameVisible(Constants.Reader.Drp.CLOSE_BTN, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMaxLevelOfElementsTree(3).setMatcher(Matcher.ContainsIgnoreCase));
        testManager.addStep("Check if Close button present");
        if (closeBtn == null)
            testManager.failTest("Close button was not found");
        takeScreenShot("Close button present in Article View");
        clicker.clickOnElement(closeBtn);
        testManager.addStep("Click on Close button");
        if (!waiter.waitForElementByNameGone(Constants.Reader.Drp.CLOSE_BTN, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMaxLevelOfElementsTree(3).setMatcher(Matcher.ContainsIgnoreCase)))
            testManager.failTest("Article View was not closed");
        testManager.addStep("Article View closed");
        takeScreenShot("Article View closed");
        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    /*
    C436018	DRP:double tap zoom in zoom out
     */
    @PreCondition(preConditions = {Condition.LOGIN, Condition.OPEN_PRODUCT},
            productName = ConfigParam.DRP_MAGAZINE,
            productType = ScreenModel.DRP_READER,
            testId = 436018,
            testTitle = "DRP:double tap zoom in zoom out")
    public void testCase436018() throws TestException {
        drpReaderScreen = new DrpReaderScreen(testManager, testHelper, paramsParser, iDevice);
        if(!drpReaderScreen.hideReaderMenu())
            testManager.retest("Reder menu opened");
        int[] screenSize = iDevice.getScreenSize();
        String defaultScreenState = takeScreenShot("default_state");
        clicker.doubleClickByXY(screenSize[0] / 2, screenSize[1] / 2);
        testManager.addStep("Double click");
        iDevice.sleep(2000);
        String zoomIn = takeScreenShot("zoom_in");
        iDevice.sleep(3000);
        testManager.addStep("Check if screen zommed in");
        if (testManager.compareTwoImages(defaultScreenState, zoomIn))
            testManager.failTest("Screen was not zoomed in");
        testManager.addStep("Screen zoomed in");
        takeScreenShot("Screen zoomed in");
        clicker.doubleClickByXY(screenSize[0] / 2, screenSize[1] / 2);
        testManager.addStep("Double click");
        iDevice.sleep(2000);
        String zoomOut = takeScreenShot("zoomed_out");
        iDevice.sleep(3000);
        testManager.addStep("Check if screen zommed out");
        if (testManager.compareTwoImages(zoomIn, zoomOut))
            testManager.failTest("Screen was not zoomed out");
        testManager.addStep("Screen zoomed out");
        takeScreenShot("Screen zoomed out");
        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    /*
    C436019	DRP:scrubber navigation
     */
    @PreCondition(preConditions = {Condition.LOGIN, Condition.OPEN_PRODUCT},
            productName = ConfigParam.DRP_MAGAZINE,
            productType = ScreenModel.DRP_READER,
            testId = 436018,
            testTitle = "DRP:scrubber navigation")
    public void testCase436019() throws TestException {
        drpReaderScreen = new DrpReaderScreen(testManager, testHelper, paramsParser, iDevice);
        if(!drpReaderScreen.openReaderMenu())
            testManager.failTest("Reder menu was not opened");
        int percentScrubberBefore = Integer.parseInt(drpReaderScreen.getSliderPercent());
        testManager.addStep("Remember percent of scrubber : " + percentScrubberBefore);
        Element slider = waiter.waitForElementByNameVisible(Constants.Reader.Drp.PAGE_SLIDER, Constants.DEFAULT_TIMEOUT);
        if (slider == null)
            testManager.retest("Slider was not found");
        double pointToDrag = percentScrubberBefore <= 50 ? 0.85 : 0.25;
        if (!drpReaderScreen.dragToValue(pointToDrag))
            testManager.retest("Error in drag method");
        iDevice.sleep(1000);
        testManager.addStep("Drag slider to " + pointToDrag * 100 + " percent");
        takeScreenShot("After drag slider to " + pointToDrag * 100 + " percent");
        testManager.addStep("Check if slider changed");
        int percentScrubberAfter = Integer.parseInt(drpReaderScreen.getSliderPercent());
        if(percentScrubberBefore < 50){
            if (percentScrubberAfter < 50)
                testManager.failTest("Scrubber was not changed");
        }else if (percentScrubberAfter > 50)
            testManager.failTest("Scrubber was not changed");
        testManager.addStep("Scrubber moved");
        if(!drpReaderScreen.openPageFromSlider())
            testManager.failTest("Page not opened");
        testManager.addStep("Page opened");
        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    /*
    C436021	DRP: bookmark
     */
    @PreCondition(preConditions = {Condition.LOGIN, Condition.OPEN_PRODUCT, Condition.REMOVE_BOOKMARKS},
            productName = ConfigParam.DRP_MAGAZINE,
            productType = ScreenModel.DRP_READER,
            testId = 436021,
            testTitle = "DRP: bookmark")
    public void testCase436021() throws TestException {
        drpReaderScreen = new DrpReaderScreen(testManager, testHelper, paramsParser, iDevice);
        if(!drpReaderScreen.hideReaderMenu())
            testManager.retest("Reader menu opened");
        testManager.addStep("Check if Add_Bookmark button present");
        if (waiter.waitForElementByNameExists(Constants.Reader.Drp.ADD_BOOKMARK, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMatcher(Matcher.ContainsIgnoreCase)) == null)
            testManager.failTest("Add bookmark (+) button was not found");
        testManager.addStep("Add Bookmark (+) button present");
        takeScreenShot("Add Bookmark (+) button present");
        if(!drpReaderScreen.addBookmark())
            testManager.failTest("Add bookmark failed");
        testManager.addStep("Click on Add bookmark (+) button");
        takeScreenShot("Add bookmark clicked");
        if (!drpReaderScreen.openReaderMenu())
            testManager.retest("Reader menu was not opened");
        if (!drpReaderScreen.openContents())
            testManager.retest("Contents was not opened");
        if (!drpReaderScreen.openBookmarkTab())
            testManager.retest("Bookmark tab was not opened");
        if (drpReaderScreen.isBookmarkTableEmpty())
            testManager.failTest("The page with bookmark do not shown in the Bookmarks tab");
        testManager.addStep("Bookmark tab shows in table");
        takeScreenShot("Bookmark tab shows in table");
        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    /*
    C436022	DRP:swipe forward a few pages
     */
    @PreCondition(preConditions = {Condition.LOGIN, Condition.OPEN_PRODUCT, Condition.REMOVE_BOOKMARKS},
            productName = ConfigParam.DRP_MAGAZINE,
            productType = ScreenModel.DRP_READER,
            testId = 436022,
            testTitle = "DRP:swipe forward a few pages")
    public void testCase436022() throws TestException {
        drpReaderScreen = new DrpReaderScreen(testManager, testHelper, paramsParser, iDevice);
        if(!drpReaderScreen.hideReaderMenu())
            testManager.retest("Reader menu opened");
        if(!drpReaderScreen.addBookmark())
            testManager.retest("Add bookmark failed");
        takeScreenShot("Add bookmark clicked");
        drpReaderScreen.swipePage(Constants.SwipeSide.LEFT);
        iDevice.sleep(1500);
        testManager.addStep("Swipe forward");
        takeScreenShot(" after swipe forward");
        if(waiter.waitForElementByNameExists(Constants.Reader.Drp.REMOVE_BOOKMARK, 5000,
                new IConfig().setMatcher(Matcher.ContainsIgnoreCase)) != null)
            testManager.failTest("Page was not scrolled");
        testManager.addStep("Page scrolled");
        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    /*
    C436023	DRP:swipe backward a few pages
     */
    @PreCondition(preConditions = {Condition.LOGIN, Condition.OPEN_PRODUCT, Condition.REMOVE_BOOKMARKS},
            productName = ConfigParam.DRP_MAGAZINE,
            productType = ScreenModel.DRP_READER,
            testId = 436023,
            testTitle = "DRP:swipe backward a few pages")
    public void testCase436023() throws TestException {
        drpReaderScreen = new DrpReaderScreen(testManager, testHelper, paramsParser, iDevice);
        if(!drpReaderScreen.hideReaderMenu())
            testManager.retest("Reader menu opened");
        if(!drpReaderScreen.addBookmark())
            testManager.retest("Add bookmark failed");
        takeScreenShot("Add bookmark clicked");
        drpReaderScreen.swipePage(Constants.SwipeSide.RIGHT);
        iDevice.sleep(1500);
        testManager.addStep("Swipe backward");
        takeScreenShot(" after swipe backward");
        if(waiter.waitForElementByNameExists(Constants.Reader.Drp.REMOVE_BOOKMARK, 5000,
                new IConfig().setMatcher(Matcher.ContainsIgnoreCase)) != null)
            testManager.failTest("Page was not scrolled");
        testManager.addStep("Page scrolled");
        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    //C436024	Comic book - download
    @PreCondition(preConditions = {Condition.LOGIN, Condition.OPEN_SCREEN},
            testId = 436024,
            testTitle = "Comic book - download [bnauto]")
    public void testCase436024() throws TestException {
        initLibraryScreen();
        libraryScreen.searchProduct(testManager.getDrpComics());
        nookUtil.waitForScreenModel(ScreenModel.SEARCH, Constants.DEFAULT_TIMEOUT);
        initSearchScreen();
        Element product = searchScreen.findNotDownloadedProduct();
        if(product == null)
            testManager.retest("not downloaded product is not found");
        if(!searchScreen.downloadProduct(product))
            testManager.failTest(testManager.getDrpComics() + " product is not downloaded");
        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    /*
    C436025	Comic book - zoom view
     */
    @PreCondition(preConditions = {Condition.LOGIN, Condition.OPEN_PRODUCT},
            productName = ConfigParam.DRP_COMICS,
            productType = ScreenModel.DRP_READER,
            testId = 436025,
            testTitle = "Comic book - zoom view")
    public void testCase436025() throws TestException {
        drpReaderScreen = new DrpReaderScreen(testManager, testHelper, paramsParser, iDevice);
        String beforeZoom = takeScreenShot("zoom_view_before");
        if(!drpReaderScreen.openReaderMenu())
            testManager.retest("Reader menu was not opened");
        Element zoomView = waiter.waitForElementByNameVisible(Constants.Reader.Drp.ZOOM_VIEW_BTN, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase));
        if (zoomView == null)
            testManager.failTest("Zoom View button was not found");
        clicker.clickByXY(zoomView.getX() + zoomView.getWidth()/2, zoomView.getY() + zoomView.getHeight()/2);
        testManager.addStep("Click on Zoom View");
        iDevice.sleep(2000);
//        if (!waiter.waitForElementByNameGone(Constants.Reader.Drp.ZOOM_VIEW_BTN, 5000,
//                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase)))
        if(drpReaderScreen.isReaderMenuOpened())
            testManager.retest("Zoom view button was not gone");
        String afterZoom = takeScreenShot("zoom_view_after");
        if (testManager.compareTwoImages(beforeZoom, afterZoom))
            testManager.failTest("Comics zoom view was not activated");
        testManager.addStep("Zoom view activated");
        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }

    /*
    C436036	Deferred sign in: settings
     */
    @PreCondition(preConditions = {Condition.DEFERRED_SIGN_IN},
            testId = 436036,
            testTitle = "Deferred sign in: settings")
    public void testCase436036() throws TestException {
        Preparer preparer = new Preparer(iDevice, nookUtil, paramsParser);
        preparer.openScreen(Constants.SideMenu.SETTINGS);
        if(!nookUtil.waitForScreenModel(ScreenModel.SETTINGS, Constants.DEFAULT_TIMEOUT))
            testManager.retest("Settings screen was not opened");
        settingsScreen = new SettingsScreen(testManager, testHelper, paramsParser, iDevice);
        settingsScreen.checkThatDeferredOptions();
        testManager.testCaseInfo.setStatusId(Status.PASSED);
    }



    @PreCondition(preConditions = {Condition.NONE},
            productName = ConfigParam.DRP_MAGAZINE,
            productType = ScreenModel.DRP_READER,
            testId = 123456,
            testTitle = "demo")
    public void testCase123456() throws TestException {
        iDevice.sleep(15000);
        takeScreenShot("123");
        iDevice.sleep(1000);
        takeScreenShot("321");
        iDevice.sleep(5000);
        String image1 = ParamsParser.getInstance().getPathToResultsFolder() +"/123.png";
        String image2 = ParamsParser.getInstance().getPathToResultsFolder() +"321.png";
        testManager.compareTwoImages(image1, image2);
        iDevice.i("#######FINISH###########");
    }
}
