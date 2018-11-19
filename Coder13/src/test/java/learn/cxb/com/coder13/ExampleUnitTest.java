package learn.cxb.com.coder13;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void switchTest() {
        int value = 9;

        switch (value) {
            case 1:
                String result = "1";
                break;
            case 3:
                break;
            case 9:
                result = "9";
                System.out.println(result);
                break;
            case 7:
                break;
        }
    }


}