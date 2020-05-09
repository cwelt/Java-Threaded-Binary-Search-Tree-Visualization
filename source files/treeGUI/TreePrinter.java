package treeGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.swing.JPanel;

import binaryTree.BSTNode;
import wiredBinarySearchTree.WiredBST;

/**
 * @author Chanan Welt
 * 
 * This class is responsible for visualizing the tree graphically.
 * This is implemented via swing framework, 
 * by calculating each node's x & y coordinates according to their intern-relationship
 * with one another (i.e, parent, left, right, etc), and then just drawing ovals and lines
 * based on these coordinates in-order to represent the nodes and edges.
 * this took allot of time and effort, allot of failed attempts at first, 
 * hundreds of code lines deleted and re-implemented, and eventually,
 * a pretty good result, though a few more optimization would be made if the time was unlimitied. 
 *        ,--,
 * enjoy (^__^) 
 */

@SuppressWarnings("serial")
public class TreePrinter <T extends Comparable<T>> extends JPanel 
{
	//Instance variables
	private WiredBST<T> tree; 
	
	// Font & GUI variables:
	private Font defaultFont, styledFont, headerFont;
	private FontMetrics fontMetrics;
	private int fontHeight;
	private static Color darkGreen = new Color(0, 180, 0);
	
	// coordinates positions variables: 
	private int treeHeight;
	private int nodeDimension;
	private int globalHorizontalOffset;
	private HashMap<BSTNode<T>, Point> coordinatesTable;
	private final int NUM_OF_LINES_IN_EACH_NODE = 4; // key, parent, left & right 
	
	// flag to control display of wire pointers:
	private boolean displayWires;
	
	/**
	 * Constructor 
	 * @param tree - the wired binary search tree to display. 
	 */
	public TreePrinter(WiredBST<T> tree)
	{
		// Set tree: 
		this.tree = tree;
		
		// Initialize font settings: 
		defaultFont = new Font("coolvetica", 0, 14);
		styledFont = new Font("coolvetica", Font.BOLD, 13);
		headerFont = new Font("coolvetica", Font.BOLD, 26);
		fontMetrics = getFontMetrics(getFont());
		fontHeight = fontMetrics.getHeight();
		setFont(defaultFont);

		// Set dimensions of the nodes according to font capacity:
		nodeDimension = (fontHeight * this.NUM_OF_LINES_IN_EACH_NODE)+10;
		
		// Set large enough size to enable scrolling:  
		setPreferredSize(this.getPreferedSize());
		
		// Set initial display mode flag for wires: 
		displayWires = false;
	}
	
	/**
	 * setTree: used for clearing screen and starting a new one from fresh data. 
	 * @param tree - the new tree to display. 
	 */
	public void setTree(WiredBST<T> tree)
	{
		this.tree = tree;
	}


	/**
	 *  paintComponent: 
	 *  Override generic paint method to call concrete drawing one.
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		// call super first for initialization:
		super.paintComponent(g);
		
		// clear old printout: 
		g.clearRect(0, 0, getWidth(), getHeight());
		
		// invoke customized painting method to do the real painting job:
		printTree(g);
	}
	
	/**
	 * printTree: 
	 * Responsible for the painting of tree.
	 * @param g Graphics object used to draw nodes and arcs. 
	 */
	public void printTree(Graphics g)
	{
		// print tree only if it's not empty of course: 
		if (tree.getRoot() != null)
		{
			// Set a table that has an entry for each node with it's coordinates: 
			calculateCoordinates(); 
			
			// Iterate the table and print each node's shape, data & arc according to it's position:
			for (Map.Entry<BSTNode<T>, Point> entry : coordinatesTable.entrySet())
			{
				printNode(entry.getKey() ,entry.getValue(), g); // print node frame & data 
				printNodeArcs(entry.getKey() ,entry.getValue(), g); // print arc from node to children
			}	
		}

		// if tree is empty just print appropriate message and return:
		else 
		{
			g.setFont(headerFont);
			g.setColor(darkGreen);
			g.drawString("Tree is currently empty. Insert nodes for displaying the tree.", 125, 175);
			g.setFont(defaultFont);
			g.setColor(Color.black);
			return;
		}
	}
	
