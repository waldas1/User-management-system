package com.company.Expense;


import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class ExpensesDataBase {

    private ArrayList<ExpenseRecord> expense;
    private final String dbExpense;

    public ExpensesDataBase(String dbExpense) {

        this.dbExpense = dbExpense;
    }

    private ArrayList<ExpenseRecord> getExpenseForUser(String username) throws FileNotFoundException {
        expense = getAllExpenseRecord();
        ArrayList<ExpenseRecord> allExpenseForUser = new ArrayList<>();

        for (ExpenseRecord expenseRecord : expense) {
            if (expenseRecord.getUsername().equals(username)) {
                allExpenseForUser.add(expenseRecord);
            }
        }
        return allExpenseForUser;
    }

    public ArrayList<ExpenseRecord> getAllExpenseRecord() throws FileNotFoundException {

        File file = new File(dbExpense);
        Scanner expenseScanner = new Scanner(file);

        expense = new ArrayList<>();
        while (expenseScanner.hasNextLine()) {
            String username = expenseScanner.nextLine();
            float expenseAmount = expenseScanner.nextFloat();
            expenseScanner.nextLine();
            LocalDate expenseDate = LocalDate.parse(expenseScanner.nextLine());
            String expenseCategory = expenseScanner.nextLine();
            float cashBack = expenseScanner.nextFloat();
            expenseScanner.nextLine();
            expenseScanner.nextLine();

            expense.add(new ExpenseRecord(username, expenseAmount, expenseDate, expenseCategory, cashBack));
        }
        return expense;
    }

    public void newExpenseRegistration(ExpenseRecord expenseRecord) throws IOException {

        FileWriter expenseFw = new FileWriter(dbExpense, true);
        PrintWriter expenseWriter = new PrintWriter(expenseFw);

        expenseWriter.println(expenseRecord.getUsername());
        expenseWriter.println(expenseRecord.getExpenseAmount());
        expenseWriter.println(expenseRecord.getExpenseDate());
        expenseWriter.println(expenseRecord.getExpenseCategory());
        expenseWriter.println(expenseRecord.getCashBack());
        expenseWriter.println();
        expenseWriter.close();

    }

    public float expenseBalance(String username) throws FileNotFoundException {

        ArrayList<ExpenseRecord> expenseForUser = getExpenseForUser(username);

        float expensesAmount = 0;
        for (ExpenseRecord expenseRecord : expenseForUser)
            expensesAmount += expenseRecord.getExpenseAmount();

        return expensesAmount;
    }

    public void deleteLastExpense(String username) throws IOException {

        expense = getAllExpenseRecord();

        ArrayList<ExpenseRecord> expenseRecordsToDelete = getExpenseForUser(username);
        expense.remove(expenseRecordsToDelete.get(expenseRecordsToDelete.size()-1));

        overWriteExpense();
    }

    public void deleteAllExpenseByUsername(String username) throws IOException {

        expense = getAllExpenseRecord();

        ArrayList<ExpenseRecord> expenseRecordsDeleteByUser = getExpenseForUser(username);

        for (ExpenseRecord expenseRecord : expenseRecordsDeleteByUser) {
            expense.remove(expenseRecord);
        }
        overWriteExpense();

    }

    private void overWriteExpense() throws IOException {
        deleteExpense();
        for (ExpenseRecord expenseToDelete:expense){
            newExpenseRegistration(expenseToDelete);
        }
    }

    private void deleteExpense() throws IOException {
        new FileWriter(dbExpense,false);
    }
}
