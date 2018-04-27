package kr.co.idiots;

import java.lang.reflect.InvocationTargetException;

import javafx.scene.input.Dragboard;
import kr.co.idiots.model.POPNode;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPScriptArea;
import kr.co.idiots.model.POPVariableNode;
import kr.co.idiots.model.operation.POPOperationSymbol;
import kr.co.idiots.util.POPNodeDataFormat;
import kr.co.idiots.view.POPSolvingLayoutController;

public class POPNodeFactory {
	
	public static Object createNode(Dragboard db) {
		POPNodeType type = Enum.valueOf(POPNodeType.class, db.getString());
		
		Object node = null;
		
		if(POPNodeType.symbolGroup.contains(type)) {
			node = createPOPNode(db.getString());
		} else if(POPNodeType.operationGroup.contains(type) || POPNodeType.compareGroup.contains(type)) {
			node = createOperationSymbol(db.getString());
		} else if(POPNodeType.variableGroup.contains(type)) {
			node = createVariableNode(db);
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
	
	public static POPVariableNode createVariableNode(Dragboard db) {
		POPNode node = new POPVariableNode(POPSolvingLayoutController.scriptArea, 
				(String) db.getContent(POPNodeDataFormat.variableNameFormat),
				(Enum.valueOf(POPNodeType.class, (String) db.getContent(POPNodeDataFormat.variableTypeFormat))));
		
		return (POPVariableNode) node;
	}
}
