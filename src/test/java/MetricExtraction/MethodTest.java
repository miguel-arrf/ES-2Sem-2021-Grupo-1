package MetricExtraction;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MethodTest {

    private static Method method;

    @BeforeAll
    static void setUp() {
        method = new Method("if", "Method");
    }

    @Test
    void calculateMethodMetrics() {
        method.calculateMethodMetrics();
        assertTrue(method.getCyclo_method() == 2 && method.getLoc_method() == 1);
    }

    @Test
    void getCyclo_method() {
        assertEquals(method.getCyclo_method(), 0);
    }

    @Test
    void getLoc_method() {
        assertEquals(method.getLoc_method(), 0);
    }

    @Test
    void getMethod_name() {
        assertEquals(method.getMethod_name(), "Method");
    }

    @Test
    void getMethod() {
        assertEquals(method.getMethod(), "if");
    }

    @Test
    void getMetricsForDetection() {
        assertNotNull(method.getMetricsForDetection());
    }

}