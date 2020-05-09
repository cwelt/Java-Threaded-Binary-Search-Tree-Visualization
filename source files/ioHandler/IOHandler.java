package ioHandler;

import java.io.File;
import java.io.IOException;

import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.management.openmbean.KeyAlreadyExistsException;

import binaryTree.BSTNode;
import student.Student;
import wiredBinarySearchTree.WiredBST;

/**
 * @author Chanan Welt
 * 
 * I/O Handler: this class is responsible for enabling input from file,
 * opening it, parsing it, dealing with i/o exceptions and invalid syntax, 
 * interacting with the data model (tree), and eventually closing the file; 
 */
public class IOHandler
{
	// Instance variables 
	private Scanner input;
	private WiredBST<Student> tree;
	
	/**
	 * Process input file method
	 * @param tree - the data model 
	 * @param inputFile - the input file to process 
	 */
	public void processInputFile(WiredBST<Student> tree, File inputFile)
	{
		this.tree = tree;
		openFile(inputFile);
		readRecords();
		closeFile();
	}

	/* utillity method for opening a file */
	private Scanner openFile(File inputFile)
	{
		try 
		{
			return input = new Scanner(inputFile);
		} 
		catch (IOException ioException)
		{
			System.err.println("Error opening file " + inputFile.getName() + ioException.getMessage());
			return null;
		} 
	}

	// read record from file
	public void readRecords()
	{
		Scanner lineParser;
		int lineCounter = 0;
		String command;
		int studentID, key;
		String studentName;
		BSTNode<Student> node;
		
		while (input.hasNext()) // while there is more to read
		{
			// get next line and print it on standard output:
			lineCounter++;				
			String currentLine = input.nextLine();                   
			System.out.printf("Line %d input: %s\nLine %d output:", lineCounter, currentLine, lineCounter);

			// set a parser for current line:
			lineParser = new Scanner(currentLine);
			
			// parse current line and branch to relevant operation:
			try 
			{
				command = lineParser.next().toUpperCase();
				switch (command)
				{
					case "INSERT":
					{
						studentID = lineParser.nextInt();
						if (lineParser.hasNext())
							studentName = lineParser.next();
						else studentName = null;					
						node = tree.insert(new Student(studentID, studentName));
						if (node != null)
							System.out.printf("Student <%s> inserted successfully\n", (node.getData()));
						else System.err.println("Error inserting " + studentID);
						break;
					}
					case "DELETE":
					{
						key = lineParser.nextInt();	
						node = tree.delete(tree.search(tree.getRoot(), new Student(key, null)));
						if (node != null)
							System.out.printf("Student <%s> deleted successfully\n", (node.getData()));
						else System.out.println("Deletion falied:: " + key + " does exist");
						break;
					}
					
					case "SEARCH":
					{
						key = lineParser.nextInt();	
						node = tree.search(tree.getRoot(), new Student(key, null));
						if (node != null)
							System.out.printf("Search succedded: <%s> \n", node);
						else System.out.println("Search failed: " + key + " does not exist in tree");
						break;
					}
					
					case "MAXIMUM": case "MINIMUM": case "MEDIAN":
					{
						if (command == "MAXIMUM")
							node = tree.getMaximum(tree.getRoot());
						else if (command == "MINIMUM")
							node = tree.getMinimum(tree.getRoot());
						else // "MEDIAN"
							node = tree.getMedian();
						if (node != null)
							System.out.printf("%s is: <%s>\n", command, node);
						else System.out.printf("Tree is empty, %s is NIL\n", command);
						break;
					}
					
					case "SUCCESSOR": case "PREDECESSOR":
					{
						key = lineParser.nextInt();	
						if (command.equals("SUCCESSOR"))
							node = tree.getSuccessor(tree.getRoot());
						else node = tree.getPredecessor(tree.getRoot());
						if (node != null)
							System.out.printf("%s is: <%s>\n", command, node);
						else System.out.printf("%s of %s is NIL\n", command, key);
						break;
					}
					
					case "PREORDER": case "POSTORDER": case "INORDER":
					{
						if (command.equals("PREORDER"))
							tree.preorderTreeWalk(tree.getRoot());
						else if(command.equals("POSTORDER"))
							tree.postorderTreeWalk(tree.getRoot());
						else tree.inorderTreeWalk(tree.getRoot());
						break;
					}
					default: 
					{
						System.out.printf("Invalid syntax format for this line\n");
					}
				}
			}
			
			catch (NumberFormatException | KeyAlreadyExistsException | NoSuchElementException exception)
			{
				System.out.printf("This line is invalid: %s. operation canceled\n", exception.getMessage());
			} 
			
			catch (Exception exception)
			{
				System.out.printf("This line is invalid: %s. operation canceled\n", exception.getMessage());
			} 
		}
	} // end method readRecords

	/* utility method for closing the input file */
	private void closeFile()
	{
		try
		{
			if (input != null)
				input.close();
		} 
		catch (Exception ioException)
		{
			System.err.println("Error closing file.");
		} 
	} 
} // end of class 
