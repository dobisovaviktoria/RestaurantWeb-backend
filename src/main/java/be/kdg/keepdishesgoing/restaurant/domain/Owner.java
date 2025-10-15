package be.kdg.keepdishesgoing.restaurant.domain;

public class Owner {
    private String id;
    private String email;
    private String name;

    public Owner(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "email: " + email
                + ", name: " + name;
    }
}
