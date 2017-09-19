package nu.datavetenskap.foobarkiosk.models;

import com.google.gson.annotations.SerializedName;

public class IAccount {
    private String id;
    @SerializedName("user_id")      private String userId;
    private String name;
    private Float balance;
    private String token;
    @SerializedName("is_complete")  private Boolean isComplete;
    @SerializedName("card_id")      private String cardId;



    @Override
    public String toString() {
        return "IAccount{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", token='" + token + '\'' +
                ", isComplete=" + isComplete +
                ", cardId='" + cardId + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }





    public void update(IAccount that) {
        this.id = that.id;
        this.userId = that.userId;
        this.name = that.name;
        this.balance = that.balance;
        this.token = that.token;
        this.isComplete = that.isComplete;
        this.cardId = that.cardId;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public float getBalance() {
        return balance;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public Boolean isEmpty() {
        return id == null &&
                userId == null &&
                token == null;
    }
}
