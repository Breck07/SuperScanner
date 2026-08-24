package com.ip.scanner;

import java.io.IOException;

public class App 
{
    private static final DomainAPI DOMAIN_API = new DomainAPI();
    private static final NmapAPI NMAP_API = new NmapAPI();
    private static final Menu MENU = new Menu();
    private static final InputHandler INPUT_HANDLER = new InputHandler();

    public static void main( String[] args ) throws IOException, InterruptedException
    {
        boolean isRunning = true;

        while(isRunning){
            MENU.displayMenu();
            MENU.displayPrompt();

            int input = INPUT_HANDLER.getIntInput();

            if(input == 3){
                System.out.println("Exiting the program...");
                isRunning = false;
            }

            if(isRunning){
                if(input == 1){
                    MENU.ipPrompt();
                    MENU.displayPrompt();

                    String ip = INPUT_HANDLER.getIP();
                    NMAP_API.callAPI(ip);

                }else if(input == 2){
                    MENU.domainPrompt();
                    MENU.displayPrompt();

                    String domain = INPUT_HANDLER.getStringInput();
                    DOMAIN_API.callAPI(domain);
                }else{
                    System.out.println("INVALID INPUT!");
                }
            }
        }
    }
}
