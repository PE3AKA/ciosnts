package com.bn.nook.ios.screen.reader;

import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.bn.nook.ios.screen.ReaderScreen;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.config.IConfig;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.elements.ElementQuery;
import com.sofment.testhelper.driver.ios.enams.UIAElementType;
import com.sofment.testhelper.driver.ios.models.IDevice;
import com.sofment.testhelper.enums.Matcher;
import org.omg.CORBA.CODESET_INCOMPATIBLE;

import java.util.ArrayList;

/**
 * Created by avsupport on 5/5/15.
 */
public class DrpReaderScreen extends ReaderScreen {
    public DrpReaderScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        super(testManager, testHelper, paramsParser, iDevice);
    }

    @Override
    public boolean isReaderMenuOpened() {
        return waiter.waitForElementByNameVisible(Constants.Reader.Drp.CONTENTS_BOOKMARKS, 1, new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase)) != null;
    }

    @Override
    public boolean openReaderMenu() {
        if(!isReaderMenuOpened()) {
            int[] screenSize = iDevice.getScreenSize();
            clicker.clickByXY(screenSize[0]/2, screenSize[1]/2);
            testManager.addStep("Open Reader menu");
            if (!isReaderMenuOpened())
                return false;
        }
        return true;
    }

    @Override
    public boolean hideReaderMenu() {
        if(isReaderMenuOpened()) {
            int[] screenSize = iDevice.getScreenSize();
            clicker.clickByXY(screenSize[0]/2, screenSize[1]/2);
            testManager.addStep("Hide reader menu");
            if (isReaderMenuOpened())
                return false;
        }
        return true;
    }

    public boolean waitWhileContentsMenuOpens(long timeout){
        return waiter.waitForElementByNameVisible(Constants.Reader.Drp.CONTENTS_TAB, timeout, new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase)) != null;
    }



    public void openHamburgerMenu() throws TestException {
        ElementQuery libraryBtnQuery = new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAToolBar,0).addElement(UIAElementType.UIAButton,Constants.Reader.Drp.LIBRARY_BTN);
        if(waiter.waitForElementVisible(Constants.DEFAULT_TIMEOUT, libraryBtnQuery) == null)
            testManager.retest("Menu button was not found");
        Element menuBtn =  getter.getElement(libraryBtnQuery);
        clicker.clickOnElement(menuBtn);
        testManager.addStep("Click on menu button (Library)");
    }

    @Override
    public boolean openContents() {
        clicker.clickOnElement(getter.getElementByName(Constants.Reader.Drp.CONTENTS_BOOKMARKS, new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase)));
        testManager.addStep("Click on Contents");
        if (!waitWhileContentsMenuOpens(Constants.DEFAULT_TIMEOUT)) {
            iDevice.i("Contents menu was not opened");
            return false;
        }
        takeScreenShot("Contents menu opened");
        return true;
    }

    public void openContentByNumber(int number) throws TestException {
        Element contentTable = waiter.waitForElementByClassExists(UIAElementType.UIATableView, Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(2));
        if (contentTable == null)
            testManager.failTest("Table Contents not found");
        ArrayList<Element> elements = getter.getElementChildren(contentTable);
        if (elements.size()==0)
            testManager.retest("No rows in table");
        if (number > elements.size())
            testManager.retest("Number bigger than rows. Rows in table: " + elements.size() + 1);
        clicker.clickOnElement(elements.get(number));
        testManager.addStep("Click on row" + number + " page in content tipe");
    }

    @Override
    public boolean closeContents() throws TestException {
        Element contentTab = getter.getElementByName(Constants.Reader.Drp.CONTENTS_TAB, new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase));
        if (contentTab == null)
            testManager.retest("Content tab is null");
        clicker.clickByXY(iDevice.getScreenSize()[0], contentTab.getY() - 50);
        testManager.addStep("Click on screen (higher tabs)");
        if (!waiter.waitForElementByNameGone(Constants.Reader.Drp.CONTENTS_TAB, Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase))){
            iDevice.i("Contents menu was not gone");
            return false;
        }
        takeScreenShot("Contents menu closed");
        return true;
    }

    @Override
    public boolean isContentsOpened() {
        return waiter.waitForElementByNameVisible(Constants.Reader.Drp.CONTENTS_TAB, 1, new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase)) != null;
    }

    @Override
    public boolean openFontSettings() throws TestException {
        return false;
    }

    @Override
    public boolean closeFontSettings() throws TestException {
        return false;
    }

    @Override
    public boolean isFontSettingsOpened() {
        return false;
    }

    public boolean isArticleViewPageOpened(){
        return waiter.waitForElementByNameVisible(Constants.Reader.Drp.ARTICLE_VIEW_PAGE, 1, new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase)) != null;
    }

    public boolean openArticleView() throws TestException {
        Element articleViewBtn = waiter.waitForElementByNameVisible(Constants.Reader.Drp.ARTICLE_VIEW, 5000,
                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase));
        if(articleViewBtn == null){
            if(!openReaderMenu())
                testManager.retest("Reader menu was not opened");
            if(!openContents())
                testManager.retest("Contents was not opened");
            openContentByNumber(2);
            articleViewBtn = waiter.waitForElementByNameVisible(Constants.Reader.Drp.ARTICLE_VIEW, Constants.DEFAULT_TIMEOUT,
                    new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase));
        }
        if (articleViewBtn == null)
            testManager.retest("articleViewBtn is null");
        clicker.clickOnElement(articleViewBtn);
        testManager.addStep("Click on article view button");
        if (waiter.waitForElementByNameVisible(Constants.Reader.Drp.ARTICLE_VIEW_PAGE, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMaxLevelOfElementsTree(3).setMatcher(Matcher.ContainsIgnoreCase)) == null)
            return false;
        return true;

    }

    @Override
    public boolean openTextOptions() throws TestException {
        Element textMenuBtn = waiter.waitForElementByNameVisible(Constants.Reader.Drp.TEXT, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase));
        if (textMenuBtn == null)
            testManager.retest("Text button was not found");
        clicker.clickOnElement(textMenuBtn);
        testManager.addStep("Click on Text button");
        return waiter.waitForElementByNameVisible(Constants.Reader.TextOptions.Size.EXTRA_SMALL_FONT_BUTTON, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMatcher(Matcher.ContainsIgnoreCase).setMaxLevelOfElementsTree(2)) != null;
    }

    @Override
    public boolean closeTextOptions() throws TestException {
        Element smallBtn = waiter.waitForElementByNameVisible(Constants.Reader.TextOptions.Size.EXTRA_SMALL_FONT_BUTTON, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase));
        if (smallBtn == null)
            testManager.retest("Text options page was not opened");
        clicker.clickByXY(iDevice.getScreenSize()[0] / 2, smallBtn.getY() - 100);
        testManager.addStep("Click on screen");
        return waiter.waitForElementByNameGone(Constants.Reader.TextOptions.Size.EXTRA_SMALL_FONT_BUTTON, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMatcher(Matcher.ContainsIgnoreCase).setMaxLevelOfElementsTree(2));
    }

    @Override
    public boolean isTextOptionsOpened() {
        return waiter.waitForElementByNameVisible(Constants.Reader.TextOptions.Size.EXTRA_SMALL_FONT_BUTTON, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMatcher(Matcher.ContainsIgnoreCase).setMaxLevelOfElementsTree(2)) != null;
    }

    public void chooseSize(String sizeName) throws TestException {
        if(!openTextOptions())
            testManager.retest("Text options not opened");
        Element sizeChoice = getter.getElementByName(sizeName, new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase));
        if (sizeChoice == null)
            testManager.retest(sizeName + " wan not found");
        clicker.clickOnElement(sizeChoice);
        testManager.addStep("Select " + sizeName);
        if(!closeTextOptions())
            testManager.retest("Text options not closed");
    }

    public boolean changeFontSize(int sizeIndex) {
        return false;
    }

    @Override
    public boolean changeFont(int sizeIndex) throws TestException {
        return false;
    }

    public void chooseFont(String font)throws TestException{
        if(!openTextOptions())
            testManager.retest("Text options not opened");
        Element element = waiter.waitForElementByNameExists(font, 1, new IConfig().setMaxLevelOfElementsTree(3));
        if(element == null) testManager.retest("Button " + font + " is not found");
        testManager.addStep("change font to " + font);
        clicker.clickOnElement(element);
        if(!closeTextOptions())
            testManager.retest("Text options not closed");
    }
    @Override
    public boolean changeTheme(int sizeIndex) throws TestException {
        return false;
    }

    public void chooseTheme(String theme) throws TestException {
        if(!openTextOptions())
            testManager.retest("Text options not opened");
        Element element = waiter.waitForElementByNameExists(theme, 1, new IConfig().setMaxLevelOfElementsTree(3));
        if(element == null) testManager.retest("Button " + theme + " is not found");
        testManager.addStep("change theme to " + theme);
        clicker.clickOnElement(element);
        testManager.addStep("Select theme" + theme);
        if(!closeTextOptions())
            testManager.retest("Text options not closed");
    }

    @Override
    public boolean changeLineSpacing(int sizeIndex) throws TestException {
        if(!openTextOptions())
            testManager.retest("Text options not opened");
        String lineSpacing = "";
        switch (sizeIndex) {
            case EpubReaderScreen.LineSpacing.SINGLE_LINE_SPACING:
                lineSpacing = Constants.Reader.TextOptions.LineSpacing.LEADING_1_BUTTON;
                break;
            case EpubReaderScreen.LineSpacing.ONE_AND_HALF_LINES_SPACING:
                lineSpacing = Constants.Reader.TextOptions.LineSpacing.LEADING_2_BUTTON;
                break;
            case EpubReaderScreen.LineSpacing.MULTIPLE_LINES_SPACING:
                lineSpacing = Constants.Reader.TextOptions.LineSpacing.LEADING_3_BUTTON;
                break;
            default:
                lineSpacing = Constants.Reader.Epub.TextOptions.LineSpacing.ONE_AND_HALF_LINES_SPACING;
                break;
        }

        Element element = waiter.waitForElementByNameExists(lineSpacing, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(element == null) testManager.retest("Button " + lineSpacing + " is not found");
        testManager.addStep("change line spacing to " + lineSpacing);
        clicker.clickOnElement(element);
        return closeTextOptions();
    }

    @Override
    public boolean changeMargin(int sizeIndex) throws TestException {
        if(!openTextOptions())
            testManager.retest("Text options not opened");
        String margin = "";
        switch (sizeIndex) {
            case EpubReaderScreen.Margin.SMALL_MARGIN:
                margin = Constants.Reader.Epub.TextOptions.Margin.SMALL_MARGIN;
                break;
            case EpubReaderScreen.Margin.MEDIUM_MARGIN:
                margin = Constants.Reader.Epub.TextOptions.Margin.MEDIUM_MARGIN;
                break;
            case EpubReaderScreen.Margin.LARGE_MARGIN:
                margin = Constants.Reader.Epub.TextOptions.Margin.LARGE_MARGIN;
                break;
            default:
                margin = Constants.Reader.Epub.TextOptions.Margin.MEDIUM_MARGIN;
                break;
        }

        Element element = waiter.waitForElementByNameExists(margin, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(element == null) testManager.retest("Button " + margin + " is not found");
        testManager.addStep("change line margin to " + margin);
        return closeTextOptions();
    }

    public void chooseMargin(String margin) throws TestException {
        if(!openTextOptions())
            testManager.retest("Text options not opened");
        Element element = waiter.waitForElementByNameExists(margin, 2, new IConfig().setMaxLevelOfElementsTree(3));
        if(element == null) testManager.retest("Button " + margin + " is not found");
        testManager.addStep("change line margin to " + margin);
        if(!closeTextOptions())
            testManager.retest("Text options not closed");
    }

    @Override
    public boolean openInformation() {
        return false;
    }

    @Override
    public boolean closeInformation() {
        return false;
    }

    @Override
    public boolean isInformationOpened() {
        return false;
    }

    @Override
    public boolean openBrightness() {
        return false;
    }

    @Override
    public boolean closeBrightness() {
        return false;
    }

    @Override
    public boolean isBrightnessOpened() {
        return false;
    }

    @Override
    public boolean openSearch() {
        return false;
    }

    @Override
    public boolean closeSearch() {
        return false;
    }

    @Override
    public boolean isSearchOpened() {
        return false;
    }

    @Override
    public boolean addBookmark() throws TestException {
        ///UIAApplication[1]/UIAWindow[1]/UIAScrollView[1]/UIAScrollView[2]/UIAButton[1]
        Element addBtn = waiter.waitForElementByNameExists(Constants.Reader.Drp.ADD_BOOKMARK, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMatcher(Matcher.ContainsIgnoreCase).setMaxLevelOfElementsTree(4));
        if (addBtn == null)
            testManager.retest("Add bookmark button was not found");
        clicker.clickByXY(addBtn.getX() + addBtn.getWidth()/2, addBtn.getY() + addBtn.getHeight()/2);
        testManager.addStep("Click on Add bookmark");
        return waiter.waitForElementByNameExists(Constants.Reader.Drp.REMOVE_BOOKMARK, Constants.DEFAULT_TIMEOUT, new IConfig().setMatcher(Matcher.ContainsIgnoreCase)) != null;
    }

    @Override
    public boolean removeBookmark() throws TestException {
        Element removeBtn = waiter.waitForElementByNameExists(Constants.Reader.Drp.REMOVE_BOOKMARK, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMatcher(Matcher.ContainsIgnoreCase));
        if (removeBtn == null)
            testManager.retest("Remove bookmark button was not found");
        clicker.clickByXY(removeBtn.getX() + removeBtn.getWidth()/2, removeBtn.getY() + removeBtn.getHeight()/2);
//        clicker.clickOnElement(removeBtn);
        testManager.addStep("Click on Add bookmark");
        return waiter.waitForElementByNameExists(Constants.Reader.Drp.ADD_BOOKMARK, Constants.DEFAULT_TIMEOUT, new IConfig().setMatcher(Matcher.ContainsIgnoreCase)) != null;
    }

    public void removeAllBookmarks() throws TestException {
        while (true){
            if (!openReaderMenu())
                testManager.retest("Reader menu was not opened");
            if (!openContents())
                testManager.retest("Contents was not opened");
            if (!openBookmarkTab())
                testManager.retest("Bookmark tab was not opened");
            if (isBookmarkTableEmpty()){
                if (!closeContents())
                    testManager.retest("Contents was not closed");
                if (!hideReaderMenu())
                    testManager.retest("Reader menu was not closed");
                return ;
            }
            if (!openPageFromBookMarkContent(0))
                testManager.retest("Error during open bookmark page");
            if(!removeBookmark())
                testManager.retest("Bookmark was not removed");
        }
    }

    @Override
    public void swipePage(Constants.SwipeSide swipeSide) throws TestException {
        Element element = waiter.waitForElementExists(1, new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAScrollView, 0));
        if (element == null)
            testManager.retest("Element is null");
        switch(swipeSide){
            case LEFT:
                drager.dragInsideElementWithOptions(element, 0.1, 0.5, 0.9, 0.5);
//                scroller.scrollLeftInsideElement(element, 0.1, 1);
                break;
            case RIGHT:
//                scroller.scrollRightInsideElement(element, 0.1, 1);
                drager.dragInsideElementWithOptions(element, 0.9, 0.5, 0.1, 0.5);
                break;
        }
    }

    public boolean openPageFromBookMarkContent(int index) throws TestException {
        Element table = waiter.waitForElementVisible(Constants.DEFAULT_TIMEOUT, new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIATableView, 0));
        if (table == null)
            testManager.retest("Table is null");
        ArrayList<Element> cells = getter.getElementChildren(table);
        if (cells.size() == 0) {
            iDevice.i("Table is empty");
            return false;
        }
        if (cells.size() < index){
            iDevice.i("Cell size < index");
            return false;
        }
        clicker.clickOnElement(cells.get(index));
        testManager.addStep("Click on " + index + " row");
        return waiter.waitForElementByNameGone(Constants.Reader.Drp.BOOKMARKS_TAB, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase));
    }

    public boolean isBookmarkTableEmpty(){
        return waiter.waitForElementByNameVisible(Constants.Reader.Drp.EMPTY_LIST, 1, new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase)) != null;
    }

    @Override
    public boolean isBookmarkAdded() {
        return waiter.waitForElementByNameVisible(Constants.Reader.Drp.REMOVE_BOOKMARK, Constants.DEFAULT_TIMEOUT, new IConfig().setMatcher(Matcher.ContainsIgnoreCase)) != null;
    }

    @Override
    public int[] getCurrentPageInfo() {
        return null;
    }

    @Override
    public boolean openBookmarkTab()throws TestException{
        if(!isContentsOpened())
            testManager.retest("Contents is not opened");
        Element bookmarkBtn = waiter.waitForElementByNameVisible(Constants.Reader.Drp.BOOKMARKS_TAB, Constants.DEFAULT_TIMEOUT,
                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase));
        if (bookmarkBtn == null)
            testManager.retest("Bookmark button was not found");
        if (isBookmarkTabOpened())
            return true;
        clicker.clickOnElement(bookmarkBtn);
        testManager.addStep("Click on Bookmark tab");
        iDevice.sleep(2000);
        return isBookmarkTabOpened();
    }

    public boolean isBookmarkTabOpened() throws TestException {
        Element bookmarkBtn = waiter.waitForElementByNameVisible(Constants.Reader.Drp.BOOKMARKS_TAB, 5000,
                new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase));
        if (bookmarkBtn == null)
            testManager.retest("Bookmark button was not found");
        if (bookmarkBtn.getValue() == null || bookmarkBtn.getValue().isEmpty())
            return false;
        if(bookmarkBtn.getValue().equals("1")) {
            takeScreenShot("Bookmark tab opened");
            return true;
        }
        return false;
    }

    @Override
    public boolean openContentTab() {
        return false;
    }

    public String getSliderPercent() throws TestException {
        Element slider = waiter.waitForElementByNameVisible(Constants.Reader.Drp.PAGE_SLIDER, Constants.DEFAULT_TIMEOUT);
        if (slider == null)
            testManager.retest("Slider was not found");
        String sliderPercent = slider.getValue();
        if (sliderPercent.isEmpty())
            testManager.retest("Slider page value is empty");
        sliderPercent = sliderPercent.replace("%", "");
        return sliderPercent;
    }

    public  boolean openPageFromSlider() throws TestException{
        if (!isReaderMenuOpened())
            testManager.retest("Reader menu is not opened");
        Element pagesList = waiter.waitForElementVisible(Constants.DEFAULT_TIMEOUT,
                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAScrollView, 1));
        if (pagesList == null)
            testManager.retest("PageList is null");
        ArrayList<Element> pages = getter.getElementChildrenByType(pagesList, UIAElementType.UIAStaticText);
        Element page = null;
        for (Element element: pages){
            if (element.getX() < 0 || element.getX() + element.getWidth() > iDevice.getScreenSize()[0])
                continue;
            page = element;
        }
        if (page == null)
            testManager.retest("Page is null");
            String pageNumber = page.getName();
        takeScreenShot("Before Click on " + pageNumber);
        iDevice.i("########################### page cord =========== X: " + page.getX() + "==========Y: " + page.getY());
//        clicker.clickOnElement(page);
        clicker.clickByXY(page.getX() + page.getWidth()/2, page.getY() + page.getHeight()/2);
        testManager.addStep("Click on " + pageNumber);
        iDevice.sleep(3000);
        takeScreenShot("After Click on " + pageNumber);
        return !isReaderMenuOpened();
    }
}
