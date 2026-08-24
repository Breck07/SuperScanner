package com.ip.scanner;
public class Menu {
    public void displayMenu(){
        System.out.println("================================================");
        System.out.println("Welcome to the advanced recon toolkit!");
        System.out.println("================================================");

        System.out.println("Please select an option below to begin: ");
        displayOptions();
    }
    private static void displayOptions(){
        System.out.println("1. Scan IP");
        System.out.println("2. Scan Domain");
        System.out.println("3. Exit");
    }
    public void displayPrompt(){
        System.out.println();
        System.out.print("user@choice> ");
    }
    public void ipPrompt(){
        System.out.println("Enter IP to scan");
    }
    public void domainPrompt(){
        System.out.println("Enter a domain to scan(Ex. example.com)");
    }
}
