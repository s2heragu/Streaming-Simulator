package web;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class UserNameSuccess extends Shell {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			UserNameSuccess shell = new UserNameSuccess(display);
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
	public UserNameSuccess(Display display) {
		super(display, SWT.SHELL_TRIM);
		setLayout(null);
		
		Label lblCongratulationsYouHave = new Label(this, SWT.NONE);
		lblCongratulationsYouHave.setAlignment(SWT.CENTER);
		lblCongratulationsYouHave.setBounds(22, 55, 407, 20);
		lblCongratulationsYouHave.setText("Congratulations! You have successfully created a STREAMWEB account.");
		
		Label lblReturnToThe = new Label(this, SWT.NONE);
		lblReturnToThe.setBounds(123, 73, 211, 21);
		lblReturnToThe.setText("Please sign in to open your account.");
		
		Button btnOk = new Button(this, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
				dispose();
			}
		});
		btnOk.setBounds(178, 100, 94, 28);
		btnOk.setText("OK");
		setDefaultButton(btnOk);
		
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Account Created");
		setSize(450, 225);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
