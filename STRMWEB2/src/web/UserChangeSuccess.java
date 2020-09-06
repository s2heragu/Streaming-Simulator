package web;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class UserChangeSuccess extends Shell {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			UserChangeSuccess shell = new UserChangeSuccess(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public UserChangeSuccess(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		Label lblYourPasswordWas = new Label(this, SWT.NONE);
		lblYourPasswordWas.setAlignment(SWT.CENTER);
		lblYourPasswordWas.setBounds(104, 79, 243, 28);
		lblYourPasswordWas.setText("Your username was successfully changed.");
		
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
				dispose();
			}
		});
		btnNewButton.setBounds(178, 99, 94, 28);
		btnNewButton.setText("OK");
		setDefaultButton(btnNewButton);
		
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Username Changed Succesfully");
		setSize(450, 225);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
