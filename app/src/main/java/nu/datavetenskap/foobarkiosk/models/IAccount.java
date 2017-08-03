package nu.datavetenskap.foobarkiosk.models;

import com.google.gson.annotations.SerializedName;

public class IAccount {
    private String id;
    private String user_id;
    private String name;
    private int balance;
    private String token;
    private Boolean is_complete;
    @SerializedName("card_id") private String cardId;




    public String getName() {
        return name;
    }





    public void update(IAccount that) {
        this.id = that.id;
        this.user_id = that.user_id;
        this.name = that.name;
        this.balance = that.balance;
        this.token = that.token;
        this.is_complete = that.is_complete;
        this.cardId = that.cardId;
    }
}
