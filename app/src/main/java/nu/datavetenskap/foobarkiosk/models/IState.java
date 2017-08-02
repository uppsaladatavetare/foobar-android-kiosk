package nu.datavetenskap.foobarkiosk.models;

public class IState {

    private IAccount account;
    private IProductState products;
    private IPurchaseState purchase;

    private class   IProductState {
        private IProduct products;
    }

    private class IPurchaseState {
        private String state;
    }
}
