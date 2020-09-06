package web;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class ThankYou extends Shell {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			ThankYou shell = new ThankYou(display);
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
	public ThankYou(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		Label lblErrorUsernameAlready = new Label(this, SWT.NONE);
		lblErrorUsernameAlready.setAlignment(SWT.CENTER);
		lblErrorUsernameAlready.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		lblErrorUsernameAlready.setBounds(56, 72, 347, 18);
		lblErrorUsernameAlready.setText("Thank you for submitting your review.");
		
		Button btnOk = new Button(this, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
				dispose();
			}
		});
		btnOk.setBounds(179, 96, 94, 28);
		btnOk.setText("OK");
		setDefaultButton(btnOk);
		
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Review Success");
		setSize(450, 225);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
