package nu.datavetenskap.foobarkiosk.models.statemodels;

import java.util.ArrayList;

import nu.datavetenskap.foobarkiosk.models.IProduct;

public class IState implements
        State.StateInterface {
    private State state;






    public ArrayList<IProduct> getProducts() {
        return state.getProducts();
    }

    public String getUsername() {
        return null;
    }
}



