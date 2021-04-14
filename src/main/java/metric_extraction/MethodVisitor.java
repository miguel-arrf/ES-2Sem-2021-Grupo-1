package metric_extraction;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.ArrayList;

public class MethodVisitor extends VoidVisitorAdapter {

    private ArrayList<Method> class_methods = new ArrayList<>();

    public void visit(MethodDeclaration n, Object arg) {
        String body = "";
        if(n.getBody().isPresent()){
            body = n.getBody().get().toString();
        }
        Method method = new Method(body, n.getName().asString());
        class_methods.add(method);
    }

    public ArrayList<Method> getMethods() {
        return class_methods;
    }

}