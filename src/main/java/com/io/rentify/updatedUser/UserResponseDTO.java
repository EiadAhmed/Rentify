package com.io.rentify.updatedUser;

public class UserResponseDTO {

    private String email;
    private String name;
    private String message;

    // Constructor
    public UserResponseDTO(String email, String name, String message) {

        this.email = email;
        this.name = name;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

