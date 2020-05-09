package wiredBinarySearchTree;

import binaryTree.BSTNode;
import binaryTree.BSTInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.management.openmbean.KeyAlreadyExistsException;

/**
 * @author Chanan Welt
 * 
 * This class implements the wired binary search tree ADT. 
 * @param <T> - the generic data type to save in each node.
 * T must be comparable, so that it can be searched and sorted. 
 */
public class WiredBST<T extends Comparable<T>> implements BSTInterface<T>
{
	//Instance variables
	private BSTNode<T> root;
	private BSTNode<T> median; 
	private int elementsSmallerThanMedian;
	private int elementsLargerThanMedian;

	/** Constructor 1 - default version.
	 *  Construct an empty wired BST(binary search tree).
	 */
	public WiredBST()
	{
		root = null;
		median = null;
		elementsSmallerThanMedian = 0;
		elementsLargerThanMedian = 0;
	}
	
	/** Constructor 2 - customized version 
	 * Construct a wired BST which is rooted from the given node.
	 * @param node - the root of the new tree.
	 * */ 
	public WiredBST(BSTNode<T> node)
	{
		root = node;
		median = node;
		elementsSmallerThanMedian = 0;
		elementsLargerThanMedian = 0;
	}
	
	// basic setter and getters: 
	public BSTNode<T> getRoot() {return root;}
	public void setRoot(BSTNode<T> newRoot) {this.root = newRoot;}

	
	/** 
	 * Insert a new element into the tree 
	 */
	@Override
	public BSTNode<T> insert(T newElement) 
	{
		// Initialization:
		BSTNode<T> x; // utility pointer to find appropriate position for new node z
		BSTNode<T> z; // the new node for the new Elements data argument  
		
		x = root;
		z = new BSTNode<T>(newElement); // create node for the new dataElement 
		
		// First we find suitable place on tree to place new node z
		while (x != null)
		{
			// navigate right if new key is larger & right child is not wired:
			if ((z.getData().compareTo(x.getData()) == 0))
				throw new KeyAlreadyExistsException(String.format("Duplicate key for element <%s>", z.getData())); 
			
			// navigate left if new key is smaller & left child is not wired:  
			else if ((z.getData().compareTo(x.getData()) < 0) && !(x.isPointerWired(x.getLeft())))
				x = x.getLeft();
			
			// navigate right if new key is larger & right child is not wired:
			else if ((z.getData().compareTo(x.getData()) > 0) &&!(x.isPointerWired(x.getRight())))
				x = x.getRight();
			
			// end navigation if next node is either wired or NIL
			else break;  
		}
		
		// Now we connect & wire z to it's place: 
		
		// case 1: tree was empty, set new node to be it's root
		if (x == null) 
			this.setRoot(z);
		
		// case 2: z should be a left child of x:
		else if ((z.getData().compareTo(x.getData()) < 0))
		{
			z.setParent(x);
			z.setLeft(x.getLeft());
			z.setRight(x);
			x.setLeft(z);
		}
		
		else // case 3: z should be a right child of x:
		{
			z.setParent(x);
			z.setLeft(x);
			z.setRight(x.getRight());
			x.setRight(z);
		}
		
		// median maintenance: 
		if (median == null) // tree was empty, set the new node z to be the median 
			median = z;
		// compare new key to median key and update the appropriate counter accordingly:  
		else if (z.getData().compareTo(median.getData()) < 0)
			this.elementsSmallerThanMedian++;
		else if (z.getData().compareTo(median.getData()) > 0) 
			this.elementsLargerThanMedian++;
		//update the median if necessary according to the counters updated state: 
		updateMedian();
		
		return z; // return the new node inserted
	}
	
