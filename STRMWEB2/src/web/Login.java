package web;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import movElem.*;
import net.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Login extends Shell {
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
			Login shell = new Login(display,stream,drive);
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
	public Login(Display display, Streamer Stream, Driver Drive) {
		super(display, SWT.SHELL_TRIM);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		
		drive = Drive;
		
		stream = Stream;
		
		Label lblStreamweb = new Label(this, SWT.NONE);
		lblStreamweb.setFont(SWTResourceManager.getFont("Lucida Grande", 30, SWT.NORMAL));
		lblStreamweb.setBounds(139, 56, 181, 59);
		lblStreamweb.setText("STREAMWEB");
		
		Button btnSignUp = new Button(this, SWT.NONE);
		btnSignUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Display dis = Display.getDefault();
				SignUp next = new SignUp(dis,stream,drive);
				drive.changeShell(next,display);
			}
		});
		btnSignUp.setBounds(334, 10, 94, 28);
		btnSignUp.setText("Sign Up");
		
		Label lblUsername = new Label(this, SWT.NONE);
		lblUsername.setBounds(107, 118, 94, 14);
		lblUsername.setText("Username:");
		
		Label lblPassword = new Label(this, SWT.NONE);
		lblPassword.setBounds(107, 155, 59, 14);
		lblPassword.setText("Password:");
		
		text = new Text(this, SWT.BORDER);
		text.setBounds(175, 115, 145, 19);
		
		text_1 = new Text(this, SWT.BORDER);
		text_1.setBounds(175, 152, 145, 19);
		
		Button btnSignIn = new Button(this, SWT.NONE);
		btnSignIn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Account a = stream.getAccount(text.getText());
				if(a==null) {
					LoginError err = new LoginError(display);
					err.open();
				}
				else {
					if(!text_1.getText().equals(a.Password())){
						LoginError err = new LoginError(display);
						err.open();
					}
					else {
						Home next = new Home(display,drive,stream,a);
						drive.changeShell(next,display);
					}
				}
			}
		});
		btnSignIn.setBounds(187, 191, 94, 28);
		btnSignIn.setText("Sign In");
		setDefaultButton(btnSignIn);
		
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Sign In");
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
