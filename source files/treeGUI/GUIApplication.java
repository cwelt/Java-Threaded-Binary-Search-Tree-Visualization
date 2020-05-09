package treeGUI;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.security.SecureRandom;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import binaryTree.BSTNode;
import ioHandler.IOHandler;
import student.Student;
import wiredBinarySearchTree.WiredBST;


/**
 * @author Chanan Welt.
 * 
 * This class is responsible for supplying a GUI interactive application 
 * which supports the wired binary search tree operations & queries.
 * It also supports a printout visualization of the existing tree. 
 * and support importing an input file with the following commands as noted below:
 * ------------|-------------------|-------------|
 * command     |arg1			   |arg2         |
 * ------------|-------------------|-------------|
 * insert		<numeric id> 		<string name>|
 * delete		<numeric id>					 |
 * search		<numeric id>					 |
 * minimum										 |
 * maximum										 |		
 * median										 |
 * predecessor	<numeric id>					 |
 * successor	<numeric id>					 |
 * inorder										 |
 * preorder										 |
 * postorder									 |
 * ----------------------------------------------|
 * Commands aren't case sensitive (i.e, you could type Delete or delete).
 * @See "inputFileExample.txt" for some examples.
 */
@SuppressWarnings("serial")
public class GUIApplication extends JFrame implements ActionListener
{
	// Data Instance variables:
	WiredBST<Student> tree; // the tree
	IOHandler ioHandler; // I/O handler for file import
	BSTNode<Student> node;
	String message;	
	
	/* GUI instance variables: */  
	
	// Panels
	private final JPanel northPanel;
	private final JPanel southPanel;
	private final JPanel eastPanel;
	private final JPanel westPanel;
	private final JPanel centerPanel;
	private final JPanel controllsPanel;
	private final JPanel inputFieldsPanel;
	private final JPanel dictionaryOperationsPanel;
	private final JPanel queriesPanel;
	private final JPanel treeWalksPanel;
	private final JPanel extraFeaturesPanel;
	private final TreePrinter<Student> canvasPanel;
	private final JScrollPane canvasScrollPane;
		
	// Buttons & Controls:
	private JButton insertButton;
	private JButton deleteButton;
	private JButton searchButton;
	private JButton successorButton;
	private JButton predecessorButton;
	private JButton minimumButton;
	private JButton maximumButton;
	private JButton medianButton;
	private JButton inOrderButton;
	private JButton preOrderButton;
	private JButton postOrderButton;
	private JButton printBFSButton;
	private JButton clearButton;
	private JButton importFileButton;
	private final JTextField studentIDField;
	private final JTextField studentNameField;
	private final JCheckBox displayWiresCheckBox;
	
	// Labels:
	private final JLabel applicationHeaderLabel; 
	private final JLabel studentIDLabel;
	private final JLabel studentNameLabel;
	private final JLabel statusBar;
	
	// Icons:
	private final Icon insertIcon;
	private final Icon deleteIcon;
	private final Icon searchIcon;
	private final Icon importIcon;
	private final Icon clearIcon;
	
	// Fonts: 
	private static final Font defaultFont = new Font("Tahoma", 0, 14);
	
	// Colors:
	private static Color green1 = new Color(152, 251, 152);
	private static Color green2 = new Color(0, 255, 152);
	private static Color green3 = new Color(0, 255, 127);
	private static Color green4 = new Color(154, 205, 20);
	
