package nu.datavetenskap.foobarkiosk.models.statemodels;

import java.util.ArrayList;

import nu.datavetenskap.foobarkiosk.models.IAccount;
import nu.datavetenskap.foobarkiosk.models.IProduct;


class State
    implements ProductList.ProductListInterface {

    private IAccount account;
    private ProductList products;
    private PurchaseState purchase;

    @Override
    public String toString() {
        return "IState{" +
                "account=" + account +
                ", products=" + products +
                ", purchase=" + purchase +
                '}';
    }






    @Override
    public ArrayList<IProduct> getProducts() {
        return products.getProducts();
    }

    interface StateInterface {
        ArrayList<IProduct> getProducts();
        String getUsername();
    }

}
