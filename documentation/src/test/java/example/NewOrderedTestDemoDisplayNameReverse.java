package example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.DisplayNameReverse.class)
public class NewOrderedTestDemoDisplayNameReverse {

    @Test
    @DisplayName("First Method")
    void nullValues() {
        System.out.println("nullValues Method");
        // perform assertions against null values
    }

    @Test
    @DisplayName("Second Method")
    void emptyValues() {
        System.out.println("emptyValues Method");
        // perform assertions against empty values
    }

    @Test
    @DisplayName("Third Method")
    void validValues() {
        System.out.println("validValues Method");
        // perform assertions against valid values
    }

}
