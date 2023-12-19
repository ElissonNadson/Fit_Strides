package com.example.fit;

public class WaterRecord {
    private int iconResId;
    private String time;
    private String name;
    private String amount;
    private String documentId;

    public WaterRecord(int iconResId, String time, String name, String amount,String documentId) {
        this.iconResId = iconResId;
        this.time = time;
        this.name = name;
        this.amount = amount;
        this.documentId = documentId;
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

    // Getter e setter para o documentId
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

}
