package MetricExtraction;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.ArrayList;

/**
 * Auxiliary class to visit a class and parse/extract its methods as instances of Method class
 */
public class MethodVisitor extends VoidVisitorAdapter {

    private ArrayList<Method> class_methods = new ArrayList<>();

    /**
     * Visits the Java class's method declarations using the parser, and for each declaration visited, constructs an instance of Method
     * @param methodDeclaration The method's declaration
     * @param arg Arguments (not used)
     */
    public void visit(MethodDeclaration methodDeclaration, Object arg) {
        String body = "";
        if(methodDeclaration.getBody().isPresent()){
            body = methodDeclaration.getBody().get().toString();
        }
        Method method = new Method(body, methodDeclaration.getName().asString());
        class_methods.add(method);
    }

    /**
     * Gets a list of methods parsed from the class declaration recurring to the class parser
     * @return An ArrayList of Method instances
     */
    public ArrayList<Method> getMethods() {
        return class_methods;
    }

}