	/**
	 * Delete (pointer version) - delete & return given node. 
	 * @param z node to be deleted. 
	 * @return z node which was deleted, or null if the tree is empty 
	 */
	@Override
	public BSTNode<T> delete(BSTNode<T> z) 
	{
		// if the given node for deletion is null, do nothing: 
		if (z == null)
			return null;
		
		// Initialize utility pointers for forward processing: 
		BSTNode<T> zParent = z.getParent();
		BSTNode<T> zLeft = z.getLeft();
		BSTNode<T> zRight = z.getRight();
		BSTNode<T> zSuccessor = this.getSuccessor(z);
		BSTNode<T> zPredecessor = this.getPredecessor(z);
		
		// Handle case 1: z has two "real" children (NOT wires):
		if (!(z.isPointerWired(zLeft)) && !(z.isPointerWired(zRight)))
		{
			if ((zRight != zSuccessor) && (!zSuccessor.isPointerWired(zSuccessor.getRight())))
			{
				zSuccessor.getRight().setParent(zSuccessor.getParent());
				zSuccessor.getParent().setLeft(zSuccessor.getRight());
			}
			
			zSuccessor.setLeft(z.getLeft());
			zSuccessor.getLeft().setParent(zSuccessor);
			
			if (zSuccessor != zRight)
			{
				zSuccessor.setRight(zRight);
				zRight.setParent(zSuccessor);
			}
			
			zSuccessor.setParent(zParent);
			if(zSuccessor.getParent() == null)
				setRoot(zSuccessor);
			else if (z == zSuccessor.getParent().getLeft())
				zSuccessor.getParent().setLeft(zSuccessor);
			else zSuccessor.getParent().setRight(zSuccessor);
			
			zPredecessor.setRight(zSuccessor);
		}
		
		// Handle case 2: z has a child on right, and a wire on left 
		else if (!z.isPointerWired(zRight) && z.isPointerWired(zLeft))
		{// we want to replace z with it's right child:
			
			// set z'ds parent to be the parent of z'ds child:
			zRight.setParent(zParent);
			
			// if z was the root of the tree then make it's child the new root:  
			if(zParent == null)
				this.setRoot(zRight);
			
			// if z was a left child, set it's parent left pointer to z'ds child:
			else if (z == zParent.getLeft())
				zParent.setLeft(zRight);
			
			// Similarly, if z was a right child, set it's parent right pointer to z'ds child: 
			else zParent.setRight(zRight);
			
			// set z'ds successor left pointer point to z'ds predecessor:
			zSuccessor.setLeft(zLeft);
		}
		
		// Case 3: z has a child on left & a wire on right:  
		else if (!z.isPointerWired(zLeft) && z.isPointerWired(zRight))
		{// we want to replace z with it's left child 
			
			// set z'ds parent to be the parent of z'ds child:
			zLeft.setParent(zParent);
				
			// if z was the root of the tree then make it's child the new root:  
			if(zParent == null)
				this.setRoot(zLeft);
				
			// if z was a left child, set it's parent left pointer to z'ds child:
			else if (z == zParent.getLeft())
				zParent.setLeft(zLeft);
				
			// Similarly, if z was a right child, set it's parent right pointer to z'ds child: 
			else zParent.setRight(zLeft);
				
			// set z'ds predecessors right pointer point to z'ds successor:
			zPredecessor.setRight(zRight);
		}
		
		// Handle case 4: z has two "leaves" i.e, both left & right pointers are wires 
		else if (z.isPointerWired(zLeft) && (z.isPointerWired(zRight)))
		{
			// if z is the root then make the root NIL
			if(zParent == null)
				this.setRoot(null);
				
			// if z is a left child, set it's parent left pointer to z'ds predecessor:
			else if (z == zParent.getLeft())
				zParent.setLeft(zPredecessor);
				
			// if z is a right child, set it's parent right pointer to z'ds successor: 
			else zParent.setRight(zSuccessor);
		}
		
		/* median maintenance */ 
		// if we just deleted the median, set a new one according to new balance: 
		if (z == median) 
		{
			if (elementsLargerThanMedian == elementsSmallerThanMedian)
			{
				median = zPredecessor;
				if (median != null)
					elementsSmallerThanMedian--;
			}
			else if (elementsLargerThanMedian > elementsSmallerThanMedian)
			{
				median = zSuccessor;
				if (median != null)
					elementsLargerThanMedian--;
			}
		}
		
		else // we didn't delete median, update new balance and update median if necessary:
		{// compare new key to median key and update the appropriate counter accordingly:  
			if (z.getData().compareTo(median.getData()) < 0)
				elementsSmallerThanMedian--;
			else if (z.getData().compareTo(median.getData()) > 0) 
				elementsLargerThanMedian--;
			//update the median if necessary according to the counters updated state: 
			updateMedian();
		}

		return z; // return node which was deleted
	}

