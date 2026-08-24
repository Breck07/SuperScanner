package com.ip.scanner;

import java.util.Scanner;

public class InputHandler {
    private static final Scanner INPUT_SCANNER = new Scanner(System.in);

    public int getIntInput(){
        int input = INPUT_SCANNER.nextInt();
        INPUT_SCANNER.nextLine();

        return input;
    }

    public String getStringInput(){
        return INPUT_SCANNER.nextLine().trim().replaceAll("[\\r\\n]", "");
    
    }
    public String getIP(){
        return INPUT_SCANNER.nextLine();
    }
}
