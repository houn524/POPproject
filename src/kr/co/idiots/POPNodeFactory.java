package kr.co.idiots;

import java.lang.reflect.InvocationTargetException;

import kr.co.idiots.model.POPArrayNode;
import kr.co.idiots.model.POPNode;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;
import kr.co.idiots.model.POPVariableNode;
import kr.co.idiots.model.operation.POPOperationSymbol;
import kr.co.idiots.view.POPSolvingLayoutController;

public class POPNodeFactory {
	
	public static Object createNode(String clsName, String varName, String varTypeName) {
		POPNodeType type = Enum.valueOf(POPNodeType.class, clsName);
		
		Object node = null;
		
		if(POPNodeType.symbolGroup.contains(type)) {
			node = createPOPNode(clsName);
		} else if(POPNodeType.operationGroup.contains(type) || POPNodeType.compareGroup.contains(type)) {
			node = createOperationSymbol(clsName);
		} else if(POPNodeType.variableGroup.contains(type)) {
			node = createVariableNode(varName, varTypeName);
		} else if(POPNodeType.arrayGroup.contains(type)) {
			node = createArrayNode(varName);
		}
		
		return node;
	}
	
	public static POPNode createPOPNode(String strType) {
		POPNodeType type = Enum.valueOf(POPNodeType.class, strType);
		
		String packType = "";
		String className = "";
		
		className = strType;
		
		packType = "symbol";
		className = "POP" + className + "Node";
		
		POPNode node = null;
		
		try {
			node = (POPNode) Class.forName("kr.co.idiots.model." + packType + "." + className).getDeclaredConstructor(POPScriptArea.class).newInstance(POPSolvingLayoutController.scriptArea);
		} catch (IllegalAccessException | InstantiationException | ClassNotFoundException | 
				IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return node;
	}
	
	public static POPOperationSymbol createOperationSymbol(String strType) {
		POPNodeType type = Enum.valueOf(POPNodeType.class, strType);
		
		String packType = "";
		String className = "";
		
		className = strType;
		
		if(POPNodeType.operationGroup.contains(type)) {
			packType = "operation";
		} else if(POPNodeType.compareGroup.contains(type)) {
			packType = "compare";
		}
		
		className = "POP" + className + "Symbol";
		
		POPOperationSymbol node = null;
		
		try {
			node = (POPOperationSymbol) Class.forName("kr.co.idiots.model." + packType + "." + className).newInstance();
		} catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return node;
	}
	
	public static POPVariableNode createVariableNode(String varName, String varTypeName) {
		POPNode node = new POPVariableNode(POPSolvingLayoutController.scriptArea, 
				varName,
				(Enum.valueOf(POPNodeType.class, varTypeName)));
		
		return (POPVariableNode) node;
	}
	
	private static POPArrayNode createArrayNode(String varName) {
		POPNode node = new POPArrayNode(POPSolvingLayoutController.scriptArea, varName);
		
		return (POPArrayNode) node;
	}
}
