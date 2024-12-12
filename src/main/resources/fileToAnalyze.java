import org.junit.Test;

public class ExampleTest {
    @Test
    public void testAddition() {
        int result = 2 + 2;
        assertEquals(4, result);
    }

    @Test
    public void testAdditionTwo() {
        int result = 2 + 2;
    }

    public void testNoAssertion() {
        int result = 2 + 3;
    }

    public void normalMethod() {
        // Not a test case
    }
}
