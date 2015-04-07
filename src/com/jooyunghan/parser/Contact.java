package com.jooyunghan.parser;

/**
 * Created by jooyung.han on 4/7/15.
 */
public class Contact {
    public final String email;
    public final String name;

    public Contact(String email, String name) {
        this.email = email;
        this.name = name;
    }
    
    @Override
    public String toString() {
        return "Contact{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (email != null ? !email.equals(contact.email) : contact.email != null) return false;
        return !(name != null ? !name.equals(contact.name) : contact.name != null);

    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

}
