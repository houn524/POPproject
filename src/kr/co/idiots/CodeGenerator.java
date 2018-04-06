package kr.co.idiots;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import kr.co.idiots.model.POPNode;
import kr.co.idiots.util.POPClassLoader;

public class CodeGenerator {
	public static int test = 1;
	
	private StringBuilder source = new StringBuilder();
	
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
