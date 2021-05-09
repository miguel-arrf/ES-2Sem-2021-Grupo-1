package MetricExtraction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClassMetricsTest {

    private static ClassMetrics classMetrics;

    @BeforeAll
    static void setUp() {
        Method method = new Method("Something", "Method");
        ArrayList<Method> list = new ArrayList<>();
        list.add(method);
        classMetrics = new ClassMetrics("TestClass", "TestPackage",0,0,0, list);
    }

    @Test
    void getLoc_class() {
        assertEquals(classMetrics.getLoc_class(), 0);
    }

    @Test
    void getNom_class() {
        assertEquals(classMetrics.getNom_class(), 0);
    }

    @Test
    void getWmc_class() {
        assertEquals(classMetrics.getWmc_class(), 0);
    }

    @Test
    void getClass_name() {
        assertEquals(classMetrics.getClass_name(), "TestClass");
    }

    @Test
    void getClass_package() {
        assertEquals(classMetrics.getClass_package(), "TestPackage");
    }

    @Test
    void getClass_methods() {
        assertFalse(classMetrics.getClass_methods().isEmpty());
    }

    @Test
    void getMetrics_by_method() {
        assertNotNull(classMetrics.getMetrics_by_method());
    }

    @Test
    void getMetricsForDetection() {
        assertNotNull(classMetrics.getMetricsForDetection());
    }
}