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

    private static final double arrowLength = 20;
    private static final double arrowWidth = 7;
    public static final double nodeMinGap = 30;
    private double decisionGap = 60;
    
    private POPFlowLine(Line line, Line arrow1, Line arrow2, Rectangle area) {
        super(line, arrow1, arrow2, area);
        this.line = line;
        this.area = area;
                        
        line.setStrokeWidth(5.0f);
        arrow1.setStrokeWidth(5.0f);
        arrow2.setStrokeWidth(5.0f);
        area.setFill(Color.rgb(0,  0,  0, 0));
        
        length = Bindings.subtract(line.endYProperty(), line.startYProperty());
        
//        area.xProperty().bind(Bindings.subtract(Bindings.min(startXProperty(), endXProperty()), 20));
//        area.yProperty().bind(Bindings.min(startYProperty(), endYProperty()));
//        area.setWidth(40);
////        area.widthProperty().bind(Bindings.add(Bindings.subtract(startXProperty(), endXProperty()), 40));
//        area.heightProperty().bind(Bindings.subtract(startYProperty(), endYProperty()));
//        area.toFront();
        
//        area.setX(Math.min(getStartX(), getEndX()) - 20);
//    	area.setY(Math.min(getStartY(), getEndY()));
//    	area.setWidth(Math.abs(getStartX() - getEndX()) + 40);
//    	area.setHeight(Math.abs(getStartY() - getEndY()));
        
        InvalidationListener updater = new InvalidationListener() {

			@Override
			public void invalidated(Observable arg0) {
				// TODO Auto-generated method stub

				double ex = getEndX();
				double ey = getEndY();
				
//				if(nextNode != null) {
//					Bounds nextNodeBound = nextNode.getBoundsInParent();
//					ex = nextNodeBound.getMinX() + (nextNodeBound.getWidth() / 2);//getEndX();
//		            ey = nextNodeBound.getMinY();//getEndY();
//		            line.setEndX(ex);
//		            line.setEndY(ey);
//				}
				
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
        
//        area.xProperty().bind(Bindings.subtract(Bindings.min(startXProperty(), endXProperty()), 20));
//        area.yProperty().bind(startYProperty());
//        area.setWidth(40);
////        area.widthProperty().bind(Bindings.add(Bindings.subtract(startXProperty(), endXProperty()), 40));
//        area.heightProperty().bind(Bindings.subtract(startYProperty(), endYProperty()));
//        area.setFill(Color.BLACK);
//        area.setX(Math.min(getStartX(), getEndX()) - 20);
//    	area.setY(Math.min(getStartY(), getEndY()));
//    	area.setWidth(Math.abs(getStartX() - getEndX()) + 40);
//    	area.setHeight(Math.abs(getStartY() - getEndY()));
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
//    	Thread thread = new Thread() {
//            @Override
//            public void run() {
//                PlatformHelper.run(() -> {
//                	Number newLength;
//                	newLength = endY.getValue() - startY.getValue();
//    				if((newLength.doubleValue() != nodeMinGap) && nextNode != null && !DragManager.isSynchronized) {
//    					if(nextNode instanceof POPDecisionEndNode) {
//    						if(decisionNode != null)
//    							decisionNode.calMaxLength();
////    						nextNode.getComponent().setLayoutY(((POPDecisionEndNode) nextNode).getDecisionNode().getLayoutY() + ((POPDecisionEndNode) nextNode).getDecisionNode().getMaxLength());
//    					} else if(prevNode instanceof POPDecisionStartNode) {
//    						nextNode.getComponent().setLayoutY(nextNode.getComponent().getLayoutY() + (nodeMinGap + 30) - newLength.doubleValue());
//    					} else {
//    						nextNode.getComponent().setLayoutY(nextNode.getComponent().getLayoutY() + (nodeMinGap - newLength.doubleValue()));
//    						
//    					}
//    						
//    					System.out.println("center : " + nextNode);
//    					
//    					nextNode.moveCenter();
//    				}
//                });
////                try { Thread.sleep(100); } catch (InterruptedException e) {}
//            }
//        };
//        
//        thread.setDaemon(true);
//        thread.start();
        
        Thread thread = new Thread() {
            @Override
            public void run() {
            	
                PlatformHelper.run(() -> {
                	pullNodes();
//                	POPSymbolNode subNode = rootNode;
//                	POPSymbolNode subNode2;
//                	while(true) {
//                		if(subNode instanceof POPLoopNode) {
//                			((POPLoopNode) subNode).getLoopStartNode().getOutFlowLine().lengthChanging(0, 0);
//                 		} else if(subNode instanceof POPDecisionNode) {
//                 			((POPDecisionNode) subNode).getLeftStartNode().getOutFlowLine().lengthChanging(0, 0);
//                 			((POPDecisionNode) subNode).getRightStartNode().getOutFlowLine().lengthChanging(0, 0);
//                 		}
//                		if(subNode.getOutFlowLine() == null || subNode.getOutFlowLine().nextNode == null) {
//                			break;
//                		}
//                		System.out.println(subNode);
//                		
//                		if(subNode.getOutFlowLine().nextNode instanceof POPDecisionEndNode) {
//                			((POPDecisionEndNode) subNode.getOutFlowLine().nextNode).getDecisionNode().calMaxLength();
//                			break;
//                		} else {
//                			subNode.getOutFlowLine().nextNode.setLayoutY(subNode.getOutFlowLine().getStartY() + nodeMinGap);
//                			if(subNode instanceof POPLoopNode) {
//                				System.out.println("루프 : " + subNode.getOutFlowLine().nextNode);
//                			}
//                		}
//                		
//                		subNode = subNode.getOutFlowLine().nextNode;
//                		System.out.println("Change!!!!!");
//                	}
//                	
//                	if(rootNode.getInFlowLine() != null) {
//                		rootNode.getInFlowLine().lengthChanging(0, 0);
//                	}
                	
                });
//                try { Thread.sleep(100); } catch (InterruptedException e) {}
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
//				lengthChanging(oldLength, newLength);
				
				
//				if(nextNode != null) {
//					nextNode.moveCenter();
//					
//				}
					
			}
        });
    }
    
    public void setOnFlowLineDrag() throws ClassNotFoundException {
    	
    	
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
				
				node = (POPSymbolNode) DragManager.draggedNode;
				insertNode((POPSymbolNode) node);
				
				DragManager.dragMoving = false;
				DragManager.draggedNode = null;
//				if(DragManager.isAllocatedNode) {
					getPrevNode().getScriptArea().addWithOutFlowLine((POPSymbolNode) node);
//					if(((POPSymbolNode) node).getOutFlowLine() != null) {
//						POPSolvingLayoutController.scriptArea.getComponent().getChildren().add(((POPSymbolNode) node).getOutFlowLine());
//					}
					DragManager.isAllocatedNode = false;
//				} else {
//					getPrevNode().getScriptArea().getComponent().getChildren().add(node.getOutFlowLine());
//				}
			} else if(db.hasImage() && POPNodeType.symbolGroup.contains(Enum.valueOf(POPNodeType.class, db.getString()))) {
				Class<? extends POPSymbolNode> nodeClass = null;
				POPSymbolNode node = null;
				node = (POPSymbolNode) POPNodeFactory.createPOPNode(db.getString());
				node.initialize();
				getPrevNode().getScriptArea().addWithOutFlowLine(node);
				insertNode(node);
			}
			event.consume();
		});
	}
    
