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
	
	private StringBuilder javaSource;
	private StringBuilder cSource;
	
	public CodeGenerator() {
		javaSource = new StringBuilder();
		cSource = new StringBuilder();
	}
	
	public void createJavaFile() throws IOException {
		// 프로젝트 Home Directory 경로 조회
        String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        
        // Source를 만들고 Java파일 생성
        File sourceFile = new File(path + "test.java"); 
        new FileWriter(sourceFile).append(javaSource).close();
	}
	
	public void createStartSource(String className) {
		javaSource.append("public class " + className + " {");
		cSource.append("#include <stdio.h>");
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
		javaSource.append("}");
	}
	
	public void writeNodeContent(POPNode node) {
		writeIndent(2);
		writeString(node.getDataInput().getCodeString());
		nextLine();
	}
	
	public String generate(POPSymbolNode startNode) throws IOException, NoSuchFieldException {
		String jdkPath = new File("").getAbsolutePath() + "\\runtime\\jdk1.8.0_144";
		System.setProperty("java.home", jdkPath);
		
		javaSource.setLength(0);
		
		POPVariableManager.declaredVars.clear();
		
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		
		generateNode(startNode);
		
		System.out.println(getJavaSource());
//		System.out.println(getCSource());
		
		
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
		
		return strResult;
		
	}
	
//	private String inputCommand(String cmd) {
//		StringBuilder buffer = new StringBuilder();
//		
//		buffer.append("cmd.exe ");
//		buffer.append("/c ");
//		buffer.append(cmd);
//		
//		return buffer.toString();
//	}
//	
//	private String execCommand(String cmd) {
//		try {
//			Process process = Runtime.getRuntime().exec(cmd);
//			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//			
//			String line = null;
//			StringBuilder readBuffer = new StringBuilder();
//			
//			while((line = bufferedReader.readLine()) != null) {
//				readBuffer.append(line);
//				readBuffer.append("\n");
//			}
//			
//			return readBuffer.toString();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.exit(1);
//		}
//		
//		return null;
//	}
	
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
		
		Object paramsObj[] = {};
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
		javaSource.append(System.lineSeparator());
		cSource.append(System.lineSeparator());
	}
	
	private void writeString(String str) {
		javaSource.append(str);
		cSource.append(str);
	}
	
	private void writeMainStart() {
		javaSource.append("public static void main(String[] args) {");
		cSource.append("int main(void) {");
	}
	
	private void writeMainStop() {
		javaSource.append("}");
		cSource.append("}");
	}
	
	private void writeIndent(int n) {
		javaSource.append(String.format("%" + n*4 + "s", ""));
		if((n - 1) * 4 > 0)
			cSource.append(String.format("%" + ((n - 1) * 4) + "s", ""));
	}
	
	public String getJavaSource() {
		return javaSource.toString();
	}
	
	public String getCSource() {
		return cSource.toString();
	}
}
