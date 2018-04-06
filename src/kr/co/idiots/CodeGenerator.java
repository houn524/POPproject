package kr.co.idiots;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeGenerator {
	
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
	}
	
	private void nextLine() {
		source.append(System.lineSeparator());
	}
	
	private void writeMainStart() {
		source.append("public static void main(String[] args) {");
	}
	
	private void writeIndent(int n) {
		source.append(String.format("%" + n*4 + "s", ""));
	}
	
	public String getSource() {
		return source.toString();
	}
}
