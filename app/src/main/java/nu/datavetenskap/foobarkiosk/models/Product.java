package nu.datavetenskap.foobarkiosk.models;

/**
 * Created by alexis on 2017-06-24.
 */

public class Product {
    protected String id;
    private String name;
    private String code;
    private String description;
    private float price;
    private boolean active;
    protected int qty;
    private String image;

    public Product(Product p) {
        id = p.id;
        name = p.name;
        code = p.code;
        description = p.description;
        price = p.price;
        active = p.active;
        qty = 1;
        image = p. image;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product) {
            return this.id.equals(((Product) obj).id);
        }
        return super.equals(obj);
    }

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

    public float getPrice() {
        return price;
    }

    public boolean isActive() {
        return active;
    }

    public int getQty() {
        return qty;
    }

}
