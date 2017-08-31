package nu.datavetenskap.foobarkiosk.models;

public class StoreEntity {
    protected String id;
    protected String name;
    protected String image;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public static class BackButtonEntity extends StoreEntity {
        @Override
        public String getName() {
            return "Back";
        }
    }

}
