package model;

import java.util.HashMap;
import java.util.Observable;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import presenter.Presenter;
/**
 * CommonModel implements Model interface
 * abstract class of the model
 * @author Eldar , Ofek
 */

public abstract class CommonModel extends Observable implements Model{
	Presenter presenter;
	HashMap<String, Maze3d> hm;
	HashMap<Maze3d, Solution<Position>> hashSolution;
	
	/**
	 * Default constructor 
	 */
	public CommonModel()
	{
		hm = new HashMap<String,Maze3d>();
		hashSolution = new HashMap<Maze3d, Solution<Position>>();
	}
	@Override	
	public void displaySolution(String name){
		Solution<Position>	solution = hashSolution.get(hm.get(name));
		if(solution == null)
		{
			setChanged();
			notifyObservers("Not exist solution for '" + name + "' maze");
		}
		else
		{
			setChanged();
			notifyObservers(solution.toString());
		}
	}

	public abstract void dir(String path);
	@Override
	public abstract void generate3dMaze(String name,int y, int z, int x);
	
	@Override
	public abstract void generate3dMaze();
	@Override
	public abstract void display(String name);
	
	@Override
	public abstract void solve(String name,String algorithm);
	
	@Override
	public abstract void solve();
	
	@Override
	public abstract void displayCrossSectionBy(String by, int index, String name);
	
	@Override
	public abstract void saveMaze(String name,String fileName);
	
	@Override
	public abstract void loadMaze(String fileName,String name);
	
	@Override
	public abstract void mazeSize(String name);
	
	@Override
	public abstract void fileSize(String name);
	
	@Override
	public abstract void exit();
	

}
