import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class SimpleJunitTest
{
    //assert takes in 3 params: expected result, actual result, message when failed

    @Test
    void unitTest()
    {
        assertEquals(5,5, "Inputs Not Equal");
    }

    @Test
    void unitTest2()
    {
        int[] a = {1,4,5};
        int[] b = {1,4,5};
        assertArrayEquals(a,b, "Arrays are Not Equal");
    }

    @Test
    void unitTest3()
    {
        assertTrue(5 == 5, "Inputs Not Equal");
    }
}