	/**
	 * Save local logical position of each node according to a base position of root.
	 * Then shift node left by offset so that tree would be displayed starting at x = 0. 
	 */
	private void calculateCoordinates()
	{
		// Initialize coordinates utility data variables:  
		coordinatesTable = new HashMap<BSTNode<T>, Point>();
		treeHeight = tree.getMaxHeight(tree.getRoot());
		int levelOffset = (int)(Math.pow(2, treeHeight));
		int horizontalRootOffset = (nodeDimension) * (levelOffset-1);
		int verticalRootOffset = 0;
		Point parentPoint, rootPoint;
		rootPoint = new Point(horizontalRootOffset, verticalRootOffset);
		int leftMostPoint = rootPoint.x;
		int x = rootPoint.x, y = rootPoint.y; // 1-d coordinates
		int sign; // positive/negative
		
		// Initialize variables for BFS scan of tree: 
		int currentLevel = -1;
		levelOffset = 2 * levelOffset;  
		BSTNode<T> currentNode, parent;
		BSTNode<T> sentinelNode = new BSTNode<T>(null);
		Queue<BSTNode<T>> queue = new LinkedList<>();
		queue.add(tree.getRoot());
		
		// Scan each level in the tree from top to bottom: 
		while (!queue.isEmpty())
		{
			currentLevel++;
			queue.add(sentinelNode); // sentinel to differ between the different levels 
			currentNode = queue.remove();
			
			levelOffset = levelOffset / 2;
			
			// scan each node in current level, left to right: 
			while (currentNode != sentinelNode)
			{
				// if current node's pointers are not threads, add children to the scan queue:
				if (!currentNode.isPointerWired(currentNode.getLeft()))
					queue.add(currentNode.getLeft());
				if (!currentNode.isPointerWired(currentNode.getRight()))
					queue.add(currentNode.getRight());
				
				// now do coordinates actual calculation work: 
				if (currentLevel == 0)
					coordinatesTable.put(currentNode, rootPoint);
				else 
				{
					parent = currentNode.getParent();
					parentPoint = coordinatesTable.get(parent);
					sign = ((currentNode == parent.getRight()) ? 1 : -1); 
					x = parentPoint.x + (sign * levelOffset * nodeDimension);
					y = parentPoint.y + (int)(1.250 * nodeDimension);
					coordinatesTable.put(currentNode, new Point(x,y));
				}
				if (x < leftMostPoint)
					leftMostPoint = x;
				
				// fetch next node: 
				currentNode = queue.remove();
			} 
		} 
		// save left most point in-order to shift left all points later by it's distance from x = 0:
		this.globalHorizontalOffset = leftMostPoint;
	}
	
	
	/**
	 * Paint an individual node frame and data. 
	 * @param node - the current node to be printed.
	 * @param coordinates - the base coordinates positions for this node.
	 * @param g - the Graphic object which could draw the node. 
	 */
	private void printNode(BSTNode<T> node, Point coordinates, Graphics g)
	{		
		g.setColor(Color.BLACK);
		
		// prepare data for printout: 
		int x = coordinates.x - globalHorizontalOffset;
		int y = coordinates.y;
		String key = node.getData().toString();
		String parent = ((node.getParent() != null) ? node.getParent().getData().toString() : "NIL");
		String left = ((node.getLeft() != null) ? node.getLeft().getData().toString() : "NIL");
		String right = ((node.getRight() != null) ? node.getRight().getData().toString() : "NIL");
		
		//print node's skeleton (frame):
		g.drawRoundRect(x, y, nodeDimension, nodeDimension, 45, 45);
		
		// set offset for node's data inside the frame: 
		int horizontalOffset = fontHeight/2+1;
		int verticalOffset = fontHeight;
		x = x + horizontalOffset;
		y = y + verticalOffset;
		
		// print (plug-in) node's data inside the skeleton frame:
		g.setFont(styledFont);
		g.drawString("Key: " + key, x, y);
		g.setFont(defaultFont);
		g.drawString("Parent: " + parent, x, y += verticalOffset);
		g.drawString("Left: " + left, x, y += verticalOffset);
		g.drawString("Right: " + right, x, y += verticalOffset);	
	}
	

