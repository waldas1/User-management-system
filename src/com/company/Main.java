package com.company;

import com.company.Income.IncomeDataBase;
import com.company.Income.IncomeRecord;
import com.company.CashBack.CashBack;
import com.company.CashBack.CashBackDataBase;
import com.company.Expense.ExpenseRecord;
import com.company.Expense.ExpensesDataBase;
import com.company.Users.DataBase;
import com.company.Users.User;
import com.company.exception.CashBackException;
import com.company.exception.UserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        DataBase dataBase = new DataBase("src/com/company/files/database.txt");
        IncomeDataBase incomeDataBase = new IncomeDataBase("src/com/company/files/IncomeRecord.txt");
        ExpensesDataBase expensesDataBase = new ExpensesDataBase("src/com/company/files/ExpenseRecord.txt");
        CashBackDataBase cashBackDataBase = new CashBackDataBase("src/com/company/files/cashBack.txt");


        Scanner sc = new Scanner(System.in);

        String input = "";

        while (!input.equals("3")) {

            mainMenu();

            input = sc.nextLine();

            switch (input) {

                case "1":

                    System.out.print("Username: ");
                    String logInUsername = sc.nextLine();

                    System.out.print("Password: ");
                    String logInPassword = sc.nextLine();

                    try {
                        User user = dataBase.getUser(logInUsername,logInPassword);
                        if (user.getRole().equals(Role.ADMIN)) {
                            System.out.println("Admin menu");

                            adminMenu(user, dataBase,incomeDataBase,expensesDataBase,cashBackDataBase);
                        }else if (user.getRole().equals(Role.SIMPLE)) {
                            System.out.println("Simple user menu");

                            simpleUserMenu(user,incomeDataBase,expensesDataBase,cashBackDataBase);
                        }
                    } catch (UserException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "2":

                    System.out.print("Insert username: ");
                    String username = sc.nextLine();
                    System.out.print("Insert password: ");
                    String password = sc.nextLine();
                    System.out.print("Insert name: ");
                    String name = sc.nextLine();
                    System.out.print("Insert surname: ");
                    String surname = sc.nextLine();
                    System.out.print("Insert age: ");
                    int age = sc.nextInt();
                    sc.nextLine();

                    try {
                        User newSavedUser=dataBase.addUser(new User(username,password, Role.SIMPLE,name,surname,age));
                        System.out.println("User added successful!");
                        simpleUserMenu(newSavedUser,incomeDataBase,expensesDataBase,cashBackDataBase);
                    } catch (UserException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "3":
                    break;
                default:
                    System.out.println("Wrong input!");
                    break;
            }
        }
    }

    private static void adminMenu(User user, DataBase dataBase,IncomeDataBase incomeDataBase,ExpensesDataBase expensesDataBase,CashBackDataBase cashBackDataBase) throws IOException {

        Scanner adminScanner = new Scanner(System.in);

        String adminInput = "";

        while (!adminInput.equals("5")){

            printAdminMenu();
            adminInput = adminScanner.nextLine();

            switch (adminInput){

                case "1":
                    printAllUsers(user);
                    break;
                case "2":

                    ArrayList<User> allUsers = dataBase.getAllUser();

                    System.out.println("---------------------------");

                    for (User allUser: allUsers){
                        printAllUsers(allUser);
                        System.out.println();
                        System.out.println("---------------------------");
                    }
                    break;
                case "3":

                    System.out.print("Insert username: ");
                    String username = adminScanner.nextLine();
                    System.out.print("Insert password: ");
                    String password = adminScanner.nextLine();
                    Role role = getRole();
                    System.out.print("Insert name: ");
                    String name = adminScanner.nextLine();
                    System.out.print("Insert surname: ");
                    String surname = adminScanner.nextLine();
                    System.out.print("Insert age: ");
                    int age = adminScanner.nextInt();
                    adminScanner.nextLine();

                    try {
                        dataBase.addUser(new User(username,password,role,name,surname,age));
                        System.out.println("That user already exist!");
                    } catch (UserException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "4":

                    System.out.println("Input username that you want to delete");
                    String usernameToDelete = adminScanner.nextLine();

                    try {
                        dataBase.deleteUserByUsername(usernameToDelete);
                        cashBackDataBase.deleteAllUsersCashBackIfFull(usernameToDelete);
                        incomeDataBase.deleteAllIncomeByUsername(usernameToDelete);
                        expensesDataBase.deleteAllExpenseByUsername(usernameToDelete);
                        System.out.println("User successful deleted!");
                    } catch (UserException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "5":

                    System.out.println("Exiting Admin menu!");
                    break;
                default:

                    System.out.println("Wrong input!");
                    break;
            }
        }
    }

    private static void printAllUsers(User user) {
        System.out.println("Username: " + user.getUsername());
        System.out.println("Password: " + user.getPassword());
        System.out.println("Name: " + user.getName());
        System.out.println("Surname: " + user.getSurname());
        System.out.println("Age: " + user.getAge());
    }

    private static void simpleUserMenu(User user, IncomeDataBase incomeDataBase,ExpensesDataBase expensesDataBase,CashBackDataBase cashBackDataBase) throws IOException, UserException {
        Scanner simpleScanner = new Scanner(System.in);
        String simpleInput = "";
        while (!simpleInput.equals("5")){
            printSimpleUserMenu();
            simpleInput = simpleScanner.nextLine();

            switch (simpleInput){
                case "1":
                    getUserInformation(user,incomeDataBase,expensesDataBase);
                    break;
                case "2":
                    userBalance(user,incomeDataBase,expensesDataBase,cashBackDataBase);
                    break;
                case "3":
                    System.out.println("Enter the name for which you want to send money: ");
                    String moneySendUser = simpleScanner.nextLine();

                    String expenseUsername = user.getUsername();
                    float expenseAmount;
                    while (true) {
                        try {
                            System.out.println("Insert amount:  ");
                            expenseAmount = simpleScanner.nextFloat();
                            break;
                        }catch (InputMismatchException e){
                            System.out.println("Input mismatch!");
                        }
                    }
                    LocalDate eDate = LocalDate.now();
                    simpleScanner.nextLine();


                    if ((incomeDataBase.incomeBalance(expenseUsername)-expenseAmount)<0){
                        System.out.println("You don't have enough money!");
                    } else {
                        expensesDataBase.newExpenseRegistration(new ExpenseRecord(expenseUsername, expenseAmount, eDate, "Sent money", 0));
                        incomeDataBase.newIncomeRegistration(new IncomeRecord(moneySendUser,expenseAmount,eDate,"Sent money by "+expenseUsername));
                        System.out.println("Successfully. Money sent to "+moneySendUser);
                    }
                    break;
                case "4":
                    System.out.println("--------------------");
                    System.out.println("All your money will be saved, until you have 100."+"\n"+"Then all money will be send automatically to income!!! ");
                    System.out.println("Username: "+ user.getUsername());

                    try {
                        cashBackDataBase.checkIfCashBackFullAndDelete(user.getUsername(),incomeDataBase);
                        System.out.println("Money successful transferred to income!");
                        System.out.println("Cash back: " + cashBackDataBase.getAllCashBackByUser(user.getUsername()));
                    } catch (CashBackException e) {
                        System.out.println(e.getMessage());
                    }
                    System.out.println("--------------------");
                    break;
                case "5":
                    System.out.println("Exiting Simple user menu!");
                    break;
                default:
                    System.out.println("Wrong input!");
                    break;
            }
        }
    }

    private static void userBalance(User user,IncomeDataBase incomeDataBase,ExpensesDataBase expensesDataBase,CashBackDataBase cashBackDataBase) throws IOException {

        Scanner balanceScanner = new Scanner(System.in);
        String balanceInput = "";
        while (!balanceInput.equals("7")){
            printBalanceMenu();
            balanceInput = balanceScanner.nextLine();

            switch (balanceInput){
                case "1":

                    addNewIncome(user,incomeDataBase);
                    break;
                case "2":
                    addNewExpense(user,incomeDataBase,expensesDataBase,cashBackDataBase);
                    break;
                case "3":
                    ArrayList<IncomeRecord> incomeRecords = incomeDataBase.getAllIncomeRecord();

                    System.out.println("---------------------------");

                    for (IncomeRecord incomeRecord1: incomeRecords){
                        if (user.getUsername().equals(incomeRecord1.getUsername())) {
                            System.out.println(incomeRecord1);
                            System.out.println();
                            System.out.println("---------------------------");
                        }
                    }
                    break;
                case "4":
                    ArrayList<ExpenseRecord> expenseRecords = expensesDataBase.getAllExpenseRecord();

                    System.out.println("---------------------------");

                    for (ExpenseRecord expenseRecords1 : expenseRecords){
                        if (user.getUsername().equals(expenseRecords1.getUsername())){
                            System.out.println(expenseRecords1);
                            System.out.println();
                            System.out.println("---------------------------");
                        }
                    }
                    break;
                case "5":

                    incomeDataBase.deleteLastIncome(user.getUsername());
                    break;
                case "6":

                    expensesDataBase.deleteLastExpense(user.getUsername());
                    break;
                case "7":
                    System.out.println("Exiting balance menu!");
                    break;
                default:
                    System.out.println("Wrong input!");
                    break;

            }
        }
    }


    private static void getUserInformation(User user,IncomeDataBase incomeDataBase,ExpensesDataBase expensesDataBase) throws FileNotFoundException {
        System.out.println("Username: " + user.getUsername());
        System.out.println("Name: " + user.getName());
        System.out.println("Surname: "+ user.getSurname());
        System.out.println("Balance: " + getBalance(user,incomeDataBase,expensesDataBase));
        System.out.println("Age: "+ user.getAge());
    }

    private static Role getRole(){
        Scanner roleScanner = new Scanner(System.in);
            String roleInput = "";

            while (true){
                System.out.println("Please select the role: ");
                System.out.println("[1] Admin");
                System.out.println("[2] Simple");
                roleInput = roleScanner.nextLine();
                switch (roleInput){
                    case "1":
                        return Role.ADMIN;
                    case "2":
                        return Role.SIMPLE;
                    default:
                        System.out.println("Wrong input!");
                        break;
                }
            }
    }

    private static void addNewExpense(User user,IncomeDataBase incomeDataBase,ExpensesDataBase expensesDataBase,CashBackDataBase cashBackDataBase) throws IOException{

        Scanner sc = new Scanner(System.in);

        String expenseUsername = user.getUsername();

        float expenseAmount;
        while (true) {
            try {
                System.out.println("Insert amount:  ");
                expenseAmount = sc.nextFloat();
                break;
            }catch (InputMismatchException e){
                System.out.println("Input mismatch!");
            }
        }

        LocalDate eDate = LocalDate.now();
        sc.nextLine();
        System.out.println("Insert category: ");
        String expenseCategory = sc.nextLine();


        if ((incomeDataBase.incomeBalance(expenseUsername)-expenseAmount)<0){
            System.out.println("Your income cant be 0 after payment!");
        }else {
            float cashback = (expenseAmount * 8) / 100;
            expensesDataBase.newExpenseRegistration(new ExpenseRecord(expenseUsername, expenseAmount, eDate, expenseCategory, cashback));
            System.out.println("Expense added!");
            cashBackDataBase.newCashBack(new CashBack(expenseUsername,cashback));
        }
    }

    private static float getBalance(User user, IncomeDataBase incomeDataBase, ExpensesDataBase expensesDataBase) throws  FileNotFoundException {
        float sumOfIncomeBalance = incomeDataBase.incomeBalance(user.getUsername());
        float sumOfExpenseBalance = expensesDataBase.expenseBalance(user.getUsername());
        return sumOfIncomeBalance - sumOfExpenseBalance;
    }

    private static void addNewIncome(User user,IncomeDataBase incomeDataBase) throws IOException {

        Scanner sc = new Scanner(System.in);

        String incomeUsername = user.getUsername();
        float incomeAmount;
        while (true) {
            try {
                System.out.println("Insert amount:  ");
                incomeAmount = sc.nextFloat();
                break;
            }catch (InputMismatchException e){
                System.out.println("Input mismatch!");
            }
        }
        LocalDate date = LocalDate.now();
        sc.nextLine();
        System.out.println("Insert category: ");
        String incomeCategory = sc.nextLine();

        incomeDataBase.newIncomeRegistration(new IncomeRecord(incomeUsername,incomeAmount,date,incomeCategory));
    }

    private static void printBalanceMenu() {
        System.out.println("[1] Add income");
        System.out.println("[2] Add expense");
        System.out.println("[3] All income history");
        System.out.println("[4] All expense history");
        System.out.println("[5] Delete income record");
        System.out.println("[6] Delete expense record");
        System.out.println("[7] Exit");
    }

    private static void printAdminMenu() {
        System.out.println("[1] Get admin information");
        System.out.println("[2] Get all users information");
        System.out.println("[3] Add new user");
        System.out.println("[4] Delete user");
        System.out.println("[5] Exit");
    }
    private static void printSimpleUserMenu() {
        System.out.println("[1] Get your information");
        System.out.println("[2] Balance");
        System.out.println("[3] Send money");
        System.out.println("[4] Cash back info");
        System.out.println("[5] EXIT");
    }

    private static void mainMenu() {
        System.out.println("[1] Log in");
        System.out.println("[2] Registration");
        System.out.println("[3] EXIT");
    }

}
