package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import io.MyCompressorOutputStream;

import algorithms.demo.SearchableMaze;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.MyMaze3dGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.mazeGenerators.PrimMaze3dGenerator;
import algorithms.mazeGenerators.SimpleMaze3dGenerator;
import algorithms.search.AStar;
import algorithms.search.BFS;
import algorithms.search.CostComparator;
import algorithms.search.MazeAirDistance;
import algorithms.search.MazeManhattenDistance;
import algorithms.search.Solution;
import algorithms.search.State;
import io.MyDecompressorInputStream;
import presenter.Properties;


/**
 * MyModel class extends CommonModel
 * manage the size of the model
 */


public class MyModel extends CommonModel {

	ExecutorService threadpool;
	HashMap<Maze3d,String> mazeFile;
	int x;
	int y;
	int z;
	int numberOfThreads;
	String algorithmForSolution;
	String algorithmForGenerate;
	String name;
	Properties properties;
	
	/**
	 * Constructor that get the properties and initialize the variables
	 * @param properties
	 */
	public MyModel(Properties properties) {
		super();
		this.properties = properties;
		setProperties(properties);
		threadpool = Executors.newFixedThreadPool(numberOfThreads);
		mazeFile = new HashMap<Maze3d,String>();
		load();
	}

	@Override
	public void generate3dMaze(String name,int y, int z, int x) {
		if(hm.containsKey(name) == true)
		{			
			setChanged();
			notifyObservers(hm.get(name));
			setChanged();
			notifyObservers("Maze '"+name+"' is already exist");
			return;
		}
		this.name = name;
		Callable<Maze3d> callable = new Callable<Maze3d>() {

			@Override
			public Maze3d call() throws Exception {
				Maze3d maze = new PrimMaze3dGenerator().generate(y, z, x);
				hm.put(name,maze);
				setChanged();
				notifyObservers(maze);
				return maze;
			}
		};
		
		threadpool.submit(callable);	
		setChanged();
		notifyObservers("Maze '" + name + "' is ready");
	}
	
	@Override
	public void generate3dMaze() {
		if(hm.containsKey(name) == true)
		{
			setChanged();
			notifyObservers(hm.get(name));
			setChanged();
			notifyObservers("Maze '"+name+"' is already exist");
			return;
		}
		Callable<Maze3d> callable = new Callable<Maze3d>(){

			@Override
			public Maze3d call() throws Exception {
				Maze3d maze;
				if(algorithmForGenerate.equals("MyMaze3dGenerator"))
					maze = new MyMaze3dGenerator().generate(y, z, x);
				else if(algorithmForGenerate.equals("PrimMaze3dGenerator"))
					maze = new PrimMaze3dGenerator().generate(y, z, x);
				else
					maze = new SimpleMaze3dGenerator().generate(y, z, x);
				hm.put(name,maze);
				setChanged();
				notifyObservers(maze);
				return maze;
			}
		};
		threadpool.submit(callable);	
		setChanged();
		notifyObservers("Maze '" + name + "' is ready");
	}
	
	@Override
	public void display(String name){
		Maze3d maze = null;
		if(name.length() == 0)
			maze = hm.get(this.name);
		else
			maze = hm.get(name);
		if(maze == null){
			setChanged();
			notifyObservers("Not exist maze by this name: '" + name+"'");
		}
		else{
			setChanged();
			notifyObservers(maze.toString()); 
		}
	}


	@Override
	public void displayCrossSectionBy(String by, int index, String name) {
		Maze3d maze = null;
		if(name.length() == 0)
			maze = hm.get(this.name);
		else
			maze = hm.get(name);
		
		String strMaze ="";
		int[][] maze2d = null;
		if(maze == null){
			setChanged();
			notifyObservers("Maze not exist");
			return;
		}
		
		
		
		try{
			switch(by){
			case "X":
				maze2d = maze.getCrossSectionByX(index);
				break;
			case "x":
				maze2d = maze.getCrossSectionByX(index);
				break;
			case "Y":
				maze2d = maze.getCrossSectionByY(index);
				break;
			case "y":
				maze2d = maze.getCrossSectionByY(index);
				break;
			case "Z":
				maze2d = maze.getCrossSectionByZ(index);
				break;
			case "z":
				maze2d = maze.getCrossSectionByZ(index);
				break;
			default:
				setChanged();
				notifyObservers("Invalid cross section");	
				return;
			}
		}
		catch(IndexOutOfBoundsException e){
			setChanged();
			notifyObservers("Invalid index");	
			return;
		}
		
		
		
		for(int i = 0; i < maze2d.length; i++){
			for(int j = 0; j < maze2d[i].length; j++)
				strMaze += String.valueOf(maze2d[i][j]) + " ";
			strMaze += '\n';
		}
		setChanged();
		notifyObservers(strMaze);	
	}

