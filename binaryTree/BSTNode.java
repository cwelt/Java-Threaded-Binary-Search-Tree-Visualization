package binaryTree;

/**
 * @author Chanan Welt
 *
 * Node - This class represents a single node in a binary tree. 
 * @param <T> - the data to be kept in each node.
 * T must implement the Comparable Interface so it could be managed in the tree by it's order.
 */

public class BSTNode<T extends Comparable<T>>
{
	// Instance variables
	private T data; // the actual object stored in this node
	private BSTNode<T> parent; 
	private BSTNode<T> right;
	private BSTNode<T> left;
	
	/** Constructor */ 
	public BSTNode(T dataElement)
	{
		data = dataElement;
		parent = null;
		right = null;
		left = null;
	}
	
	// Basic getters (access methods): 
	public T getData() {return data;}
	public BSTNode<T> getParent() {return parent;}
	public BSTNode<T> getRight() {return right;}
	public BSTNode<T> getLeft() {return left;}
	
	// Basic setters (modification methods): 
	public void setData(T dataElement) {this.data = dataElement;}
	public void setParent(BSTNode<T> parent) {this.parent = parent;}
	public void setRight(BSTNode<T> right) {this.right = right;}
	public void setLeft(BSTNode<T> left) {this.left = left;}

	/** Determine if a given pointer is pointing to real child or a wire */
	public boolean isPointerWired(BSTNode<T> child)
	{
		if (child == null || !(this.equals(child.getParent())))
			return true;
		else return false;
	}
	
	/** Representation of the tree's node contents. */
	@Override
	public String toString() 
	{
		String data = getData().toString();
		String parent = (getParent() == null) ? "NIL" : getParent().getData().toString();
		String left = (getLeft() == null) ? "NIL" : getLeft().getData().toString();
		String right = (getRight() == null) ? "NIL" : getRight().getData().toString();
		return String.format("[Key:%3s | p:%3s | l:%3s | r:%3s]", data, parent, left, right);
	}
}
