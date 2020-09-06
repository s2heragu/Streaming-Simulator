package web;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class LoginError extends Shell {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			LoginError shell = new LoginError(display);
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
	public LoginError(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		Label lblErrorInvalid = new Label(this, SWT.NONE);
		lblErrorInvalid.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 13, SWT.NORMAL));
		lblErrorInvalid.setAlignment(SWT.CENTER);
		lblErrorInvalid.setBounds(40, 57, 369, 28);
		lblErrorInvalid.setText("Error 110: Invalid username or password. Please try again.");
		
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
				dispose();
			}
		});
		btnNewButton.setBounds(180, 81, 94, 28);
		btnNewButton.setText("OK");
		setDefaultButton(btnNewButton);
		
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Error 110");
		setSize(450, 225);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
