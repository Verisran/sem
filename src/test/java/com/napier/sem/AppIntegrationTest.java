package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest
{
    static App app;
    static Connection con;

    @BeforeAll
    static void init()
    {
        app = new App(true);
        con = app.connect("localhost:33060", 15000);

    }

    @Test
    void testConnectionSuccessful()
    {
        assertNotNull(con, "Connection was successful");
    }

    @Test
    void testConnectionSuccessfulEq() throws SQLException
    {
        assertEquals(con.isValid(300), true);
    }

    @Test
    void testInvalidConnection(){
        Connection new_con = app.connect("localhost:124144", 100);
        assertNull(new_con, "Connection was handled appropriately");
    }

    @Test
    void testQueryReturnData(){
        ResultSet result = app.queryHelper(con, app.menuQueryBuilder("SUM(Population)", "country", "",""));
        long parsedResult = Long.parseLong(app.resultToStringParser(result).get(0));
        assertEquals(6078749450L, parsedResult,"Result world population was correct");
    }
    @Test
     void testQuery1_2() {
         String continent = "Asia";
         ResultSet result = app.queryHelper(con, app.menuQueryBuilder("SUM(Population)","country" ,"Continent = '" + continent + "'", "") );
         long parsedResult = Long.parseLong(app.resultToStringParser(result).get(0));
         assertEquals(3705025700L, parsedResult,"Result continent population was correct");
     }
     @Test
     void testQuery1_3() {
         String region = "Eastern Europe";
         ResultSet result = app.queryHelper(con, app.menuQueryBuilder("SUM(Population)", "country", "Region = '" + region + "'", ""));
         String pop = app.resultToStringParser(result).get(0);
         assertEquals("307026000", pop,"Result region population was correct");
     }

     @Test
     void testQuery1_4() {
         String country = "United States";
         ResultSet result = app.queryHelper(con, app.menuQueryBuilder("Population", "country", "Name = '" + country + "'", ""));
         String pop = app.resultToStringParser(result).get(0);
         assertEquals("278357000", pop,"Result country population was correct");
     }

    @Test
    void testQuery1_5() {
        String city = "London";
        ResultSet result = app.queryHelper(con, app.menuQueryBuilder("Population", "city", "Name = '" + city + "'", ""));
        String pop = app.resultToStringParser(result).get(0);
        assertEquals("7285000", pop,"Result city population was correct");
    }

    @Test
    void testQuery1_6() throws SQLException {
        String district = "Latium";
        ResultSet result = app.queryHelper(con, app.menuQueryBuilder("Population","city", "District = '" + district + "'", ""));
        if (result.next()) {
            String pop = app.resultToStringParser(result).get(0);
            assertEquals("114099", pop, "Result district population was correct");
        }
    }
}