//    public final void setStartX(double value) {
//        line.setStartX(value);
//        updateDragArea();
//    }

    public final double getStartX() {
        return line.getStartX();
    }

    public final DoubleProperty startXProperty() {
        return line.startXProperty();
    }

//    public final void setStartY(double value) {
//        line.setStartY(value);
//        startY.set(value);
//        updateDragArea();
//    }

    public final double getStartY() {
        return line.getStartY();
    }

    public final DoubleProperty startYProperty() {
        return line.startYProperty();
    }

    public final void setEndX(double value) {
//        line.setEndX(value);
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
//        line.setEndY(value);
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
//    	area.setX(Math.min(getStartX(), getEndX()) - 20);
//    	area.setY(Math.min(getStartY(), getEndY()));
//    	area.setWidth(Math.abs(getStartX() - getEndX()) + 40);
//    	area.setHeight(Math.abs(getStartY() - getEndY()));
    }
    
    public void setPrevNode(POPSymbolNode prevNode) {
    	this.prevNode = prevNode;
    	
    	Bounds nodeBound = null;
//    	if(prevNode instanceof POPDecisionNode) {
//    		nodeBound = ((POPDecisionNode) prevNode).getContents().getBoundsInParent();
//    	} else {
    		nodeBound = prevNode.getComponent().getBoundsInParent();
//    	}
    	
    	if(prevNode instanceof POPDecisionNode) {
    		startYProperty().bind(((POPDecisionNode) prevNode).getLeftEndNode().layoutYProperty());
    	} else if(prevNode instanceof POPLoopNode) {
    		startYProperty().bind(((POPLoopNode) prevNode).getLeftInFlowLine().endYProperty());
    	} else {
    		startYProperty().bind(Bindings.add(prevNode.layoutYProperty(), prevNode.heightProperty()));
    	}
    	startXProperty().bind(Bindings.add(prevNode.layoutXProperty(), Bindings.divide(prevNode.widthProperty(), 2)));
    	
//    	setStartX(nodeBound.getMinX() + (nodeBound.getWidth() / 2));
    	
    	
//    	if(prevNode instanceof POPDecisionNode) {
//    		setStartY(((POPDecisionNode) prevNode).getLeftEndNode().getLayoutY());
//    	} else if(prevNode instanceof POPLoopNode) {
//    		setStartY(((POPLoopNode) prevNode).getLeftInFlowLine().getEndY());
//    	} else {
//    		setStartY(nodeBound.getMaxY());
//    	}
//		
//		startY.set(line.getStartY());
		
//		startXProperty().bind(prevNode.bottomCenterXProperty());
//		startYProperty().bind(prevNode.bottomYProperty());
    }
    
