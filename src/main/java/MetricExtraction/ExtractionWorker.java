package MetricExtraction;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Given a Java class file, the worker parses the Java code present in the file, extracting it's information and code metrics, and storing them in a ClassMetrics instance for each Java class declaration found in the file
 */
public class ExtractionWorker implements Runnable {

    private final ArrayList<ClassMetrics> metrics = new ArrayList<>();
    private final File class_file;

    /**
     * Constructs an instance of ExtractionWorker
     * @param class_file Java source code file on which to perform extraction
     */
    public ExtractionWorker(File class_file) {
        this.class_file = class_file;
    }

    /**
     * Executes the class's behavior
     */
    @Override
    public void run() {
        try {
            CompilationUnit parser = getCodeParser(class_file);
            String class_package = "null";
            if (parser.getPackageDeclaration().isPresent())
                class_package = parser.getPackageDeclaration().get().toString().substring(8).replace(";","").trim();
            List<ClassOrInterfaceDeclaration> classes = parser.findAll(ClassOrInterfaceDeclaration.class);
            for(ClassOrInterfaceDeclaration c : classes) {
                extractClassMetrics(c, class_package);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Given a class/interface declaration, returns the construct methods present in the class
     * @param c The class/interface declaration
     * @return The constructor methods in the class declaration
     */
    private List<Method> extractConstructorMethod(ClassOrInterfaceDeclaration c) {
        List<ConstructorDeclaration> constructors = c.findAll(ConstructorDeclaration.class);
        List<Method> constructorMethods = new ArrayList<>();
        for(ConstructorDeclaration declaration : constructors) {
            String body = declaration.getBody().toString();
            Method m = new Method(body, declaration.getDeclarationAsString(false,false,false));
            constructorMethods.add(m);
        }
        return constructorMethods;
    }

    /**
     * Given a Java class declaration, extracts its information and metrics
     * @param classOrInterfaceDeclaration The class declaration
     * @param class_package The class's package
     * @throws FileNotFoundException FileNotFoundException
     */
    private void extractClassMetrics(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, String class_package) throws FileNotFoundException {
        String class_name = classOrInterfaceDeclaration.getName().asString();
        int loc_class = extractLOC_Class();
        ArrayList<Method> class_methods = extractClassMethods(classOrInterfaceDeclaration, class_name);
        class_methods.addAll(extractConstructorMethod(classOrInterfaceDeclaration));
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

    /**
     * Extracts a Java class's methods as instances of Method and stores them in a list
     * @param parser A Java parser associated with the class declaration
     * @param class_name The name of the class
     * @return An ArrayList of Method instances
     */
    private ArrayList<Method> extractClassMethods(ClassOrInterfaceDeclaration parser, String class_name) {
        MethodVisitor visitor = new MethodVisitor();
        visitor.visit(parser, null);
        for(Method m : visitor.getMethods()) {
            m.setClass_name(class_name);
        }
        return visitor.getMethods();
    }

    /**
     * Creates a code parser for parsing a given Java class file
     * @param class_file The Java source code file to be parsed
     * @return A code parser associated with the given Java class file
     */
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
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        }
        return parser;
    }

    /**
     * Scans the Java class file and counts the total number of lines of code
     * @return Number of lines of the class as int
     * @throws FileNotFoundException FileNotFoundException
     */
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

    /**
     * Gets all the metrics extracted from the class file as a list of ClassMetrics instances
     * @return An ArrayList of instances of ClassMetrics
     */
    public ArrayList<ClassMetrics> getMetrics() {
        return this.metrics;
    }
}