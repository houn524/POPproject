package kr.co.idiots.model;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import javafx.scene.layout.AnchorPane;
import kr.co.idiots.CodeGenerator;

public class POPScriptArea {
	private AnchorPane component;
	private POPSymbolNode startNode;
	private POPNode nodePointer;
	private CodeGenerator generator;
	
	public POPScriptArea(AnchorPane component) {
		this.component = component;
	}
	
	public AnchorPane getComponent() { return component; }
	
	public void add(POPSymbolNode node) {
		component.getChildren().add(node.getComponent());
		if(node.getOutFlowLine() != null)
			component.getChildren().add(node.getOutFlowLine());
	}

	public POPNode getStartNode() {
		return startNode;
	}

	public void setStartNode(POPSymbolNode startNode) {
		this.startNode = startNode;
		nodePointer = startNode;
	}
	
	public void generate() throws IOException, NoSuchFieldException {
		System.setProperty("java.home", "c:\\Program Files\\Java\\jdk1.8.0_144");
		
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		generator = new CodeGenerator();
		
		generateNode(startNode);
		
		System.out.println(generator.getSource());
		
		
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		
		int result = compiler.run(null, null, null, path + "test.java");
		
		String strResult = "";
		
		
		try {
			strResult = generator.runCode();
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
	}
	
	private void generateNode(POPSymbolNode node) throws IOException {
		
		if(node instanceof POPStartNode) {
			generator.createStartSource("test");
			generateNode(node.getOutFlowLine().getNextNode());
		} else if(node instanceof POPStopNode) {
			generator.createStopSource();
			generator.createJavaFile();
		} else {
			generator.writeNodeContent(node);
			generateNode(node.getOutFlowLine().getNextNode());
		}
	}
}