//    public void setPrevNode(POPDecisionNode prevNode) {
//    	this.prevNode = prevNode;
//    	
//    	Bounds nodeBound = null;
////    	if(prevNode instanceof POPDecisionNode) {
////    		nodeBound = ((POPDecisionNode) prevNode).getContents().getBoundsInParent();
////    	} else {
//    		nodeBound = prevNode.getComponent().getBoundsInParent();
////    	}
//    	
//    	if(nextNode instanceof POPDecisionNode) {
//        	setStartX(nodeBound.getMinX() + (nodeBound.getWidth() / 2));
//        	setStartY(((POPDecisionNode) nextNode).getLeftEndNode().getLayoutY());
//        } else {
//        	setStartX(nodeBound.getMinX() + (nodeBound.getWidth() / 2));
//    		setStartY(prevNode.getLeftEndNode().getLayoutY());
//        }
//    		
//    	
//		startY.set(line.getStartY());
//		
////		startXProperty().bind(prevNode.bottomCenterXProperty());
////		startYProperty().bind(prevNode.bottomYProperty());
//    }
//    
//    public void setPrevNode(POPLoopNode prevNode) {
//    	this.prevNode = prevNode;
//    	
//    	Bounds nodeBound = null;
//    	nodeBound = prevNode.getComponent().getBoundsInParent();
//    	
//    	if(nextNode instanceof POPLoopNode) {
//        	setStartX(nodeBound.getMinX() + (nodeBound.getWidth() / 2));
//        	setStartY(((POPLoopNode) nextNode).getLeftInFlowLine().getEndY());
//        } else {
//        	setStartX(nodeBound.getMinX() + (nodeBound.getWidth() / 2));
//    		setStartY(prevNode.getLeftInFlowLine().getEndY());
//    		System.out.println("dd");
//        }
//    		
//    	
//		startY.set(line.getStartY());
//		
//    }
        
    public void setNextNode(POPSymbolNode nextNode) {
    	this.nextNode = nextNode;
    	
    	nextNode.setInFlowLine(this);
    	Bounds nextNodeBound = null;
//    	if(nextNode instanceof POPDecisionNode) {
//    		nextNodeBound = ((POPDecisionNode) nextNode).getContents().getBoundsInParent();
//    		System.out.println(nextNodeBound);
//    	} else {
    		nextNodeBound = nextNode.getComponent().getBoundsInParent();
//    	}
//		Bounds nextNodeBound = nextNode.getComponent().getBoundsInParent();
    	
    	endXProperty().bind(Bindings.add(nextNode.layoutXProperty(), Bindings.divide(nextNode.widthProperty(), 2)));
    	endYProperty().bind(nextNode.layoutYProperty());
    		
//		setEndX(nextNodeBound.getMinX() + (nextNodeBound.getWidth() / 2));
//		setEndY(nextNodeBound.getMinY());
			
//		endY.set(getEndY());
		
//		endXProperty().bind(nextNode.topCenterXProperty());
//		endYProperty().bind(nextNode.topYProperty());
    }
        
