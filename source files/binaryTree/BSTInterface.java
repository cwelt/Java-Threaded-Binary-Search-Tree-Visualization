package binaryTree;

/**
 * @author Chanan Welt
 * ADT Interface of basic Binary Search Tree operations. 
 * @param <T> - The data type saved in tree.
 */
public interface BSTInterface<T extends Comparable<T>>
{	
	public BSTNode<T> getRoot();
	public void setRoot(BSTNode<T> newRoot);
	public BSTNode<T> insert(T data);
	public BSTNode<T> delete(BSTNode<T> node);
	public BSTNode<T> search(BSTNode<T> node, T data);
	public BSTNode<T> getSuccessor(BSTNode<T> x);
	public BSTNode<T> getPredecessor(BSTNode<T> x);
	public BSTNode<T> getMinimum(BSTNode<T> node);
	public BSTNode<T> getMaximum(BSTNode<T> node);
	public String inorderTreeWalk(BSTNode<T> node);
	public String preorderTreeWalk(BSTNode<T> node);
	public String postorderTreeWalk(BSTNode<T> node);
}
