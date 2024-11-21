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

    @Test   //Connection Test
    void resultCatchNullTest()
    {
        ResultSet rs = null;
        assertTrue("Result Was Null".equals( app.resultToStringParser(rs).get(0) ), "Null input wasn't handled properly");
    }

    @Test
    void testMenuSelect(){
        assertEquals(0, app.menuSelection(0), "menu selection failed");
    }

    @Test
    void testMenuQueryBuilder(){
        assertEquals("SELECT pop FROM database WHERE pop = 20", app.menuQueryBuilder("pop", "database", "pop = 20", ""), "query didnt build properly");
    }
}