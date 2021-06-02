package example.timing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TimingExtensionDemo.class)
public class TimingExtensionTestDemo {

    @Test
    void sleep200ms() throws Exception {
        Thread.sleep(200);
    }

    @Test
    void sleep300ms() throws Exception {
        Thread.sleep(300);
    }

    @Test
    void sleep400ms() throws Exception {
        Thread.sleep(400);
    }

    @Test
    void sleep500ms() throws Exception {
        Thread.sleep(500);
    }

    @Test
    void sleep600ms() throws Exception {
        Thread.sleep(600);
    }

}
