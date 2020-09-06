package web;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class PasswordError extends Shell {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			PasswordError shell = new PasswordError(display);
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
	public PasswordError(Display display) {
		super(display, SWT.SHELL_TRIM);
		setLayout(null);
		
		Label lblErrorIncorrect = new Label(this, SWT.NONE);
		lblErrorIncorrect.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 13, SWT.NORMAL));
		lblErrorIncorrect.setAlignment(SWT.CENTER);
		lblErrorIncorrect.setBounds(51, 65, 348, 27);
		lblErrorIncorrect.setText("Error 207: Incorrect password entered. Please try again.");
		
		Button btnOk = new Button(this, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
				dispose();
			}
		});
		btnOk.setBounds(178, 90, 94, 27);
		btnOk.setText("OK");
		setDefaultButton(btnOk);
		
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Error 207");
		setSize(450, 225);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
