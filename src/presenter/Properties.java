package presenter;

import java.io.Serializable;

public class Properties implements Serializable{
	
	private static final long serialVersionUID = 42L;
	int x;
	int y;
	int z;
	int numberOfThreads;
	String name;
	String algorithemForSolution;
	String algorithemForGenerate;
	//String ui;

	public Properties() {
		this.x = 6;
		this.y = 6;
		this.z = 6;
		this.numberOfThreads = 10;
		this.algorithemForSolution = "BFS";
		this.algorithemForGenerate = "MyMaze3dGenerator";
		this.name = "Maze1";
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getNumberOfThreads() {
		return numberOfThreads;
	}

	public void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlgorithemForSolution() {
		return algorithemForSolution;
	}

	public void setAlgorithemForSolution(String algorithemForSolution) {
		this.algorithemForSolution = algorithemForSolution;
	}

	public String getAlgorithemForGenerate() {
		return algorithemForGenerate;
	}

	public void setAlgorithemForGenerate(String algorithemForGenerate) {
		this.algorithemForGenerate = algorithemForGenerate;
	}
	

}
