package kr.co.idiots.util;

import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.AnchorPane;
import kr.co.idiots.model.POPVariableNode;

public class DragManager {
	public static Node draggedNode;
	public static boolean dragMoving = false;
	public static boolean isAllocatedNode = false;
	public static boolean isSynchronized = false;
	public static AnchorPane dragRootPane;
	public static TabPane tabPane;

	public static double lastCenterXOfStartNode;
	public static boolean isAdjustPosSync = true;
}
