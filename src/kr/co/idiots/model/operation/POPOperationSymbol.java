package kr.co.idiots.model.operation;

import java.io.InputStream;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import kr.co.idiots.POPVariableManager;
import kr.co.idiots.model.POPArrayNode;
import kr.co.idiots.model.POPBlank;
import kr.co.idiots.model.POPNode;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPVariableNode;
import kr.co.idiots.model.symbol.POPDecisionNode;
import kr.co.idiots.model.symbol.POPLoopNode;
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
	
	protected ContextMenu contextMenu;
	private POPArrayNode parentArrayNode;
	
	public POPOperationSymbol() {
		
		InputStream stream = getClass().getResourceAsStream("/images/Operation.png");
		Image img = new Image(stream);
		imgShape = new ImageView(img);
		imgShape.setStyle("-fx-effect: dropshadow(three-pass-box, black, 3, 0, 0, 1);");
		
		getChildren().add(imgShape);
		StackPane.setAlignment(imgShape, Pos.CENTER);
		
		contents = new FlowPane();
		getChildren().add(contents);
		StackPane.setAlignment(contents, Pos.CENTER);
		
		
		contents.setAlignment(Pos.CENTER);
		contents.setHgap(3);
		setOnNodeDrag();
		
		
	}
	
	public void initialize(POPNode parentNode) {
		if(parentNode instanceof POPSymbolNode) {
			this.parentNode = (POPSymbolNode) parentNode;
		} else if(parentNode instanceof POPArrayNode) {
			this.parentArrayNode = (POPArrayNode) parentNode;
		}			
		
		for(Node child : contents.getChildren()) {
			if(child instanceof POPBlank) {
				((POPBlank) child).setEditable(true);
			}
		}
		
		MenuItem deleteItem = new MenuItem("연산 기호 삭제");
		POPOperationSymbol thisSymbol = this;
		deleteItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if(isInitialized) {
					if(parentSymbol != null) {
						
						lastIndex = parentSymbol.getContents().getChildren().indexOf(thisSymbol);
						parentSymbol.getContents().getChildren().remove(thisSymbol);
						parentSymbol.getContents().getChildren().add(lastIndex, new POPBlank(parentSymbol));
						parentSymbol.initialize(parentSymbol.getParentNode());
						parentSymbol.setContentsAutoSize();
						
						event.consume();
						return;
					} else {
						lastIndex = -1;
					}
					POPSolvingLayoutController.scriptArea.getComponent().getChildren().remove(thisSymbol);
				}
			}
			
		});
		
		contextMenu = new ContextMenu();
		contextMenu.setAutoHide(true);
		contextMenu.getItems().add(deleteItem);
		
		this.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

			@Override
			public void handle(ContextMenuEvent event) {
				// TODO Auto-generated method stub
				if(isRootSymbol) {
					return;
				}
				
				contextMenu.show(imgShape, event.getScreenX(), event.getScreenY());
				event.consume();
			}
			
		});
		
		isInitialized = true;
	}
	
	private void setOnNodeDrag() {
		
		setOnMousePressed(event -> {
			if(event.getButton().equals(MouseButton.PRIMARY)) {
				if(contextMenu != null)
					contextMenu.hide();
			}
		});
		
		setOnMouseDragged(event -> {
			event.setDragDetect(true);
			event.consume();
		});
		
		setOnDragDetected(event -> {
			if(!event.getButton().equals(MouseButton.PRIMARY))
				return;
			
			if(isRootSymbol) {
				return;
			}
			
			Node on = (Node) event.getTarget();
			Dragboard db = on.startDragAndDrop(TransferMode.COPY_OR_MOVE);
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
				} else if(parentArrayNode != null) {
					lastIndex = parentArrayNode.getContents().getChildren().indexOf(this);
					parentArrayNode.getContents().getChildren().remove(this);
					parentArrayNode.getContents().getChildren().add(lastIndex, parentArrayNode.getIndexBlank());
					parentArrayNode.initialize(parentArrayNode.getParentSymbol(), parentArrayNode.getParentArrayNode());
					parentArrayNode.resizeContents();
					
					event.consume();
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
			
			if (parentSymbol != null && event.getTransferMode() != TransferMode.MOVE && event.getTransferMode() != TransferMode.COPY) {
				POPBlank lastBlank = (POPBlank) parentSymbol.getContents().getChildren().get(lastIndex);
				lastBlank.insertNode(this);
			} 
			else if(parentSymbol == null && event.getTransferMode() != TransferMode.MOVE && event.getTransferMode() != TransferMode.COPY) {
				POPSolvingLayoutController.scriptArea.getComponent().getChildren().add(this);
			}
			
			DragManager.dragMoving = false;
			DragManager.draggedNode = null;
			DragManager.isAllocatedNode = false;
			DragManager.isSynchronized = false;
		});
	}
	
	public void setInitWidth() {
		double width = 0;
		
		for(int i = 0; i < contents.getChildren().size(); i++) {
			if(contents.getChildren().get(i) instanceof POPBlank)
				width += ((POPBlank) contents.getChildren().get(i)).getPrefWidth();
			else if(contents.getChildren().get(i) instanceof POPArrayNode) {
				width += ((POPArrayNode) contents.getChildren().get(i)).getContents().getPrefWrapLength();
			}
			else if(contents.getChildren().get(i) instanceof POPVariableNode) {
				width += ((POPVariableNode) contents.getChildren().get(i)).getContents().getPrefWrapLength();
			}
			else
				width += contents.getChildren().get(i).getBoundsInLocal().getWidth();
			width += contents.getHgap();
			
			if(i == contents.getChildren().size())
				break;
		}
		
		initWidth = width + 25;
		
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
			else if(contents.getChildren().get(i) instanceof POPArrayNode) {
				width += ((POPArrayNode) contents.getChildren().get(i)).getContents().getPrefWrapLength();
			}
			else if(contents.getChildren().get(i) instanceof POPVariableNode) {
				width += ((POPVariableNode) contents.getChildren().get(i)).getContents().getPrefWrapLength();
			}
			else
				width += contents.getChildren().get(i).getBoundsInLocal().getWidth();
			width += contents.getHgap();
			
			if(i == contents.getChildren().size())
				break;
		}
		
		if(width > contents.getPrefWrapLength() - 25)
			contents.setPrefWrapLength(width + 25);
		else if(width < contents.getPrefWrapLength() - 25 && contents.getPrefWrapLength() > initWidth) {
			contents.setPrefWrapLength(Math.max(width + 25, initWidth));
		}
		
		imgShape.setFitWidth(Math.max(contents.getPrefWrapLength(), initWidth));
		if(parentSymbol != null) {
			parentSymbol.setContentsAutoSize();
		} else if(parentNode != null) {
			if(parentNode instanceof POPDecisionNode || parentNode instanceof POPLoopNode) {
				parentNode.setNodeAutoSize(contents.getPrefWrapLength() + 50);
			} else {
				parentNode.setNodeAutoSize(contents.getPrefWrapLength());
			}
			
		}
		
		if(this.parentNode != null && parentNode instanceof POPDecisionNode) {
			((POPDecisionNode) parentNode).adjustPositionThread();
		} else if(this.parentNode != null && parentNode instanceof POPLoopNode) {
			((POPLoopNode) parentNode).adjustPositionThread();
		}
		
		if(this.parentNode != null && parentNode.getOutFlowLine() != null && parentNode.getOutFlowLine().getLoopNode() != null) {
			parentNode.getOutFlowLine().getLoopNode().adjustPositionThread();
		} else if(this.parentNode != null && parentNode.getOutFlowLine() != null && parentNode.getOutFlowLine().getDecisionNode() != null) {
			parentNode.getOutFlowLine().getDecisionNode().adjustPositionThread();
		}
		
		if(parentNode != null) {
			parentNode.moveCenter();
		}
		
		if(parentArrayNode != null) {
			parentArrayNode.resizeContents();
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
		playSymbol();
		return strCode; 
	}
	
	public String getValueString() throws NullPointerException {
		playSymbol();
		return strValue;
	}
	
	public Object executeSymbol() {
		return null;
	}
	
	public void playSymbol() throws NullPointerException, NumberFormatException {
		leftCode = "";
		leftValue = "";
		rightValue = "";
		
		if(contents.getChildren().get(0) instanceof POPOperationSymbol) {
			POPOperationSymbol symbol = (POPOperationSymbol) contents.getChildren().get(0);
			symbol.playSymbol();
			
			leftValue = symbol.executeSymbol().toString();
		} else if(contents.getChildren().get(0) instanceof POPArrayNode) {
			POPArrayNode array = (POPArrayNode) contents.getChildren().get(0);
			leftCode += array.getName();
			if(POPVariableManager.declaredArrs.containsKey(array.getName())) {
				leftValue = array.getValue();
			}
		} else if(contents.getChildren().get(0) instanceof POPVariableNode) {
			POPVariableNode variable = (POPVariableNode) contents.getChildren().get(0);
			leftCode += variable.getName();
			if(POPVariableManager.declaredVars.containsKey(variable.getName())) {
				leftValue = POPVariableManager.declaredVars.get(variable.getName()).toString();
			} else if(!(this instanceof POPEqualSymbol)) {
				this.parentNode.getImgView().setStyle("-fx-effect: dropshadow(three-pass-box, red, 10, 0, 0, 1);");
				this.parentNode.setException(true);
				throw new NullPointerException();
			}
		} else {
			POPBlank blank = (POPBlank) contents.getChildren().get(0);
			if(blank.getText().isEmpty()) {
				this.parentNode.getImgView().setStyle("-fx-effect: dropshadow(three-pass-box, red, 10, 0, 0, 1);");
				this.parentNode.setException(true);
				throw new NullPointerException("빈 칸");
			}
			leftValue += blank.getText();
		}
		
		if(contents.getChildren().size() <= 1)
			return;
		
		if(contents.getChildren().get(2) instanceof POPOperationSymbol) {
			POPOperationSymbol symbol = (POPOperationSymbol) contents.getChildren().get(2);
			symbol.playSymbol();
			
			rightValue = symbol.executeSymbol().toString();
		} else if(contents.getChildren().get(2) instanceof POPArrayNode) {
			POPArrayNode array = (POPArrayNode) contents.getChildren().get(2);
			rightCode += array.getName();
			if(POPVariableManager.declaredArrs.containsKey(array.getName())) {
				rightValue = array.getValue();
			}
		} else if(contents.getChildren().get(2) instanceof POPVariableNode) {
			POPVariableNode variable = (POPVariableNode) contents.getChildren().get(2);
			if(POPVariableManager.declaredVars.containsKey(variable.getName())) {
				rightValue = POPVariableManager.declaredVars.get(variable.getName()).toString();
			} else {
				this.parentNode.getImgView().setStyle("-fx-effect: dropshadow(three-pass-box, red, 10, 0, 0, 1);");
				this.parentNode.setException(true);
				throw new NullPointerException();
			}
		}  else {
			POPBlank blank = (POPBlank) contents.getChildren().get(2);
			if(blank.getText().isEmpty()) {
				this.parentNode.getImgView().setStyle("-fx-effect: dropshadow(three-pass-box, red, 10, 0, 0, 1);");
				this.parentNode.setException(true);
				throw new NullPointerException("빈 칸");
			}
			rightValue += blank.getText();
		}
		
		
		if(symbol.equals(" = ")) {
//			if(!POPVariableManager.declaredVars.containsKey(leftCode)) {
//				POPVariableNode variable = (POPVariableNode) contents.getChildren().get(0);
//				String type = "";
//				switch(variable.getType()) {
//				case IntegerVariable :
//					type = "int ";
//					break;
//				case DoubleVariable :
//					type = "double ";
//					break;
//				case StringVariable :
//					type = "String ";
//					break;
//				}
//				strCode = type + strCode;
//			}
			
			
			leftValue = leftCode;
			strValue = leftValue + strValue;
			
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
