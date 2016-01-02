package boot;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import model.MyModel;
import presenter.Presenter;
import presenter.Properties;
import view.MyView;

public class Run {

	public static void main(String[] args) {
		XMLDecoder d;
		Properties properties = new Properties();
		//UserInterface ui = null;
		
		try {
			d = new XMLDecoder(new BufferedInputStream(new FileInputStream("Properties.xml")));
			properties = (Properties) d.readObject();
			d.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		//		if(properties.getUi().equals("CLI"))
		//		ui = new CLI(new BufferedReader(new InputStreamReader(System.in)), new PrintWriter(System.out));
		//	else if(properties.getUi().equals("GUI"))
		//		ui = new GUI("Maze 3D GAME", 1200, 700);
		
		MyModel model = new MyModel(properties);
		MyView view = new MyView(new BufferedReader(new InputStreamReader(System.in)),new PrintWriter(System.out));
		Presenter presenter = new Presenter(model,view);
		
		model.addObserver(presenter);
		view.addObserver(presenter);
		
		System.out.println("You need to choose one of the following commands every time:\ndir <path>\ngenerate3dMaze <name> <y> <z> <x>\ndisplay <name>\ndisplayCrossSectionBy <X,Y or Z> <index> <name>\nsaveMaze <name> <file name>\nloadMaze <file name> <name>\nmazeSize <name>\nfileSize <name>\nsolve <name> <algorithm>\ndisplaySolution <name>\nexit");
		view.start();

	}

}








