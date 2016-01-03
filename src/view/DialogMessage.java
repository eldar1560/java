package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class DialogMessage extends Dialog{
	Object result;
	String messege;
    /**
     * DialogMessage constructor
     * @param parent
     * @param style
     * @param messege
     */
    public DialogMessage (Shell parent, int style, String messege) {
    	super (parent, style);
        this.messege = messege;
    }
    /**
     * DialogMessage constructor
     * @param parent
     * @param style
     * @param messege
     */
    public DialogMessage (Shell parent, String messege) {
    	super (parent, 0);
        this.messege = messege;
    }
    /**
     * open the dialog
     */
    public Object open () {
        Shell parent = getParent();
        Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText("Messege box");
        shell.setSize(150, 100);
            
        Label label = new Label(shell, SWT.NONE);
		label.setText(messege);
		label.setBounds(35, 5, 100, 20);

		Button okButton = new Button(shell, SWT.PUSH);
		okButton.setBounds(50, 35, 40, 25);
		okButton.setText("OK");
		okButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		
		okButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				shell.close();
				
			}
		});
		
        shell.open();
        Display display = parent.getDisplay();
        while (!shell.isDisposed()) {
        	if (!display.readAndDispatch()) display.sleep();
        }
        return result;
    }
}