	/**
	 * Delete (key version) - delete & return node which contains the given data. 
	 * This method is an overloaded version of Delete, enabling applications to invoke deletion directly 
	 * by a given key, without the need to search for it's node explicit reference pointer. 
	 * @param k key(data) to be deleted. 
	 * @return z node which was deleted, or null if k does not exist in tree.  
	 */
	public BSTNode<T> delete(T k) 
	{
		// search for a node containing the key k: 
		BSTNode<T> z = search(this.getRoot(), k);
		
		// invoke the pointer version deletion method with the node which was found:   
		return delete(z);
	}
	
	/**
	 * Search:
	 * @param x - starting node for the search, i.e. the root node of the tree. 
	 * @param k - the data (key) to search for.
	 * @return reference to node if k was found, or NIL otherwise. 
	 */
	@Override
	public BSTNode<T> search(BSTNode<T> x, T k) 
	{
		while (x != null)
		{	// check the order relationship between x data & k:
			int comparisonResult = x.getData().compareTo(k);
			
			// if keys are equal (we found k) - just return result:
			if (comparisonResult == 0)  
				return x;
			
			// if k > x.key, continue searching on right sub-tree: 
			else if ((comparisonResult < 0) && !(x.isPointerWired(x.getRight())))
				x = x.getRight();
			
			// if k < x.key, continue searching on left sub-tree: 
			else if ((comparisonResult > 0) && !(x.isPointerWired(x.getLeft())))
				x = x.getLeft();
			else return null;
		}
		// if k isn't found (or if the tree is empty) return NIL:
		return null; 
	}
	
	/** 
	 * getSuccessor
	 * @param node of which wer'e interested in getting it's successor. 
	 * @return reference to node which is the in-order successor of the input node.  
	 */
	@Override
	public BSTNode<T> getSuccessor(BSTNode<T> node) 
	{		
		// if given node is NIL or maximum node in tree, just return NIL: 
		if (node == null || node.getRight() == null)
			return null;
		
		// if right child is wired, then it's the successor - just return it in O(1): 
		if (node.isPointerWired(node.getRight()))
			return node.getRight();
		
		// if right child is "real", return minimum of right sub-tree:
		node = node.getRight();
		while (!(node.isPointerWired(node.getLeft())))
			node = node.getLeft();
		return node;
	}
	
	/** 
	 * getPredecessor
	 * @param node of which wer'e interested in getting it's predecessor. 
	 * @return reference to node which is the in-order predecessor of the input node.  
	 */
	@Override
	public BSTNode<T> getPredecessor(BSTNode<T> node) 
	{
		// if given node is NIL or minimum node in tree, just return NIL: 
		if (node == null || node.getLeft() == null)
			return null;
		
		// if left child is wired, then it's the predecessor - just return it: 
		if (node.isPointerWired(node.getLeft()))
			return node.getLeft();
		
		// if left child is "real", return maximum of left sub-tree:
		node = node.getLeft();
		while (!(node.isPointerWired(node.getRight())))
			node = node.getRight();
		return node;
	}

	/** 
	 * getMinimum:
	 * @param node of sub-tree in which we're interested in finding it's minimum. 
	 * @return reference to node with local minimum key in sub-tree, or NIL if sub-tree is empty.   
	 */
	@Override
	public BSTNode<T> getMinimum(BSTNode<T> node)
	{
		// if sub-tree is empty, just return NIL 
		if (node == null)
			return null;
		
		// follow left path until NIL or a left wire is reached:  
		while(!node.isPointerWired(node.getLeft()))
				node = node.getLeft();
		
		// return node (if left is NIL or a wire, it's the local minimum of the given sub-tree):
		return node;
	}

