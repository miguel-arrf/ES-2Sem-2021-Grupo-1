package testpackage;
import java.io.IOException;
import java.io.Serializable;
public class SimpleClass extends Object implements Serializable {
public static String aStaticField;
/**
*@deprecated
*/
public final int aFinalField = 5;
public final static int aFinalStaticField = 0;
public SimpleClass() throws IOException, Exception {
System.out.println("Hello World!");
int a = 0;
Runnable r = new Runnable() {
public void run() { 
}
};
try {
r.run();
} catch (Exception e) {
a = a++;
}
}
interface AnInnnerClass {
}
public static void main(String[] args) throws Exception{
new SimpleClass();
}
}
