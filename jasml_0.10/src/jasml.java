import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.jasml.classes.JavaClass;
import com.jasml.compiler.JavaClassDumpper;
import com.jasml.compiler.ParsingException;
import com.jasml.compiler.SourceCodeParser;
import com.jasml.decompiler.JavaClassParser;
import com.jasml.decompiler.SourceCodeBuilder;
import com.jasml.decompiler.SourceCodeBuilderConfiguration;

public class jasml {
	static String usage;
	static {
		usage = "Usage : \r\n";
		usage += "java jasml [-options] name \r\n";
		usage += "name            the name of the file to process.\r\n";
		usage += "options can be:\n";
		usage += "-d              decompile java class into .jasm file.\r\n";
		usage += "-c              compile jasm file into .class file.\r\n";
		usage += "-o              override without prompt if the file already exists.\r\n";
		usage += "-r              process all sub-directories.\r\n";
		usage += "                if this is specified, name must be a directory name.\r\n";
		usage += "-h              show a discription for each instruction.\r\n";
		usage += "-s              label will occupy a single line in generated .jasm file.\r\n";
		usage += "-l              generate LineNumberTable.";
	}

	public static boolean dset = false;

	public static boolean cset = false;

	public static boolean hset = false;

	public static boolean sset = false;

	public static boolean lset = false;

	public static boolean oset = false;

	public static boolean rset = false;

	public static String filename = null;

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.print(usage);
			System.exit(0);
		}
		processArgs(args);
		process(new File(filename));
	}

	private static void process(File file) {
		if (file.isDirectory() == true) {
			processDir(file);
		} else {
			processFile(file);
		}
	}

	private static void processDir(File dir) {
		File[] files = dir.listFiles();
		File tf;
		for (int i = 0; i < files.length; i++) {
			tf = files[i];
			if (rset == true && tf.isDirectory() == true) {
				processDir(tf);
			} else if ((dset == true && tf.getName().endsWith(".class")) || (cset == true && tf.getName().endsWith(".jasm"))) {
				processFile(tf);
			}
		}
	}

	private static void processFile(File file) {
		String temp;
		temp = file.getAbsolutePath();
		int i = temp.lastIndexOf('.');
		temp = temp.substring(0, i);
		File nfile;
		if (dset == true) {
			// decompile .class into .jasm
			nfile = new File(temp + ".jasm");
			if (nfile.exists() == true) {
				if (oset == false) {
					System.err.println("already exists : " + nfile.getAbsolutePath());
					return;
				} else {
					System.err.println("overriding     : " + nfile.getAbsolutePath());
				}
			} else {
				System.err.println("writing     : " + nfile.getAbsolutePath());
			}
			decompile(file, nfile);
		} else {
			nfile = new File(temp + ".class");
			if (nfile.exists() == true) {
				if (oset == false) {
					System.err.println("already exists : " + nfile.getAbsolutePath());
					return;
				} else {
					System.err.println("overriding     : " + nfile.getAbsolutePath());
				}
			} else {
				System.err.println("writing     : " + nfile.getAbsolutePath());
			}
			compile(file, nfile);
		}
	}

	private static void decompile(File classFile, File srcFile) {
		try {
			JavaClassParser jcp = new JavaClassParser();
			JavaClass clazz = jcp.parseClass(classFile);
			SourceCodeBuilder scb = new SourceCodeBuilder(new SourceCodeBuilderConfiguration(true, lset, sset, hset));
			String s = scb.toString(clazz);
			FileWriter wr = new FileWriter(srcFile);
			wr.write(s);
			wr.flush();
			wr.close();
		} catch (IOException ioe) {
			error("can not output source file");
			error(ioe.getMessage());
			System.exit(0);
		}
	}

	private static void compile(File srcFile, File classFile) {
		try {
			if (srcFile.exists() == false) {
				System.out.println("file.dose.not.exist:" + srcFile.getAbsolutePath());
				System.exit(0);
			}
			SourceCodeParser scp = new SourceCodeParser(srcFile);
			JavaClass clazz = scp.parse();
			JavaClassDumpper jcd = new JavaClassDumpper(clazz, classFile);
			jcd.dump();
		} catch (ParsingException pe) {
			error("Compilation errors:");
			error(pe.getMessage());
			System.exit(0);
		} catch (IOException ioe) {
			error("Can not out put class file");
			error(ioe.getMessage());
			System.exit(0);
		}
	}

	private static void error(String msg) {
		System.err.println(msg);
	}

	private static void processArgs(String[] args) {
		String s;
		for (int i = 0; i < args.length; i++) {
			s = args[i];
			if (s.equals("-d")) {
				dset = true;
			} else if (s.equals("-c")) {
				cset = true;
			} else if (s.equals("-o")) {
				oset = true;
			} else if (s.equals("-h")) {
				hset = true;
			} else if (s.equals("-s")) {
				sset = true;
			} else if (s.equals("-l")) {
				lset = true;
			} else if (s.equals("-r")) {
				rset = true;
			} else if (i == args.length - 1) {
				filename = args[i];
			} else {
				System.err.println("unrecognized option:" + args[i]);
				System.exit(0);
			}
		}
		if (dset == false && cset == false) {
			System.err.println("must at least specify -d or -c option");
			System.exit(0);
		} else if (dset == true && cset == true) {
			System.err.println("only one of -d or -c option can be specified");
			System.exit(0);
		}
		if (filename == null) {
			System.err.println("must specify the file name to process");
			System.exit(0);
		}
		File f = new File(filename);
		if (rset == false) {
			if (f.exists() == false) {
				System.err.print("file dose not exsits:" + filename);
				System.exit(0);
			}
			if (f.isDirectory() == true) {
				// directory is ok
			} else if (dset == true && filename.endsWith(".class") == false) {
				System.err.print("not a valid java class file name:" + filename);
				System.exit(0);
			} else if (cset == true && filename.endsWith(".jasm") == false) {
				System.err.print("not a valid jasm file name:" + filename);
				System.exit(0);
			}
		} else {
			if (f.exists() == false) {
				System.err.print("directory dose not exsits:" + filename);
				System.exit(0);
			}
			if (f.isDirectory() == false) {
				System.err.println("should not specify the -r option, not a directory:" + filename);
				System.exit(0);
			}
		}
	}
}