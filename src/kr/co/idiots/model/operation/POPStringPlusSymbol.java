package kr.co.idiots.model.operation;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import kr.co.idiots.POPVariableManager;
import kr.co.idiots.model.POPBlank;
import kr.co.idiots.model.POPNodeType;
import kr.co.idiots.model.POPVariableNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPStringPlusSymbol extends POPOperationSymbol {

	private ImageView stringPlus;
	
	public POPStringPlusSymbol() {
		super();
		
		InputStream stream = getClass().getResourceAsStream("/images/StringOperation.png");
		Image img = new Image(stream);
		imgShape.setImage(img);
		
		this.type = POPNodeType.StringPlus;
		symbol = " + ";
		
		leftBlank = new POPBlank(this);
		contents.getChildren().add(leftBlank);
		
		stream = getClass().getResourceAsStream("/images/Plus.png");
		img = new Image(stream);
		stringPlus = new ImageView(img);
		contents.getChildren().add(stringPlus);
		
		rightBlank = new POPBlank(this);
		contents.getChildren().add(rightBlank);
		
		setInitWidth();
	}
	
	@Override
	public void playSymbol() throws NullPointerException {
		leftCode = "";
		leftValue = "";
		rightValue = "";

		if(contents.getChildren().get(0) instanceof  POPLineSymbol) {
			leftValue += System.lineSeparator();
		} else if(contents.getChildren().get(0) instanceof POPOperationSymbol) {
			POPOperationSymbol symbol = (POPOperationSymbol) contents.getChildren().get(0);
			symbol.playSymbol();
			
			leftValue = symbol.executeSymbol().toString();
		} else if(contents.getChildren().get(0) instanceof POPVariableNode) {
			POPVariableNode variable = (POPVariableNode) contents.getChildren().get(0);
			leftCode += variable.getName();
			if(POPVariableManager.declaredVars.containsKey(variable.getName())) {
				leftValue = POPVariableManager.declaredVars.get(variable.getName()).toString();
			} 
		} else {
			POPBlank blank = (POPBlank) contents.getChildren().get(0);
			leftValue += blank.getText();
		}

		if(contents.getChildren().get(2) instanceof POPLineSymbol) {
			rightValue += System.lineSeparator();
		} else if(contents.getChildren().get(2) instanceof POPOperationSymbol) {
			POPOperationSymbol symbol = (POPOperationSymbol) contents.getChildren().get(2);
			symbol.playSymbol();
			
			rightValue = symbol.executeSymbol().toString();
		} else if(contents.getChildren().get(2) instanceof POPVariableNode) {
			POPVariableNode variable = (POPVariableNode) contents.getChildren().get(2);
			if(POPVariableManager.declaredVars.containsKey(variable.getName())) {
				rightValue = POPVariableManager.declaredVars.get(variable.getName()).toString();
			} 
		} else {
			POPBlank blank = (POPBlank) contents.getChildren().get(2);
			rightValue += blank.getText();
		}
		
		strValue = leftValue + rightValue;
	}
	
	@Override
	public Object executeSymbol() {
		return strValue;
	}
}
