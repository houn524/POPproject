package kr.co.idiots.model;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class POPEqualSymbol extends POPOperationSymbol {
	
	private ImageView equal;
	
	public POPEqualSymbol(POPDataInput parentDataInput) {
		super(parentDataInput);
		
		symbol = " = ";
		
		leftBlank = new POPBlank(this);
		contents.getChildren().add(leftBlank);
		
		InputStream stream = getClass().getResourceAsStream("/images/Equal.png");
		Image img = new Image(stream);
		equal = new ImageView(img);
		contents.getChildren().add(equal);
		
		rightBlank = new POPBlank(this);
		contents.getChildren().add(rightBlank);
		
		setInitWidth();
//		double width = 0;
//		
//		for(int i = 0; i < contents.getChildren().size(); i++) {
//			System.out.println("i : " + contents.getChildren().get(i).getBoundsInLocal().getWidth());
//			if(contents.getChildren().get(i) instanceof POPBlank)
//				width += ((POPBlank) contents.getChildren().get(i)).getPrefWidth();
//			else
//				width += contents.getChildren().get(i).getBoundsInLocal().getWidth();
//			width += contents.getHgap();
//			
//			if(i == contents.getChildren().size())
//				break;
//		}
//		
//		initWidth = width + 40;
//		shape.setFitWidth(initWidth);
//		
//		contents.setPrefWrapLength(initWidth);
//		contents.setMinWidth(initWidth);
		
		
		
//		contents.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Bounds> arg0, Bounds oldBound, Bounds newBound) {
//				// TODO Auto-generated method stub
//				System.out.println("contents changed");
//				System.out.println(newBound.getWidth());
////				if(newBound.getHeight() > oldBound.getHeight()) {
////					contents.setPrefWrapLength(contents.getPrefWrapLength() + 10);
////					System.out.println("yes");
////				} else if(newBound.getHeight() < oldBound.getHeight())
////					contents.setPrefWrapLength(contents.getPrefWrapLength() - 10);
//				
//				
//				shape.setFitWidth(newBound.getWidth());
//				
////				if(newBound.getWidth() + 1 > contents.getPrefWrapLength())
////					contents.setPrefWrapLength(contents.getPrefWrapLength() + 1);
////				contents.setPrefWrapLength(shape.getBoundsInLocal().getWidth());
////				shape.setFitHeight(newBound.getHeight() + 1);
//			}
//
//		});
	}
	
}
