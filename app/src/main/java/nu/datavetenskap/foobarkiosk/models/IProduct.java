package nu.datavetenskap.foobarkiosk.models;

public class IProduct extends Product {

    private Boolean selected;
    private Boolean loading;
    private Boolean failed;




    @Override
    public String toString() {
        return "IProduct{" + super.toString() +
                "selected=" + selected +
                ", loading=" + loading +
                ", failed=" + failed +
                '}';
    }
}
