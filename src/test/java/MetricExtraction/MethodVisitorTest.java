package MetricExtraction;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MethodVisitorTest {

    private static MethodVisitor methodVisitor;

    @BeforeAll
    static void setUp() {
        methodVisitor = new MethodVisitor();
    }

    @Test
    void visit() {
        MethodDeclaration declaration = new MethodDeclaration();
        methodVisitor.visit(declaration,null);
        assertFalse(methodVisitor.getMethods().isEmpty());
    }

    @Test
    void getMethods() {
        assertNotNull(methodVisitor.getMethods());
    }
}