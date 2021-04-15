package metric_extraction;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExtractionWorker implements Runnable {

    private ArrayList<ClassMetrics> metrics = new ArrayList<>();
    private File class_file;

    public ExtractionWorker(File class_file) {
        this.class_file = class_file;
    }

    @Override
    public void run() {
        try {
            CompilationUnit parser = getCodeParser(class_file);
            String class_package = "null";
            if (parser.getPackageDeclaration().isPresent())
                class_package = parser.getPackageDeclaration().get().toString().substring(8).replace(";","").trim();
            List<ClassOrInterfaceDeclaration> classes = parser.findAll(ClassOrInterfaceDeclaration.class);
            for(ClassOrInterfaceDeclaration c : classes) {
                String class_name = c.getName().asString();
                int loc_class = extractLOC_Class();
                ArrayList<Method> class_methods = extractClassMethods(c);
                int nom_class = class_methods.size();
                int wmc_class = 0;
                if(nom_class != 0) {
                    for(Method method : class_methods) {
                        method.calculateMethodMetrics();
                        wmc_class += method.getCyclo_method();
                    }
                }
                metrics.add(new ClassMetrics(class_name, class_package, loc_class, nom_class, wmc_class, class_methods));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Method> extractClassMethods(ClassOrInterfaceDeclaration parser) {
        MethodVisitor visitor = new MethodVisitor();
        visitor.visit(parser, null);
        return visitor.getMethods();
    }

    private CompilationUnit getCodeParser(File class_file) {
        InputStream in = null;
        CompilationUnit parser = null;
        try {
            in = new FileInputStream(class_file);
            parser = StaticJavaParser.parse(in);
        } catch(FileNotFoundException x) {
            x.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return parser;
    }

    private int extractLOC_Class() throws FileNotFoundException {
        Scanner scanner = new Scanner(class_file);
        int counter = 0;
        while(scanner.hasNextLine()) {
            counter++;
            scanner.nextLine();
        }
        scanner.close();
        return counter;
    }

    public ArrayList<ClassMetrics> getMetrics() {
        return this.metrics;
    }
}