package view;

import java.util.HashMap;

import presenter.Command;

public interface UserChoice {
	/**
	 * set the view
	 * @param view
	 */
	void setView(View view);
	/**
	 * start the thread
	 */
	void start();
	/**
	 * display the message
	 * @param message
	 */
	void setMessage(String message);
	/**
	 * set commands into hash map
	 * @param hashCommand
	 */
	void setHashCommand(HashMap<String, Command> hc);
}
