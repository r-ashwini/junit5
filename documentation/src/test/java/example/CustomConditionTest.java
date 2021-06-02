package example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfReachable;
/*Test for validating custom condition 'EnableIfReachable' */
public class CustomConditionTest
{
    @Test
    @EnabledIfReachable(url = "http://example.org/", timeoutMillis = 1000)
    void reachableUrlEnabled()
    {
        System.out.println("This test is executed as the URL is reachable");
    }

    @Test
    @EnabledIfReachable(url = "http://org.example/", timeoutMillis = 1000)
    void unreachableUrlDisabled()
    {
        System.out.println("URL is not reachable");
    }
}