	/**
	 * Constructor - Initialize all GUI components:
	 * @param tree - the wired binary tree to manipulate and display: 
	 */
	public GUIApplication(WiredBST<Student> tree) 
	{
		super("Wired Binary Search Tree Application");
		this.tree = tree;
		this.ioHandler = new IOHandler();
		this.setSize(1000, 700);
		this.setFont(defaultFont);
		
		// Create panels
		northPanel = new JPanel(new BorderLayout());
		southPanel = new JPanel(new BorderLayout());
		eastPanel = new JPanel(new BorderLayout());
		westPanel = new JPanel(new BorderLayout());
		centerPanel = new JPanel(new BorderLayout());
		canvasPanel = new TreePrinter<Student>(tree);		
		controllsPanel = new JPanel(new GridLayout(5,0,5,5));
		inputFieldsPanel = new JPanel(new GridLayout(1,0,5,5));
		dictionaryOperationsPanel = new JPanel(new GridLayout(1,0,5,5));
		queriesPanel = new JPanel(new GridLayout(1,0,5,5));
		treeWalksPanel = new JPanel(new GridLayout(1,3,5,5));
		extraFeaturesPanel = new JPanel(new GridLayout(1,0,5,5));
		northPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		centerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		southPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		canvasPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		inputFieldsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		dictionaryOperationsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		queriesPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		treeWalksPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		extraFeaturesPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		// Set main panels locations:
		this.add(northPanel, BorderLayout.NORTH);
		this.add(southPanel, BorderLayout.SOUTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(eastPanel, BorderLayout.EAST);
		this.add(westPanel, BorderLayout.WEST);
		
		// Application Title 
		applicationHeaderLabel = new JLabel("Wired Binary Search Tree GUI application", SwingConstants.LEFT);
		applicationHeaderLabel.setBackground(new Color (200,255,175));
		applicationHeaderLabel.setOpaque(true);
		northPanel.add(applicationHeaderLabel, BorderLayout.PAGE_START);
						
		// Canvas panel display area section:
		canvasScrollPane = new JScrollPane(canvasPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		centerPanel.add(canvasScrollPane);

		// Input fields panel: 
		studentIDLabel = new JLabel("Student ID (key): ");
		studentIDLabel.setFont(defaultFont);
		studentIDField = new JTextField("100", 8);
		studentIDField.setBackground(new Color(250, 250, 210));
		studentIDField.setFont(defaultFont);
		studentIDField.addActionListener(this);
		studentIDField.setToolTipText("Enter Students uniqe identification number");
		studentNameLabel = new JLabel("Student Name (optional): ");
		studentNameLabel.setFont(defaultFont);
		studentNameField = new JTextField("chanan welt", 16);
		studentNameField.setFont(defaultFont);
		studentNameField.addActionListener(this);
		studentNameField.setToolTipText("Enter students name, or blank");
		studentNameField.setBackground(new Color(250, 250, 210));
		inputFieldsPanel.add(studentIDLabel);
		inputFieldsPanel.add(studentIDField);
		inputFieldsPanel.add(studentNameLabel);
		inputFieldsPanel.add(studentNameField);
		studentIDLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		studentNameLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		studentIDField.setBorder(BorderFactory.createLineBorder(Color.black));
		studentNameField.setBorder(BorderFactory.createLineBorder(Color.black));
		
		// Dictionary operations panel: 
		insertIcon = new ImageIcon(getClass().getResource("insert.png"));
		insertButton = new JButton("Insert", insertIcon);
		insertButton.setToolTipText("Insert new student to tree");
		insertButton.setBackground(green1);
		insertButton.addActionListener(this);
		deleteIcon = new ImageIcon(getClass().getResource("delete.png"));
		deleteButton = new JButton("Delete", deleteIcon);
		deleteButton.setToolTipText("Delete student from tree");
		deleteButton.setBackground(green1);
		deleteButton.addActionListener(this);
		searchIcon = new ImageIcon(getClass().getResource("search.png"));
		searchButton = new JButton("Search", searchIcon);
		searchButton.setBackground(green1);
		searchButton.setToolTipText("Search for student in tree by it's id");
		searchButton.addActionListener(this);
		dictionaryOperationsPanel.add(insertButton);
		dictionaryOperationsPanel.add(deleteButton);
		dictionaryOperationsPanel.add(searchButton);
		
		// Queries panel: 
		minimumButton = new JButton("Minimum");
		maximumButton = new JButton("Maximum");
		medianButton = new JButton("Median");
		predecessorButton = new JButton("Predecessor");
		successorButton = new JButton("Successor");	
		minimumButton.setBackground(green2);
		maximumButton.setBackground(green2);
		medianButton.setBackground(green2);
		predecessorButton.setBackground(green2);
		successorButton.setBackground(green2);
		minimumButton.addActionListener(this);
		maximumButton.addActionListener(this);
		medianButton.addActionListener(this);
		predecessorButton.addActionListener(this);
		successorButton.addActionListener(this);
		queriesPanel.add(minimumButton);
		queriesPanel.add(maximumButton);
		queriesPanel.add(medianButton);
		queriesPanel.add(predecessorButton);
		queriesPanel.add(successorButton);

		// Tree walk traversals panel: 
		inOrderButton = new JButton("Inorder tree walk");
		preOrderButton = new JButton("Preorder tree walk");
		postOrderButton = new JButton("Postorder tree walk");
		inOrderButton.setBackground(green3);
		preOrderButton.setBackground(green3);
		postOrderButton.setBackground(green3);
		inOrderButton.addActionListener(this);
		preOrderButton.addActionListener(this);
		postOrderButton.addActionListener(this);
		treeWalksPanel.add(inOrderButton);
		treeWalksPanel.add(preOrderButton);
		treeWalksPanel.add(postOrderButton);
		
		// Extra Features Panel:
		importIcon = new ImageIcon(getClass().getResource("import.png"));
		importFileButton = new JButton("Import input File", importIcon);
		printBFSButton = new JButton("BFS print");
		clearIcon = new ImageIcon(getClass().getResource("clear.png"));
		clearButton = new JButton("Clear Tree", clearIcon);
		importFileButton.addActionListener(this);
		printBFSButton.addActionListener(this);
		clearButton.addActionListener(this);
		displayWiresCheckBox = new JCheckBox("Display Tree Wires");
		displayWiresCheckBox.setToolTipText("if this is checked, non null wires would be displayed in red");
		displayWiresCheckBox.setBackground(Color.YELLOW);
		displayWiresCheckBox.addActionListener(this);
		importFileButton.setBackground(green4);
		printBFSButton.setBackground(green4);
		clearButton.setBackground(green4);
		extraFeaturesPanel.add(importFileButton);
		extraFeaturesPanel.add(printBFSButton);
		extraFeaturesPanel.add(clearButton);
		extraFeaturesPanel.add(displayWiresCheckBox);
		
		// Controls panel setup:
		controllsPanel.add(inputFieldsPanel);
		controllsPanel.add(dictionaryOperationsPanel);
		controllsPanel.add(queriesPanel);
		controllsPanel.add(treeWalksPanel);
		controllsPanel.add(extraFeaturesPanel);
		southPanel.add(controllsPanel, BorderLayout.NORTH);
		
		// Status bar section:
		statusBar = new JLabel("@Chanan Welt: 20407 Maman16");
		statusBar.setFont(new Font("Tahoma", Font.BOLD, 17));
		statusBar.setOpaque(true);
		statusBar.setBackground(Color.LIGHT_GRAY);
		southPanel.add(statusBar, BorderLayout.SOUTH);
		statusBar.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	/**
	 * GUI event handler: handles all push buttons and check box: 
	 */
	public void actionPerformed(ActionEvent event) 
	{		
		//clear status bar from previous status:
		clearStatusBar();
		
		// validate input:
		if(!inputIsValid(event))
			return;
		
		//get input fields values:
		int inputID;
		if (!studentIDField.getText().isEmpty())
			inputID = Integer.parseInt(studentIDField.getText());
		else inputID = 0;
		String inputName = studentNameField.getText();
		
		// check which object fired the event and treat it accordingly:
		Object triggeringObject = event.getSource();
		
		// INSERT:
		if(triggeringObject == insertButton)
		{
			try
			{
				node = tree.insert(new Student(inputID, inputName));
			} 
			catch (KeyAlreadyExistsException exception)
			{
				message = "Insert faild:" + exception.getMessage() + "key must be uniqe!";
				displayMessage(message, JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (node != null)
			{
				message = String.format("Student <%s> inserted successfully", (inputID + " " + inputName));
				displayMessage(message, JOptionPane.INFORMATION_MESSAGE);
			}
			repaint();
			return;
		}
		
		// DELETE
		else if (triggeringObject == deleteButton)
		{
			node = tree.delete(tree.search(tree.getRoot(), new Student(inputID, null)));
			if (node == null)
				displayMessage(("Deleteion failed: ID " + inputID + " does not exist on tree"), JOptionPane.ERROR_MESSAGE);
			else 
			{
				message = String.format("Student <%s> deleted successfully", (node.getData()));
				displayMessage(message, JOptionPane.INFORMATION_MESSAGE);
			}
			repaint();
			return;
		}
		
		// SEARCH
		else if (triggeringObject == searchButton)
		{
			Student studentKey = new Student(inputID, null);
			node = tree.search(tree.getRoot(), studentKey);
			if (node == null)
				displayMessage(("Search failed: ID " + inputID + " does not exist on this tree"), JOptionPane.INFORMATION_MESSAGE);
			else 
			{
				message = String.format("Student <%s> found", (node.toString()));
				displayMessage(message, JOptionPane.INFORMATION_MESSAGE);
			}
			return;
		}
		
		// MAXIMUM:
		else if (triggeringObject == maximumButton)
		{
			node = tree.getMaximum(tree.getRoot());
			if (node == null)
				displayMessage(("No maximum, tree is empty "), JOptionPane.ERROR_MESSAGE);
			else 
				displayMessage((String.format("Maximum found: <%s>", node)), JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		// MINIMUM
		else if (triggeringObject == minimumButton)
		{
			node = tree.getMinimum(tree.getRoot());
			if (node == null)
				displayMessage(("No minimum, tree is empty "), JOptionPane.ERROR_MESSAGE);
			else 
				displayMessage((String.format("Minimum found: <%s>", node)), JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		// MEDIAN
		else if (triggeringObject == medianButton)
		{
			node = tree.getMedian();
			if (node == null)
				displayMessage(("No median, tree is empty "), JOptionPane.ERROR_MESSAGE);
			else 
				displayMessage((String.format("Median found: <%s>", node)), JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		// SUCCESSOR
		else if (triggeringObject == successorButton)
		{
			Student studentKey = new Student(inputID, null);
			node = tree.search(tree.getRoot(), studentKey);
			if (node == null)
				displayMessage(("successor not found: key "+inputID+" does not exist"), JOptionPane.INFORMATION_MESSAGE);
			else 
			{
				node = tree.getSuccessor(node);
				if (node != null)
					displayMessage((String.format("Successor of <%d> is <%s>", inputID, node.getData())), JOptionPane.INFORMATION_MESSAGE);
				else displayMessage((String.format("<%d> is the max node. it has no successor.", inputID)), JOptionPane.INFORMATION_MESSAGE);
			}	
			return;
		}
		
		// PREDECESSOR
		else if (triggeringObject == predecessorButton)
		{
			Student studentKey = new Student(inputID, null);
			node = tree.search(tree.getRoot(), studentKey);
			if (node == null)
				displayMessage(("predecessor not found: key "+inputID+" does not exist"), JOptionPane.INFORMATION_MESSAGE);
			else 
			{
				node = tree.getPredecessor(node);
				if (node != null)
					displayMessage((String.format("Predecessor of <%d> is <%s>", inputID, node.getData())), JOptionPane.INFORMATION_MESSAGE);
				else 
					displayMessage((String.format("<%d> is the minimum node. it has no predecessor.", inputID)), JOptionPane.INFORMATION_MESSAGE);
			}	
			return;
		}
		
		// IN-ORDER TREE TRAVESERAL:
		else if (triggeringObject == inOrderButton)
		{
			message = tree.inorderTreeWalk(tree.getRoot());
			System.out.println(message);
			setStatusBar(message);
			return;
		}
		
		// PRE-ORDER TREE TRAVESERAL:
		else if (triggeringObject == preOrderButton)
		{
			message = tree.preorderTreeWalk(tree.getRoot());
			System.out.println(message);
			setStatusBar(message);
			return;
		}
		
		// POST-ORDER TREE TRAVESERAL:
		else if (triggeringObject == postOrderButton)
		{
			message = tree.postorderTreeWalk(tree.getRoot());
			System.out.println(message);
			setStatusBar(message);
			return;
		}
		
		// BREADTH-DEPTH-FIRST TREE TRAVESERAL:
		else if (triggeringObject == printBFSButton)
		{
			System.out.println(tree);
			setStatusBar("Breadth-Search-Scan Printout was sent to standart output");
			return;
		}
		
		// CLEAR CURRENT TREE:
		else if (triggeringObject == clearButton)
		{
			int dialogResult = JOptionPane.showConfirmDialog(this, "clear entire tree?");
			if (dialogResult == JOptionPane.OK_OPTION)
			{
				this.tree = new WiredBST<Student>();
				setStatusBar("Tree was cleared, The tree is now empty.");
				canvasPanel.setTree(tree);
				repaint();
				return;
			}
		}
		
		// TOGGLE DISPLAY MODE FOR WIRES:
		else if (triggeringObject == displayWiresCheckBox)
		{
			if(displayWiresCheckBox.isSelected())
				canvasPanel.setDisplayWires(true);
			else canvasPanel.setDisplayWires(false);
				repaint();
				return;
		}
		
		// IMPORT INPUT FILE:
		else if (triggeringObject == this.importFileButton)
		{
			// let the user select the desired input file:
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int result = fileChooser.showOpenDialog(this);

			// if user clicked Cancel button or exited dialog, return: 
			if (result != JFileChooser.APPROVE_OPTION)
				return;

			// pass file to ioHandler for further processing: 
			try 
			{
				File inputFile = fileChooser.getSelectedFile();
				ioHandler.processInputFile(tree, inputFile);
				repaint();
				return;
			}
			catch (Exception e)
			{
				displayMessage(("Error opening file" + e.getMessage()), JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}

	/** Utility initialization method for setting GUI first time:*/
	public void initGui() 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/* Display Message on GUI and\or standard error output */
	private void displayMessage(String message, int messageType)
	{
		if (messageType == JOptionPane.ERROR_MESSAGE)
		{
			JOptionPane.showMessageDialog(this, message, "Wired BST Application", JOptionPane.ERROR_MESSAGE);
			System.err.println(message);
		}
		else 
		{
			setStatusBar(message);
			System.out.println(message);
		}
	}

	/* Display messages on GUI status bar  */
	private void setStatusBar(String message)
	{
		SecureRandom randomNumbers = new SecureRandom();
		int randomRedColor = randomNumbers.nextInt(256);
		int randomBlueColor = randomNumbers.nextInt(256);
		statusBar.setOpaque(true);
		statusBar.setText(message);
		statusBar.setBackground(new Color(randomRedColor, 255, randomBlueColor));
	}
	
	/* Clear status bar from previous status */
	public void clearStatusBar()
	{
		statusBar.setText(null);
		statusBar.setBackground(Color.LIGHT_GRAY);
	}
	
	/* Input validation tests */
	private boolean inputIsValid(ActionEvent event)
	{
		// validate student id input is a natural number:
		String inputID = studentIDField.getText();
		
		// make sure all mandatory input fields have input:
		if (inputID.isEmpty())
		{
			Object triggeringObject = event.getSource();
			if ((triggeringObject == insertButton) || (triggeringObject == deleteButton)
				|| (triggeringObject == searchButton) || (triggeringObject == predecessorButton)
				|| ((triggeringObject == predecessorButton)))
			{
				displayMessage(String.format("Student ID is a mandatory field for this operation"), JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		else // make sure student id is a positive integer:
		{
			try
			{	
				int value = Integer.parseInt(inputID);
				if (value <= 0)
				{
					message = String.format("Invalid student id input: \"%s\", ID must be a positive number", inputID);
					displayMessage(message, JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
			catch (NumberFormatException e)
			{
				message = String.format("Invalid student id input: \"%s\", ID must be numeric", inputID);
				displayMessage(message, JOptionPane.ERROR_MESSAGE);
				return false;
			} 
		}
		return true;
	}
} // end of class 
