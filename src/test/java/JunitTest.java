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
    void unitTest()
    {
        ResultSet rs = null;
        assertTrue("Result Was Null".equals( app.resultToStringParser(rs).get(0) ));
    }


}