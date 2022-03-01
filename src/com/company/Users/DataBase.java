package com.company.Users;

import com.company.Role;
import com.company.exception.UserException;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DataBase {

    private final String dbpath;
    public ArrayList<User> user;
    public File file;
    public FileWriter fw;
    public PrintWriter writer;

    public DataBase(String dbpath) {

        this.dbpath = dbpath;
    }

    public User getUser(String username, String password) throws FileNotFoundException, UserException {
        user = getAllUser();
        for (User users : user) {
            if (users.getUsername().equals(username) && users.getPassword().equals(password)) {
                return users;
            }
        }
        throw new UserException("Wrong username or password!");
    }

    public ArrayList<User> getAllUser() throws FileNotFoundException {
        file = new File(dbpath);
        Scanner scanner = new Scanner(file);

        ArrayList<User> users = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String username = scanner.nextLine();
            String password = scanner.nextLine();
            String role = scanner.nextLine();
            String name = scanner.nextLine();
            String surname = scanner.nextLine();
            int age = scanner.nextInt();
            scanner.nextLine();
            scanner.nextLine();

            users.add(new User(username, password, Role.valueOf(role), name, surname, age));
        }
        return users;
    }

    public User addUser(User user) throws IOException, UserException {

        ArrayList<User> users = getAllUser();

        for (User userCheck : users ) {

            if (userCheck.getUsername().equals(user.getUsername())) {
                throw new UserException("This user already exist!");
            }
        }
        newRegistraision(user);
        return user;
    }

    private void newRegistraision(User user) throws IOException {

        fw = new FileWriter(dbpath, true);
        writer = new PrintWriter(fw);

        writer.println(user.getUsername());
        writer.println(user.getPassword());
        writer.println(user.getRole());
        writer.println(user.getName());
        writer.println(user.getSurname());
        writer.println(user.getAge());
        writer.println();
        writer.close();
    }

    public void deleteUserByUsername(String usernameToDelete) throws IOException, UserException {

        user = getAllUser();
        User userToDelete = getUserToDelete(usernameToDelete, user);
        user.remove(userToDelete);

        flushFile();
        for (User userToWrite : user) {
            newRegistraision(userToWrite);
        }
    }

    private User getUserToDelete(String usernameToDelete, ArrayList<User> users) throws UserException {

        for (User user : users) {
            if (user.getUsername().equals(usernameToDelete)) {
                if (!user.getRole().isDeletable()){
                    throw new UserException("User with ADMIN role cant be deleted!");
                }
                return user;
            }
        }
        throw new UserException("User with this: " + usernameToDelete+" not exist!");
    }

    private void flushFile() throws IOException {
        new FileWriter(dbpath,false);
    }

}