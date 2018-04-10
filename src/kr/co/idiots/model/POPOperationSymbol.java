package kr.co.idiots.model;

import java.io.InputStream;

import javax.script.ScriptException;

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
import kr.co.idiots.util.Calculator;
import kr.co.idiots.util.TypeChecker;

enum POPSymbolType {
	Equal, Plus, Output
}

public class POPOperationSymbol extends StackPane {
	protected POPDataInput parentDataInput;
	protected ImageView shape;
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
	protected POPSymbolType type;
	protected double initWidth;
	protected boolean isRootSymbol = false;
//	protected DoubleProperty contentsWidth;
	
	public POPOperationSymbol() {
		
		
//		contentsWidth = new SimpleDoubleProperty();
		
		InputStream stream = getClass().getResourceAsStream("/images/Operation.png");
		Image img = new Image(stream);
		shape = new ImageView(img);
		
		getChildren().add(shape);
		StackPane.setAlignment(shape, Pos.CENTER);
		
		contents = new FlowPane();
		getChildren().add(contents);
		StackPane.setAlignment(contents, Pos.CENTER);
		
		
		contents.setAlignment(Pos.CENTER);
		contents.setHgap(5);
//		contents.setPrefWrapLength(shape.getBoundsInLocal().getWidth());//parentDataInput.getParentNode().getImageView().getBoundsInLocal().getWidth());
		
		
//		contentWidthProperty().addListener(new ChangeListener<Object>() {
//
//			@Override
//			public void changed(ObservableValue arg0, Object oldValue, Object newValue) {
//				// TODO Auto-generated method stub
//				if((Double)newValue >= contents.getPrefWrapLength() - 20)
//					contents.setPrefWrapLength((Double)newValue + 20);
//				else if((Double)newValue <= contents.getPrefWrapLength() - 20 && contents.getPrefWrapLength() > 150) {
//					contents.setPrefWrapLength((Double)newValue + 20);
//				
//					System.out.println("what?");
//					System.out.println(contents.getPrefWrapLength());
//					System.out.println("newVal : " + newValue);
//					
//				}
//				
//				shape.setFitWidth(contents.getPrefWrapLength());	
//			}
//			
//		});
		
		
//		shape.boundsInLocalProperty().addListener(new ChangeListener<Bounds>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
//				// TODO Auto-generated method stub
//				System.out.println("length " + newBound.getWidth());
//				contents.setPrefWrapLength(newBound.getWidth());
//			}
//			
//		});
		setOnNodeDrag();
	}
	
	public void initialize(POPDataInput parentDataInput) {
		this.parentDataInput = parentDataInput;
		setOnNodeDrag();
	}
	
	private void setOnNodeDrag() {
		
		setOnMouseDragged(event -> {
			event.setDragDetect(true);
			event.consume();
		});
		
		setOnDragDetected(event -> {
			Node on = (Node) event.getTarget();
			Dragboard db = on.startDragAndDrop(TransferMode.COPY);
			ClipboardContent content = new ClipboardContent();
			content.putString(getType().toString());
			content.putImage(shape.getImage());
				
			db.setContent(content);
			event.consume();
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
		
		shape.setFitWidth(initWidth);
		
		contents.setPrefWrapLength(initWidth);
		contents.setMinWidth(initWidth);
	}
	
	public void setContentsAutoSize() {
		
		
		double width = 0;
		
		for(int i = 0; i < contents.getChildren().size(); i++) {
			System.out.println("i : " + contents.getChildren().get(i).getBoundsInLocal().getWidth());
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
		
		shape.setFitWidth(Math.max(contents.getPrefWrapLength(), initWidth));
		if(!isRootSymbol) {
			this.getParentDataInput().getRootSymbol().setContentsAutoSize();
			return;
		}
		parentDataInput.getParentNode().setNodeAutoSize(contents.getPrefWrapLength());
	}
	
	public void add(int index, POPVariableNode node) {
		contents.getChildren().add(index, node);
//		if(index == 0)
//			leftValue = node.getName();
//		else
//			rightValue = node.getName();
		
		setContentsAutoSize();
	}
	
	public void add(int index, POPOperationSymbol node) {
		contents.getChildren().add(index, node);
		
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
			leftCode += symbol.getCodeString();
			leftValue += symbol.getValueString();
		} else if(contents.getChildren().get(0) instanceof POPVariableNode) {
			POPVariableNode variable = (POPVariableNode) contents.getChildren().get(0);
			leftCode += variable.getName();
			if(!this.getType().equals(POPSymbolType.Equal)) {
				leftCode += ".value()";
			}
			if(POPVariableManager.declaredVars.containsKey(variable.getName())) {
				leftValue = POPVariableManager.declaredVars.get(variable.getName()).toString();
			}
		} else {
			POPBlank blank = (POPBlank) contents.getChildren().get(0);
			leftCode += blank.getText();
			leftValue += blank.getText();
		}
		
//		str = str + symbol;
		
		if(contents.getChildren().get(2) instanceof POPOperationSymbol) {
			POPOperationSymbol symbol = (POPOperationSymbol) contents.getChildren().get(2);
			symbol.generateString();
			rightCode += symbol.getCodeString();
			rightValue += symbol.getValueString();
		} else if(contents.getChildren().get(2) instanceof POPVariableNode) {
			POPVariableNode variable = (POPVariableNode) contents.getChildren().get(2);
			rightCode += variable.getName() + ".value()";
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
				try {
					strCode = "Object " + strCode;//checkType(Calculator.eval(rightValue)) + strCode;
					POPVariableManager.declaredVars.put(leftCode, Calculator.eval(rightValue));
				} catch (ScriptException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
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
	
	public POPDataInput getParentDataInput() {
		return parentDataInput;
	}

	public void setParentDataInput(POPDataInput parentDataInput) {
		this.parentDataInput = parentDataInput;
	}

	public POPBlank getLeftBlank() {
		return leftBlank;
	}

	public void setLeftBlank(POPBlank leftBlank) {
		this.leftBlank = leftBlank;
	}

	public POPBlank getRightBlank() {
		return rightBlank;
	}

	public void setRightBlank(POPBlank rightBlank) {
		this.rightBlank = rightBlank;
	}

	public String getLeftValue() {
		return leftCode;
	}

	public void setLeftValue(String leftValue) {
		this.leftCode = leftValue;
	}

	public String getRightValue() {
		return rightCode;
	}

	public void setRightValue(String rightValue) {
		this.rightCode = rightValue;
	}

	public FlowPane getContents() {
		return contents;
	}

	public void setContents(FlowPane contents) {
		this.contents = contents;
	}

	public POPSymbolType getType() {
		return type;
	}

	public void setType(POPSymbolType type) {
		this.type = type;
	}

	public boolean isRootSymbol() {
		return isRootSymbol;
	}

	public void setRootSymbol(boolean isRootSymbol) {
		this.isRootSymbol = isRootSymbol;
	}
	
//	public double getContentsWidth() { return contentsWidth.get(); }
//	public void setContentsWidth(double value) { contentsWidth.set(value); }
//	public DoubleProperty contentWidthProperty() { return contentsWidth; }
}
