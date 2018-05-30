package kr.co.idiots.model;

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
import kr.co.idiots.POPNodeFactory;
import kr.co.idiots.model.symbol.POPDecisionEndNode;
import kr.co.idiots.model.symbol.POPDecisionNode;
import kr.co.idiots.model.symbol.POPDecisionStartNode;
import kr.co.idiots.model.symbol.POPLoopNode;
import kr.co.idiots.model.symbol.POPStartNode;
import kr.co.idiots.model.symbol.POPSymbolNode;
import kr.co.idiots.util.DragManager;
import kr.co.idiots.util.PlatformHelper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPFlowLine extends Group {

    private Line line;
    private Line arrow1;
    private Line arrow2;
    private Rectangle area;
    protected POPSymbolNode prevNode;
    protected POPSymbolNode nextNode;
    protected POPDecisionNode decisionNode = null;
    protected POPLoopNode loopNode = null;
    protected POPSymbolNode rootNode = null;
    protected POPStartNode startNode = null;
    
    protected DoubleProperty startY = new SimpleDoubleProperty(0);
    protected DoubleProperty endY = new SimpleDoubleProperty(0);
    protected NumberBinding length;// = Bindings.subtract(line.endYProperty(), line.startYProperty());

    public POPFlowLine() {
    	this(new Line(), new Line(), new Line(), new Rectangle());
    }
    
    public POPFlowLine(POPSymbolNode prevNode, POPSymbolNode nextNode) {
        this(new Line(), new Line(), new Line(), new Rectangle());
        this.prevNode = prevNode;
    	this.nextNode = nextNode;
    	
    }

    private static final double arrowLength = 10;
    private static final double arrowWidth = 7;
    public static final double nodeMinGap = 30;
    private double decisionGap = 40;
    
    private POPFlowLine(Line line, Line arrow1, Line arrow2, Rectangle area) {
        super(line, arrow1, arrow2, area);
        this.line = line;
        this.arrow1 = arrow1;
        this.arrow2 = arrow2;
        this.area = area;
                        
        line.setStrokeWidth(3.0f);
        arrow1.setStrokeWidth(3.0f);
        arrow2.setStrokeWidth(3.0f);
        area.setFill(Color.rgb(0,  0,  0, 0));
        
        length = Bindings.subtract(line.endYProperty(), line.startYProperty());
        
        InvalidationListener updater = new InvalidationListener() {

			@Override
			public void invalidated(Observable arg0) {
				// TODO Auto-generated method stub

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
			}
        	
        };
        
        setOnLengthChange();

        this.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds arg1, Bounds arg2) {
				// TODO Auto-generated method stub
				area.setX(Math.min(getStartX(), getEndX()) - 20);
		    	area.setY(Math.min(getStartY(), getEndY()));
		    	area.setWidth(Math.abs(getStartX() - getEndX()) + 40);
		    	area.setHeight(Math.abs(getStartY() - getEndY()));
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
    
    public void pullNodes() {
    	POPSymbolNode subNode = prevNode;
    	POPSymbolNode subNode2;
    	while(true) {
    		if(subNode instanceof POPLoopNode) {
    			((POPLoopNode) subNode).getLoopStartNode().getOutFlowLine().pullNodes();
     		} else if(subNode instanceof POPDecisionNode) {
     			((POPDecisionNode) subNode).getLeftStartNode().getOutFlowLine().pullNodes();
     			((POPDecisionNode) subNode).getRightStartNode().getOutFlowLine().pullNodes();
     		}
    		
    		if(subNode instanceof POPLoopNode) {
    			((POPLoopNode) subNode).adjustPosition();
    		} else if(subNode instanceof POPDecisionNode) {
    			((POPDecisionNode) subNode).adjustPosition();
    		}
    		
    		if(subNode.getOutFlowLine() == null || subNode.getOutFlowLine().nextNode == null) {
    			break;
    		}
    		
    		if(subNode.getOutFlowLine().nextNode instanceof POPDecisionEndNode) {
    			((POPDecisionEndNode) subNode.getOutFlowLine().nextNode).getDecisionNode().calMaxLength();
    			break;
    		} else {
    			subNode.getOutFlowLine().nextNode.setLayoutY(subNode.getOutFlowLine().getStartY() + nodeMinGap);
    			if(subNode instanceof POPLoopNode) {
    			}
    		}
    		
    		
    		
    		subNode = subNode.getOutFlowLine().nextNode;
    	}
    	
    }
    
    public void pullNodesThread() {
        
        Thread thread = new Thread() {
            @Override
            public void run() {
                PlatformHelper.run(() -> {
                	pullNodes();
                });
            }
        };
        
        thread.setDaemon(true);
        thread.start();
    }
    
    protected void setOnLengthChange() {
    	length.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldLength, Number newLength) {
				// TODO Auto-generated method stub
			}
        });
    }
    
    public void setOnFlowLineDrag() throws ClassNotFoundException {
    	
    	
		setOnDragOver(event -> {
			Dragboard db = event.getDragboard();
			if(db.hasImage() && POPNodeType.symbolGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {//!POPNodeType.db.getString().equals("Variable")) {
				event.acceptTransferModes(TransferMode.COPY);
    			DropShadow dropShadow = new DropShadow();
        		dropShadow.setRadius(5.0);
        		dropShadow.setOffsetX(3.0);
        		dropShadow.setOffsetY(3.0);
        		dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        		this.setEffect(dropShadow);
        		
        		line.setStroke(Color.GREEN);
        		arrow1.setStroke(Color.GREEN);
        		arrow2.setStroke(Color.GREEN);
			}
			
			event.consume();
		});
		
		this.setOnDragExited(event -> {
			this.setEffect(null);
			
			line.setStroke(Color.BLACK);
    		arrow1.setStroke(Color.BLACK);
    		arrow2.setStroke(Color.BLACK);
		});
		
		setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if(DragManager.dragMoving) {
				Node node = null;
				
				node = (POPSymbolNode) DragManager.draggedNode;
				insertNode((POPSymbolNode) node);
				
				DragManager.dragMoving = false;
				DragManager.draggedNode = null;
				
				getPrevNode().getScriptArea().addWithOutFlowLine((POPSymbolNode) node);
				DragManager.isAllocatedNode = false;
			} else if(db.hasImage() && POPNodeType.symbolGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				Class<? extends POPSymbolNode> nodeClass = null;
				POPSymbolNode node = null;
				node = (POPSymbolNode) POPNodeFactory.createPOPNode(db.getString());
				node.initialize();
				insertNode(node);
				if(node instanceof POPLoopNode)
					((POPLoopNode) node).adjustPosition();
				else if(node instanceof POPDecisionNode)
					((POPDecisionNode) node).adjustPosition();
				getPrevNode().getScriptArea().addWithOutFlowLine(node);
			}
			event.consume();
		});
	}
    
    public final double getStartX() {
        return line.getStartX();
    }

    public final DoubleProperty startXProperty() {
        return line.startXProperty();
    }

    public final double getStartY() {
        return line.getStartY();
    }

    public final DoubleProperty startYProperty() {
        return line.startYProperty();
    }

    public final double getEndX() {
        return line.getEndX();
    }

    public final DoubleProperty endXProperty() {
        return line.endXProperty();
    }

    public final void setEndY(double value) {
        endY.set(value);
    }

    public final double getEndY() {
        return line.getEndY();
    }

    public final DoubleProperty endYProperty() {
        return line.endYProperty();
    }
    
    public double getLength() { return length.getValue().doubleValue(); }
        
    public void setPrevNode(POPSymbolNode prevNode) {
    	this.prevNode = prevNode;
    	
    	Bounds nodeBound = null;
    	
    	if(prevNode instanceof POPDecisionNode) {
    		startYProperty().bind(((POPDecisionNode) prevNode).getLeftEndNode().layoutYProperty());
    	} else if(prevNode instanceof POPLoopNode) {
    		startYProperty().bind(((POPLoopNode) prevNode).getLeftInFlowLine().endYProperty());
    	} else {
    		startYProperty().bind(Bindings.add(prevNode.layoutYProperty(), prevNode.heightProperty()));
    	}
    	startXProperty().bind(Bindings.add(prevNode.layoutXProperty(), Bindings.divide(prevNode.widthProperty(), 2)));
    	
    }
    
    public void setNextNode(POPSymbolNode nextNode) {
    	this.nextNode = nextNode;
    	
    	nextNode.setInFlowLine(this);
    	Bounds nextNodeBound = null;
		nextNodeBound = nextNode.getComponent().getBoundsInParent();
    	
    	endXProperty().bind(Bindings.add(nextNode.layoutXProperty(), Bindings.divide(nextNode.widthProperty(), 2)));
    	endYProperty().bind(nextNode.layoutYProperty());
    		
    }
        
    
    public void insertNode(POPSymbolNode node) {    
    	
    	if(nextNode.getParentNode() != null) {
    		node.setParentNode(nextNode.getParentNode());
    	}
    	
    	node.getComponent().setLayoutX(line.getStartX() - (node.getComponent().getBoundsInParent().getWidth() / 2));
    	
    	if(prevNode instanceof POPDecisionStartNode) {
    		node.getComponent().setLayoutY(prevNode.getComponent().getBoundsInParent().getMaxY() + nodeMinGap + 30);
    	} else {
    		node.getComponent().setLayoutY(prevNode.getComponent().getBoundsInParent().getMaxY() + nodeMinGap);
    	}
    	
    	node.getOutFlowLine().setPrevNode(node);
    	node.getOutFlowLine().setNextNode(nextNode);
    	if(decisionNode != null) {
    		node.getOutFlowLine().setDecisionNode(decisionNode);
    		
    		decisionNode.getSubNodes().add(node);
    		if(decisionNode.getLeftEndNode().getInFlowLine().length.doubleValue() < nodeMinGap || decisionNode.getRightEndNode().getInFlowLine().length.doubleValue() < nodeMinGap) {//((POPDecisionEndNode) nextNode).getInFlowLine().length.doubleValue() < 0) {
        		
    			if(node instanceof POPDecisionNode) {
    				((POPDecisionNode) node).setParentDecisionNode(decisionNode);
    			}
    			
    			decisionNode.calMaxLength();
    			
    		}
    		
    	}
    	
    	if(loopNode != null) {
    		node.getOutFlowLine().setLoopNode(loopNode);
    		
    		loopNode.getSubNodes().add(node);
    	}
    			
    	if(decisionNode != null) {
    		decisionNode.adjustPositionThread();
    		node.getComponent().setLayoutX(line.getStartX() - (node.getComponent().getBoundsInParent().getWidth() / 2));
    	} else if(loopNode != null) {
    		loopNode.adjustPositionThread();
    	}
    	
    	
    	if(!node.isInitialized) {
    		node.initialize();
    	}
    	
    	node.setAllocated(true);    	
    	
    	setNextNode(node);
    	
    	POPSymbolNode root = node.getParentNode();
    	while(true) {
    		if(root.getParentNode() != null) {
    			root = root.getParentNode();
    		} else {
    			break;
    		}
    	}
    	root.getOutFlowLine().pullNodesThread();
    }
    
    public void removeNodeOfDecision() {
    	if(decisionNode != null) {
			decisionNode.getSubNodes().remove(prevNode);
			
			decisionNode.adjustPositionThread();
			
			if(decisionNode.getParentDecisionNode() != null) {
				if(prevNode == decisionNode) {
					decisionNode.setParentDecisionNode(null);
				}
			}
		}
    }
    
    public void removeNodeOfLoop() {
    	if(loopNode != null) {
			loopNode.getSubNodes().remove(prevNode);
			
			loopNode.adjustPositionThread();
		}
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