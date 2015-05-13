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
            TestManager.addStep("Open Reader menu");
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
            TestManager.addStep("Hide reader menu");
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
        TestManager.addStep("Click on menu button (Library)");
    }

    @Override
    public boolean openContents() {
        clicker.clickOnElement(getter.getElementByName(Constants.Reader.Drp.CONTENTS_BOOKMARKS, new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase)));
        TestManager.addStep("Click on Contents");
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
        TestManager.addStep("Click on row" + number + " page in content tipe");
    }

    @Override
    public boolean closeContents() {
        clicker.clickOnElement(getter.getElementByName(Constants.Reader.Drp.CONTENTS_BOOKMARKS, new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase)));
        TestManager.addStep("Click on Contents");
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
        TestManager.addStep("Click on article view button");
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
        TestManager.addStep("Click on Text button");
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
        TestManager.addStep("Click on screen");
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
        TestManager.addStep("Select " + sizeName);
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
        TestManager.addStep("change font to " + font);
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
        TestManager.addStep("change theme to " + theme);
        clicker.clickOnElement(element);
        TestManager.addStep("Select theme" + theme);
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
        TestManager.addStep("change line spacing to " + lineSpacing);
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
        TestManager.addStep("change line margin to " + margin);
        return closeTextOptions();
    }

    public void chooseMargin(String margin) throws TestException {
        if(!openTextOptions())
            testManager.retest("Text options not opened");
        Element element = waiter.waitForElementByNameExists(margin, 2, new IConfig().setMaxLevelOfElementsTree(3));
        if(element == null) testManager.retest("Button " + margin + " is not found");
        TestManager.addStep("change line margin to " + margin);
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
    public boolean addBookmark() {
        return false;
    }

    @Override
    public boolean removeBookmark() {
        return false;
    }

    @Override
    public boolean isBookmarkAdded() {
        return false;
    }

    @Override
    public int[] getCurrentPageInfo() {
        return null;
    }

    @Override
    public boolean openBookmarkTab() {
        return false;
    }

    @Override
    public boolean openContentTab() {
        return false;
    }

    @Override
    public boolean dragToValue(double value) throws TestException {
        return false;
    }

    public String getSliderPercent() throws TestException {
        Element slider = waiter.waitForElementByNameVisible(Constants.Reader.Drp.PAGE_SLIDER, Constants.DEFAULT_TIMEOUT);
        if (slider == null)
            testManager.retest("Slider was not found");
        String sliderPercent = slider.getValue();
        if (sliderPercent.isEmpty())
            testManager.retest("Slider page value is empty");
        sliderPercent = sliderPercent.replace("\\D", "");
        return sliderPercent;
    }
}
