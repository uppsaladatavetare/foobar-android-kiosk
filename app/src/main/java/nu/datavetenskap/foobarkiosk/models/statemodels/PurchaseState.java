package nu.datavetenskap.foobarkiosk.models.statemodels;

class PurchaseState {

    private String state;
    private int cost;

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

}
