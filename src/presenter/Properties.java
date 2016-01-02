package presenter;

import java.io.Serializable;

public class Properties implements Serializable{
	
	private static final long serialVersionUID = 42L;
	int x;
	int y;
	int z;
	int numberOfThreads;
	String name;
	String algorithmForSolution;
	String algorithmForGenerate;
	//String ui;

	public Properties() {
		this.x = 6;
		this.y = 6;
		this.z = 6;
		this.numberOfThreads = 10;
		this.algorithmForSolution = "BFS";
		this.algorithmForGenerate = "MyMaze3dGenerator";
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
		return algorithmForSolution;
	}

	public void setAlgorithemForSolution(String algorithmForSolution) {
		this.algorithmForSolution = algorithmForSolution;
	}

	public String getAlgorithemForGenerate() {
		return algorithmForGenerate;
	}

	public void setAlgorithemForGenerate(String algorithmForGenerate) {
		this.algorithmForGenerate = algorithmForGenerate;
	}
	

}
