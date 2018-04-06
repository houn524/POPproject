package kr.co.idiots.util;

import kr.co.idiots.model.POPSymbolNode;
import kr.co.idiots.model.POPVariableNode;

public class DragManager {
	public static POPSymbolNode draggedSymbolNode;
	public static POPVariableNode draggedVariableNode;
	
//	private static final DataFormat nodeFormat = new DataFormat("kr.co.idiots.model.POPNode");
	
//	public static void setOnNodeDrag(POPNode node) {
//		
//		node.getComponent().setOnMouseDragged(event -> {
//			event.setDragDetect(true);
//			event.consume();
//		});
//		
//		node.getComponent().setOnDragDetected(event -> {
//			Node on = (Node) event.getTarget();
//			Dragboard db = on.startDragAndDrop(TransferMode.COPY);
//			ClipboardContent content = new ClipboardContent();
//			content.putString(node.getType().toString());
//			content.putImage(node.getImageView().getImage());
//			content.put(nodeFormat, node);
////			if(node.getType() == POPNodeType.Variable) {
////				POPVariableNode varNode = (POPVariableNode) node;
////				Map<String, Object> map = new HashMap<String, Object>();
////				map.put(varNode.getName(), varNode.getValue());
////				content.putAll(map);
////			}
//				
//			db.setContent(content);
//			event.consume();
//		});
//	}
	
//	public static void setOnBlankDrag(POPBlank blank) {
//		blank.setOnDragOver(event -> {
//			Dragboard db = event.getDragboard();
//			if(db.hasImage() && db.getString().equals("Variable")) {
//				event.acceptTransferModes(TransferMode.COPY);
//			}
//		});
//		
//		blank.setOnDragDropped(event -> {
//			Dragboard db = event.getDragboard();
//			if(db.hasImage() && db.getString().equals("Variable")) {
//				blank.insertNode((POPNode)db.getContent(nodeFormat));
//			}
//		});
//	}
	
//	public static void setOnFlowLineDrag(POPFlowLine flowLine) {
//		flowLine.setOnDragOver(event -> {
//			Dragboard db = event.getDragboard();
//			if(db.hasImage() && !db.getString().equals("Variable")) {
//				event.acceptTransferModes(TransferMode.COPY);
//			}
//		});
//		
//		flowLine.setOnDragDropped(event -> {
//			Dragboard db = event.getDragboard();
//			boolean success = false;
//			if(db.hasImage() && !db.getString().equals("Variable")) {
//				try {
//					System.out.println(db.getString());
////					Reflections reflections = new Reflections("kr.co.idiots.model");
//					Class<? extends POPSymbolNode> nodeClass = (Class<? extends POPSymbolNode>) Class.forName("kr.co.idiots.model.POP" + db.getString() + "Node");
//					POPSymbolNode node = nodeClass.getDeclaredConstructor(POPScriptArea.class).newInstance(flowLine.getPrevNode().getScriptArea());
//					flowLine.getPrevNode().getScriptArea().add(node);
//					flowLine.insertNode(node);
//				} catch (ClassNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (InstantiationException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IllegalArgumentException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (InvocationTargetException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (NoSuchMethodException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (SecurityException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
////				flowLine.getPrevNode().setNextNode(node);
////				node.setNextNode(flowLine.getNextNode());
//			}
//		});
//	}
}