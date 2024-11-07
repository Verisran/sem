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
        int selection;

        //selection message, will be shown each time there is a selection to be made
        System.out.println(
                "\n\t---\tplease make the following selections\t---"
                + "\n 1 - Basic Population Queries\t 2 - All the x in y Queries"
                + "\n3 - top N queries\t 4 - Complex population queries"
                + "\n5 - reports\t 0 - quit\n\n"
        );

        Scanner sc = new Scanner(System.in);
        System.out.print('>');
        selection = sc.nextInt();
        switch (selection) {

            case 1: // basic population queries

                System.out.println("\n\t>>>1\t basic population queries selected..."
                    + "\n 1 - Population of the world.\t 2 - Population of a Continent"
                    + "\n 3 - Population of a region. \t 4 - Population of a country"
                    + "\n 5 - population of a city"
                );
                break;

            case 2: // all x in y
                System.out.println("\n\t>>>2\t all x in y queries selected..."
                        + "\n 1 - All countries in the world.\t 2 - All countries in a continent"
                        + "\n 3 - All countries in a region. \t 4 - All Cities in the world"
                        + "\n 5 - All cities in a country. \t 6 - All cities in a continent"
                        + "\n 7 - All cities in a region. \t 8 - All cities in a country"
                        + "\n\n----\tcurrently unavailable for code review 2\t----"
                        + "\n 9 - All capital cities in the world. \t 6 - All capital cities in a continent"
                        + "\n 10 - ALl capital cities in a region"
                );
                break;

            case 3: //top N queries
                System.out.println("\n\t>>>3\t top N queries selected..."
                        + "\n\n----\tcurrently unavailable for code review 2\t----"
                        + "\n 1 - Top 'N' populated capital cities in the world.\t 2 - Top 'N' populated capital cities of a Continent"
                        + "\n 3 - Top 'N' populated capital cities in a region. \t 4 - Top 'N' populated cities in the world."
                        + "\n 5 - Top 'N' populated cities in a continent \t 6 - Top 'N' populated cities in a region"
                        + "\n 7 - Top 'N' populated cities in a country \t 8 - Top 'N' populated cities in a district"
                        + "\n 9 - Top 'N' populated countries in the world \t 9 - Top 'N' populated countries in a Continent"
                        + "\n 10 - top 'N' populated countries in a region."
                );

                break;

            case 4: //complex population queries
                System.out.println("\n\t>>>4\t Complex population queries selected..."
                        + "\n\n----\tcurrently unavailable for code review 2\t----"
                        + "\n 1 - population of people, people living in cities, and people not living in cities in each continent."
                        + "\n 2 - population of people, people living in cities, and people not living in cities in each region."
                        + "\n 3 - population of people, people living in cities, and people not living in cities in each country."
                        + "\n 4 - No of people that speak specific languages and their % of world population"
                );
                break;

            case 5: //reports.
                System.out.println("\n\t>>>5\t Report Generation Selected..."
                        + "\n 1 - Countries report.\t 2 - City report"
                        + "\n 3 - Capital City Report. \t 4 - Population report (country)"
                        + "\n 5 - Population report (region). \t 6- Population report (continent)"
                );
                break;

            case 0: // exit
                System.out.println("\n\t>>>0\t Good bye.");
                exit = true;
                break;

            default: // error
                System.out.println("\n\t>>>Invalid selection! Try again.");
                break;

        } // switch end

        return exit;
    }
}