	@Override
	public void saveMaze(String name,String fileName) {
		Maze3d maze = hm.get(name);
		if(maze == null){
			setChanged();
			notifyObservers("Maze '" + name + "' not exist");
			return;
		}
		
		OutputStream out = null;
		try {
			out = new MyCompressorOutputStream(new FileOutputStream(fileName + ".maz"));
			out.write(maze.toByteArray());	
			mazeFile.put(maze, fileName + ".maz");
		} catch (FileNotFoundException e) {
			setChanged();
			notifyObservers(e.getMessage());
			return;
		} catch (IOException e) {
			setChanged();
			notifyObservers(e.getMessage());
			return;
		}
		finally{
			try {
				out.flush();
			} catch (IOException e) {
				setChanged();
				notifyObservers(e.getMessage());
			}
			try {
				out.close();
			} catch (IOException e) {
				setChanged();
				notifyObservers(e.getMessage());
			}
		}
		
		setChanged();
		notifyObservers("The maze '"+name+"' was saved successfully in the file '" + fileName + "'");
	}

	@Override
	public void loadMaze(String fileName,String name) {
		Maze3d loaded = null;
		boolean isOpen = false;
		
		try{
			@SuppressWarnings("unused")
			File file = new File(fileName + ".maz");
		}
		catch(NullPointerException e){
			setChanged();
			notifyObservers("File not exist");
			return;
		}
			
		InputStream in=null;
		try {
			in = new MyDecompressorInputStream(new FileInputStream(fileName + ".maz"));
			isOpen = true;
			byte b[] = new byte[4096];
			in.read(b);
			loaded = new Maze3d(b);
		}
		catch (FileNotFoundException e) {
			setChanged();
			notifyObservers(e.getMessage());
			return;
		}
		catch (IOException e) {
			setChanged();
			notifyObservers(e.getMessage());
			return;
		}
		catch(NullPointerException e)
		{
			setChanged();
			notifyObservers(e.getMessage());
			return;
		}
		finally
		{
			try {
				if(isOpen)
					in.close();
			} catch (IOException e) 
			{
				setChanged();
				notifyObservers("Maze '"+ name+"' was unsuccessfully");
			}
		}
			
		hm.put(name, loaded);
		mazeFile.put(loaded, fileName + ".maz");
		setChanged();
		notifyObservers("The maze '" + name + "' has loaded successfully");
	}
	
	@Override
	public void mazeSize(String name) {
		Maze3d maze = hm.get(name);
		if(maze == null){
			setChanged();
			notifyObservers("Maze " + name + " not exist");
			return;
		}
		
		int size = 4*(maze.getMaze().length * maze.getMaze()[0].length *maze.getMaze()[0][0].length + 3 + 3);
		setChanged();
		notifyObservers("The maze size of '" + name + "' in memory: " + size +" bytes");		
	}


	@Override
	public void fileSize(String name) {
		try{
			String filePath = mazeFile.get(hm.get(name));
			if(filePath == null){
				setChanged();
				notifyObservers("Maze '" + name + "' not exist in any file");
				return;
			}
			File maze = new File(filePath);
			setChanged();
			notifyObservers("The file size of maze '" + name + "' is: " + maze.length()+" bytes");
		}
		catch (NullPointerException e){
			setChanged();
			notifyObservers("Not exist '" + name + "' file");
		}
	}

