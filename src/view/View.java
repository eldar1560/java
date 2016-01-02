package view;

import java.util.HashMap;

import presenter.Command;
/**
 * The view Interface , set the view functionals to the machine from the cli and starts the program.
 * @author Eldar , Ofek
 *
 */
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
	/**
	 * send notification to the presenter with the string[] that we got
	 * @param str
	 */
	void notifyMe(String[] str);
	/**
	 * send notification to the presenter with the string that we got
	 * @param str
	 */
	void notifyMe(String str);
}
