package kr.co.idiots.model;

import java.lang.reflect.InvocationTargetException;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPFlowLine extends Group {

    private Line line;
    private Rectangle area;
    private POPSymbolNode prevNode;
    private POPSymbolNode nextNode;
    
    private DoubleProperty startY = new SimpleDoubleProperty(0);
    private DoubleProperty endY = new SimpleDoubleProperty(0);
    private NumberBinding length = Bindings.subtract(endY, startY);

    public POPFlowLine() {
    	this(new Line(), new Line(), new Line(), new Rectangle());
    }
    
    public POPFlowLine(POPSymbolNode prevNode, POPSymbolNode nextNode) {
        this(new Line(), new Line(), new Line(), new Rectangle());
        this.prevNode = prevNode;
    	this.nextNode = nextNode;
    }

    private static final double arrowLength = 20;
    private static final double arrowWidth = 7;
    public static final double nodeMinGap = 30;

    private POPFlowLine(Line line, Line arrow1, Line arrow2, Rectangle area) {
        super(line, arrow1, arrow2, area);
        this.line = line;
        this.area = area;
        
        line.setStrokeWidth(5.0f);
        arrow1.setStrokeWidth(5.0f);
        arrow2.setStrokeWidth(5.0f);
        area.setFill(Color.rgb(0,  0,  0, 0));
        
        InvalidationListener updater = o -> {
            double ex = getEndX();
            double ey = getEndY();
            double sx = getStartX();
            double sy = getStartY();

            arrow1.setEndX(ex);
            arrow1.setEndY(ey);
            arrow2.setEndX(ex);
            arrow2.setEndY(ey);

            if (ex == sx && ey == sy) {
                // arrow parts of length 0
                arrow1.setStartX(ex);
                arrow1.setStartY(ey);
                arrow2.setStartX(ex);
                arrow2.setStartY(ey);
            } else {
                double factor = arrowLength / Math.hypot(sx-ex, sy-ey);
                double factorO = arrowWidth / Math.hypot(sx-ex, sy-ey);

                // part in direction of main line
                double dx = (sx - ex) * factor;
                double dy = (sy - ey) * factor;

                // part ortogonal to main line
                double ox = (sx - ex) * factorO;
                double oy = (sy - ey) * factorO;

                arrow1.setStartX(ex + dx - oy);
                arrow1.setStartY(ey + dy + ox);
                arrow2.setStartX(ex + dx + oy);
                arrow2.setStartY(ey + dy - ox);
            }
        };
        
        length.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldLength, Number newLength) {
				// TODO Auto-generated method stub
				if(newLength.doubleValue() < nodeMinGap && nextNode != null) {
					nextNode.getComponent().setLayoutY(nextNode.getComponent().getLayoutY() + (nodeMinGap - newLength.doubleValue()));
					nextNode.moveCenter();
				}
			}
        });

        startXProperty().addListener(updater);
        startYProperty().addListener(updater);
        endXProperty().addListener(updater);
        endYProperty().addListener(updater);
        updater.invalidated(null);
        
        try {
			setOnFlowLineDrag();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void setOnFlowLineDrag() throws ClassNotFoundException {
		setOnDragOver(event -> {
			Dragboard db = event.getDragboard();
			if(db.hasImage() && POPNodeType.symbolGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {//!POPNodeType.db.getString().equals("Variable")) {
				event.acceptTransferModes(TransferMode.COPY);
			}
		});
		
		setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if(db.hasImage() && POPNodeType.symbolGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				Class<? extends POPSymbolNode> nodeClass = null;
				POPSymbolNode node = null;
				try {
					nodeClass = (Class<? extends POPSymbolNode>) Class.forName("kr.co.idiots.model.POP" + db.getString() + "Node");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					node = nodeClass.getDeclaredConstructor(POPScriptArea.class).newInstance(getPrevNode().getScriptArea());
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getPrevNode().getScriptArea().add(node);
				insertNode(node);
			}
		});
	}
    
    public final void setStartX(double value) {
        line.setStartX(value);
        updateDragArea();
    }

    public final double getStartX() {
        return line.getStartX();
    }

    public final DoubleProperty startXProperty() {
        return line.startXProperty();
    }

    public final void setStartY(double value) {
        line.setStartY(value);
        startY.set(value);
        updateDragArea();
    }

    public final double getStartY() {
        return line.getStartY();
    }

    public final DoubleProperty startYProperty() {
        return line.startYProperty();
    }

    public final void setEndX(double value) {
        line.setEndX(value);
        updateDragArea();
    }

    public final double getEndX() {
        return line.getEndX();
    }

    public final DoubleProperty endXProperty() {
        return line.endXProperty();
    }

    public final void setEndY(double value) {
        line.setEndY(value);
        endY.set(value);
        updateDragArea();
    }

    public final double getEndY() {
        return line.getEndY();
    }

    public final DoubleProperty endYProperty() {
        return line.endYProperty();
    }
    
    public double getLength() { return length.getValue().doubleValue(); }
        
    private void updateDragArea() {
    	area.setX(Math.min(getStartX(), getEndX()) - 20);
    	area.setY(Math.min(getStartY(), getEndY()));
    	area.setWidth(Math.abs(getStartX() - getEndX()) + 40);
    	area.setHeight(Math.abs(getStartY() - getEndY()));
    }
    
    public void setPrevNode(POPSymbolNode prevNode) {
    	this.prevNode = prevNode;
    	
    	Bounds nodeBound = prevNode.getComponent().getBoundsInParent();
    	
    	setStartX(nodeBound.getMinX() + (nodeBound.getWidth() / 2));
		setStartY(nodeBound.getMaxY());
		startY.set(line.getStartY());
    }
        
    public void setNextNode(POPSymbolNode nextNode) {
    	this.nextNode = nextNode;

    	nextNode.setInFlowLine(this);
		Bounds nextNodeBound = nextNode.getComponent().getBoundsInParent();
		
		setEndX(nextNodeBound.getMinX() + (nextNodeBound.getWidth() / 2));
		setEndY(nextNodeBound.getMinY());
		endY.set(getEndY());
    }
        
    public void insertNode(POPSymbolNode node) {    	
    	node.getComponent().setLayoutX(line.getStartX() - (node.getComponent().getBoundsInParent().getWidth() / 2));
    	node.getComponent().setLayoutY(prevNode.getComponent().getBoundsInParent().getMaxY() + nodeMinGap);
    	
    	node.getOutFlowLine().setPrevNode(node);
    	node.getOutFlowLine().setNextNode(nextNode);
    	setNextNode(node);    	
    	
    	node.initialize();
    	
    }
    
    public void adjustPosition(POPNode node) {
    	if(length.getValue().doubleValue() <= node.getComponent().getBoundsInParent().getHeight() + 5) {
    		nextNode.getComponent().setTranslateY(node.getComponent().getBoundsInParent().getHeight() + nodeMinGap);
    	}
    }
}