	/** 
	 * getMaximum:
	 * @param node of sub-tree in which we're interested in finding it's maximum. 
	 * @return reference to node with local maximum key in sub-tree, or NIL if sub-tree is empty.   
	 */
	@Override
	public BSTNode<T> getMaximum(BSTNode<T> node)
	{
		// if sub-tree is empty, just return NIL 
		if (node == null)
			return null;
		
		// follow right path until NIL or a right wire is reached:  
		while(!node.isPointerWired(node.getRight()))
				node = node.getRight();
		
		// return node (if right is NIL or a wire, it's the local maximum of the given sub-tree):
		return node;
	}

	/**
	 * In-order tree walk traversal: traverse the tree in-order. 
	 * Each node is visited 2 times at most with constant time for each node, therefore O(n). 
	 * @param x root of sub-tree to be traversed.
	 */
	@Override
	public String inorderTreeWalk(BSTNode<T> x) 
	{
		// save title of operation:
		StringBuilder result = new StringBuilder();
		result.append(">Inorder tree walk: ");

		// if sub-tree is empty, print appropriate message & return:
		if (x == null)
		{
			result.append("The sub-tree which is rooted in given node is empty.");
			System.out.println(result);
			return result.toString();
		}
		
		// get minimum of x's sub-tree (first element of in-order tree walk):
		x = this.getMinimum(x);
		
		// scan all nodes from minimum to maximum (most right node);
		while (x != null)
		{
			// visit current node (print\save it's contents): 
			result.append(x.getData().toString() + " --> ");
			
			// get successor via the O(1) improvement in case right pointer is a thread: 
			x = this.getSuccessor(x);
		}
		
		// add terminating string & return results:
		return result.append("||").toString();
	}

	/**
	 * Preorder tree walk traversal: visit node first, children later.
	 * Each node is visited 2 times at most with constant time for each node, therefore O(n). 
	 * @param x root of sub-tree to be traversed.
	 */
	@Override
	public String preorderTreeWalk(BSTNode<T> x)
	{
		// save title of operation:
		StringBuilder resultBuffer = new StringBuilder();	
		if (x == root)
			resultBuffer.append(">Preorder tree walk: ");
				
		// if sub-tree is empty, print appropriate message & return:
		if (x == null)
			return resultBuffer.append(("The tree is empty.")).toString();
		
		// visit current node (i.e. print it's contents):
		resultBuffer.append(x.getData() + " --> ");
		
		// visit left node only if it's not wired: 
		if (!x.isPointerWired(x.getLeft()))
			resultBuffer.append(preorderTreeWalk(x.getLeft()));
			
		// visit right node only if it's not wired: 
		if (!x.isPointerWired(x.getRight()))
			resultBuffer.append(preorderTreeWalk(x.getRight()));
		
		// add terminating sign for last visited node: 
		if (x == getMaximum(root))
			resultBuffer.append("||");
		
		// return result:
		return resultBuffer.toString();
	}
	
	/**
	 * Post order tree walk traversal: visit children first, node later.
	 * Each node is visited 2 times at most with constant time for each node, therefore O(n). 
	 * @param x root of sub-tree to be traversed.
	 */
	@Override
	public String postorderTreeWalk(BSTNode<T> x) 
	{
		StringBuilder resultBuffer = new StringBuilder();	
		
		// save title of operation:
		if (x == root)
			resultBuffer.append(">Post order tree walk: ");
		
		
		// if sub-tree is empty, print appropriate message & return:
		if (x == null)
			return resultBuffer.append("The sub-tree which is rooted in given node is empty.").toString();
		
		// visit left node only if it's not wired: 
		if (!x.isPointerWired(x.getLeft()))
			resultBuffer.append(postorderTreeWalk(x.getLeft()));
			
		// visit right node only if it's not wired: 
		if (!x.isPointerWired(x.getRight()))
			resultBuffer.append(postorderTreeWalk(x.getRight()));
		
		// visit current node (i.e. print it's contents):
		resultBuffer.append(x.getData() + " --> ");
		
		// if this last node, print terminating string: 
		if (x == root)
			resultBuffer.append("||");
		
		// return result
		return resultBuffer.toString();
	}
	
	
	/**
	 * Get Median:
	 * @return median node in the tree which has the (lower)median key(data)
	 * in the collection of all elements in tree, or NIL if tree is empty.
	 */
	public BSTNode<T> getMedian()
	{
		return median;
	}
	
