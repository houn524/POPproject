package kr.co.idiots;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import kr.co.idiots.model.POPNode;
import kr.co.idiots.model.POPStartNode;
import kr.co.idiots.model.POPStopNode;
import kr.co.idiots.model.POPSymbolNode;
import kr.co.idiots.util.POPClassLoader;

public class CodeGenerator {
	public static int test = 1;
	
	private StringBuilder source;
	
	public CodeGenerator() {
		source = new StringBuilder();
	}
	
	public void createJavaFile() throws IOException {
		// 프로젝트 Home Directory 경로 조회
        String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        
        // Source를 만들고 Java파일 생성
        File sourceFile = new File(path + "test.java"); 
        new FileWriter(sourceFile).append(source).close();
	}
	
	public void createStartSource(String className) {
		source.append("public class " + className + " {");
		nextLine();
		nextLine();
		writeIndent(1);
		writeMainStart();
		nextLine();
	}
	
	public void createStopSource() {
		writeIndent(1);
		writeMainStop();
		nextLine();
		source.append("}");
	}
	
	public void writeNodeContent(POPNode node) {
		writeIndent(2);
		writeString(node.getDataInput().toString());
		nextLine();
	}
	
	public String generate(POPSymbolNode startNode) throws IOException, NoSuchFieldException {
		String jdkPath = new File("").getAbsolutePath() + "\\runtime\\jdk1.8.0_144";
//		String jdkPath = "C:\\Program Files\\Java\\jdk1.8.0_144";
		System.setProperty("java.home", jdkPath);
		
		source.setLength(0);
		
		POPVariableManager.declaredVars.clear();
		
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		
		generateNode(startNode);
		
		System.out.println(getSource());
		
		
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		
		int result = compiler.run(null, null, null, path + "test.java");
		
		String strResult = "";
		
		
		try {
			strResult = runCode();
		} catch (IllegalAccessException | InstantiationException | NoSuchMethodException | SecurityException
				| IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("result Output : \n" + strResult);
//		ps.flush();
//		outputStream = null;
//		System.out.flush();
//		compiler = null;
//		ps.close();
		
		return strResult;
	}
	
	private void generateNode(POPSymbolNode node) throws IOException {
		
		if(node instanceof POPStartNode) {
			createStartSource("test");
			generateNode(node.getOutFlowLine().getNextNode());
		} else if(node instanceof POPStopNode) {
			createStopSource();
			createJavaFile();
		} else {
			writeNodeContent(node);
			generateNode(node.getOutFlowLine().getNextNode());
		}
	}
	
	public String runCode() throws IllegalAccessException, InstantiationException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException, NoSuchFieldException {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		
		ClassLoader parentClassLoader = POPClassLoader.class.getClassLoader();
		POPClassLoader classLoader = new POPClassLoader(parentClassLoader);
		Class thisClass = classLoader.loadClass("test");
		
//		File file = new File(path + "test.class");
//		
//		@SuppressWarnings("deprecation")
//		URL url = file.toURL();
//		URL[] urls = new URL[] { url };
//		
//		ClassLoader loader = new URLClassLoader(urls);
//		
//		Class thisClass = loader.loadClass("test", true);
		
//		Class params[] = {};
		Object paramsObj[] = {};
//		Object instance = thisClass.newInstance();
		Method thisMethod = thisClass.getMethod("main", String[].class);
		String[] params = null;	
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(outputStream);
		PrintStream old = System.out;
		System.setOut(ps);
		
		thisMethod.invoke(null, (Object) params);
		
		System.out.flush();
		System.setOut(old);
		String result = outputStream.toString();
		outputStream = null;
		old = null;
		
		return result;
	}
	
	private void nextLine() {
		source.append(System.lineSeparator());
	}
	
	private void writeString(String str) {
		source.append(str);
	}
	
	private void writeMainStart() {
		source.append("public static void main(String[] args) {");
	}
	
	private void writeMainStop() {
		source.append("}");
	}
	
	private void writeIndent(int n) {
		source.append(String.format("%" + n*4 + "s", ""));
	}
	
	public String getSource() {
		return source.toString();
	}
}
