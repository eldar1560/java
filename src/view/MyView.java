package view;


import java.util.HashMap;
import java.util.Observable;

import presenter.Command;
/**
 * the implementation of view
 * @author Eldar , ofek
 *
 */
public class MyView extends Observable implements View{
	UserChoice uc;
	HashMap<String,Command> hc;

	/**
	 * Constructor of MyView , set the view of the user interface
	 */
	public MyView(UserChoice uc)
	{
		this.uc = uc;
		uc.setView(this);
	}
	@Override
	public void start() {
		uc.start();
	}

	@Override
	public void displayMessage(String message) {
		uc.setMessage(message);
		
	}

	@Override
	public void setHashCommand(HashMap<String, Command> hc) {
		uc.setHashCommand(hc);
		this.hc = hc;
	}
	
	public void notifyMe(String[] str)
	{
		setChanged();
		notifyObservers(str);
	}
	@Override
	public void notifyMe(String str) {
		setChanged();
		notifyObservers(str);
	}
	
}
