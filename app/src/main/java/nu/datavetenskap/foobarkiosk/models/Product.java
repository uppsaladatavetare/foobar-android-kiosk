package nu.datavetenskap.foobarkiosk.models;

/**
 * Created by alexis on 2017-06-24.
 */

public class Product {
    private String id;
    private String name;
    private String code;
    private String description;
    private Number price;
    private boolean active;
    private Number qty;
    private String image;

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", active=" + active +
                ", qty=" + qty +
                ", image='" + image + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public Number getPrice() {
        return price;
    }

    public boolean isActive() {
        return active;
    }

    public Number getQty() {
        return qty;
    }


}
