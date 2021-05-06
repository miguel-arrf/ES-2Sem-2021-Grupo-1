package CodeSmellDetection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

public class CodeSmellTest {

    private static CodeSmell testSmell;

    @BeforeAll
    static void setUp() {
        testSmell = new CodeSmell("TestSmell", null, true);
    }

    @Test
    void isClassSmell() {
        testSmell.setClass_smell(false);
        assertFalse(testSmell.isClassSmell());
    }

    @Test
    void setClass_smell() {
        boolean classSmellInitialValue = testSmell.isClassSmell();
        testSmell.setClass_smell(false);
        assertNotEquals(classSmellInitialValue, testSmell.isClassSmell());
    }

    @Test
    void getName() {
        assertNotNull(testSmell.getName());
    }

    @Test
    void getRule() {
        assertNull(testSmell.getRule());
    }

    @Test
    void setName() {
        String name = testSmell.getName();
        testSmell.setName("New Name");
        assertNotEquals(name, testSmell.getName());
    }

    @Test
    void setRule() {
        testSmell.setRule(null);
        assertNull(testSmell.getRule());
    }
}
