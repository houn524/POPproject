package kr.co.idiots.model;

import java.lang.reflect.InvocationTargetException;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import kr.co.idiots.model.symbol.POPDecisionNode;
import kr.co.idiots.model.symbol.POPSymbolNode;
import kr.co.idiots.util.DragManager;
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
        
        InvalidationListener updater = new InvalidationListener() {

			@Override
			public void invalidated(Observable arg0) {
				// TODO Auto-generated method stub

				double ex = getEndX();
				double ey = getEndY();
				
				if(nextNode != null) {
					Bounds nextNodeBound = nextNode.getBoundsInParent();
					ex = nextNodeBound.getMinX() + (nextNodeBound.getWidth() / 2);//getEndX();
		            ey = nextNodeBound.getMinY();//getEndY();
		            line.setEndX(ex);
		            line.setEndY(ey);
				}
				
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
			}
        	
        };
        
//        InvalidationListener updater = o -> {
//        	
//            double ex = getEndX();
//            double ey = getEndY();
//            double sx = getStartX();
//            double sy = getStartY();
//
//            arrow1.setEndX(ex);
//            arrow1.setEndY(ey);
//            arrow2.setEndX(ex);
//            arrow2.setEndY(ey);
//
//            if (ex == sx && ey == sy) {
//                // arrow parts of length 0
//                arrow1.setStartX(ex);
//                arrow1.setStartY(ey);
//                arrow2.setStartX(ex);
//                arrow2.setStartY(ey);
//            } else {
//                double factor = arrowLength / Math.hypot(sx-ex, sy-ey);
//                double factorO = arrowWidth / Math.hypot(sx-ex, sy-ey);
//
//                // part in direction of main line
//                double dx = (sx - ex) * factor;
//                double dy = (sy - ey) * factor;
//
//                // part ortogonal to main line
//                double ox = (sx - ex) * factorO;
//                double oy = (sy - ey) * factorO;
//
//                arrow1.setStartX(ex + dx - oy);
//                arrow1.setStartY(ey + dy + ox);
//                arrow2.setStartX(ex + dx + oy);
//                arrow2.setStartY(ey + dy - ox);
//            }
//        };
        
        length.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldLength, Number newLength) {
				// TODO Auto-generated method stub
				newLength = endY.getValue() - startY.getValue();
				if((newLength.doubleValue() < nodeMinGap || newLength.doubleValue() > nodeMinGap) && nextNode != null && !DragManager.isSynchronized) {
					if(nextNode instanceof POPDecisionNode) {
						((POPDecisionNode) nextNode).getContents().setLayoutY(((POPDecisionNode) nextNode).getContents().getLayoutY() + (nodeMinGap - newLength.doubleValue()));
					} else {
						nextNode.getComponent().setLayoutY(nextNode.getComponent().getLayoutY() + (nodeMinGap - newLength.doubleValue()));
					}
					
//					Bounds nextNodeBound = nextNode.getComponent().getBoundsInParent();
//					setEndX(nextNodeBound.getMinX() + (nextNodeBound.getWidth() / 2));
//					setEndY(nextNodeBound.getMinY());
//					endY.set(getEndY());
//					System.out.println(getStartY() + " : " + getEndY());
//					System.out.println(delta);
//					System.out.println(nextNode + " : " + nextNode.getBoundsInParent());
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
    	
//    	area.addEventFilter(Event.ANY, event -> {
//    		
//    		if(event.getEventType().toString().equals("MOUSE_ENTERED")) {
//    			System.out.println(event.getEventType());
//    		}
//    	});
//    	this.setOn
    	
//    	setOnMouseMoved(event -> {
//    		System.out.println("Move");
//    	});
//    	
//    	setOnMouseDragEntered(event -> {
//    		System.out.println("enter");
//    		if(DragManager.dragMoving) {// && DragManager.draggedNode instanceof POPSymbolNode) {
//    			DropShadow dropShadow = new DropShadow();
//        		dropShadow.setRadius(5.0);
//        		dropShadow.setOffsetX(3.0);
//        		dropShadow.setOffsetY(3.0);
//        		dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
//        		this.setEffect(dropShadow);
//    		}
//    	});
//    	
//    	setOnMouseExited(event -> {
//    		
//    	});
    	
		setOnDragOver(event -> {
			Dragboard db = event.getDragboard();
			if(db.hasImage() && POPNodeType.symbolGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {//!POPNodeType.db.getString().equals("Variable")) {
				event.acceptTransferModes(TransferMode.MOVE);
    			DropShadow dropShadow = new DropShadow();
        		dropShadow.setRadius(5.0);
        		dropShadow.setOffsetX(3.0);
        		dropShadow.setOffsetY(3.0);
        		dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        		this.setEffect(dropShadow);
			}
		});
		
		this.setOnDragExited(event -> {
			this.setEffect(null);
		});
		
		setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if(DragManager.dragMoving) {
				Node node = null;
				if(DragManager.draggedNode instanceof POPDecisionNode) {
					node = (POPDecisionNode) DragManager.draggedNode;
					insertNode((POPDecisionNode) node);
				} else {
					node = (POPSymbolNode) DragManager.draggedNode;
					insertNode((POPSymbolNode) node);
				}
				
				DragManager.dragMoving = false;
				DragManager.draggedNode = null;
//				if(DragManager.isAllocatedNode) {
					getPrevNode().getScriptArea().add((POPSymbolNode) node);
					DragManager.isAllocatedNode = false;
//				} else {
//					getPrevNode().getScriptArea().getComponent().getChildren().add(node.getOutFlowLine());
//				}
			} else if(db.hasImage() && POPNodeType.symbolGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				Class<? extends POPSymbolNode> nodeClass = null;
				POPSymbolNode node = null;
				try {
					nodeClass = (Class<? extends POPSymbolNode>) Class.forName("kr.co.idiots.model.symbol.POP" + db.getString() + "Node");
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
			event.consume();
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
//        endY.set(value);
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
    	
    	Bounds nodeBound = null;
    	if(prevNode instanceof POPDecisionNode) {
    		nodeBound = ((POPDecisionNode) prevNode).getContents().getBoundsInParent();
    	} else {
    		nodeBound = prevNode.getComponent().getBoundsInParent();
    	}
    	
    	setStartX(nodeBound.getMinX() + (nodeBound.getWidth() / 2));
		setStartY(nodeBound.getMaxY());
		startY.set(line.getStartY());
		
//		startXProperty().bind(prevNode.bottomCenterXProperty());
//		startYProperty().bind(prevNode.bottomYProperty());
    }
        
    public void setNextNode(POPSymbolNode nextNode) {
    	this.nextNode = nextNode;
    	
    	nextNode.setInFlowLine(this);
    	Bounds nextNodeBound = null;
    	if(nextNode instanceof POPDecisionNode) {
    		nextNodeBound = ((POPDecisionNode) nextNode).getContents().getBoundsInParent();
    		System.out.println(nextNodeBound);
    	} else {
    		nextNodeBound = nextNode.getComponent().getBoundsInParent();
    	}
//		Bounds nextNodeBound = nextNode.getComponent().getBoundsInParent();
		setEndX(nextNodeBound.getMinX() + (nextNodeBound.getWidth() / 2));
		System.out.println("*** setnext : " + nextNodeBound.getMinY());
		setEndY(nextNodeBound.getMinY());
		System.out.println(getEndY());
		if(nextNode instanceof POPDecisionNode) {
			
			System.out.println(getEndY());
		}
			
//		endY.set(getEndY());
		
//		endXProperty().bind(nextNode.topCenterXProperty());
//		endYProperty().bind(nextNode.topYProperty());
    }
        
    public void insertNode(POPDecisionNode node) {
    	node.getContents().setLayoutX(line.getStartX() - (node.getContents().getBoundsInParent().getWidth() / 2));
    	node.getContents().setLayoutY(prevNode.getComponent().getBoundsInParent().getMaxY() + nodeMinGap);
    	
    	node.getOutFlowLine().setPrevNode(node);
    	node.getOutFlowLine().setNextNode(nextNode);
    	setNextNode(node); 
    	
    	if(!node.isInitialized) {
    		node.initialize();
    	}
    	
//    	if(node instanceof POPDecisionNode) {
//    		((POPDecisionNode) node).adjustPosition();
//    	}
    	
    	node.setAllocated(true);
    }
    
    public void insertNode(POPSymbolNode node) {    
    	
    	node.getComponent().setLayoutX(line.getStartX() - (node.getComponent().getBoundsInParent().getWidth() / 2));
        node.getComponent().setLayoutY(prevNode.getComponent().getBoundsInParent().getMaxY() + nodeMinGap);
    	
    	node.getOutFlowLine().setPrevNode(node);
    	node.getOutFlowLine().setNextNode(nextNode);
    	setNextNode(node); 
    	
    	if(!node.isInitialized) {
    		node.initialize();
    	}
    	
//    	if(node instanceof POPDecisionNode) {
//    		((POPDecisionNode) node).adjustPosition();
//    	}
    	
    	node.setAllocated(true);    	
    }
        
    public void removeNode(POPSymbolNode node) {
    	prevNode.getScriptArea().getComponent().getChildren().remove(node.getOutFlowLine());
    }
    
    public void adjustPosition(POPNode node) {
    	if(length.getValue().doubleValue() <= node.getComponent().getBoundsInParent().getHeight() + 5) {
    		nextNode.getComponent().setTranslateY(node.getComponent().getBoundsInParent().getHeight() + nodeMinGap);
    	}
    }
    
    public void clear() {
    	nextNode = null;
    }
}