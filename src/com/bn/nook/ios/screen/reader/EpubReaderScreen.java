package com.bn.nook.ios.screen.reader;

import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.bn.nook.ios.screen.ReaderScreen;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.config.IConfig;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.models.IDevice;
import com.sofment.testhelper.enums.Matcher;

/**
 * Created by avsupport on 5/5/15.
 */
public class EpubReaderScreen extends ReaderScreen {

    public EpubReaderScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        super(testManager, testHelper, paramsParser, iDevice);
    }

    @Override
    public boolean isReaderMenuOpened() {
        return waiter.waitForElementByNameVisible(Constants.Reader.Epub.CONTENTS, 1, new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase)) != null;
    }

    @Override
    public boolean openReaderMenu() {
        if(isReaderMenuOpened()) return true;
        int[] screenSize = iDevice.getScreenSize();
        TestManager.addStep(String.format("click on screen center to open reader menu [%s, %s]", screenSize[0]/2, screenSize[1]/2));
        clicker.clickByXY(screenSize[0]/2, screenSize[1]/2);
        return isReaderMenuOpened();
    }

    @Override
    public boolean hideReaderMenu() {
        if(!isReaderMenuOpened()) return true;
        int[] screenSize = iDevice.getScreenSize();
        clicker.clickByXY(screenSize[0]/2, screenSize[1]/2);
        return !isReaderMenuOpened();
    }

    @Override
    public boolean openContents() throws TestException {
        if(!isContentsOpened()) {
            openReaderMenu();
            Element element = waiter.waitForElementByNameVisible(Constants.Reader.Epub.CONTENTS, 1, new IConfig().setMaxLevelOfElementsTree(2));
            if(element == null) {
                testManager.retest("Contents button " + Constants.Reader.Epub.CONTENTS + " is not found");
            }
            TestManager.addStep("open contents");
            clicker.clickOnElement(element);
            return isContentsOpened();
        }
        return false;
    }

    @Override
    public boolean closeContents() throws TestException {
        if(isContentsOpened()) {
            Element element = waiter.waitForElementByNameVisible(Constants.Reader.Epub.Contents.CLOSE_BUTTON, 1, new IConfig().setMaxLevelOfElementsTree(2));
            if(element == null) {
                testManager.retest("close button " + Constants.Reader.Epub.Contents.CLOSE_BUTTON + " is not found");
            }
            TestManager.addStep("click on button " + Constants.Reader.Epub.Contents.CLOSE_BUTTON);
            clicker.clickOnElement(element);
        }
        return false;
    }

    @Override
    public boolean isContentsOpened() {
        return waiter.waitForElementByNameVisible(Constants.Reader.Epub.Contents.CONTENTS_TAB, 1, new IConfig().setMaxLevelOfElementsTree(2)) != null;
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

    @Override
    public boolean openTextOptions() throws TestException {
        if(isTextOptionsOpened()) {
            return true;
        }
        openReaderMenu();
        Element textOptions = waiter.waitForElementByNameVisible(Constants.Reader.Epub.TEXT_OPTIONS, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(textOptions == null) testManager.retest("button to open text options is not found");
        TestManager.addStep(String.format("open text options [%s]", Constants.Reader.Epub.TEXT_OPTIONS));
        return clicker.clickOnElement(textOptions) && isTextOptionsOpened();
    }

    @Override
    public boolean closeTextOptions() throws TestException {
        if(!isTextOptionsOpened()) {
            return true;
        }
        Element closeButton = waiter.waitForElementByNameVisible(Constants.Reader.Epub.TextOptions.CLOSE, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(closeButton == null) testManager.retest("button to close text options is not found");
        TestManager.addStep(String.format("close text options [%s]", Constants.Reader.Epub.TextOptions.CLOSE));
        return clicker.clickOnElement(closeButton) && !isTextOptionsOpened();
    }

    @Override
    public boolean isTextOptionsOpened() {
        return waiter.waitForElementByNameVisible(Constants.Reader.Epub.TextOptions.Size.SMALL_FONT, 1, new IConfig().setMaxLevelOfElementsTree(2)) != null;
    }

    @Override
    public boolean changeFontSize(int sizeIndex) throws TestException {
        openTextOptions();
        String fontName = "";
        switch (sizeIndex) {
            case FontSize.EXTRA_SMALL_FONT:
                fontName = Constants.Reader.Epub.TextOptions.Size.EXTRA_SMALL_FONT;
                break;
            case FontSize.EXTRA_LARGE_FONT:
                fontName = Constants.Reader.Epub.TextOptions.Size.EXTRA_LARGE_FONT;
                break;
            case FontSize.LARGE_FONT:
                fontName = Constants.Reader.Epub.TextOptions.Size.LARGE_FONT;
                break;
            case FontSize.MEDIUM_LARGE_FONT:
                fontName = Constants.Reader.Epub.TextOptions.Size.MEDIUM_LARGE_FONT;
                break;
            case FontSize.MEDIUM_SMALL_FONT:
                fontName = Constants.Reader.Epub.TextOptions.Size.MEDIUM_SMALL_FONT;
                break;
            case FontSize.SMALL_FONT:
                fontName = Constants.Reader.Epub.TextOptions.Size.SMALL_FONT;
                break;
            default:
                fontName = Constants.Reader.Epub.TextOptions.Size.MEDIUM_LARGE_FONT;
                break;
        }

        Element element = waiter.waitForElementByNameVisible(fontName, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(element == null) testManager.retest("Button " + fontName + " is not found");
        TestManager.addStep("change font size to " + fontName);
        return clicker.clickOnElement(element);
    }

    @Override
    public boolean changeFont(int sizeIndex) throws TestException {
        openTextOptions();
        String font = "";
        switch (sizeIndex) {
            case Font.CENTURY_SCHOOLBOOK:
                font = Constants.Reader.Epub.TextOptions.Font.CENTURY_SCHOOLBOOK;
                break;
            case Font.GEORGIA:
                font = Constants.Reader.Epub.TextOptions.Font.GEORGIA;
                break;
            case Font.ASCENDER_SANS:
                font = Constants.Reader.Epub.TextOptions.Font.ASCENDER_SANS;
                break;
            case Font.GILL_SANS:
                font = Constants.Reader.Epub.TextOptions.Font.GILL_SANS;
                break;
            case Font.TREBUCHET:
                font = Constants.Reader.Epub.TextOptions.Font.TREBUCHET;
                break;
            case Font.DUTCH:
                font = Constants.Reader.Epub.TextOptions.Font.DUTCH;
                break;
            default:
                font = Constants.Reader.Epub.TextOptions.Font.CENTURY_SCHOOLBOOK;
                break;
        }

        Element element = waiter.waitForElementByNameExists(font, 1, new IConfig().setMaxLevelOfElementsTree(3));
        if(element == null) testManager.retest("Button " + font + " is not found");
        TestManager.addStep("change font to " + font);
        return clicker.clickOnElement(element);
    }

    @Override
    public boolean changeTheme(int sizeIndex) throws TestException {
        openTextOptions();
        String theme = "";
        switch (sizeIndex) {
            case Theme.DAY:
                theme = Constants.Reader.Epub.TextOptions.Theme.DAY;
                break;
            case Theme.NIGHT:
                theme = Constants.Reader.Epub.TextOptions.Theme.NIGHT;
                break;
            case Theme.GRAY:
                theme = Constants.Reader.Epub.TextOptions.Theme.GRAY;
                break;
            case Theme.BUTTER:
                theme = Constants.Reader.Epub.TextOptions.Theme.BUTTER;
                break;
            case Theme.MOCHA:
                theme = Constants.Reader.Epub.TextOptions.Theme.MOCHA;
                break;
            case Theme.SEPIA:
                theme = Constants.Reader.Epub.TextOptions.Theme.SEPIA;
                break;
            default:
                theme = Constants.Reader.Epub.TextOptions.Theme.DAY;
                break;
        }

        Element element = waiter.waitForElementByNameExists(theme, 1, new IConfig().setMaxLevelOfElementsTree(3));
        if(element == null) testManager.retest("Button " + theme + " is not found");
        TestManager.addStep("change theme to " + theme);
        return clicker.clickOnElement(element);
    }

    @Override
    public boolean changeLineSpacing(int sizeIndex) throws TestException {
        openTextOptions();
        String lineSpacing = "";
        switch (sizeIndex) {
            case LineSpacing.SINGLE_LINE_SPACING:
                lineSpacing = Constants.Reader.Epub.TextOptions.LineSpacing.SINGLE_LINE_SPACING;
                break;
            case LineSpacing.ONE_AND_HALF_LINES_SPACING:
                lineSpacing = Constants.Reader.Epub.TextOptions.LineSpacing.ONE_AND_HALF_LINES_SPACING;
                break;
            case LineSpacing.MULTIPLE_LINES_SPACING:
                lineSpacing = Constants.Reader.Epub.TextOptions.LineSpacing.MULTIPLE_LINES_SPACING;
                break;
            default:
                lineSpacing = Constants.Reader.Epub.TextOptions.LineSpacing.ONE_AND_HALF_LINES_SPACING;
                break;
        }

        Element element = waiter.waitForElementByNameExists(lineSpacing, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(element == null) testManager.retest("Button " + lineSpacing + " is not found");
        TestManager.addStep("change line spacing to " + lineSpacing);
        return clicker.clickOnElement(element);
    }

    @Override
    public boolean changeMargin(int sizeIndex) throws TestException {
        openTextOptions();
        String margin = "";
        switch (sizeIndex) {
            case Margin.SMALL_MARGIN:
                margin = Constants.Reader.Epub.TextOptions.Margin.SMALL_MARGIN;
                break;
            case Margin.MEDIUM_MARGIN:
                margin = Constants.Reader.Epub.TextOptions.Margin.MEDIUM_MARGIN;
                break;
            case Margin.LARGE_MARGIN:
                margin = Constants.Reader.Epub.TextOptions.Margin.LARGE_MARGIN;
                break;
            default:
                margin = Constants.Reader.Epub.TextOptions.Margin.MEDIUM_MARGIN;
                break;
        }

        Element element = waiter.waitForElementByNameExists(margin, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(element == null) testManager.retest("Button " + margin + " is not found");
        TestManager.addStep("change line margin to " + margin);
        return clicker.clickOnElement(element);
    }

    @Override
    public boolean openInformation() throws TestException {
        if(isInformationOpened()) return  true;
        openReaderMenu();
        Element infoButton = waiter.waitForElementByNameVisible(Constants.Reader.Epub.INFORMATION, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(infoButton == null) testManager.retest(String.format("button to open product information is not found [%s]", Constants.Reader.Epub.INFORMATION));
        TestManager.addStep("click to open product information");
        return clicker.clickOnElement(infoButton) && isInformationOpened();
    }

    @Override
    public boolean closeInformation() throws TestException {
        if(!isInformationOpened()) return  true;
        Element infoButton = waiter.waitForElementByNameVisible(Constants.ProductDetails.BACK_BUTTON, 1, new IConfig().setMaxLevelOfElementsTree(3));
        if(infoButton == null) testManager.retest(String.format("button to back to book from product information screen is not found [%s]", Constants.ProductDetails.BACK_BUTTON));
        TestManager.addStep("click to open product information");
        return clicker.clickOnElement(infoButton) && !isInformationOpened();
    }

    @Override
    public boolean isInformationOpened() {
        return waiter.waitForElementByNameVisible(Constants.ProductDetails.MANAGE_BUTTON, 1, new IConfig().setMaxLevelOfElementsTree(3)) != null;
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
        if(isBookmarkAdded()) return true;
        Element element = waiter.waitForElementByNameVisible(Constants.Reader.Epub.ADD_BOOKMARK, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(element == null) {
            testManager.retest("add bookmark button is not found (" + Constants.Reader.Epub.ADD_BOOKMARK + ")");
        }
        TestManager.addStep("click on button add bookmark " + Constants.Reader.Epub.ADD_BOOKMARK);
        clicker.clickOnElement(element);
        return isBookmarkAdded();
    }

    @Override
    public boolean removeBookmark() throws TestException {
        if(!isBookmarkAdded()) return true;
        Element element = waiter.waitForElementByNameVisible(Constants.Reader.Epub.REMOVE_BOOKMARK, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(element == null) {
            testManager.retest("add bookmark button is not found (" + Constants.Reader.Epub.REMOVE_BOOKMARK + ")");
        }
        TestManager.addStep("click on button add bookmark " + Constants.Reader.Epub.REMOVE_BOOKMARK);
        clicker.clickOnElement(element);
        return !isBookmarkAdded();
    }

    @Override
    public boolean isBookmarkAdded() {
        return waiter.waitForElementByNameVisible(Constants.Reader.Epub.REMOVE_BOOKMARK, 1, new IConfig().setMaxLevelOfElementsTree(2)) != null;
    }

    @Override
    public int[] getCurrentPageInfo() {
        openReaderMenu();
        int[] pageInfo = new int[]{0, 0};
        Element element = waiter.waitForElementByNameVisible(Constants.Reader.Epub.PAGE_POSITION, 1, new IConfig().setMatcher(Matcher.ContainsIgnoreCase).setMaxLevelOfElementsTree(2));
        if(element == null) return pageInfo;
        String info = element.getValue();
        String[] splittedInfo = info.trim().split(Constants.Reader.Epub.PAGE_POSITION);
        if(splittedInfo.length == 2) {
            pageInfo[0] = Integer.parseInt(splittedInfo[0]);
            pageInfo[1] = Integer.parseInt(splittedInfo[1]);
        }
        return pageInfo;
    }

    @Override
    public boolean openBookmarkTab() throws TestException {
        openReaderMenu();
        openContents();
        Element element = waiter.waitForElementByNameVisible(Constants.Reader.Epub.Contents.BOOKMARKS_TAB, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(element == null) {
            testManager.retest("can not found " + Constants.Reader.Epub.Contents.BOOKMARKS_TAB);
        }
        TestManager.addStep("click on " + Constants.Reader.Epub.Contents.BOOKMARKS_TAB);
        return clicker.clickOnElement(element);
    }

    @Override
    public boolean openContentTab() throws TestException {
        openReaderMenu();
        openContents();
        Element element = waiter.waitForElementByNameVisible(Constants.Reader.Epub.Contents.CONTENTS_TAB, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(element == null) {
            testManager.retest("can not found " + Constants.Reader.Epub.Contents.CONTENTS_TAB);
        }
        TestManager.addStep("click on " + Constants.Reader.Epub.Contents.CONTENTS_TAB);
        return clicker.clickOnElement(element);
    }

    @Override
    public void removeAllBookmarks() throws TestException {

    }

    @Override
    public void swipePage(Constants.SwipeSide swipeSide) throws TestException {

    }

    @Override
    public boolean dragToValue(double value) throws TestException {
        openReaderMenu();
        Element slider = waiter.waitForElementByNameVisible(Constants.Reader.Epub.PAGE_SLIDER, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(slider == null) {
            testManager.retest(String.format("slider is not found: [%s]", Constants.Reader.Epub.PAGE_SLIDER));
            return false;
        }
        String progress = slider.getValue().replaceAll("%", "").trim();

        double progressValue = Double.parseDouble(progress)/(double)100;

        TestManager.addStep("drag to value: " + value);
        return drager.dragInsideElementWithOptions(slider, progressValue, 0.5, value, 0.5);
    }

    public static class FontSize {
        public static final int EXTRA_SMALL_FONT = 0;
        public static final int SMALL_FONT = 1;
        public static final int MEDIUM_SMALL_FONT = 2;
        public static final int MEDIUM_LARGE_FONT = 3;
        public static final int LARGE_FONT = 4;
        public static final int EXTRA_LARGE_FONT = 5;
    }

    public static class Font {
        public static final int CENTURY_SCHOOLBOOK = 0;
        public static final int GEORGIA = 1;
        public static final int ASCENDER_SANS = 2;
        public static final int GILL_SANS = 3;
        public static final int TREBUCHET = 4;
        public static final int DUTCH = 5;
    }

    public static class Theme {
        public static final int DAY = 0;
        public static final int NIGHT = 1;
        public static final int GRAY = 2;
        public static final int BUTTER = 3;
        public static final int MOCHA = 4;
        public static final int SEPIA = 5;
    }

    public static class LineSpacing {
        public static final int SINGLE_LINE_SPACING = 0;
        public static final int ONE_AND_HALF_LINES_SPACING = 1;
        public static final int MULTIPLE_LINES_SPACING = 2;
    }

    public static class Margin {
        public static final int SMALL_MARGIN = 0;
        public static final int MEDIUM_MARGIN = 1;
        public static final int LARGE_MARGIN = 2;
    }
}
