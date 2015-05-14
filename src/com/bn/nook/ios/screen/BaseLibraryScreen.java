package com.bn.nook.ios.screen;

import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.config.IConfig;
import com.sofment.testhelper.driver.ios.config.IWaiterConfig;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.enams.SearchCondition;
import com.sofment.testhelper.driver.ios.enams.UIAElementType;
import com.sofment.testhelper.driver.ios.models.IDevice;
import com.sofment.testhelper.enums.Matcher;

import java.util.ArrayList;

/**
 * Created by avsupport on 5/13/15.
 */
public class BaseLibraryScreen extends BaseScreen {
    public BaseLibraryScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        super(testManager, testHelper, paramsParser, iDevice);
    }

    public boolean openMenu() throws TestException {
        Element menu = super.waitForMenuButton(Constants.DEFAULT_TIMEOUT);
        if(menu == null) {
            testManager.retest("menu button is not found");
        }
        TestManager.addStep("Click on menu button");
        clicker.clickOnElement(menu);
        return waiter.waitForElementByNameVisible(Constants.Library.Menu.MY_SHELVES, 5000, new IConfig().setMaxLevelOfElementsTree(3)) != null;
    }

    public void openMenu(int menuIndex) throws TestException {
        openMenu();
        clickOnMenuItem(menuIndex);
    }

    public ArrayList<Element> getProducts() throws TestException {
        Element collectionView = waiter.waitForElementByClassVisible(UIAElementType.UIACollectionView,
                Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(2));
        if(collectionView == null) {
            testManager.retest("collection view with search results is not found");
        }
        return getter.getElementChildren(collectionView);
    }


    public boolean clickOnMenuItem(int menuIndex) throws TestException {
        String name = "";
        Element tableView = waiter.waitForElementByNames(new IWaiterConfig().setMaxLevelOfElementsTree(2).setSearchCondition(SearchCondition.VALUE).addMatcher(Matcher.ContainsIgnoreCase), 1, "to 5 of 5", "to 6 of 6");
//        Element tableView = waiter.waitForElementByNameVisible("to 5 of 5", 1, new IConfig().setMaxLevelOfElementsTree(2).setSearchCondition(SearchCondition.VALUE).setMatcher(Matcher.ContainsIgnoreCase));
        switch (menuIndex) {
            case MenuItems.HOME:
                name = Constants.Library.Menu.HOME;
                break;
            case MenuItems.CURRENT_READ:
                name = Constants.Library.Menu.CURRENT_READ;
                break;
            case MenuItems.LIBRARY:
                name = Constants.Library.Menu.LIBRARY;
                break;
            case MenuItems.MY_SHELVES:
                name = Constants.Library.Menu.MY_SHELVES;
                break;
            case MenuItems.QUICK_READS:
                name = Constants.Library.Menu.QUICK_READS;
                break;
            case MenuItems.MESSAGES:
                name = Constants.Library.Menu.MESSAGES;
                break;
            case MenuItems.SETTINGS:
                name = Constants.Library.Menu.SETTINGS;
                break;
        }
        Element element = waiter.waitForElementByNameVisible(name, 5000, new IConfig().setMaxLevelOfElementsTree(1).setParentElement(tableView));
        if(element == null) {
            testManager.retest("Menu item " + name + " is not found");
        }
        TestManager.addStep("click on " + name);
        return clicker.clickOnElement(element);
    }

    public boolean searchProduct(String searchProduct) throws TestException {
        Element searchBar = openSearch();
        return inputText(searchProduct + "\n", searchBar);
    }

    public Element openSearch() throws TestException {
        Element toolbar = waiter.waitForElementByClassVisible(UIAElementType.UIAToolBar, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(toolbar == null) {
            testManager.retest("toolbar is not found");
        }

        Element search = waiter.waitForElementByNameVisible("Search NOOK", 1, new IConfig().setParentElement(toolbar).setMaxLevelOfElementsTree(1));
        if(search == null) {
            testManager.retest("search button is not found");
        }

        TestManager.addStep("Click on the search button");
        clicker.clickOnElement(search);

        Element searchBar = waiter.waitForElementByClassVisible(UIAElementType.UIASearchBar, 5000, new IConfig().setMaxLevelOfElementsTree(2));
        if(searchBar == null) {
            testManager.retest("search bar is not found");
        }
        return searchBar;
    }

    public static class MenuItems {
        public static final int HOME = 0;
        public static final int CURRENT_READ = 1;
        public static final int LIBRARY = 2;
        public static final int MY_SHELVES = 3;
        public static final int QUICK_READS = 4;
        public static final int MESSAGES = 5;
        public static final int SETTINGS = 6;
    }

    public static class FilterItems {
        public static final int NAN = -1;
        public static final int ALL_ITEMS = 0;
        public static final int BOOKS = 1;
        public static final int MAGAZINES = 2;
        public static final int NEWSPAPERS = 3;
        public static final int MY_FILES = 4;
        public static final int ARCHIVED = 5;
        public static final int EVERYTHING_ELSE = 6;
    }

    public static class SortItems {
        public static final int NAN = -1;
        public static final int RECENT = 0;
        public static final int TITLE = 1;
        public static final int AUTHOR = 2;
    }

    public boolean openSettings() throws TestException {
        return openMenu() && clickOnMenuItem(MenuItems.SETTINGS);
    }

    public boolean openFilter() throws TestException {
//        Element filterButton = waiter.waitForElementByNameExists(Constants.Library.FILTER, 5000, new IConfig().setMaxLevelOfElementsTree(4).setMatcher(Matcher.ContainsIgnoreCase));
//        if(filterButton == null) testManager.retest("Filter button is not found");
//        TestManager.addStep("click on filter button");
//        return clicker.clickOnElement(filterButton);
//        if(collection == null)
        collection = waitForCollection();
//        Element filterButton = waiter.waitForElementByNameExists(Constants.Library.SORT, 5000, new IConfig().setMaxLevelOfElementsTree(4).setMatcher(Matcher.ContainsIgnoreCase));
//        if(filterButton == null) testManager.retest("Sort button is not found");
//        TestManager.addStep("click on sort button");
//        return clicker.clickOnElement(filterButton);

        int x = collection.getX() + 50;
        int y = collection.getY() + collection.getHeight()/20;
        TestManager.addStep("open filter");
        return clicker.clickByXY(x, y);
    }

    public boolean chooseFilter(int filterIndex) throws TestException {
        String searchQuery = "";
        switch (filterIndex) {
            case FilterItems.ALL_ITEMS :
                searchQuery = Constants.Library.Filter.ALL_ITEMS;
                break;
            case FilterItems.BOOKS :
                searchQuery = Constants.Library.Filter.BOOKS;
                break;
            case FilterItems.MAGAZINES :
                searchQuery = Constants.Library.Filter.MAGAZINES;
                break;
            case FilterItems.NEWSPAPERS :
                searchQuery = Constants.Library.Filter.NEWSPAPERS;
                break;
            case FilterItems.MY_FILES :
                searchQuery = Constants.Library.Filter.MY_FILES;
                break;
            case FilterItems.ARCHIVED :
                searchQuery = Constants.Library.Filter.ARCHIVED;
                break;
            case FilterItems.EVERYTHING_ELSE :
                searchQuery = Constants.Library.Filter.EVERYTHING_ELSE;
                break;
        }

        Element necessaryFilter = waiter.waitForElementByNameExists(searchQuery, 1, new IConfig().setMaxLevelOfElementsTree(3));
        if(necessaryFilter == null) {
            testManager.retest(searchQuery + " is not found");
        }
        TestManager.addStep("click on the " + searchQuery);
        return clicker.clickOnElement(necessaryFilter);
    }

    private Element collection;

    public boolean openSort() throws TestException {
//        if(collection == null)
        collection = waitForCollection();
//        Element filterButton = waiter.waitForElementByNameExists(Constants.Library.SORT, 5000, new IConfig().setMaxLevelOfElementsTree(4).setMatcher(Matcher.ContainsIgnoreCase));
//        if(filterButton == null) testManager.retest("Sort button is not found");
//        TestManager.addStep("click on sort button");
//        return clicker.clickOnElement(filterButton);

        int x = collection.getX() + collection.getWidth() - 50;
        int y = collection.getY() + collection.getHeight()/20;

        return clicker.clickByXY(x, y);
    }

    public boolean chooseSort(int sortIndex) throws TestException {
        String searchQuery = "";
        switch (sortIndex) {
            case SortItems.RECENT :
                searchQuery = Constants.Library.Sort.RECENT;
                break;
            case SortItems.TITLE :
                searchQuery = Constants.Library.Sort.TITLE;
                break;
            case SortItems.AUTHOR :
                searchQuery = Constants.Library.Sort.AUTHOR;
                break;
        }

        Element necessaryFilter = waiter.waitForElementByNameExists(searchQuery, 1, new IConfig().setMaxLevelOfElementsTree(3));
        if(necessaryFilter == null) {
            testManager.retest(searchQuery + " is not found");
        }
        TestManager.addStep("click on the " + searchQuery);
        return clicker.clickOnElement(necessaryFilter);
    }

    public int getCurrentFilter() {
        Element filterButton = waiter.waitForElementByNameExists(Constants.Library.FILTER, 1, new IConfig().setMaxLevelOfElementsTree(4).setMatcher(Matcher.ContainsIgnoreCase));
        if(filterButton == null) return FilterItems.NAN;
        String filterName = filterButton.getName();
        if(filterName == null) return FilterItems.NAN;
        if(filterName.contains(Constants.Library.Filter.ALL_ITEMS)) return FilterItems.ALL_ITEMS;
        if(filterName.contains(Constants.Library.Filter.BOOKS)) return FilterItems.BOOKS;
        if(filterName.contains(Constants.Library.Filter.MAGAZINES)) return FilterItems.MAGAZINES;
        if(filterName.contains(Constants.Library.Filter.NEWSPAPERS)) return FilterItems.NEWSPAPERS;
        if(filterName.contains(Constants.Library.Filter.MY_FILES)) return FilterItems.MY_FILES;
        if(filterName.contains(Constants.Library.Filter.ARCHIVED)) return FilterItems.ARCHIVED;
        if(filterName.contains(Constants.Library.Filter.EVERYTHING_ELSE)) return FilterItems.EVERYTHING_ELSE;
        return FilterItems.NAN;
    }

    public String getFilterNameByIndex(int index) {
        switch (index) {
            case FilterItems.ALL_ITEMS:
                return Constants.Library.Filter.ALL_ITEMS;
            case FilterItems.BOOKS:
                return Constants.Library.Filter.BOOKS;
            case FilterItems.MAGAZINES:
                return Constants.Library.Filter.MAGAZINES;
            case FilterItems.NEWSPAPERS:
                return Constants.Library.Filter.NEWSPAPERS;
            case FilterItems.ARCHIVED:
                return Constants.Library.Filter.ARCHIVED;
            case FilterItems.MY_FILES:
                return Constants.Library.Filter.MY_FILES;
            case FilterItems.EVERYTHING_ELSE:
                return Constants.Library.Filter.EVERYTHING_ELSE;
            default:
                return Constants.Library.Filter.NAN;
        }
    }

    public String getSortNameByIndex(int index) {
        switch (index) {
            case SortItems.AUTHOR:
                return Constants.Library.Sort.AUTHOR;
            case SortItems.TITLE:
                return Constants.Library.Sort.TITLE;
            case SortItems.RECENT:
                return Constants.Library.Sort.RECENT;
            default:
                return Constants.Library.Sort.NAN;
        }
    }

    public int getCurrentSort() {
        Element sortButton = waiter.waitForElementByNameExists(Constants.Library.SORT, 1, new IConfig().setMaxLevelOfElementsTree(4).setMatcher(Matcher.ContainsIgnoreCase));
        if(sortButton == null) return SortItems.NAN;
        String filterName = sortButton.getName();
        if(filterName == null) return SortItems.NAN;
        if(filterName.contains(Constants.Library.Sort.RECENT)) return SortItems.RECENT;
        if(filterName.contains(Constants.Library.Sort.AUTHOR)) return SortItems.AUTHOR;
        if(filterName.contains(Constants.Library.Sort.TITLE)) return SortItems.TITLE;
        return SortItems.NAN;
    }

    public boolean changeSort(int filterIndex) throws TestException {
        return openSort() && chooseSort(filterIndex);
    }

    public boolean changeFilter(int filterIndex) throws TestException {
        return openFilter() && chooseFilter(filterIndex);
    }
}
