package com.tareg.onlineshopping.model;

public class Address {
    private long id;
    private long userId;
    private String label;
    private String recipientName;
    private String phone;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private boolean isDefault;

    public Address(long id, long userId, String label, String recipientName, String phone,
                   String line1, String line2, String city, String state,
                   String postalCode, String country, boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.label = label;
        this.recipientName = recipientName;
        this.phone = phone;
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.isDefault = isDefault;
    }

    public long getId() { return id; }
    public long getUserId() { return userId; }
    public String getLabel() { return label; }
    public String getRecipientName() { return recipientName; }
    public String getPhone() { return phone; }
    public String getLine1() { return line1; }
    public String getLine2() { return line2; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getPostalCode() { return postalCode; }
    public String getCountry() { return country; }
    public boolean isDefault() { return isDefault; }

    public String asShippingText() {
        String extraLine = line2 == null || line2.trim().isEmpty() ? "" : ", " + line2.trim();
        return recipientName + " (" + phone + "), " + line1 + extraLine + ", "
                + city + ", " + state + " " + postalCode + ", " + country;
    }
}

