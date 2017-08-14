package nu.datavetenskap.foobarkiosk.models.statemodels;

public class PurchaseState {

    public static transient final String WAITING = "WAITING";
    public static transient final String ONGOING = "ONGOING";
    public static transient final String PENDING = "PENDING";
    public static transient final String FINALIZED = "FINALIZED";
    public static transient final String PROFILE = "PROFILE";

    private String state;
    private Integer cost;

    PurchaseState() {
        state = WAITING;
    }

    /*
    Possible states:
        WAITING
        ONGOING
        PENDING
        FINALIZED
        PROFILE

    And perhaps more that I don't know of...
    Should be able to be converted to enum, but don't know how and I'm lazy.
    */

    void update(PurchaseState that) {
        this.state = that.state;
    }

    public String getPurcaseState() {
        return state;
    }

    public void setPurchaseState(String purchaseState) {
        state = purchaseState;
    }
}