	/**
	 * Paint an individual node's arcs to his children. 
	 * If the boolean flag was set on, also non-null wires would be painted,
	 * i.e, all wires other than minimum's left & maximum's right. 
	 * The wires are painted in red to distinguish them from the real pointers, 
	 * which are painted in green. 
	 * @param node - the current node to be printed.
	 * @param coordinates - the base coordinates positions for this node.
	 * @param g - the Graphic object which could draw the node. 
	 */
	public void printNodeArcs(BSTNode<T> node, Point coordinates, Graphics g)
	{
		g.setColor(darkGreen);
		
		// get absolute position of current point:
		int x = coordinates.x - globalHorizontalOffset;
		int y = coordinates.y;
		
		// calculate the coordinates of it's ports to left and right pointers:
		int nodeLeftPortXCoordinate = x;
		int nodeRightPortXCoordinate = x + nodeDimension;
		int nodeYPort = y + nodeDimension -(int)(nodeDimension * 0.125); // approximately in 45 degrees
				
		// print arc to left child (or wire): 
		if (!node.isPointerWired(node.getLeft())) // if it's a real left child: 
		{
			Point left = coordinatesTable.get(node.getLeft());
			int leftMiddleUpperPointX = left.x + (nodeDimension/2) - globalHorizontalOffset;
			g.drawLine(nodeLeftPortXCoordinate, nodeYPort, leftMiddleUpperPointX, left.y);
		}
		else if (displayWires && node.getLeft() != null) // if it's a left wire: 
		{
			Point predecessor = coordinatesTable.get(node.getLeft());
			int predecessorXPort = predecessor.x + (nodeDimension/2) - globalHorizontalOffset;
			int predecessorYPort = predecessor.y + nodeDimension;
			Point connectionPoint = new Point(predecessorXPort,  y + (int)(nodeDimension * 0.125));
			
			int xPositions[] = {nodeLeftPortXCoordinate, connectionPoint.x, predecessorXPort};
			int yPositions[] = {nodeYPort, connectionPoint.y, predecessorYPort};
			g.setColor(Color.red);
			g.drawPolyline(xPositions, yPositions, 3);
			g.setColor(darkGreen);
		}
		
		// Similarly, deal with right pointer: 
		if (!node.isPointerWired(node.getRight()))
		{
			Point right = this.coordinatesTable.get(node.getRight());
			int rightMiddleUpperPointX = right.x + (this.nodeDimension/2) - globalHorizontalOffset;
			g.drawLine(nodeRightPortXCoordinate, nodeYPort, rightMiddleUpperPointX, right.y);
		}
		else if (displayWires && node.getRight() != null)
		{
			Point successor = coordinatesTable.get(node.getRight());
			int successorXPort = successor.x + (nodeDimension/2) - globalHorizontalOffset;
			int successorYPort = successor.y + nodeDimension;
			Point connectionPoint = new Point(successorXPort,  y + (int)(nodeDimension * 0.125));
			
			int xPositions[] = {nodeRightPortXCoordinate, connectionPoint.x, successorXPort};
			int yPositions[] = {nodeYPort, connectionPoint.y, successorYPort};
			g.setColor(Color.red);
			g.drawPolyline(xPositions, yPositions, 3);
			g.setColor(darkGreen);
		}
	}
	
	/* set large enough size to enable scrolling vertically and horizon'. */
	private Dimension getPreferedSize()
	{
		return new Dimension(8192,8192);
	}
	
	/**
	 * Toggle display mode of non null wires
	 * @param flag
	 */
	public void setDisplayWires(boolean flag)
	{
		displayWires = flag;
	}
}// end of class
