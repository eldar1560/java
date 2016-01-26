package presenter;

import java.io.Serializable;

/**
 * the Properties class - contains the variables that the user is giving us from xml
 * @author Eldar ,Ofek
 *
 */

public class Properties implements Serializable{
	
	private static final long serialVersionUID = 42L;
	int x;
	int y;
	int z;
	int numberOfThreads;
	String name;
	String algorithmForSolution;
	String algorithmForGenerate;
	String uc;
	
	/**
	 * Constructor for properties - default values
	 */
	public Properties() {
		this.x = 6;
		this.y = 6;
		this.z = 6;
		this.numberOfThreads = 10;
		this.algorithmForSolution = "BFS";
		this.algorithmForGenerate = "MyMaze3dGenerator";
		this.name = "Maze1";
		this.uc = "GUI";
	}
	/**
	 * get the user choice
	 * @return
	 */
	public String getUc() {
		return uc;
	}
	/**
	 * set the user choice
	 * @param uc
	 */
	public void setUc(String uc) {
		this.uc = uc;
	}
	
	/**
	 * get the x size
	 * @return
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * set the x size
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * get the y size
	 * @return
	 */
	public int getY() {
		return y;
	}
	/**
	 * set the y size
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * get the z size
	 * @return
	 */
	public int getZ() {
		return z;
	}

	/**
	 * set the z size
	 * @param z
	 */
	public void setZ(int z) {
		this.z = z;
	}

	/**
	 * get the number of threads
	 * @return
	 */
	public int getNumberOfThreads() {
		return numberOfThreads;
	}
	
	/**
	 * set the number of threads
	 * @param numberOfThreads
	 */
	public void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}

	/**
	 * get the name of the maze
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * set the name of the maze
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * get the algorithm for solution
	 * @return
	 */
	public String getAlgorithemForSolution() {
		return algorithmForSolution;
	}

	/** 
	 * set the algorithm for solution
	 * @param algorithmForSolution
	 */
	public void setAlgorithemForSolution(String algorithmForSolution) {
		this.algorithmForSolution = algorithmForSolution;
	}

	/**
	 * get the algorithm for generate the maze
	 * @return
	 */
	public String getAlgorithemForGenerate() {
		return algorithmForGenerate;
	}

	/**
	 * set the algorithm for generate the maze
	 * @param algorithmForGenerate
	 */
	public void setAlgorithemForGenerate(String algorithmForGenerate) {
		this.algorithmForGenerate = algorithmForGenerate;
	}
	

}
