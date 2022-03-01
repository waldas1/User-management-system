package com.company.CashBack;

import Income.IncomeDataBase;
import Income.IncomeRecord;
import com.company.exception.CashBackException;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class CashBackDataBase {

    private ArrayList<CashBack> cashBacks;
    private final String dbCashBack;
    private FileWriter cashBackFw;
    private PrintWriter cashBackWriter;
    private File file;

    public CashBackDataBase(String dbCashBack) {
        this.dbCashBack = dbCashBack;
    }

    private ArrayList<CashBack> getAllCashBack() throws FileNotFoundException {

        file = new File(dbCashBack);
        Scanner cashBackScanner = new Scanner(file);

        cashBacks = new ArrayList<>();
        while (cashBackScanner.hasNextLine()) {
            String username = cashBackScanner.nextLine();
            float cashBack = cashBackScanner.nextFloat();
            cashBackScanner.nextLine();
            cashBackScanner.nextLine();

            cashBacks.add( new CashBack(username,cashBack));
        }
        return cashBacks;
    }

    public void newCashBack(CashBack cashBack) throws IOException {
        cashBackFw = new FileWriter(dbCashBack, true);
        cashBackWriter = new PrintWriter(cashBackFw);

        cashBackWriter.println(cashBack.getUsername());
        cashBackWriter.println(cashBack.getCashBack());
        cashBackWriter.println();
        cashBackWriter.close();

    }

    public float getAllCashBackByUser(String username) throws IOException {

        cashBacks = getAllCashBack();

        float cashBackCounter = 0;

        for (CashBack getValues : cashBacks) {
            if (getValues.getUsername().equals(username)) {
                cashBackCounter += getValues.getCashBack();
            }
        }
        return cashBackCounter;
    }

    public void checkIfCashBackFullAndDelete(String username, IncomeDataBase incomeDataBase) throws IOException, CashBackException {

        float cashBackCounter = getAllCashBackByUser(username);

        if (cashBackCounter == 100) {
            incomeDataBase.newIncomeRegistration(new IncomeRecord(username, cashBackCounter, LocalDate.now(), "From cashBack"));
            deleteAllUsersCashBackIfFull(username);
        }else{
            throw new CashBackException("Yours cash back sum is not full (100) !"+"\n"+"Yours sum is: " + cashBackCounter);
        }
    }

    public void deleteAllUsersCashBackIfFull(String username) throws IOException {

        cashBacks = getAllCashBack();
        ArrayList<CashBack> cashBacksToDelete = new ArrayList<>();

        for (CashBack cashBackToDelete : cashBacks){
            if (cashBackToDelete.getUsername().equals(username)){
                cashBacksToDelete.add(cashBackToDelete);
            }
        }
        for (CashBack cashBackToDelete : cashBacksToDelete) {
            cashBacks.remove(cashBackToDelete);
        }
        deleteCash();
        for (CashBack cashDelete : cashBacks){
            newCashBack(cashDelete);
        }
    }

    private void deleteCash() throws IOException {
        new FileWriter(dbCashBack, false);
    }
}
