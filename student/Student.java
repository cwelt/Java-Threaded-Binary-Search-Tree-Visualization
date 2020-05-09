package student;

/**
 * 
 * @author Chanan Welt
 * 
 * Student - class representing student with an ID and name. 
 */
public class Student implements Comparable<Student>  
{
	// Instance variables
	private Integer id;
	private String name;
	
	/** Constructor */ 
	public Student(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return id.toString(); // only id is relevant for the tree 
	}

	/**CompareTo: Compare between 2 students by their Identification number property.
	 *  This is simply done by using the java standard Integer class comparison.
	 *  @param otherStudent student to be compared to.
	 *  @return 0, negative number or positive number, if this student id is accordingly 
	 *   equal, less than or larger than the other students id. 
	 */
	@Override
	public int compareTo(Student otherStudent)
	{
		return this.id.compareTo(otherStudent.id);
	}

	/** checks student equality: returns true if both have same id, false otherwise. */ 
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Student)
		{
			return this.id.equals(((Student) obj).id);
		}
		else return false;
	}
	
	//basic Getters:
	public Integer getId() {return id;}
	public String getName(){return name;}
	
} // end of class
