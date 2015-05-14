package com.bn.nook.ios.screen.reader;

import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.bn.nook.ios.screen.BaseLibraryScreen;
import com.bn.nook.ios.screen.BaseScreen;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.elements.ElementQuery;
import com.sofment.testhelper.driver.ios.enams.UIAElementType;
import com.sofment.testhelper.driver.ios.interfaces.AlertCallBack;
import com.sofment.testhelper.driver.ios.models.IDevice;

import java.util.ArrayList;

/**
 * Created by automation on 5/11/15.
 */
public class MyShelvesScreen extends BaseLibraryScreen {

    public MyShelvesScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        super(testManager, testHelper, paramsParser, iDevice);
    }

    public boolean pressOnNavigationBarButton(String label, boolean isStrict) throws TestException {
        Element element = getter.getElement(new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIANavigationBar, 0)
                .addElement(UIAElementType.UIAButton, label));
        if(element == null)
            return false;
        testManager.addStep("Press on button '" + label + "'");
        if(!clicker.clickOnElement(element)) {
            if(isStrict)
                testManager.retest("Can not click on " + label);
            else return false;
        }
        return true;
    }

    public boolean pressOnToolBarButton(String label, boolean isStrict) throws TestException {
        Element element = getter.getElement(new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIAToolBar, 0)
                .addElement(UIAElementType.UIAButton, label));
        if(element == null)
            return false;
        testManager.addStep("Press on button '" + label + "'");
        if(!clicker.clickOnElement(element)) {
            if(isStrict)
                testManager.retest("Can not click on " + label);
            else return false;
        }
        return true;
    }

    public boolean pressOnEditItem(String label, boolean isStrict) throws TestException {
        ElementQuery elementQuery = null;
        switch (label) {
            case Constants.My_Shelves.CANCEL_BUTTON:
                elementQuery  = new ElementQuery()
                        .addElement(UIAElementType.UIAActionSheet, 0)
                        .addElement(UIAElementType.UIAButton, label);
                break;
            case Constants.My_Shelves.EDIT_BUTTON:
            case Constants.My_Shelves.DELETE_BUTTON:
            case Constants.My_Shelves.RENAME_BUTTON:
                elementQuery  = new ElementQuery()
                        .addElement(UIAElementType.UIAActionSheet, 0)
                        .addElement(UIAElementType.UIACollectionView, 0)
                        .addElement(UIAElementType.UIACollectionCell, label);
                break;
        }

        if(elementQuery == null) {
            iDevice.i("Label '" + label + "' was not found ");
            return false;
        }
        Element element = getter.getElement(elementQuery);
        if(element == null) {
            pressOnToolBarButton(Constants.My_Shelves.EDIT_BUTTON, isStrict);
            element = waiter.waitForElementVisible(10000, elementQuery);
        }

        if(element == null) {
            if(isStrict)
                testManager.retest("Element " + label + " was not found");
            else return false;
        }

        testManager.addStep("Press on '" + label + "'");
        if(!clicker.clickOnElement(element)) {
            if(isStrict)
                testManager.retest("Can not click on " + label);
            else return false;
        }
        return true;
    }

    /**
     *
     * @param productsShelf - list with current products of the shelf
     * @return Element that was not added in shelf
     */
    public Element findNotAddedElement(ArrayList<Element> productsShelf) throws TestException {
        ArrayList<Element> productsEditMode = getProducts(1, 5000);
        boolean isFindElement;
        for(Element element1 : productsEditMode) {
            isFindElement = false;
            for(Element element : productsShelf) {
                if(element1.getName().equals(element.getName())) {
                    isFindElement = true;
                }
            }

            if(!isFindElement) {
                return element1;
            }
        }
        return null;
    }

    public boolean isExistProduct(ArrayList<Element> products, Element book, boolean isStrict) throws TestException {
        for(Element element : products) {
            if(element.getName().equals(book.getName())) {
                return true;
            }
        }

        if(isStrict)
            testManager.retest("Book " + book.getName() + " was not found");
        return true;
    }

    public Element waitForScreen(String title, long timeout, boolean isStrict) throws TestException {
        Element element = waiter.waitForElementVisible(timeout, new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIANavigationBar, 0)
                .addElement(UIAElementType.UIAStaticText, title));
        if(element == null) {
            if(isStrict)
                testManager.retest("Can not find element " + Constants.My_Shelves.TITLE_EDIT_SHELF);
            else return null;
        }
        return element;
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

    /**
     * Inside open shelf
     * @return name
     */
    public String getShelfName() {
        Element element = getter.getElement(new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIACollectionView, 0)
                .addElement(UIAElementType.UIAStaticText, 0));
        if(element != null) {
            String value = element.getValue();
            iDevice.i("Value: " + value);
            String[] arr = value.split("\\.");
            return arr[0];
        }
        return null;
    }

    public boolean editShelf(boolean isStrict) throws TestException {
        Element editButton = waitForElement(new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIAToolBar, 0)
                .addElement(UIAElementType.UIAButton, Constants.My_Shelves.EDIT_BUTTON), Constants.DEFAULT_TIMEOUT, true);

        ArrayList<Element> products = getProducts(1, 5000);

        if(!clicker.clickOnElement(editButton))
            testManager.retest("Can not click on Edit");

        pressOnEditItem(Constants.My_Shelves.EDIT_BUTTON, true);

        waitForScreen(Constants.My_Shelves.TITLE_EDIT_SHELF, Constants.DEFAULT_TIMEOUT, true);

        Element newProduct = findNotAddedElement(products);
        if(newProduct == null)
            testManager.retest("New book was not found for shelf");

        if(!scroller.scrollToVisible(newProduct))
            testManager.retest("Can not scrollable to book '" + newProduct.getName() + "'");

        testManager.addStep("Select book '" + newProduct.getName() + "'");
        clicker.clickOnElement(newProduct);

        pressOnNavigationBarButton(Constants.My_Shelves.DONE_BUTTON, true);

        waitForElement(new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIAToolBar, 0)
                .addElement(UIAElementType.UIAButton, Constants.My_Shelves.EDIT_BUTTON), Constants.DEFAULT_TIMEOUT, true);

        products = getProducts(1, 5000);
        if(!isExistProduct(products, newProduct, false)) {
            if(isStrict)
                testManager.failTest("The added book " + newProduct.getName() + " was not found");
            else return false;
        }
        return true;
    }

    public boolean renameShelf(boolean isStrict) throws TestException {
        Element editButton = waitForElement(new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIAToolBar, 0)
                .addElement(UIAElementType.UIAButton, Constants.My_Shelves.EDIT_BUTTON), Constants.DEFAULT_TIMEOUT, true);

        String shelfName = getShelfName();
        iDevice.i("Current shelf name " + shelfName);

        if(!clicker.clickOnElement(editButton))
            testManager.retest("Can not click on Edit");


        final int[] alertState = new int[1];
        alertState[0] = 0;
        iDevice.setAlertHandler(new AlertCallBack() {
            @Override
            public void handleAlert(ArrayList<Element> arrayList) {
                iDevice.i("############Handle Alert");
                for (Element element : arrayList) {
                    iDevice.i("Alert Element name:" + element.getName());
                    if (element.getName().toLowerCase().equals(Constants.My_Shelves.ALERT_TITLE_RENAME_SHELF.toLowerCase())) {
                        iDevice.i("Alert Element exist");
                        alertState[0] = 1;
                        break;
                    }
                }
            }
        });

        pressOnEditItem(Constants.My_Shelves.RENAME_BUTTON, true);

        iDevice.i("waiting for alert Rename Shelf");
        long startTimer = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTimer < 20000) {
            if(alertState[0] != 0) {
                iDevice.i("Alert is visible");
                break;
            }
        }
        iDevice.sleep(1000);
        if(alertState[0] == 0)
            testManager.retest("Alert '" + Constants.My_Shelves.ALERT_TITLE_RENAME_SHELF + "' was not found");

        String newShelfName = testManager.getRandomShelfName();
        String currentShelfName = getShelfName();

        if(shelfName.equals(currentShelfName))
            testManager.retest("Shelf name is not changed");

        if(!newShelfName.equals(currentShelfName)) {
            if(isStrict)
                testManager.failTest("Shelf name is not changed");
            else return false;
        }

        return true;
    }

    public boolean removeShelf(boolean isStrict) throws TestException {
        Element editButton = waitForElement(new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIAToolBar, 0)
                .addElement(UIAElementType.UIAButton, Constants.My_Shelves.EDIT_BUTTON), Constants.DEFAULT_TIMEOUT, true);

        String shelfName = getShelfName();

        if(!clicker.clickOnElement(editButton))
            testManager.retest("Can not click on Edit");

        final int[] alertState = new int[1];
        alertState[0] = 0;
        iDevice.setAlertHandler(new AlertCallBack() {
            @Override
            public void handleAlert(ArrayList<Element> arrayList) {
                iDevice.i("############Handle Alert");
                for (Element element : arrayList) {
                    iDevice.i("Alert Element name:" + element.getName());
                    if (element.getName().toLowerCase().equals(Constants.My_Shelves.ALERT_TITLE_DELETE_SHELF.toLowerCase())) {
                        iDevice.i("Alert Element exist");
                        alertState[0] = 1;
                        break;
                    }
                }
            }
        });

        pressOnEditItem(Constants.My_Shelves.DELETE_BUTTON, true);

        iDevice.i("waiting for alert Rename Shelf");
        long startTimer = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTimer < 20000) {
            if(alertState[0] != 0) {
                iDevice.i("Alert is visible");
                break;
            }
        }

        iDevice.sleep(1000);
        if(alertState[0] == 0)
            testManager.retest("Alert '" + Constants.My_Shelves.ALERT_TITLE_DELETE_SHELF + "' was not found");

        waitForElement(new ElementQuery()
                .addElement(UIAElementType.UIAWindow, 0)
                .addElement(UIAElementType.UIAToolBar, 0)
                .addElement(UIAElementType.UIAButton, Constants.My_Shelves.ADD_BUTTON), Constants.DEFAULT_TIMEOUT, true);

        ArrayList<Element> shelves = getProducts(1, 5000);
        for(Element shelf : shelves) {
            if(shelf.equals(shelfName)) {
                if(isStrict)
                    testManager.failTest("The removed shelf '" + shelfName + "' is exist");
                else return false;
            }
        }

        return true;
    }
}
