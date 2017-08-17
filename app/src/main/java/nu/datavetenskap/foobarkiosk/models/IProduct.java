package nu.datavetenskap.foobarkiosk.models;

public class IProduct extends Product {

    private Boolean selected;
    private Boolean loading;
    private Boolean failed;

    public IProduct(Product prod) {
        super(prod);
        selected = false;
        loading = false;
        failed = false;
    }

    public void incrementAmount() {
        this.qty++;
    }

    public void decrementAmount() {
        this.qty--;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "IProduct{" + super.toString() +
                "selected=" + selected +
                ", loading=" + loading +
                ", failed=" + failed +
                '}';
    }
}
