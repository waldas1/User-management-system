package Income;

import java.time.LocalDate;

public class IncomeRecord {

    private String username;
    private float incomeAmount;
    private LocalDate incomeDate;
    private String incomeCategory;

    public IncomeRecord(String username,float incomeAmount,LocalDate incomeDate, String incomeCategory) {
        this.username = username;
        this.incomeAmount = incomeAmount;
        this.incomeDate = incomeDate;
        this.incomeCategory = incomeCategory;
    }

    public String getUsername() {
        return username;
    }

    public float getIncomeAmount() {
        return incomeAmount;
    }

    public LocalDate getIncomeDate() {
        return incomeDate;
    }

    public String getIncomeCategory() {
        return incomeCategory;
    }

    @Override
    public String toString() {
        return "Incomes: " + "\n"+
                " | Username: " +  username + "\n"+
                " | Amount: " + incomeAmount +"\n"+
                " | Date: " + incomeDate +"\n"+
                " | Category: " + incomeCategory;
    }

}