//    public void insertNode(POPDecisionNode node) {
//    	node.getContents().setLayoutX(line.getStartX() - (node.getContents().getBoundsInParent().getWidth() / 2));
//    	node.getContents().setLayoutY(prevNode.getComponent().getBoundsInParent().getMaxY() + nodeMinGap);
//    	
//    	node.getOutFlowLine().setPrevNode(node);
//    	node.getOutFlowLine().setNextNode(nextNode);
//    	setNextNode(node); 
//    	
//    	if(!node.isInitialized) {
//    		node.initialize();
//    	}
//    	
////    	if(node instanceof POPDecisionNode) {
////    		((POPDecisionNode) node).adjustPosition();
////    	}
//    	
//    	node.setAllocated(true);
//    }
    
    public void insertNode(POPSymbolNode node) {    
    	
//    	if(rootNode != null) {
//    		node.getOutFlowLine().setRootNode(rootNode);
//    	}
    	
    	if(nextNode.getParentNode() != null) {
    		node.setParentNode(nextNode.getParentNode());
    	}
    	
//    	if(startNode != null) {
//    		node.getOutFlowLine().setStartNode(startNode);
//    		if(node instanceof SubNodeIF) {
//    			for(Node subNode : ((SubNodeIF) node).getSubNodes()) {
//    				if(subNode instanceof POPSymbolNode && ((POPSymbolNode) subNode).getOutFlowLine() != null) {
//    					((POPSymbolNode) subNode).getOutFlowLine().setStartNode(startNode);
//    				}
//    			}
//    		}
//    	}
    	
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
    			
//    			if(node instanceof POPDecisionNode) {
//    				decisionNode.setMaxLength(decisionNode.getMaxLength() + ((POPDecisionNode) node).getMaxHeight() + nodeMinGap + 30);
//    				((POPDecisionNode) node).setParentDecisionNode(decisionNode);
//    			} else {
//    				decisionNode.setMaxLength(decisionNode.getMaxLength() + node.getComponent().getBoundsInParent().getHeight() + nodeMinGap);
//    			}
    			decisionNode.calMaxLength();
    			
//        		nextNode.getComponent().setLayoutY(decisionNode.getLayoutY() + decisionNode.getMaxLength());
        		
        		
    		}
//    		decisionNode.adjustPosition();
//    		System.out.println("**");
//    		System.out.println("ab");
    		
    	}
    	
    	if(loopNode != null) {
    		node.getOutFlowLine().setLoopNode(loopNode);
    		
    		loopNode.getSubNodes().add(node);
//    		if(decisionNode.getLeftEndNode().getInFlowLine().length.doubleValue() < nodeMinGap || decisionNode.getRightEndNode().getInFlowLine().length.doubleValue() < nodeMinGap) {//((POPDecisionEndNode) nextNode).getInFlowLine().length.doubleValue() < 0) {
//        		
//    			if(node instanceof POPDecisionNode) {
//    				((POPDecisionNode) node).setParentDecisionNode(decisionNode);
//    			}
//    			
////    			if(node instanceof POPDecisionNode) {
////    				decisionNode.setMaxLength(decisionNode.getMaxLength() + ((POPDecisionNode) node).getMaxHeight() + nodeMinGap + 30);
////    				((POPDecisionNode) node).setParentDecisionNode(decisionNode);
////    			} else {
////    				decisionNode.setMaxLength(decisionNode.getMaxLength() + node.getComponent().getBoundsInParent().getHeight() + nodeMinGap);
////    			}
//    			decisionNode.calMaxLength();
//    			
//        		nextNode.getComponent().setLayoutY(decisionNode.getLayoutY() + decisionNode.getMaxLength());
//        		
//    		}
//    		loopNode.adjustPosition();
//    		System.out.println("cd");
    	}
    			
    	 
    	
//    	setNextNode(node);
    	if(decisionNode != null) {
    		decisionNode.adjustPositionThread();
    		node.getComponent().setLayoutX(line.getStartX() - (node.getComponent().getBoundsInParent().getWidth() / 2));
    	} else if(loopNode != null) {
    		loopNode.adjustPositionThread();
    	}
    	
    	
    	if(!node.isInitialized) {
    		node.initialize();
    	}
    	
//    	if(node instanceof POPDecisionNode) {
//    		((POPDecisionNode) node).adjustPosition();
//    	}
    	
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
    	
    	
//    	if(startNode != null) {
//    		startNode.getOutFlowLine().lengthChanging(0, 0);
//    	} else {
//    		System.out.println("요래");
//    		lengthChanging(0, 0);
//    	}
    	
//    	if(nextNode instanceof POPDecisionEndNode) {
//    		((POPDecisionEndNode) nextNode).getDecisionNode().adjustPosition();
//    
//    	}
//    	if(loopNode != null)
//    		loopNode.adjustPosition();
    }
    
    public void removeNodeOfDecision() {
    	if(decisionNode != null) {
			decisionNode.getSubNodes().remove(prevNode);
			
//			decisionNode.getLeftEndNode().setLayoutY(decisionNode.getLayoutY() + decisionNode.getMaxLength());
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
			
//			if(decisionNode.getParentDecisionNode() != null) {
//				if(prevNode == decisionNode) {
//					decisionNode.setParentDecisionNode(null);
//				}
//			}
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