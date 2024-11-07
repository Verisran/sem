//test connect to sql database from lab

package com.napier.sem;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class App
{
    //main of app
    public static void main(String[] args)
    {
        App app = new App();
        // introduction to application running
        System.out.println("---\tGROUP 33 SEM DATABASE INTERFACE\t---");

        String location;
        int delay;
        if(args.length < 1)
        {
            location ="localhost:33060";
            delay = 10000;
        }
        else
        {
            location = args[0];
            delay = Integer.parseInt(args[1]);
        }
        Connection con = app.connect(location, delay);
        System.out.println("notice: selection is wip and certain queries don't work.\n\n\n---\tWELCOME TO THE DATABASE INTERFACE!\t---");
        if(con == null){return;}
        app.menu(con);

        Scanner scan = new Scanner(System.in);
        int sel = Integer.parseInt(scan.nextLine());

        try {
            // Close connection
            con.close();}
        catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
    }

    public void menu(Connection con) {
        boolean exit = false;
        int selection = 0; //temp
        while (!exit)
        {
            //selection message, will be shown each time there is a selection to be made
            System.out.println(
                    "\n\t---\tplease make the following selections\t---"
                            + "\n1 - Basic Population Queries\t 2 - All the x in y Queries"
                            + "\n3 - top N queries\t 4 - Complex population queries"
                            + "\n5 - reports\t 0 - quit\n\n"
            );
            System.out.print("> ");
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
                    System.out.println("\n\t>>>0\tGood bye.");
                    exit = true;
                    break;

                default: // error
                    System.out.println("\n\t>>>Invalid selection! Try again.");
                    break;
            }
        }
    }

    public Connection connect(String location, int delay) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        boolean shouldWait = false;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                if (shouldWait) {
                    // Wait a bit for db to start
                    Thread.sleep(delay);
                }
                //jdbc:mysql://docker-mysql/database?autoReconnect=true&useSSL=false
                // Connect to database
                Connection con = DriverManager.getConnection("jdbc:mysql://" + location + "/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "world");
                System.out.println("Successfully connected");
                return con;
            }
            catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());
                // Let's wait before attempting to reconnect
                shouldWait = true;
            }
            catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
        return null;
    }
}