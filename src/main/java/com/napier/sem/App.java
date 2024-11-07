//test connect to sql database from lab

package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

public class App
{
    //main of app
    public static void main(String[] args)
    {
        // introduction to application running
        System.out.println("---\tGROUP 33 SEM DATABASE INTERFACE\t---");
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        // Connection to the database
        Connection con = null;
        int retries = 100;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("\n---\tConnecting to database...\t---");
            try
            {
                // Wait a bit for db to start
                // NOTE FROM OD: reduced thread.sleep as a test, can be kept to stop the app from spamming us with connection attempts, takes about 18 retries on my system
                Thread.sleep(10000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://db:3306/world?useSSL=false", "root", "world");
                System.out.println("---\tSuccessfully connected!\t---");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }

        if (con != null) // connection is available on database
        {
            System.out.println("notice: selection is wip and certain queries don't work.\n\n\n---\tWELCOME TO THE DATABASE INTERFACE!\t---");
            boolean closed = false; // loop until exited out of menu

            while (!closed) {
                closed = menu(); // goes throw menu selection
            }

            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    public static boolean menu() {
        boolean exit = false;
        int selection = 0;

        //selection message, will be shown each time there is a selection to be made
        System.out.println(
                "\n\t---\tplease make the following selections\t---"
                + "\n 1 - Basic Population Queries\t 2 - All the x in y Queries"
                + "\n3 - top N queries\t 4 - Complex population queries"
                + "\n5 - reports\t 0 - quit"
        );
        Scanner sc = new Scanner(System.in);
        selection = sc.nextInt();
        switch (selection) {
            case 1: // basic population queries

                break;

            case 2: // all x in y

                break;

            case 3: //top N queries

                break;

            case 4: //complex population queries

                break;

            case 5: //reports.

                break;

            case 0: // exit
                exit = true;
                break;

            default: // error
                System.out.println("Invalid selection! Try again.");
                break;
        }

        return exit;
    }
}