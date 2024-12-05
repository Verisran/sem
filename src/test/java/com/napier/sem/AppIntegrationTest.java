package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.ResultSet;

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
        assertNotNull(con, "Connection was unsuccessful");
    }

    @Test
    void testQueryReturnData(){
        ResultSet result = app.queryHelper(con, app.menuQueryBuilder("SUM(Population)", "country", "",""));
        long parsedResult = Long.parseLong(app.resultToStringParser(result).get(0));
        assertEquals(6078749450L, parsedResult,"Result population wasn't correct");
    }
}
