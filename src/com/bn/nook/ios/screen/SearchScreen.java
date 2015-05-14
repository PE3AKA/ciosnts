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

import static com.bn.nook.ios.screen.SearchScreen.ProductState.*;

/**
 * Created by avsupport on 5/5/15.
 */
public class SearchScreen extends BaseScreen {
    public SearchScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        super(testManager, testHelper, paramsParser, iDevice);
    }

    public boolean closeSearch() {
        Element closeButton = getCloseButton();
        if(getCloseButton() != null) {
            testManager.addStep("click on the close button");
            return clicker.clickOnElement(closeButton);
        }
        return false;
    }

    public Element getCloseButton() {
        return getter.getElementByName(Constants.Search.CANCEL, new IConfig().setMaxLevelOfElementsTree(2));
    }

    public Element findSample() throws TestException {
        Element collectionView = waiter.waitForElementByClassVisible(UIAElementType.UIACollectionView, Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(2));
        if(collectionView == null) {
            testManager.retest("collection view with search results is not found");
        }
        Element sample = waiter.waitForElementByNameExists(Constants.Search.SAMPLE, Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(3).setParentElement(collectionView));
        if(sample != null) {
            testManager.addStep("swipe to sample product");
            if(scroller.scrollToVisible(sample)) return sample;
        }
        return null;
    }

    public Element findNotDownloadedProduct() throws TestException {
        Element collectionView = waiter.waitForElementByClassVisible(UIAElementType.UIACollectionView, Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(2));
        if(collectionView == null) {
            testManager.retest("collection view with search results is not found");
        }
        Element product = waiter.waitForElementByNameExists(Constants.Search.NOT_DOWNLOADED, Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(1).setMatcher(Matcher.ContainsIgnoreCase).setParentElement(collectionView));
        if(product != null) {
            testManager.addStep("swipe to downloaded product");
            scroller.scrollToVisible(product);
            return product;
        }
        return null;
    }

    public ArrayList<Element> getSearchResults(int minSearchResultProducts, long timeout) throws TestException {
        return getProducts(minSearchResultProducts, timeout);
    }

    public boolean downloadProduct(Element product, String name) throws TestException {
        if(product == null) return false;
        Element button = waiter.waitForElementByClassExists(UIAElementType.UIAButton, 1, new IConfig().setMaxLevelOfElementsTree(2).setParentElement(product));

        if (button != null && button.getName() != null && button.getName().equals(Constants.Search.SAMPLE)) {
            testManager.addStep("click on product " + product.getName() + " to download");
            clicker.clickOnElement(button);

            Element element = null;
            if ((element = waiter.waitForElementByNames(new IWaiterConfig()
                            .setOnlyVisible(true)
                            .setMaxLevelOfElementsTree(3)
                            .setParentElement(product)
                            .setSearchCondition(SearchCondition.NAME)
                            .addMatcher(Matcher.FullMatch).addInstance(0),
                    Constants.DOWNLOAD_PRODUCT_TIMEOUT,
                    Constants.Search.READ,
                    Constants.Search.SAMPLE)) == null) {
//            testManager.retest("the book name: " + product.getName() + " is not found.");
                return false;
            }
            if (element.getName() != null && !element.getName().equals(Constants.Search.READ)) {
                iDevice.sleep(2000);
                if ((element = waiter.waitForElementByNames(new IWaiterConfig()
                                .setOnlyVisible(true)
                                .setMaxLevelOfElementsTree(3)
                                .setParentElement(product)
                                .setSearchCondition(SearchCondition.NAME)
                                .addMatcher(Matcher.FullMatch).addInstance(0),
                        Constants.DOWNLOAD_PRODUCT_TIMEOUT,
                        Constants.Search.READ,
                        Constants.Search.SAMPLE)) == null) {
//                testManager.retest("the book name: " + product.getName() + " is not found.");
                    return false;
                }
                if (element.getName() != null && !element.getName().equals(Constants.Search.READ)) {
                    return false;
                }
            }
        } else {
            Element element = getProduct(name);
            switch (getProductState(element)) {
                case DOWNLOADED:
                    return true;
                case NOT_DOWNLOADED:
                    testManager.addStep("click on product " + product.getName() + " to download");
                    clicker.clickOnElement(element);
                    return waitForDownloaded(name, Constants.DOWNLOAD_PRODUCT_TIMEOUT);
                case DOWNLOADING:
                    return waitForDownloaded(name, Constants.DOWNLOAD_PRODUCT_TIMEOUT);
            }
        }
        return true;
    }

    private boolean waitForDownloaded(String name, long timeout) throws TestException {
        long startTime = System.currentTimeMillis();
        while (true) {
            Element element = getProduct(name);
            if (getProductState(element) == DOWNLOADED) return true;
            if (System.currentTimeMillis() - startTime > timeout) return false;
        }
    }

    public enum ProductState{NOT_DOWNLOADED, DOWNLOADED, DOWNLOADING}

    public Element getProduct(String productName) throws TestException {
        Element collectionView = waiter.waitForElementByClassVisible(UIAElementType.UIACollectionView, 1000, new IConfig().setMaxLevelOfElementsTree(2));
        if(collectionView == null)
            testManager.retest("collection view with search results is not found");
        ArrayList<Element> products = getter.getElementChildrenByType(collectionView, UIAElementType.UIACollectionCell);
        if (products.size() == 0)
            testManager.retest("Product size 0");
        for (Element element: products) {
            if (element.getName().toLowerCase().contains(productName.toLowerCase())) {
                return element;
            }
        }
        return null;
    }

    public ProductState getProductState(Element product) throws TestException {
        if (product == null)
            testManager.retest("Product is not found");
        String state = product.getName();
        if(state.toLowerCase().contains("not downloaded"))
            return NOT_DOWNLOADED;
        if (state.toLowerCase().contains("downloading"))
            return DOWNLOADING;
        return DOWNLOADED;
    }
}
