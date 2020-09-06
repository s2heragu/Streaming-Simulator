package web;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import java.io.*;
import java.util.*;
import movElem.*;
import net.*;
import sort.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class SignUp extends Shell {
	private Text text;
	private Text text_1;
	private static Streamer stream;
	private static Driver drive;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			SignUp shell = new SignUp(display,stream,drive);
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
	public SignUp(Display display, Streamer Stream, Driver Drive) {
		super(display, SWT.SHELL_TRIM);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		setLayout(null);
		
		drive = Drive;
		
		stream = Stream;
		
		Label lblStreamwebSignUp = new Label(this, SWT.NONE);
		lblStreamwebSignUp.setFont(SWTResourceManager.getFont("Lucida Sans", 15, SWT.BOLD));
		lblStreamwebSignUp.setBounds(151, 54, 164, 35);
		lblStreamwebSignUp.setText("STREAMWEB Sign Up");
		
		Label lblUsername = new Label(this, SWT.NONE);
		lblUsername.setBounds(112, 98, 59, 14);
		lblUsername.setText("Username: ");
		
		Label lblPassword = new Label(this, SWT.NONE);
		lblPassword.setBounds(112, 141, 59, 14);
		lblPassword.setText("Password:");
		
		text = new Text(this, SWT.BORDER);
		text.setBounds(177, 95, 125, 19);
		
		text_1 = new Text(this, SWT.BORDER);
		text_1.setBounds(177, 141, 125, 19);
		
		Button btnSignUp = new Button(this, SWT.NONE);
		btnSignUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					stream.addAccount(text.getText(), text_1.getText());
					UserNameSuccess u2 = new UserNameSuccess(display);
					u2.open();
				}
				catch(Exception ex) {
					UsernameError u1 = new UsernameError(display);
					u1.open();
				}
			}
		});
		btnSignUp.setBounds(190, 180, 94, 28);
		btnSignUp.setText("Sign Up");
		setDefaultButton(btnSignUp);
		
		Button btnBack = new Button(this, SWT.NONE);
		btnBack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Login back = new Login(display,stream,drive);
				drive.changeShell(back,display);
			}
		});
		btnBack.setBounds(10, 21, 94, 28);
		btnBack.setText("Back");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Sign Up");
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
