package web;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.Account;
import net.Streamer;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class ChangePass extends Shell {
	private Text text;
	private Text text_1;
	private static Driver drive;
	private static Account account;
	private static Streamer stream;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			ChangePass shell = new ChangePass(display, drive, stream, account);
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
	public ChangePass(Display display,Driver Drive, Streamer Stream, Account Account) {
		super(display, SWT.SHELL_TRIM);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		setLayout(null);
		
		drive = Drive;
		stream = Stream;
		account = Account;
		
		Button btnBack = new Button(this, SWT.NONE);
		btnBack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AccountSettings back = new AccountSettings(display,drive,stream,account);
				drive.changeShell(back,display);
			}
		});
		btnBack.setBounds(25, 25, 94, 28);
		btnBack.setText("Back");
		
		text = new Text(this, SWT.BORDER);
		text.setBounds(194, 82, 146, 19);
		
		Label lblCurrentPassword = new Label(this, SWT.NONE);
		lblCurrentPassword.setBounds(86, 85, 108, 14);
		lblCurrentPassword.setText("Current Password:");
		
		text_1 = new Text(this, SWT.BORDER);
		text_1.setBounds(194, 130, 146, 19);
		
		Label lblNewPassword = new Label(this, SWT.NONE);
		lblNewPassword.setBounds(105, 133, 94, 14);
		lblNewPassword.setText("New Password:");
		
		Button btnChangePassword = new Button(this, SWT.NONE);
		btnChangePassword.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					account.ChangePassword(text.getText(), text_1.getText());
					Profile back = new Profile(display,drive,stream,account);
					drive.changeShell(back,display);
					PassChangeSuccess next = new PassChangeSuccess(display);
					next.open();
				}
				catch(Exception ex) {
					PasswordError err = new PasswordError(display);
					err.open();
				}
			}
		});
		btnChangePassword.setBounds(149, 175, 139, 28);
		btnChangePassword.setText("Change Password");
		setDefaultButton(btnChangePassword);
		
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Change Password");
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
