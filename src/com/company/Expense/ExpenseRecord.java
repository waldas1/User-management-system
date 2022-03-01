package com.company.Expense;

import java.time.LocalDate;

public class ExpenseRecord {

    private String username;
    private float expenseAmount;
    private LocalDate expenseDate;
    private String expenseCategory;
    private float cashBack;



    public ExpenseRecord(String username, float expenseAmount,LocalDate expenseDate, String expenseCategory, float cashBack) {
        this.username = username;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;
        this.expenseCategory = expenseCategory;
        this.cashBack = cashBack;

    }

    public float getCashBack() {
        return cashBack;
    }

    public String getUsername() {
        return username;
    }

    public float getExpenseAmount() {
        return expenseAmount;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    @Override
    public String toString() {
        return "Expenses: " +"\n"+
                " | Username: " +username +"\n"+
                " | Amount: " + expenseAmount +"\n"+
                " | Date: " + expenseDate +"\n"+
                " | Category: " + expenseCategory +"\n"+
                " | Cash Back: " + cashBack;
    }
}
