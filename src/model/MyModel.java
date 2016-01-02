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
	String algorithemForSolution;
	String algorithemForGenerate;
	String name;
	Properties properties;
	

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
			notifyObservers("Maze '"+name+"' is already exist");
			return;
		}
		Callable<Maze3d> callable = new Callable<Maze3d>() {

			@Override
			public Maze3d call() throws Exception {
				Maze3d maze = new MyMaze3dGenerator().generate(y, z, x);
				hm.put(name,maze);
				setChanged();
				notifyObservers("Maze '" + name + "' is ready");
				return maze;
			}
		};
		
		threadpool.submit(callable);		
	}
	
	@Override
	public void generate3dMaze() {
		if(hm.containsKey(name) == true)
		{
			setChanged();
			notifyObservers("Maze '"+name+"' is already exist");
			return;
		}
		Callable<Maze3d> callable = new Callable<Maze3d>() {

			@Override
			public Maze3d call() throws Exception {
				Maze3d maze;
				if(algorithemForGenerate.equals("MyMaze3dGenerator"))
					maze = new MyMaze3dGenerator().generate(y, z, x);
				else if(algorithemForGenerate.equals("PrimMaze3dGenerator"))
					maze = new PrimMaze3dGenerator().generate(y, z, x);
				else
					maze = new SimpleMaze3dGenerator().generate(y, z, x);
				hm.put(name,maze);
				setChanged();
				notifyObservers("Maze '" + name + "' is ready");
				return maze;
			}
		};
		
		threadpool.submit(callable);
		
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

	/*@Override
	public void createSolution(String str) {
		String[] parm=str.split(" ");
		boolean isDefault;
		boolean notStartPositon;
		if(parm.length < 1 || parm.length > 3){
			setChanged();
			notifyObservers("Done: Invalid Command");
			return;
		}
		
		String name = null;
		if(parm.length == 2)
			name = parm[0];
		else if(parm.length == 1 && !parm[0].equals(""))
			name = parm[0];
		else
			name = this.name;

		
		Solution<Position> solution = hashSolution.get(name);
		
		if(solution != null){
			if(parm.length==3){
				int x = Integer.parseInt(parm[0]);
				int y = Integer.parseInt(parm[1]);
				int z = Integer.parseInt(parm[2]);
				State<Position> newStart = new State<Position>(new Position(x,y,z));
				if(solution.indexOf(newStart) == solution.toString().split(" ").length-1){
					setChanged();
					notifyObservers("Done: solution for " + name + " is ready");
					return;
				}
			}
			else{
				setChanged();
				notifyObservers("Done: solution for " + name + " is ready");
				return;
			}	
		}
		
		if(parm.length == 2)
			isDefault = false;
		else
			isDefault = true;

		if(parm.length == 3){
			notStartPositon = true;
		}
		else
			notStartPositon = false;
		
		Maze3d maze = hm.get(name);
		String string = name;
		
		Callable<Solution<Position>> callable = new Callable<Solution<Position>>() {

			@Override
			public Solution<Position> call() throws Exception {
				Solution<Position> solution = null;
				if((isDefault && algorithemForSolution.equals("BFS")) || (!isDefault && parm[1].equalsIgnoreCase("bfs"))){
					if(maze != null){
						BFS<Position> bfs = new BFS<Position>();
						MazeDomain md = new MazeDomain(maze);
						if(notStartPositon){
							int x = Integer.parseInt(parm[0]);
							int y = Integer.parseInt(parm[1]);
							int z = Integer.parseInt(parm[2]);
							md.setStartState(new State<Position>(new Position(x,y,z)));
						}
						solution = bfs.search(md);
						hashSolution.put(hm.get(string), solution);
						setChanged();
						notifyObservers("Done: solution for " + string + " is ready");
					}
					else{
						setChanged();
						notifyObservers("Done: Invalid name");
					}
				}
				else if((isDefault && algorithemForSolution.equals("A* Manhattan Distance")) || (!isDefault && parm[1].equalsIgnoreCase("ManhattanDistance"))){
					if(maze != null){
						AStar<Position> astarManhattanDistance = new AStar<Position>(new MazeManhattanDistance(new State<Position>(maze.getGoalPosition())));
						MazeDomain md = new MazeDomain(maze);
						if(notStartPositon){
							int x = Integer.parseInt(parm[0]);
							int y = Integer.parseInt(parm[1]);
							int z = Integer.parseInt(parm[2]);
							md.setStartState(new State<Position>(new Position(x,y,z)));
						}
						solution = astarManhattanDistance.search(md);
						hashSolution.put(hm.get(string), solution);
						setChanged();
						notifyObservers("Done: solution for " + string + " is ready");
					}
					else{
						setChanged();
						notifyObservers("Done: Invalid name");
					}
				}
				else if((isDefault && algorithemForSolution.equals("A* Air Distance")) || (!isDefault && parm[1].equalsIgnoreCase("AirDistance"))){
					if(maze != null){
						AStar<Position> astarAirDistance = new AStar<Position>(new MazeAirDistance(new State<Position>(maze.getGoalPosition())));
						SearchableMaze md = new SearchableMaze(maze);
						if(notStartPositon){
							int x = Integer.parseInt(parm[0]);
							int y = Integer.parseInt(parm[1]);
							int z = Integer.parseInt(parm[2]);
							md.setStartState(new State<Position>(new Position(x,y,z)));
						}
						solution = astarAirDistance.search(md);
						hashSolution.put(hm.get(string), solution);
						setChanged();
						notifyObservers("Done: solution for " + string + " is ready");
					}
					else{
						setChanged();
						notifyObservers("Done: Invalid name");
					}
				}
				else{
					setChanged();
					notifyObservers("Done: Invalid algorithm");
				}
				return solution;
			}
		};
		
		threadpool.submit(callable);
	}*/

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

	public void save() {
		/*ObjectOutputStream out = null;
		try{
			out = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream("SolutionMap.zip")));
			out.writeObject(hashSolution);
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
		}*/
		FileOutputStream fileSolutions;
		GZIPOutputStream GZIPOutput;
		ObjectOutputStream out = null;
		try {
			fileSolutions = new FileOutputStream("solution.zip");
			GZIPOutput = new GZIPOutputStream(fileSolutions);
			out = new ObjectOutputStream(GZIPOutput);
			out.writeObject(hm);
			out.writeObject(hashSolution);
			out.writeObject(mazeFile);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			setChanged();
			notifyObservers(e.getMessage());
		}
		catch (IOException e) {
			setChanged();
			notifyObservers("IOexception "+e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void load() {
		/*ObjectInputStream in = null;
		try{
			in = new ObjectInputStream(new GZIPInputStream(new FileInputStream("SolutionMap.zip")));
			hashSolution = (HashMap<Maze3d, Solution<Position>>) in.readObject();
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
		}*/
		FileInputStream fileSolutions;
		GZIPInputStream GZIPInput;
		ObjectInputStream in;
		try {
			fileSolutions = new FileInputStream("solution.zip");
			GZIPInput = new GZIPInputStream(fileSolutions);
			in = new ObjectInputStream(GZIPInput);
			hm = (HashMap<String, Maze3d>) in.readObject();
			hashSolution = (HashMap<Maze3d, Solution<Position>>) in.readObject();
			mazeFile = (HashMap<Maze3d, String>) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			setChanged();
			notifyObservers(e.getMessage());
		}
		catch (IOException e) {
			setChanged();
			notifyObservers(e.getMessage());
		}	
		catch (ClassNotFoundException e) {
		setChanged();
		notifyObservers(e.toString());
		}
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public void setAlgorithemForSolution(String algorithemForSolution) {
		this.algorithemForSolution = algorithemForSolution;
	}

	public void setAlgorithemForGenerate(String algorithemForGenerate) {
		this.algorithemForGenerate = algorithemForGenerate;
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

	public void setProperties(Properties properties) {
		this.properties = properties;
		
		setName(properties.getName());		
		setX(properties.getX());
		setY(properties.getY());
		setZ(properties.getZ());
		setAlgorithemForGenerate(properties.getAlgorithemForGenerate());
		setAlgorithemForSolution(properties.getAlgorithemForSolution());
		setNumberOfThreads(properties.getNumberOfThreads());
		threadpool = Executors.newFixedThreadPool(numberOfThreads);
		
	}
	@Override
	public void solve(String name, String algorithm) {
		if(hashSolution.containsKey(hm.get(name)) == true)
		{
			setChanged();
			notifyObservers("Solution for '" + name + "' is ready");
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
			notifyObservers("Solution for '" + name + "' is ready");
			return;
		}	
		Callable<Solution<Position>> callable = new Callable<Solution<Position>>() {
			@Override
			public Solution<Position> call() throws Exception {
				if(algorithemForSolution.equalsIgnoreCase("bfs")){
					Maze3d maze = hm.get(name);
					if(maze != null){
						CostComparator<Position> c = new CostComparator<Position>();
						BFS<Position> bfs = new BFS<Position>(c);
						Solution<Position> bfsSolution = bfs.search(new SearchableMaze(maze));
						hashSolution.put(maze, bfsSolution);
						setChanged();
						notifyObservers("Solution for '" + name + "' is ready");
					}
					else{
						setChanged();
						notifyObservers("Invalid name");
					}
				}
				else if(algorithemForSolution.equalsIgnoreCase("MazeManhattanDistance")){
					Maze3d maze = hm.get(name);
					if(maze != null){
						CostComparator<Position> c = new CostComparator<Position>();
						AStar<Position> astarManhattanDistance = new AStar<Position>(new MazeManhattenDistance(new State<Position>(maze.getGoalPosition())),c);
						Solution<Position> astarManhattan = astarManhattanDistance.search(new SearchableMaze(maze));
						hashSolution.put(maze, astarManhattan);
						setChanged();
						notifyObservers("Solution for '" + name + "' is ready");
					}
					else{
						setChanged();
						notifyObservers("Invalid name");
					}
				}
				else if(algorithemForSolution.equalsIgnoreCase("MazeAirDistance")){
					Maze3d maze = hm.get(name);
					if(maze != null){
						CostComparator<Position> c = new CostComparator<Position>();
						AStar<Position> astarAirDistance = new AStar<Position>(new MazeAirDistance(new State<Position>(maze.getGoalPosition())),c);
						Solution<Position> astarAir = astarAirDistance.search(new SearchableMaze(maze));
						hashSolution.put(maze, astarAir);
						setChanged();
						notifyObservers("Solution for '" + name + "' is ready");
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

}
