package com.practiceproject.linkchat_back.adminLoginPageController;

import java.util.Scanner;
// This class is to register an admin

public class AdminRegistration {
    private UserService userService;
    private Scanner scanner;

    // Constructor
    public AdminRegistration(UserService userService, Scanner scanner) {
        this.userService = userService;
        this.scanner = scanner;
    }

    // Method to execute admin registration
    public void execute() {
        System.out.println("Enter admin username:");
        String username = scanner.nextLine();

        System.out.println("Enter admin password:");
        String password = scanner.nextLine();

        System.out.println("Enter admin email:");
        String email = scanner.nextLine();

        try {
            // Assuming UserService has a method to add a user
            userService.addUser(username, password, email);
            System.out.println("Admin registered successfully!");
        } catch (Exception e) {
            System.out.println("Error during admin registration: " + e.getMessage());
        }
    }
}