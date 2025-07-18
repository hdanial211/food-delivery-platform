package com.fooddeliveryplatform.model;


import javax.json.Json;
import javax.json.JsonObject;

public class Restaurant {
    private Long restaurantId;
    private String closingTime;
    private int deliveryRadius;
    private String description;
    private String logoUrl;
    private String name;
    private String openingTime;
    private User owner;
    private Long ownerId;
    private boolean isActive;

    public Restaurant() {}
    public Restaurant(Long restaurantId, String closingTime, int deliveryRadius, String description,
                      String logoUrl, String name, String openingTime, User owner, boolean isActive) {
        this.restaurantId = restaurantId;
        this.closingTime = closingTime;
        this.deliveryRadius = deliveryRadius;
        this.description = description;
        this.logoUrl = logoUrl;
        this.name = name;
        this.openingTime = openingTime;
        this.owner = owner;
        this.isActive = isActive;
    }
    public Restaurant(Long restaurantId, String closingTime, int deliveryRadius, String description,
                      String logoUrl, String name, String openingTime, Long ownerId, boolean isActive) {
        this.restaurantId = restaurantId;
        this.closingTime = closingTime;
        this.deliveryRadius = deliveryRadius;
        this.description = description;
        this.logoUrl = logoUrl;
        this.name = name;
        this.openingTime = openingTime;
        this.ownerId = ownerId;
        this.isActive = isActive;
    }
    public Long getId() {
        return restaurantId;
    }
    public void setId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
    public String getClosingTime() {
        return closingTime;
    }
    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }
    public int getDeliveryRadius() {
        return deliveryRadius;
    }
    public void setDeliveryRadius(int deliveryRadius) {
        this.deliveryRadius = deliveryRadius;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getLogoUrl() {
        return logoUrl;
    }
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOpeningTime() {
        return openingTime;
    }
    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }
    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    public static Restaurant fromJson(JsonObject json) {
        User user = new User();
        Restaurant restaurant = new Restaurant();
        /*
         * {
                "action": "restaurantregister",
                "username": "restaurantowner",
                "address": "123 Restaurant St",
                "email": "restaurant@test.com",
                "fullName": "Restaurant Owner",
                "isActive": 1,
                "password": "securepassword123",
                "phone": "1234567890",
                "role" : "RESTAURANT",
                "closingTime": "22:00",
                "deliveryRadius": 5,
                "description": "Best restaurant in town",
                "logoUrl": "http://example.com/logo.png",
                "name": "Best Restaurant",
                "openingTime": "10:00",
                "isActive": 1
            }
         */
        user.setUsername(json.getString("username"));
        user.setAddress(json.getString("address"));
        user.setEmail(json.getString("email"));
        user.setFullName(json.getString("fullName"));
        user.setActive(json.getBoolean("isActive", true));
        user.setPassword(json.getString("password")); // Should be hashed
        user.setPhone(json.getString("phone"));
        user.setRole(UserRole.valueOf(json.getString("role", "RESTAURANT"))); // Default to RESTAURANT role

        restaurant.setClosingTime(json.getString("closingTime"));
        restaurant.setDeliveryRadius(json.getInt("deliveryRadius"));
        restaurant.setDescription(json.getString("description"));
        restaurant.setLogoUrl(json.getString("logoUrl"));
        restaurant.setName(json.getString("name"));
        restaurant.setOpeningTime(json.getString("openingTime"));
        restaurant.setOwner(user);
        restaurant.setActive(json.getBoolean("isActive2", true)); // Default to true if not
        
        return restaurant;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("id", restaurantId)
            .add("closingTime", closingTime.toString())
            .add("deliveryRadius", deliveryRadius)
            .add("description", description)
            .add("logoUrl", logoUrl)
            .add("name", name)
            .add("openingTime", openingTime.toString())
            .add("owner", owner.toJson()) // Assuming User has a toJson() method
            .add("isActive", isActive)
            .build();
    }
}
