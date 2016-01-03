package view;

import java.util.HashMap;

import presenter.Command;

public class GUI extends BasicWindow implements UserChoice{
	
	/**
	 * constructor for GUI
	 * @param title
	 * @param width
	 * @param height
	 */
	public GUI(String title, int width, int height) {
		super(title, width, height);
		
	}

	View view;
	HashMap <String,Command> hc;
	@Override
	public void setView(View view) {
		this.view = view;
	}

	@Override
	public void start() {
		run();
		
	}

	@Override
	public void setMessage(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHashCommand(HashMap<String, Command> hc) {
		this.hc = hc;
		
	}

	@Override
	void initWidgets() {
		// TODO Auto-generated method stub
		
	}

}
