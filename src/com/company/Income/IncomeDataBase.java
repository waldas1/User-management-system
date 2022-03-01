package com.company.Income;



import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class IncomeDataBase {

    private ArrayList<IncomeRecord> income;
    private final String dbIncome;

    public IncomeDataBase(String dbIncome){
        this.dbIncome = dbIncome;
    }

    private ArrayList<IncomeRecord> getIncomesForUser(String username) throws FileNotFoundException{

        income = getAllIncomeRecord();
        ArrayList<IncomeRecord> allIncomesForUser = new ArrayList<>();

        for (IncomeRecord incomeRecord : income){
            if (incomeRecord.getUsername().equals(username)){
                allIncomesForUser.add(incomeRecord);
            }
        }
        return allIncomesForUser;
    }

    public ArrayList<IncomeRecord> getAllIncomeRecord() throws FileNotFoundException {

        File incomeFile = new File(dbIncome);
        Scanner incomeScanner = new Scanner(incomeFile);

        income = new ArrayList<>();
        while (incomeScanner.hasNextLine()) {
            String username = incomeScanner.nextLine();
            float incomeAmount = incomeScanner.nextFloat();
            incomeScanner.nextLine();
            LocalDate incomeDate = LocalDate.parse(incomeScanner.nextLine());
            String incomeCategory = incomeScanner.nextLine();
            incomeScanner.nextLine();

            income.add(new IncomeRecord(username, incomeAmount, incomeDate, incomeCategory));
        }
        return income;
    }

    public void newIncomeRegistration(IncomeRecord incomeRecord) throws IOException {

        FileWriter incomeFw = new FileWriter(dbIncome, true);
        PrintWriter incomeWriter = new PrintWriter(incomeFw);

        incomeWriter.println(incomeRecord.getUsername());
        incomeWriter.println(incomeRecord.getIncomeAmount());
        incomeWriter.println(incomeRecord.getIncomeDate());
        incomeWriter.println(incomeRecord.getIncomeCategory());
        incomeWriter.println();
        incomeWriter.close();

    }

    public float incomeBalance(String username) throws FileNotFoundException {

        ArrayList<IncomeRecord> incomesForUsers = getIncomesForUser(username);
        float incomeAmount = 0;

        for (IncomeRecord incomeRecord : incomesForUsers) {
            incomeAmount+= incomeRecord.getIncomeAmount();
        }

        return incomeAmount;
    }
    public void deleteLastIncome(String username) throws IOException {

        income = getAllIncomeRecord();

        ArrayList<IncomeRecord> incomeRecordsToDelete =getIncomesForUser(username);
        income.remove(incomeRecordsToDelete.get(incomeRecordsToDelete.size()-1));

        overWriteIncome();
    }

    public void deleteAllIncomeByUsername (String username) throws IOException {

        income = getAllIncomeRecord();

        ArrayList<IncomeRecord> incomeRecordsDeleteByUser = getIncomesForUser(username);

        for (IncomeRecord incomeRecord: incomeRecordsDeleteByUser) {
            income.remove(incomeRecord);
        }

        overWriteIncome();
    }

    private void overWriteIncome() throws IOException {
        deleteIncome();
        for (IncomeRecord incomeToDelete:income){
            newIncomeRegistration(incomeToDelete);
        }
    }

    private void deleteIncome() throws IOException {
        new FileWriter(dbIncome,false);
    }
}
