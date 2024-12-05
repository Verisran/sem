//test connect to sql database from lab

package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App
{
    //Parameters
    private boolean test_mode = false;
    public boolean get_test_mode() {
        return test_mode;
    }

    //Constructor
    //override test mode
    public App(boolean do_test){
        test_mode = do_test;
    }
    //default no change to test_mode
    public App(){
    }

    //==================================MAIN==================================\\
    public static void main(String[] args)
    {
        App app = new App();

        String location;
        int delay;
        //EXIT OVERRIDE FOR DOCKER COMPOSE CUS IT LOOPS INFINITELY THERE!
        boolean using_compose;
        //String user = "";
        //String pass = "";

        if(args.length < 1)
        {
            location ="localhost:33060";
            delay = 10000;
            using_compose = false;
            //user = "root";
            //pass = "world";
        }
        else
        {
            location = args[0];
            delay = Integer.parseInt(args[1]);
            using_compose = true;
            //user = args[2];
            //pass = args[3];
        }
        Connection con = app.connect(location, delay); //use defUser and pass

        System.out.println("app.test_mode");

        if(con == null || app.test_mode || using_compose){
            System.out.println("Menu skipped due to: \n-Testmode:" + app.test_mode + "\nConnection failure: " + (con == null) + "\nUsing compose: " + using_compose);
            return;
        }
        app.menuQueries(con);


        try {
            // Close connection
            con.close();}
        catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
    }


    //====================================FUNCTIONS====================================\\

    public int menuSelection(){
        return menuSelection(-1);
    }

    public int menuSelection(int selection)
    {
        //selection message, will be shown each time there is a selection to be made
        System.out.println(
                "\n\t---\tplease make the following selections\t---"
                        + "\n1 - Basic Population Queries\t 2 - All the x in y Queries"
                        + "\n3 - top N queries\t 4 - Complex population queries"
                        + "\n5 - reports\t 0 - quit\n\n"
        );
        System.out.print("> ");
        if(selection == -1) {
            selection = getMenuInput();
        }
        return selection;
    }

    //leave each query selection thingy empty "" as such if not needed in the query, kinda redundant but ehh.
    public String menuQueryBuilder(String select, String from, String where, String order){
        String final_query = "";

        if(!select.isEmpty()) {
            final_query += "SELECT " + select;
        }
        if(!from.isEmpty()){
            final_query += " FROM " + from;
        }
        if(!where.isEmpty()) {
            final_query += " WHERE " + where;
        }
        if(!order.isEmpty()) {
            final_query += " ORDER BY " + order;
        }
        return final_query;
    }


    public void menuQueries(Connection con) {
        System.out.println("---\tGROUP 33 SEM DATABASE INTERFACE\t---");
        System.out.println("notice: selection is wip and certain queries don't work.\n\n\n---\tWELCOME TO THE DATABASE INTERFACE!\t---");
        boolean exit = false;
        while (!exit)
        {
            int selection = menuSelection();
            switch (selection) {
                case 1: // basic population queries

                    System.out.println("\n>>> 1\t basic population queries selected..."
                            + "\n 1 - Population of the world.\t 2 - Population of a Continent"
                            + "\n 3 - Population of a region. \t 4 - Population of a country"
                            + "\n 5 - population of a city. \t 6 - Population of a district"
                    );

                    switch (getMenuInput()) {
                        case 1: //world population
                            try {
                                ResultSet result = queryHelper(con, menuQueryBuilder("SUM(Population)", "country", "",""));
                                System.out.println("population of the world is: " + resultToStringParser(result) + " people\n");

                            } catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;
                        case 2: // continent
                            try {
                                System.out.println("Please enter a Continent: ");
                                String continent = getStringInput();
                                ResultSet result = queryHelper(con, menuQueryBuilder("SUM(Population)","country" ,"Continent = '" + continent + "'", "") );
                                System.out.println("population of "+ continent + " is: " + resultToStringParser(result) + " people\n");

                            } catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;
                        case 3: //region population
                            try {
                                System.out.println("Please enter a Region: ");
                                String region = getStringInput();
                                ResultSet result = queryHelper(con, menuQueryBuilder("SUM(Population)", "country", "Region = '" + region + "'", ""));
                                String pop = resultToStringParser(result).get(0);
                                System.out.println("population of " + region + " is: " + pop + " people\n");

                            } catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            exit = true;
                            break;
                        case 4: //Country population
                            try {
                                System.out.println("Please enter a Country: ");
                                String country = getStringInput();

                                ResultSet result = queryHelper(con, menuQueryBuilder("Population", "country", "Name = '" + country + "'", ""));
                                String pop = resultToStringParser(result).get(0);
                                System.out.println("population of "+ country + " is: " + pop + " people\n");

                            } catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            exit = true;
                            break;
                        case 5: // city population
                            try {
                                System.out.println("Please enter a City: ");
                                String city = getStringInput();

                                ResultSet result = queryHelper(con, menuQueryBuilder("Population", "city", "Name = '" + city + "'", ""));
                                String pop = resultToStringParser(result).get(0);
                                System.out.println("The population for "+ city + " is " + pop + "\n");

                            }
                            catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;
                        case 6: // district population
                            try {
                                String district = getStringInput();
                                ResultSet result = queryHelper(con, menuQueryBuilder("Population","city", "District = '" + district + "'", ""));
                                if (result.next()) {
                                    String pop = resultToStringParser(result).get(0);
                                    System.out.println("The population for "+ district + " is " + pop + "\n");
                                }
                            }
                            catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        default:
                            System.out.println("\nInvalid selection, how rude...\n\n");
                            break;
                    }
                    break;

                //-----------------------------NEED TO BE ADAPTED TO NEWEST METHODS-----------------------------
                //-----------------------------ISSUE WITH RESULT SET TO STRING-----------------------------------
                // not appropriate
                case 2: // all x in y
                    System.out.println("\n\t>>>2\t all x in y queries selected..."
                            + "\n 1 - All countries in the world.\t 2 - All countries in a continent"
                            + "\n 3 - All countries in a region. \t 4 - All Cities in the world"
                            + "\n 5 - All cities in a country. \t 6 - All cities in a continent"
                            + "\n 7 - All cities in a region. \t 8 - All cities in a district"
                            + "\n\n----\tcurrently unavailable for code review 3\t----"
                            + "\n 9 - All capital cities in the world. \t 10 - All capital cities in a continent"
                            + "\n 11 - ALl capital cities in a region"
                    );

                    switch (getMenuInput())
                    {
                        case 1: // all countries in the world
                            try {
                                ResultSet result = queryHelper(con, "SELECT Name, Population FROM country ORDER BY Population DESC");
                                List<String> resultList = resultToStringParser(result);
                                for (String s : resultList) {
                                    System.out.println(s);
                                }

                            } catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;
                        case 2: // all countries in a continent
                            try {
                                String continent = getStringInput();
                                ResultSet result = queryHelper(con, menuQueryBuilder("Name, Population", "country", "Continent = ' "+ continent + "'", "Population DESC"));
                                List<String> resultList = resultToStringParser(result);
                                for (String s : resultList) {
                                    System.out.println(s);
                                }
                            } catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 3: // all countries in a region
                            try {
                                String region = getStringInput();
                                ResultSet result = queryHelper(con, menuQueryBuilder("Name, Population", "country", "Region = ' "+ region + "'", "Population DESC"));
                                List<String> resultList = resultToStringParser(result);
                                for (String s : resultList) {
                                    System.out.println(s);
                                }
                            } catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 4: // all Cities in the world
                            try {
                                ResultSet result = queryHelper(con, "SELECT Name, Population FROM city ORDER BY Population DESC");
                                while (result.next()){
                                    String city = result.getString("Name");
                                    System.out.println(city);
                                }
                            } catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 5: // all cities in a country
                            try {
                                String country = getStringInput();
                                ResultSet result = queryHelper(con, "SELECT Name, Population FROM city WHERE CountryCode" +
                                        " = (SELECT Code FROM country WHERE Name = '" + country + "') ORDER BY Population DESC");
                                while (result.next()){
                                    country = result.getString("Name");
                                    System.out.println(country);
                                }
                            } catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 6: // all cities in a continent
                            try {
                                String continent = getStringInput();
                                ResultSet result = queryHelper(con, "SELECT Name, Population FROM city WHERE CountryCode" +
                                        " = (SELECT Code FROM country WHERE Continent = '" + continent + "') ORDER BY Population DESC");
                                while (result.next()){
                                    String country = result.getString("Name");
                                    System.out.println(country);
                                }
                            } catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;


                        case 7: // all cities in a region
                            try {
                                String region = getStringInput();
                                ResultSet result = queryHelper(con, "SELECT Name, Population FROM city WHERE CountryCode" +
                                        " = (SELECT Code FROM country WHERE Region = '" + region + "') ORDER BY Population DESC");
                                while (result.next()){
                                    String country = result.getString("Name");
                                    System.out.println(country);
                                }
                            } catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 8: // all cities in a district
                            try {
                                String district = getStringInput();
                                ResultSet result = queryHelper(con, "SELECT Name, Population FROM city WHERE District = '" +  district + "' ORDER BY Population DESC");
                                while (result.next()){
                                    String country = result.getString("Name");
                                    System.out.println(country);
                                }
                            } catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        // ignore past here for code rev 2...
                        case 9: // all capital cities in the world
                            try {
                                ResultSet result = queryHelper(con, "SELECT Name FROM city WHERE ID = " +  " = (SELECT Capital FROM country)");
                                while (result.next()){
                                    String country = result.getString("Name");
                                    System.out.println(country);
                                }
                            }catch (Exception e){
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 10: // all capital cities in a continent
                            try {
                                String continent = getStringInput();
                                ResultSet result = queryHelper(con, "SELECT Name FROM city WHERE ID = " + " = (SELECT Capital FROM country WHERE Continent = '" + continent);
                                while (result.next()){
                                    String country = result.getString("Name");
                                    System.out.println(country);
                                }
                            }catch (Exception e){
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 11: // all capital cities in a region
                            try {
                                String region = getStringInput();
                                ResultSet result = queryHelper(con, "SELECT Name FROM city WHERE ID = " + " = (SELECT Capital FROM country WHERE Region = '" + region + "'");
                                while (result.next()){
                                    String country = result.getString("Name");
                                    System.out.println(country);
                                }
                            }catch (Exception e){
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }

                            break;

                        default:
                            System.out.println("\nInvalid selection, how rude...\n\n");
                            break;
                    }
                    break;

                case 3: //top N queries
                    System.out.println("\n\t>>>3\t top N queries selected..."
                            + "\n\n----\tcurrently unavailable for code review 2\t----"
                            + "\n 1 - Top 'N' populated capital cities in the world.\t 2 - Top 'N' populated capital cities of a Continent"
                            + "\n 3 - Top 'N' populated capital cities in a region. \t 4 - Top 'N' populated cities in the world."
                            + "\n 5 - Top 'N' populated cities in a continent \t 6 - Top 'N' populated cities in a region"
                            + "\n 7 - Top 'N' populated cities in a country \t 8 - Top 'N' populated cities in a district"
                            + "\n 9 - Top 'N' populated countries in the world \t 10 - Top 'N' populated countries in a Continent"
                            + "\n 11 - top 'N' populated countries in a region."
                    );
                    switch (getMenuInput())
                    {
                        case 1: //Top 'N' populated capital cities in the world
                            try {
                                int N = getMenuInput();
                                ResultSet result = queryHelper(con, "SELECT city.Name FROM city JOIN country ON city.ID = country.Capital ORDER BY city.Population DESC LIMIT " + N);
                                while (result.next()) {
                                    String city = result.getString("city.Name");
                                    System.out.println(city);
                                }
                            } catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 2: //Top 'N' populated capital cities of a continent
                            try {
                                int N = getMenuInput();
                                String continent = getStringInput();
                                ResultSet result = queryHelper(con, "SELECT city.Name FROM city JOIN country ON city.ID = country.Capital WHERE country.continent = '" + continent + "' ORDER BY city.Population DESC LIMIT " + N);
                                while (result.next()) {
                                    String city = result.getString("city.Name");
                                    System.out.println(city);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 3: //Top 'N' populated capital cities in a region
                            try {
                                int N = getMenuInput();
                                String region = getStringInput();
                                ResultSet result = queryHelper(con, "SELECT city.Name FROM city  JOIN country ON city.ID = country.Capital WHERE country.region = '"+region+"' ORDER BY city.Population DESC LIMIT " + N);
                                while (result.next()) {
                                    String city = result.getString("city.Name");
                                    System.out.println(city);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 4: //Top 'N' populated cities in the world
                            try {
                                int N = getMenuInput();
                                ResultSet result = queryHelper(con, "SELECT Name FROM city ORDER BY Population DESC LIMIT " + N);
                                while (result.next()) {
                                    String city = result.getString("Name");
                                    System.out.println(city);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 5://Top 'N' populated cities in a continent
                            try {
                                int N = getMenuInput();
                                String continent = getStringInput();
                                ResultSet result = queryHelper(con, "SELECT city.Name FROM city JOIN country ON city.CountryCode = country.Code WHERE country.Continent = '" + continent + "' ORDER BY city.Population DESC LIMIT " + N);
                                while (result.next()) {
                                    String city = result.getString("city.Name");
                                    System.out.println(city);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 6://Top 'N' populated cities in a region
                            try {
                                int N = getMenuInput();
                                String region = getStringInput();
                                ResultSet result = queryHelper(con, "SELECT city.Name FROM city JOIN country ON city.CountryCode = country.Code WHERE country.Region = '"+region+"' ORDER BY city.Population DESC LIMIT " + N);
                                while (result.next()) {
                                    String city = result.getString("city.Name");
                                    System.out.println(city);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 7://Top 'N' populated cities in a country
                            try {
                                int N = getMenuInput();
                                String country = getStringInput();
                                ResultSet result = queryHelper(con, "SELECT city.Name FROM city JOIN country ON city.CountryCode = country.Code WHERE country.Name = '"+country+"' ORDER BY city.Population DESC LIMIT " + N);
                                while (result.next()) {
                                    String city = result.getString("city.Name");
                                    System.out.println(city);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 8://Top 'N' populated cities in a district
                            try {
                                int N = getMenuInput();
                                String district = getStringInput();
                                ResultSet result = queryHelper(con, "SELECT Name FROM city WHERE District = '"+district+"' ORDER BY Population DESC LIMIT " + N);
                                while (result.next()) {
                                    String city = result.getString("Name");
                                    System.out.println(city);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 9://Top 'N' populated countries in the world
                            try {
                                int N = getMenuInput();
                                ResultSet result = queryHelper(con, "SELECT Name FROM country ORDER BY Population DESC LIMIT " + N);
                                while (result.next()){
                                    String country = result.getString("Name");
                                    System.out.println(country);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 10://Top 'N' populated countries in a continent
                            try {
                                int N = getMenuInput();
                                String continent = getStringInput();
                                ResultSet result = queryHelper(con, "SELECT Name FROM country WHERE Continent = '"+continent+"' ORDER BY Population DESC LIMIT " + N);
                                while (result.next()){
                                    String country = result.getString("Name");
                                    System.out.println(country);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 11://Top 'N' populated countries in a region
                            try {
                                int N = getMenuInput();
                                String region = getStringInput();
                                ResultSet result = queryHelper(con, "SELECT Name FROM country WHERE Region = '" + region +"' ORDER BY Population DESC LIMIT " + N);
                                while (result.next()){
                                    String country = result.getString("Name");
                                    System.out.println(country);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;
                    }
                    break;

                case 4: //complex population queries
                    System.out.println("\n\t>>>4\t Complex population queries selected..."
                            + "\n\n----\tcurrently unavailable for code review 2\t----"
                            + "\n 1 - population of people, people living in cities, and people not living in cities in each continent."
                            + "\n 2 - population of people, people living in cities, and people not living in cities in each region."
                            + "\n 3 - population of people, people living in cities, and people not living in cities in each country."
                            + "\n 4 - No of people that speak specific languages and their % of world population"
                    );
                    switch (getMenuInput())
                    {
                        case 1://Pop of people, pop of people in cities and not in cities in each continent
                            try {
                                ResultSet result = queryHelper(con,"SELECT country.Continent, SUM(country.Population) AS 'Population', " +
                                        "SUM(city.Population) AS 'People in cities', (SUM(country.Population) - SUM(city.Population)) AS 'People not in cities' " +
                                        "FROM country JOIN city ON country.Code = city.CountryCode " +
                                        "GROUP BY country.Continent" );
                                while(result.next()){
                                    String continent = result.getString("Continent");
                                    String people = result.getString("Population");
                                    String peopleInCities = result.getString("People in cities");
                                    String peopleNotInCities = result.getString("People not in cities");
                                    System.out.println("Continent: " + continent + "\tPopulation: " + people + "\tPeople in cities: " + peopleInCities + "\tPeople not in cities: " + peopleNotInCities);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 2://Pop of people, pop of people in cities and not in cities in each region
                            try {
                                ResultSet result = queryHelper(con,"SELECT country.Region, SUM(country.Population) AS 'Population', " +
                                        "SUM(city.Population) AS 'People in cities', (SUM(country.Population) - SUM(city.Population)) AS 'People not in cities' " +
                                        "FROM country JOIN city ON country.Code = city.CountryCode " +
                                        "GROUP BY country.Region" );
                                while(result.next()){
                                    String region = result.getString("Region");
                                    String people = result.getString("Population");
                                    String peopleInCities = result.getString("People in cities");
                                    String peopleNotInCities = result.getString("People not in cities");
                                    System.out.println("Region: " + region + "\tPopulation: " + people + "\tPeople in cities: " + peopleInCities + "\tPeople not in cities: " + peopleNotInCities);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 3://Pop of people, pop of people in cities and not in cities in each country
                            try {
                                ResultSet result = queryHelper(con,"SELECT country.Name, SUM(country.Population) AS 'Population', " +
                                        "SUM(city.Population) AS 'People in cities', (SUM(country.Population) - SUM(city.Population)) AS 'People not in cities' " +
                                        "FROM country JOIN city ON country.Code = city.CountryCode " +
                                        "GROUP BY country.Name" );
                                while(result.next()){
                                    String country = result.getString("Name");
                                    String people = result.getString("Population");
                                    String peopleInCities = result.getString("People in cities");
                                    String peopleNotInCities = result.getString("People not in cities");
                                    System.out.println("Country Name: " + country + "\tPopulation: " + people + "\tPeople in cities: " + peopleInCities + "\tPeople not in cities: " + peopleNotInCities);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 4://Number of ppl speaking languages and % from most to least
                            try {
                                String querystr = "SELECT countrylanguage.Language, SUM(country.Population) AS PopulationNumber, CONCAT(ROUND(SUM(country.Population) / (SELECT SUM(Population) FROM country) * 100 , 1) , '%') " +
                                        "FROM countrylanguage " +
                                        "JOIN country ON countrylanguage.CountryCode = country.Code " +
                                        "WHERE countrylanguage.Language = 'English' " +
                                        "OR countrylanguage.Language = 'Chinese' " +
                                        "OR countrylanguage.Language = 'Hindi' " +
                                        "OR countrylanguage.Language = 'Spanish' " +
                                        "OR countrylanguage.Language = 'Arabic' " +
                                        "GROUP BY countrylanguage.Language " +
                                        "ORDER BY PopulationNumber DESC";
                                ResultSet result = queryHelper(con, querystr);
                                List<String> resultList = resultToStringParser(result);
                                for (String s : resultList) {
                                    System.out.println(s);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;
                    }
                    break;

                case 5: //reports.
                    System.out.println("\n\t>>>5\t Report Generation Selected..."
                            + "\n 1 - Countries report.\t 2 - City report"
                            + "\n 3 - Capital City Report. \t 4 - Population report (country)"
                            + "\n 5 - Population report (region). \t 6- Population report (continent)"
                    );

                    switch(getMenuInput()){
                        case 1: // country report
                            try {
                                String country = getStringInput();
                                ResultSet rs = queryHelper(con, "SELECT Code, Name, Continent, Region, Population, Capital  FROM country WHERE Name = '" + country + "'");
                                if (rs.next()) {
                                    String code = rs.getString("Code");
                                    String name = rs.getString("Name");
                                    String reg = rs.getString("Region");
                                    String pop = rs.getString("Continent");
                                    String capID = rs.getString("Capital");
                                    System.out.println("Code: " + code + "\tName:" + name + "\tRegion:" + reg + "\tPopulation:" + pop + "\tCapital City ID:" + capID);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 2: // city report
                            try {
                                String city = getStringInput();
                                ResultSet rs = queryHelper(con, "SELECT city.Name AS `City`, country.Name AS `Country`, District, city.Population FROM city " +
                                        "JOIN country ON city.CountryCode = country.Code WHERE city.Name = '" + city + "'");
                                if (rs.next()) {
                                    String cityName = rs.getString("City");
                                    String countryName = rs.getString("Country");
                                    String district = rs.getString("District");
                                    String pop = rs.getString("city.Population");
                                    System.out.println("City: " + cityName + "\tCountry:" + countryName + "\tDistrict:" + district + "\tPopulation:" + pop);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 3: // capital city report
                            try {
                                String city = getStringInput();
                                ResultSet rs = queryHelper(con, "SELECT city.Name, country.Name, District, city.population FROM city " +
                                        "JOIN country ON city.CountryCode = country.Code WHERE city.Name = '" + city + "' AND country.Capital = city.ID");
                                if (rs.next()) {
                                    String cityName = rs.getString("city.Name");
                                    String countryName = rs.getString("country.Name");
                                    String pop = rs.getString("city.Population");
                                    System.out.println("City: " + cityName + " Country:" + countryName + " Population:" + pop);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 4: //Population report country
                            try {
                                String country = getStringInput();
                                ResultSet rs = queryHelper(con, "SELECT country.Name, SUM(country.Population) AS TotalPopulation, CONCAT(ROUND(SUM(city.Population) / SUM(country.Population) * 100 ,1), '%') AS PercentageInCities," +
                                        "CONCAT(ROUND((SUM(country.Population) - SUM(city.Population)) / SUM(country.Population) * 100, 1), '%') AS PercentageNotInCities" +
                                        "FROM country " +
                                        "JOIN city ON country.Code = city.CountryCode" +
                                        "WHERE country.Name = '" + country + "' GROUP BY country.Name");
                                if (rs.next()) {
                                    String countryName = rs.getString("country.Name");
                                    String totalPop = rs.getString("TotalPopulation");
                                    String perInCities = rs.getString("PercentageInCities");
                                    String perNotInCities = rs.getString("PercentageNotInCities");
                                    System.out.println("Country: " + countryName + " Population: " + totalPop + "Percentage living in cities: " + perInCities + "Percentage not living in cities: " + perNotInCities);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 5: //Population report region
                            try {
                                String region = getStringInput();
                                ResultSet rs = queryHelper(con, "SELECT country.Region, SUM(country.Population) AS TotalPopulation, CONCAT(ROUND(SUM(city.Population) / SUM(country.Population) * 100 ,1), '%') AS PercentageInCities," +
                                        "CONCAT(ROUND((SUM(country.Population) - SUM(city.Population)) / SUM(country.Population) * 100, 1), '%') AS PercentageNotInCities" +
                                        "FROM country " +
                                        "JOIN city ON country.Code = city.CountryCode" +
                                        "WHERE country.Name = '" + region + "' GROUP BY country.Region");
                                if (rs.next()) {
                                    String countryRegion = rs.getString("country.Region");
                                    String totalPop = rs.getString("TotalPopulation");
                                    String perInCities = rs.getString("PercentageInCities");
                                    String perNotInCities = rs.getString("PercentageNotInCities");
                                    System.out.println("Country: " + countryRegion + " Population: " + totalPop + "Percentage living in cities: " + perInCities + "Percentage not living in cities: " + perNotInCities);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;

                        case 6: //Population report continent
                            try {
                                String continentInp = getStringInput();
                                ResultSet rs = queryHelper(con, "SELECT country.Continent, SUM(country.Population) AS TotalPopulation, CONCAT(ROUND(SUM(city.Population) / SUM(country.Population) * 100 ,1), '%') AS PercentageInCities," +
                                        "CONCAT(ROUND((SUM(country.Population) - SUM(city.Population)) / SUM(country.Population) * 100, 1), '%') AS PercentageNotInCities" +
                                        "FROM country " +
                                        "JOIN city ON country.Code = city.CountryCode" +
                                        "WHERE country.Name = '" + continentInp + "' GROUP BY country.Continent");
                                if (rs.next()) {
                                    String continent = rs.getString("country.Continent");
                                    String totalPop = rs.getString("TotalPopulation");
                                    String perInCities = rs.getString("PercentageInCities");
                                    String perNotInCities = rs.getString("PercentageNotInCities");
                                    System.out.println("Country: " + continent + " Population: " + totalPop + "Percentage living in cities: " + perInCities + "Percentage not living in cities: " + perNotInCities);
                                }
                            }catch (Exception e) {
                                System.out.println("error trying to do statement.." + e.getMessage());
                            }
                            break;
                    }
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
    //============================================================================\\

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
                //System.out.println("Please enter the username for the database: "); //default: root
                //String user = getStringInput();
                //System.out.println("Please enter the password for the database: "); //default: world
                //String password = getStringInput();
                String user = "root";
                String password = "world";
                //jdbc:mysql://docker-mysql/database?autoReconnect=true&useSSL=false
                // Connect to database
                Connection con = DriverManager.getConnection("jdbc:mysql://" + location + "/world?allowPublicKeyRetrieval=true", user, password);
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


    private static int getMenuInput()
    {
        Scanner scan = new Scanner(System.in);
        int input;
        try {
            input = scan.nextInt();
        }
        catch (Exception e) {
            input = -1;
        }

        return input;
    }

    private static String getStringInput(){
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }

    public ResultSet queryHelper(Connection con, String query_stmt) {
        Statement stmnt;
        try {
            stmnt = con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            return stmnt.executeQuery(query_stmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //Returns ALL columns of the inputted resultSet in order and accessible by using .get(index) on the result
    public List<String> resultToStringParser(ResultSet resultSet){

        try {
            //Allows for multiple columns
            List<String> queryResult = new ArrayList<>();
            //CatchNull
            if(resultSet == null){
                queryResult.add("Result Was Null");
                return queryResult;}

            //get metadata and colum count
            ResultSetMetaData rsMetaData = resultSet.getMetaData();
            int columns = rsMetaData.getColumnCount();
            while(resultSet.next()) {
                //Loops through all columns
                for (int i = 1; i <= columns; i++) {
                    //Dynamically Selects each type
                    int columnType = rsMetaData.getColumnType(i);
                    //int corresponds to Type
                    switch (columnType) {
                        case Types.CHAR:
                        case Types.VARCHAR:
                            queryResult.add(resultSet.getString(i));
                            break;
                        case Types.INTEGER:
                            queryResult.add(Integer.toString( resultSet.getInt(i) ));
                            break;
                        case Types.DECIMAL:
                            queryResult.add(Long.toString( resultSet.getLong(i) ));
                            break;
                        case Types.NUMERIC:
                            queryResult.add(Double.toString(resultSet.getDouble(i)) );
                            break;
                        case Types.FLOAT:
                            queryResult.add(Float.toString(resultSet.getFloat(i)) );
                            break;
                    }
                }
            }
            return queryResult;
        }
        catch (SQLException e) {
            System.out.println("SOME ERROR HAPPENED.");
            return null;
        }
    }
}