	/*
	 * Maintain median during insertion and deletion of elements,
	 * by keeping track on how much elements or lager than current median,
	 * how much lower, and making sure it is balanced towards the lower median. 
	 */
	private void updateMedian()
	{
		// case 1: counters are equally balanced, no need for update -  
		if (elementsSmallerThanMedian == elementsLargerThanMedian)
			return;
		
		// case 2: unbalanced from above by more than 1 - make median's successor new median:
		if  (elementsLargerThanMedian - elementsSmallerThanMedian > 1)
		{// set the median's successor to be the new median:
			median = getSuccessor(median); //this is because our median is lower median ("chetsion tachton")
			//update counters according to new median:
			elementsSmallerThanMedian++;
			elementsLargerThanMedian--;
		}
		
		// case 3: unbalanced from bellow - - make median's predecessor new median:
		else if  (elementsSmallerThanMedian > elementsLargerThanMedian)
		{// set the median's predecessor to be the new median:
			median = getPredecessor(median); //this is because our median is lower median ("chetsion tachton")
			//update counters according to new median:
			elementsSmallerThanMedian--;
			elementsLargerThanMedian++;
		}
	}
		
	/**
	 * Get Maximum Height
	 * Utility method which recursively calculates max height of the sub-rooted tree.
	 * @param node sub-tree to. For height of entire tree, pass the root node. 
	 * @return max height of the sub-tree rooted in the node given in input.
	 */
	public int getMaxHeight(BSTNode<T> node)
	{
		int rightHeight, leftHeight, maxHeight;
		
		// if this is a true leaf (child of max or minimum node) then return height zero:
		if (node == null)
			return 0;
		
		// if both children are wires, this is a leaf, return height zero:
		if (node.isPointerWired(node.getLeft()) && node.isPointerWired(node.getRight()))
			return 0;
		
		// if left node is not wired, calculate it's max height:
		if (!node.isPointerWired(node.getLeft()))
			leftHeight = getMaxHeight(node.getLeft());
		else leftHeight = 0;
		
		// if right node is not wired, calculate it's max height:
		if (!node.isPointerWired(node.getRight()))
			rightHeight = getMaxHeight(node.getRight());
		else rightHeight = 0;
		
		// determine which sub-tree has max height:
		maxHeight = (rightHeight >= leftHeight) ? rightHeight : leftHeight;
		
		// return calculated max height of sub-tree + 1 for current node: 
		return maxHeight + 1;
	}
	
	/**
	 * Get Maximum Height
	 * Utility method which recursively calculates max height of the sub-rooted tree.
	 * @param node sub-tree to. For height of entire tree, pass the root node. 
	 * @return max height of the sub-tree rooted in the node given in input.
	 */
	public int getMaxWidth(BSTNode<T> node)
	{
		int maxWidth = 0;
		int currentlevelNodeCounter;
		BSTNode<T> currentNode;
		BSTNode<T> sentinelNode = new BSTNode<T>(null);
		Queue<BSTNode<T>> queue = new LinkedList<>();
		queue.add(this.getRoot());
		while (!queue.isEmpty())
		{
			currentlevelNodeCounter = 0;
			queue.add(sentinelNode);
			currentNode = queue.remove();
			
			while (currentNode != sentinelNode)
			{
				currentlevelNodeCounter++;
				if (!currentNode.isPointerWired(currentNode.getLeft()))
					queue.add(currentNode.getLeft());
				if (!currentNode.isPointerWired(currentNode.getRight()))
					queue.add(currentNode.getRight());
				currentNode = queue.remove();
			}
			if (currentlevelNodeCounter > maxWidth)
				maxWidth = currentlevelNodeCounter;
		}
		return maxWidth;
	}
	


