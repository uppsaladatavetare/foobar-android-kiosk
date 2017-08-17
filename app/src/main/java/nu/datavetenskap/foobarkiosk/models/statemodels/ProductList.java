package nu.datavetenskap.foobarkiosk.models.statemodels;

import java.util.ArrayList;

import nu.datavetenskap.foobarkiosk.models.IProduct;

class ProductList {
    private ArrayList<IProduct> products;
    private int page;
    private int maxPage;

    ProductList() {
        products = new ArrayList<>();
        page = 0;
        maxPage = 0;
    }

    @Override
    public String toString() {
        return "ProductList{" +
                "products=" + products +
                '}';
    }

    ArrayList<IProduct> getProducts() {
        return products;
    }

    public void clear() {
        products.clear();
    }


    interface ProductListInterface {
        ArrayList<IProduct> getProducts();
    }
}
