package nu.datavetenskap.foobarkiosk.models.statemodels;

import java.util.ArrayList;

import nu.datavetenskap.foobarkiosk.models.IAccount;
import nu.datavetenskap.foobarkiosk.models.IProduct;

public class IState {
    private State state;



    public void update(IState that) {
        state.update(that.getState());

    }


    public ArrayList<IProduct> getProducts() {
        return state.getProducts();
    }


    public IAccount getAccount() {
        return state.getAccount();
    }



    private State getState() {
        return state;
    }







    private class State {

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


        private ArrayList<IProduct> getProducts() {
            return products.getProducts();
        }

        private void update(State that) {
            account.update(that.account);
            purchase.update(that.purchase);

            ArrayList<IProduct> list = this.getProducts();
            list.clear();
            list.addAll(that.getProducts());

        }


         private IAccount getAccount() {
            return account;
        }

    }
}



