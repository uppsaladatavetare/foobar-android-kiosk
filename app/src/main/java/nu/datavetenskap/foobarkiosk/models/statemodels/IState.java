package nu.datavetenskap.foobarkiosk.models.statemodels;

import java.util.ArrayList;

import nu.datavetenskap.foobarkiosk.models.IAccount;
import nu.datavetenskap.foobarkiosk.models.IProduct;

public class IState {
    private State state;

    public IState() {
        state = new State();
    }

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

    public void setAccount(IAccount account) {
        this.state.setAccount(account);
    }

    public String getPurchaseState() {
        return state.getPurcaseState();
    }

    public void setPurchaseState(String purchaseState) {
        state.setPurchaseState(PurchaseState.ONGOING);
    }


    private class State {

        private IAccount account;
        private ProductList products;
        private PurchaseState purchase;

        protected State(){
            account = new IAccount();
            products = new ProductList();
            purchase = new PurchaseState();
        }

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
             if (account != null && account.isEmpty()) {
                 account = null;
             }
             return account;
        }

        public void setAccount(IAccount account) {
            this.account = account;
        }

        public String getPurcaseState() {
            return purchase.getPurcaseState();
        }

        public void setPurchaseState(String purchaseState) {
            purchase.setPurchaseState(PurchaseState.ONGOING);
        }
    }
}