	@Override
	public void exit(){
		save();
		threadpool.shutdown();
		try {
			while(!(threadpool.awaitTermination(10, TimeUnit.SECONDS)));
		} catch (InterruptedException e) {
			setChanged();
			notifyObservers(e.getMessage());
		}
	}
	/**
	 * save the hash maps to the zip
	 */
	public void save() {
		ObjectOutputStream out = null;
		try{
			out = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream("Maze3dMap.zip")));
			out.writeObject(hm);
			out.writeObject(mazeFile);
			out.flush();
		} catch (FileNotFoundException e) {
			setChanged();
			notifyObservers(e.getMessage());
		}
		catch(IOException e){
			setChanged();
			notifyObservers(e.getMessage());
		} finally{
			try{
				out.close();
			}catch(IOException e)
			{
				setChanged();
				notifyObservers(e.getMessage());
			}
		}
	}

	/**
	 * load the hash maps to the zip
	 */
	@SuppressWarnings("unchecked")
	public void load() {
		ObjectInputStream in = null;
		try{
			in = new ObjectInputStream(new GZIPInputStream(new FileInputStream("Maze3dMap.zip")));
			hm = (HashMap<String, Maze3d>) in.readObject();
			mazeFile = (HashMap<Maze3d,String>) in.readObject();
		} catch (FileNotFoundException e) {
			setChanged();
			notifyObservers(e.getMessage());
		}
		catch(IOException e){
			setChanged();
			notifyObservers(e.getMessage());
		}catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
			finally{
		
			try{
				if(in!= null)
					in.close();
			}catch(IOException e)
			{
				setChanged();
				notifyObservers(e.getMessage());
			}
		}

	}
	/**
	 * set the x from by the properties
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * set the y from by the properties
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * set the z from by the properties
	 * @param z
	 */
	public void setZ(int z) {
		this.z = z;
	}
	/**
	 * set the algorithm for solution by the properties
	 * @param algorithmForSolution
	 */
	public void setAlgorithmForSolution(String algorithmForSolution) {
		this.algorithmForSolution = algorithmForSolution;
	}
	/**
	 * set the algorithm for generate by the properties
	 * @param algorithmForGenerate
	 */
	public void setAlgorithmForGenerate(String algorithmForGenerate) {
		this.algorithmForGenerate = algorithmForGenerate;
	}
	/**
	 * get the number of the threads from the properties
	 * @return
	 */
	public int getNumberOfThreads() {
		return numberOfThreads;
	}

	/**
	 * set the number of the threads by the properties
	 * @param numberOfThreads
	 */
	public void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}

	/**
	 * get the name of the maze from the properties
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * set the name from the properties
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * set the variables from the properties
	 * @param properties
	 */
	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
		setChanged();
		setName(properties.getName());		
		setX(properties.getX());
		setY(properties.getY());
		setZ(properties.getZ());
		setAlgorithmForGenerate(properties.getAlgorithemForGenerate());
		setAlgorithmForSolution(properties.getAlgorithemForSolution());
		setNumberOfThreads(properties.getNumberOfThreads());
		threadpool = Executors.newFixedThreadPool(numberOfThreads);
		notifyObservers("The properties is uploaded successfully");
	}
	@Override
	public void solve(String name, String algorithm) {
		if(hashSolution.containsKey(hm.get(name)) == true)
		{
			setChanged();
			notifyObservers("Solution for '" + name + "' is ready");
			setChanged();
			notifyObservers(hashSolution.get(hm.get(name)));
			return;
		}	
		Callable<Solution<Position>> callable = new Callable<Solution<Position>>() {
			@Override
			public Solution<Position> call() throws Exception {
				if(algorithm.equalsIgnoreCase("bfs")){
					Maze3d maze = hm.get(name);
					if(maze != null){
						CostComparator<Position> c = new CostComparator<Position>();
						BFS<Position> bfs = new BFS<Position>(c);
						Solution<Position> bfsSolution = bfs.search(new SearchableMaze(maze));
						hashSolution.put(maze, bfsSolution);
						setChanged();
						notifyObservers("Solution for '" + name + "' is ready");
						setChanged();
						notifyObservers(bfsSolution);
					}
					else{
						setChanged();
						notifyObservers("Invalid name");
					}
				}
				else if(algorithm.equalsIgnoreCase("MazeManhattanDistance")){
					Maze3d maze = hm.get(name);
					if(maze != null){
						CostComparator<Position> c = new CostComparator<Position>();
						AStar<Position> astarManhattanDistance = new AStar<Position>(new MazeManhattenDistance(new State<Position>(maze.getGoalPosition())),c);
						Solution<Position> astarManhattan = astarManhattanDistance.search(new SearchableMaze(maze));
						hashSolution.put(maze, astarManhattan);
						setChanged();
						notifyObservers("Solution for '" + name + "' is ready");
						setChanged();
						notifyObservers(astarManhattan);
					}
					else{
						setChanged();
						notifyObservers("Invalid name");
					}
				}
				else if(algorithm.equalsIgnoreCase("MazeAirDistance")){
					Maze3d maze = hm.get(name);
					if(maze != null){
						CostComparator<Position> c = new CostComparator<Position>();
						AStar<Position> astarAirDistance = new AStar<Position>(new MazeAirDistance(new State<Position>(maze.getGoalPosition())),c);
						Solution<Position> astarAir = astarAirDistance.search(new SearchableMaze(maze));
						hashSolution.put(maze, astarAir);
						setChanged();
						notifyObservers("Solution for '" + name + "' is ready");
						setChanged();
						notifyObservers(astarAir);
					}
					else{
						setChanged();
						notifyObservers("Invalid name");
					}
				}
				else
				{
					setChanged();
					notifyObservers("Invalid algorithm");
				}
				return hashSolution.get(hm.get(name));
			}
		};
		threadpool.submit(callable);
	}
	@Override
	public void dir(String path)
	{
		try {
			File f = new File(path);	
			String[] string = f.list(); 
			String listPath = "";
			
			for(int i = 0; i <string.length; i++)
				listPath += string[i] + '\n';
			setChanged();
			notifyObservers(listPath);
		}
		catch (NullPointerException e){
			setChanged();
			notifyObservers("Invalid path");
		}
		
	}
	@Override
	public void solve() {
		if(hashSolution.containsKey(hm.get(name)) == true)
		{
			setChanged();
			notifyObservers(hashSolution.get(hm.get(name)));
			setChanged();
			notifyObservers("Solution for '" + name + "' is ready");
			return;
		}	
		Callable<Solution<Position>> callable = new Callable<Solution<Position>>() {
			@Override
			public Solution<Position> call() throws Exception {
				if(algorithmForSolution.equalsIgnoreCase("bfs")){
					Maze3d maze = hm.get(name);
					if(maze != null){
						CostComparator<Position> c = new CostComparator<Position>();
						BFS<Position> bfs = new BFS<Position>(c);
						Solution<Position> bfsSolution = bfs.search(new SearchableMaze(maze));
						hashSolution.put(maze, bfsSolution);
						setChanged();
						notifyObservers(bfsSolution);
					}
					else{
						setChanged();
						notifyObservers("Invalid name");
					}
				}
				else if(algorithmForSolution.equalsIgnoreCase("MazeManhattanDistance")){
					Maze3d maze = hm.get(name);
					if(maze != null){
						CostComparator<Position> c = new CostComparator<Position>();
						AStar<Position> astarManhattanDistance = new AStar<Position>(new MazeManhattenDistance(new State<Position>(maze.getGoalPosition())),c);
						Solution<Position> astarManhattan = astarManhattanDistance.search(new SearchableMaze(maze));
						hashSolution.put(maze, astarManhattan);
						setChanged();
						notifyObservers(astarManhattan);
					}
					else{
						setChanged();
						notifyObservers("Invalid name");
					}
				}
				else if(algorithmForSolution.equalsIgnoreCase("MazeAirDistance")){
					Maze3d maze = hm.get(name);
					if(maze != null){
						CostComparator<Position> c = new CostComparator<Position>();
						AStar<Position> astarAirDistance = new AStar<Position>(new MazeAirDistance(new State<Position>(maze.getGoalPosition())),c);
						Solution<Position> astarAir = astarAirDistance.search(new SearchableMaze(maze));
						hashSolution.put(maze, astarAir);
						setChanged();
						notifyObservers(astarAir);
					}
					else{
						setChanged();
						notifyObservers("Invalid name");
					}
				}
				else
				{
					setChanged();
					notifyObservers("Invalid algorithm");
				}
				return hashSolution.get(hm.get(name));
			}
		};
		threadpool.submit(callable);
		setChanged();
		notifyObservers("Solution for '" + name + "' is ready");
		
	}

}
