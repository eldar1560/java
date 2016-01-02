package view;

import java.util.HashMap;

import presenter.Command;

public interface View {
	/**
	 * starts the program
	 */
	void start();
	/**
	 * display the message to the out file.
	 * @param message the message you want to display
	 */
	void displayMessage(String message);
	/**
	 * set the hash commands that we will send to the cli 
	 * @param hc the full hash command
	 */
	void setHashCommand(HashMap<String,Command> hc);
	void notifyMe(String[] str);
	void notifyMe(String str);
}
