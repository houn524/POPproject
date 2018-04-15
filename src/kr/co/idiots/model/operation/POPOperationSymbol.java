package kr.co.idiots.model.operation;

import java.io.InputStream;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import kr.co.idiots.POPVariableManager;
import kr.co.idiots.model.POPBlank;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPVariableNode;
import kr.co.idiots.model.symbol.POPSymbolNode;
import kr.co.idiots.util.ClipboardUtil;
import kr.co.idiots.util.DragManager;
import kr.co.idiots.util.TypeChecker;
import kr.co.idiots.view.POPSolvingLayoutController;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPOperationSymbol extends StackPane {
	protected POPOperationSymbol parentSymbol;
	protected POPSymbolNode parentNode;
	protected ImageView imgShape;
	protected FlowPane contents;
	protected POPBlank leftBlank;
	protected POPBlank rightBlank;
	protected String strCode = "";
	protected String leftCode = "";
	protected String rightCode = "";
	protected String strValue = "";
	protected String leftValue = "";
	protected String rightValue = "";
	protected String symbol;
	protected POPNodeType type;
	protected double initWidth;
	protected boolean isRootSymbol = false;
	protected boolean isInitialized = false;
	protected int lastIndex = -1;
	
	public POPOperationSymbol() {
		
		InputStream stream = getClass().getResourceAsStream("/images/Operation.png");
		Image img = new Image(stream);
		imgShape = new ImageView(img);
		
		getChildren().add(imgShape);
		StackPane.setAlignment(imgShape, Pos.CENTER);
		
		contents = new FlowPane();
		getChildren().add(contents);
		StackPane.setAlignment(contents, Pos.CENTER);
		
		
		contents.setAlignment(Pos.CENTER);
		contents.setHgap(5);
		setOnNodeDrag();
	}
	
	public void initialize(POPSymbolNode parentNode) {
		this.parentNode = parentNode;
		
		for(Node child : contents.getChildren()) {
			if(child instanceof POPBlank) {
				((POPBlank) child).setEditable(true);
			}
		}
		
//		if(leftBlank != null) {
//			leftBlank.setEditable(true);
//		}
//		
//		if(rightBlank != null) {
//			rightBlank.setEditable(true);
//		}
//		
		isInitialized = true;
	}
	
	private void setOnNodeDrag() {
		
		setOnMouseDragged(event -> {
			event.setDragDetect(true);
			event.consume();
		});
		
		setOnDragDetected(event -> {
			if(isRootSymbol) {
				return;
			}
			
			Node on = (Node) event.getTarget();
			Dragboard db = on.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(getType().toString());
				
			db.setContent(content);
			
			db.setContent(ClipboardUtil.makeClipboardContent(event, this, getType().toString()));
			
			if(isInitialized) {
				DragManager.draggedNode = this;
				DragManager.dragMoving = true;
				
				if(parentSymbol != null) {
					lastIndex = parentSymbol.getContents().getChildren().indexOf(this);
					parentSymbol.getContents().getChildren().remove(this);
					parentSymbol.getContents().getChildren().add(lastIndex, new POPBlank(parentSymbol));
					parentSymbol.initialize(parentSymbol.getParentNode());
					parentSymbol.setContentsAutoSize();
					
					event.consume();
					return;
				} else {
					lastIndex = -1;
				}
				POPSolvingLayoutController.scriptArea.getComponent().getChildren().remove(this);
			}
			
			event.consume();
		});
		
		
		setOnDragDone(event -> {
			if(!isInitialized || isRootSymbol)
				return;
			
			if (parentSymbol != null && event.getTransferMode() != TransferMode.MOVE) {
				POPBlank lastBlank = (POPBlank) parentSymbol.getContents().getChildren().get(lastIndex);
				lastBlank.insertNode(this);
			} 
			else if(parentSymbol == null && event.getTransferMode() != TransferMode.MOVE) {
				POPSolvingLayoutController.scriptArea.getComponent().getChildren().add(this);
			}
			
			DragManager.dragMoving = false;
			DragManager.draggedNode = null;
			DragManager.isAllocatedNode = false;
		});
	}
	
	public void setInitWidth() {
		double width = 0;
		
		for(int i = 0; i < contents.getChildren().size(); i++) {
			if(contents.getChildren().get(i) instanceof POPBlank)
				width += ((POPBlank) contents.getChildren().get(i)).getPrefWidth();
			else
				width += contents.getChildren().get(i).getBoundsInLocal().getWidth();
			width += contents.getHgap();
			
			if(i == contents.getChildren().size())
				break;
		}
		
		initWidth = width + 40;
		
		imgShape.setFitWidth(initWidth);
		this.setMaxWidth(imgShape.getBoundsInLocal().getWidth());
		this.setMaxHeight(imgShape.getBoundsInLocal().getHeight());
		
		contents.setPrefWrapLength(initWidth);
		contents.setMinWidth(initWidth);
	}
	
	public void setContentsAutoSize() {
		double width = 0;
		
		for(int i = 0; i < contents.getChildren().size(); i++) {
			if(contents.getChildren().get(i) instanceof POPBlank)
				width += ((POPBlank) contents.getChildren().get(i)).getPrefWidth();
			else
				width += contents.getChildren().get(i).getBoundsInLocal().getWidth();
			width += contents.getHgap();
			
			if(i == contents.getChildren().size())
				break;
		}
		
		if(width > contents.getPrefWrapLength() - 40)
			contents.setPrefWrapLength(width + 20);
		else if(width < contents.getPrefWrapLength() - 40 && contents.getPrefWrapLength() > initWidth) {
			contents.setPrefWrapLength(Math.max(width + 20, initWidth));
		}
		
		imgShape.setFitWidth(Math.max(contents.getPrefWrapLength(), initWidth));
		if(parentSymbol != null) {
			parentSymbol.setContentsAutoSize();
		} else if(parentNode != null) {
			parentNode.setNodeAutoSize(contents.getPrefWrapLength());
		}
	}
	
	public void add(int index, POPVariableNode node) {
		contents.getChildren().add(index, node);
		node.setOperationSymbol(this);
		
		setContentsAutoSize();
	}
	
	public void add(int index, POPOperationSymbol node) {
		contents.getChildren().add(index, node);
		node.setParentSymbol(this);
		
		setContentsAutoSize();
	}
	
	public void remove(Node node) {
		int index = contents.getChildren().indexOf(node);
		contents.getChildren().remove(node);
		if(index == 0)
			leftCode = "";
		else
			rightCode = "";
		
		setContentsAutoSize();
	}
	
	public String getCodeString() {
		generateString();
		return strCode; 
	}
	
	public String getValueString() {
		generateString();
		return strValue;
	}
	
	public void generateString() {
		strCode = "";
		leftCode = "";
		rightCode = "";
		strValue = "";
		leftValue = "";
		rightValue = "";
		
		if(contents.getChildren().get(0) instanceof POPOperationSymbol) {
			POPOperationSymbol symbol = (POPOperationSymbol) contents.getChildren().get(0);
			symbol.generateString();
			leftCode += "( " + symbol.getCodeString() + " )";
			leftValue += "( " + symbol.getValueString() + " )";
		} else if(contents.getChildren().get(0) instanceof POPVariableNode) {
			POPVariableNode variable = (POPVariableNode) contents.getChildren().get(0);
			leftCode += variable.getName();
			if(POPVariableManager.declaredVars.containsKey(variable.getName())) {
				leftValue = POPVariableManager.declaredVars.get(variable.getName()).toString();
			}
		} else {
			POPBlank blank = (POPBlank) contents.getChildren().get(0);
			leftCode += blank.getText();
			leftValue += blank.getText();
		}
				
		if(contents.getChildren().get(2) instanceof POPOperationSymbol) {
			POPOperationSymbol symbol = (POPOperationSymbol) contents.getChildren().get(2);
			symbol.generateString();
			rightCode += "( " + symbol.getCodeString() + " )";
			rightValue += "( " + symbol.getValueString() + " )";
		} else if(contents.getChildren().get(2) instanceof POPVariableNode) {
			POPVariableNode variable = (POPVariableNode) contents.getChildren().get(2);
			rightCode += variable.getName();
			if(POPVariableManager.declaredVars.containsKey(variable.getName())) {
				rightValue = POPVariableManager.declaredVars.get(variable.getName()).toString();
			}
		} else {
			POPBlank blank = (POPBlank) contents.getChildren().get(2);
			rightCode += blank.getText();
			rightValue += blank.getText();
		}
		
		strCode = leftCode + symbol + rightCode;
		strValue = leftValue + symbol + rightValue;
		
		if(symbol.equals(" = ")) {
			if(!POPVariableManager.declaredVars.containsKey(leftCode)) {
				POPVariableNode variable = (POPVariableNode) contents.getChildren().get(0);
				String type = "";
				switch(variable.getType()) {
				case IntegerVariable :
					type = "int ";
					break;
				case DoubleVariable :
					type = "double ";
					break;
				case StringVariable :
					type = "String ";
					break;
				}
				strCode = type + strCode; //checkType(Calculator.eval(rightValue)) + strCode;	
			}
			
//			try {
//				POPVariableManager.declaredVars.put(leftCode, Calculator.eval(rightValue));
//			} catch (ScriptException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			leftValue = leftCode;
			strValue = leftValue + strValue;
			
//			System.out.println(leftCode + " : " + POPVariableManager.declaredVars.get(leftCode));
//			System.out.println(strValue);
			strCode = strCode + ";";
		}
	}
	
	private String checkType(String value) {
		if(TypeChecker.isInteger(value))
			return "int ";
		else if(TypeChecker.isDouble(value))
			return "double ";
		else
			return "String ";
	}
}
