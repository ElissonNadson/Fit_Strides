package com.example.fit;

public class WaterRecord {
    private int iconResId;
    private String time;
    private String name;
    private String amount;

    public WaterRecord(int iconResId, String time, String name, String amount) {
        this.iconResId = iconResId;
        this.time = time;
        this.name = name;
        this.amount = amount;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
