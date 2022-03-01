package com.company.CashBack;


public class CashBack {
    private String username;
    private float cashBack;

    public CashBack(String username, float cashBack) {
        this.username = username;
        this.cashBack = cashBack;
    }


    public String getUsername() {
        return username;
    }

    public float getCashBack() {
        return cashBack;
    }

}
