package game;

import java.util.*;

public class Tree<T>{
	private List<Tree<T>> children;
	private T node;

	public Tree(T object){
		children = new ArrayList<Tree<T>>();
		node = object;
	}

	public T getObject(){
		return node; 
	}

	public List<Tree<T>> getChildren(){
		return children;
	}

	public void addChild(Tree<T> child){
		children.add(child);
	}
}