package com.napier.sem;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.ResultSet;

class JunitTest
{

        //assert takes in 3 params: expected result, actual result, message when failed
        App app = new App(true);

        @Test   //Connection Test
        void testResultHandleNull()
        {
            ResultSet rs = null;
            assertEquals("Result Was Null", app.resultToStringParser(rs).get(0), "Null input wasn't handled properly");
        }

        @Test
        void testMenuSelect () {
            assertEquals(651, app.menuSelection(651), "menu selection failed");
            assertEquals(1,app.menuSelection(1), "menu selection failed");
        }

        @Test
        void testMenuQueryBuilder () {
            assertEquals("SELECT pop FROM database WHERE pop = 20", app.menuQueryBuilder("pop", "database", "pop = 20", ""), "query didnt build properly");
        }

}