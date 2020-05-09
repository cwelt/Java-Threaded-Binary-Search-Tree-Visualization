package main;

import student.Student;
import treeGUI.GUIApplication;
import wiredBinarySearchTree.WiredBST;

/**
 * @author Chanan Welt.
 * This is the main program for initiating the application.
 */
public class Main 
{
	public static void main(String[] args)
	{
		// create an empty wired binary search tree: 
		WiredBST<Student> wiredBinarySearchTree = new WiredBST<Student>();
				
		// initiate gui application: 
		GUIApplication app = new GUIApplication(wiredBinarySearchTree);
		app.initGui();
	}
}