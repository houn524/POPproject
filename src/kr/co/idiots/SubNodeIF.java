package kr.co.idiots;

import java.util.ArrayList;

import javafx.scene.Node;

public interface SubNodeIF {
	
	public ArrayList<Node> getSubNodes();
	public void visibleSubNodes();
	public void invisibleSubNodes();
}