	/** 
	 *  Return string representation of the tree, by scanning it in BFS fashion,
	 *  top to bottom and printing each level nodes from left to right.
	 */
	@Override
	public String toString()
	{
		if (root == null)
			return "Tree is Empty";
		
		StringBuilder treeOutputBuffer = new StringBuilder();
		String title = "\n============================\n";
		treeOutputBuffer.append(title + "Tree state Printout with BFS" + title);
		int currentlevel = -1;
		BSTNode<T> currentNode;
		BSTNode<T> sentinelNode = new BSTNode<T>(null);
		Queue<BSTNode<T>> queue = new LinkedList<>();
		queue.add(root);
		
		// scan each level in the tree top to bottom: 
		while (!queue.isEmpty())
		{
			currentlevel++;
			queue.add(sentinelNode); // sentinel to differ between the different levels 
			currentNode = queue.remove();
			treeOutputBuffer.append(String.format("Level [%d]:    ", currentlevel));
			
			// scan each node in current level, left to right: 
			while (currentNode != sentinelNode)
			{
				// print current nodes details: 
				treeOutputBuffer.append(currentNode.toString() + " ---> ");
				
				// if pointers are not threads, add children scan queue:
				if (!currentNode.isPointerWired(currentNode.getLeft()))
					queue.add(currentNode.getLeft());
				if (!currentNode.isPointerWired(currentNode.getRight()))
					queue.add(currentNode.getRight());
				
				// fetch next node: 
				currentNode = queue.remove();
			}
			treeOutputBuffer.append("\n");
		}
		return treeOutputBuffer.toString();
	}
		
	/**
	 * Utility method for test purposes iterating all nodes in tree by any order. 
	 * @param root node of the tree 
	 * @return the tree nodes as a Collection in an ArrayList. 
	 */
	public ArrayList<BSTNode<T>> getNodeList(BSTNode<T> x) 
	{
		// if sub-tree is empty, print appropriate message & return:
		if (x == null)
			return null;

		ArrayList<BSTNode<T>> nodeList = new ArrayList<BSTNode<T>>();

		// get minimum of x's sub-tree (first element of in-order tree walk):
		x = this.getMinimum(x);
		
		// scan all nodes from minimum to maximum (most right node);
		while (x != null)
		{
			// visit current node (print\save it's contents): 
			nodeList.add(x);
			
			// get successor via the O(1) improvement in case right pointer is a thread: 
			x = this.getSuccessor(x);
		}
		
		//return result;
		return nodeList;
	}
	
	/**
	 * Utility method for test purposes that convert wired BST to regular one. 
	 * @return the root node of the standard binary search tree.  
	 */
	public BSTNode<T> getRegularBinarySearchTree()
	{
		BSTNode<T> wiredNode, regularNode;
		T nodeKey;
		
		// Map wired tree nodes in key:value list: 
		HashMap<T, BSTNode<T>> wiredMappingTable = new HashMap<T, BSTNode<T>>();
		ArrayList<BSTNode<T>> wiredList = this.getNodeList(root);
		for (BSTNode<T> node: wiredList)
			wiredMappingTable.put(node.getData(), node);
		
		// Create copy of the nodes (only data, no references): 
		ArrayList<BSTNode<T>> regularList = new ArrayList<BSTNode<T>>(wiredList.size());
		for (BSTNode<T> node: wiredList)
			regularList.add(new BSTNode<T>(node.getData()));
		
		// Map regular nodes into key-value table:
		HashMap<T, BSTNode<T>> regularMappingTable = new HashMap<T, BSTNode<T>>();
		for (BSTNode<T> node: regularList)
			regularMappingTable.put(node.getData(), node);
		
		// map pointer reference according to key:
		for (int i = 0; i < regularList.size(); i++)
		{	
			// current regular node: 
			regularNode = regularList.get(i);
			nodeKey = regularNode.getData();
			
			// get parallel node from wired tree: 
			wiredNode = wiredMappingTable.get(nodeKey);
			
			// set relevant reference in regular node according to parallel references:
			if (wiredNode.getParent() != null)
				regularNode.setParent(regularMappingTable.get(wiredNode.getParent().getData()));
			if (!wiredNode.isPointerWired(wiredNode.getLeft()))
				regularNode.setLeft((regularMappingTable.get(wiredNode.getLeft().getData())));
			if (!wiredNode.isPointerWired(wiredNode.getRight()))
				regularNode.setRight((regularMappingTable.get(wiredNode.getRight().getData())));
		}
		
		// return root node of regular tree:
		nodeKey = getRoot().getData();
		return regularMappingTable.get(nodeKey);
	}
} // end of class 
