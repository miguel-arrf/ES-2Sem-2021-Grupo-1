JASML is a tool providing alternative ways, other than the commonly used java programming language, to view and edit the java class file - through asm like java macro instructions specified in The Java Language Specification.

As we all known, the java is a platform independent, and this is achieved through the JVM. The code is written once, and compiled into .class files(with the javac.exe or other compilers), which is then executed by JVM on different platforms. However, there is another way to construct the .class files, by directly writting the macro insctructions which can be recognized by JVM. Also, the .class files can be decompiled into macro instructions. The java macro instructions to java is what asm to C.

JASML provides ways to decompile java .class file into .jasm file. So, even without the source code, developers can now investigate the inner implementation of java classes.

JASML also provides means to compile from .jasm file to .class file, providing another way to implement a java class, and more interesting, allowing users to modify the java classes without the source code at hand(this is actually the reason why JASML is created).

JAML supports all the instructions and attributes defined in The Java Language Specification(except the LineNumber attribute which is missed when compiling .jasm files).

Any Problems or Suggestions please sent to Jiang Yang at yang.jiang.z@gmail.com

