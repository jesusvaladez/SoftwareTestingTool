import org.junit.Test;
public class ExampleTest {
    @org.junit.Test
    public void testAddition() {
        int result = 2 + 2;
        assertEquals(4, result);
    }

    @org.junit.Test
    public void testAdditionTwo() {
        int result = 2 + 2;
    }

    public void testNoAssertion() {
        int result = 2 + 3;
    }

    public void normalMethod() {
    }
}