package com.fooddeliveryplatform.model;

import javax.json.Json;
import javax.json.JsonObject;

import com.fooddeliveryplatform.util.PasswordUtil;

public class User {
    private Long userId;
    private String username;
    private String email;
    private String password; // Store hashed (e.g., BCrypt)
    private UserRole role; // CUSTOMER, RESTAURANT, DELIVERY_PARTNER
    private String fullName;
    private String phone;
    private String address;
    private boolean isActive;

    public User() {}

    public User(Long userId, String username, String address, String email, String fullName, boolean isActive,
                String password, String phone, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.isActive = isActive;
    }

    public Long getUserId() {return userId;}
    public void setUserId(Long userId) {this.userId = userId;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public UserRole getRole() {return role;}
    public void setRole(UserRole role) {this.role = role;}

    public String getFullName() {return fullName;}
    public void setFullName(String fullName) {this.fullName = fullName;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}
    
    public boolean isActive() {return isActive;}
    public void setActive(boolean isActive) {this.isActive = isActive;}
    
    public String getPasswordHash() {
        return PasswordUtil.hashPassword(this.password);
    }

    public void setPasswordHash(String passwordHash) {
        this.password = passwordHash; // Store the hashed password
    }

    public JsonObject toJson() {
        JsonObject json = Json.createObjectBuilder()
            .add("userId", userId)
            .add("username", username)
            .add("email", email)
            .add("password", password) // Should be hashed
            .add("role", role.name())
            .add("fullName", fullName)
            .add("phone", phone)
            .add("address", address)
            .add("isActive", isActive)
            .build();
        return json;
    }

    public static User fromJson(JsonObject json) {
        User user = new User();
        user.setUsername(json.getString("username"));
        user.setEmail(json.getString("email"));
        user.setPassword(json.getString("password")); // Should be hashed
        user.setRole(UserRole.valueOf(json.getString("role", "CUSTOMER"))); // Default to CUSTOMER role
        user.setFullName(json.getString("fullName"));
        user.setPhone(json.getString("phone"));
        user.setAddress(json.getString("address"));
        user.setActive(json.getBoolean("isActive", true)); // Default to true if not present
        return user;
    }
}
