package nu.datavetenskap.foobarkiosk.models;

/**
 * Created by alexis on 2017-06-24.
 */

public class Product extends StoreEntity {
    private String code;
    private String description;
    private float price;
    private boolean active;
    private String category;
    protected int qty;

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

    protected Product() {
        id = "0000";
        name = "Unknown Item";
        description = "Unknown Item";
        price = 0;
        qty = 1;
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





    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
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

    public String getCategory() {
        return category;
    }
}
