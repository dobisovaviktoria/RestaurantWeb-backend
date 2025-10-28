package be.kdg.keepdishesgoing.restaurant.domain;

public class Owner {
    private final String id;
    private final String email;
    private final String name;

    private Owner(String id, String email, String name) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Owner id cannot be empty");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Invalid contactEmail");

        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static Owner create(String id, String email, String name) {
        return new Owner(id, email, name);
    }

    public static Owner fromPersistence(String id, String email, String name) {
        return new Owner(id, email, name);
    }

    public String id() {
        return id;
    }

    public String email() {
        return email;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "Owner: contactEmail=" + email + ", name=" + name;
    }
}
