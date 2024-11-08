import com.napier.sem.App;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class JunitTest
{
    //assert takes in 3 params: expected result, actual result, message when failed
    App app = new App();
    Connection con = app.connect("localhost:33060", 8000);


    @Test   //Connection Test
    void unitTest()
    {
        assertNotNull(con, "Connection failed.");
    }

    @Test
    void unitTest2() throws SQLException //Table Select Test
    {
        ResultSet tables = app.queryHelper(con, "SELECT * FROM country");
        assertTrue(tables.next(), "Table wasnt found");
    }
}