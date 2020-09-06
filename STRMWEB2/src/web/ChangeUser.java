package web;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.Account;
import net.Streamer;
import org.eclipse.wb.swt.SWTResourceManager;

public class ChangeUser extends Shell {

	private static Driver drive;
	private static Streamer stream;
	private static Account account;
	private Text text;
	private Text text_1;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			ChangeUser shell = new ChangeUser(display,drive,stream,account);
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
	public ChangeUser(Display display,Driver Drive, Streamer Stream, Account Account) {
		super(display, SWT.SHELL_TRIM);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		createContents();
		
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
		
		Label lblCurrentPassword = new Label(this, SWT.NONE);
		lblCurrentPassword.setBounds(86, 85, 108, 14);
		lblCurrentPassword.setText("Current Password:");
		
		
		Label lblNewUser = new Label(this, SWT.NONE);
		lblNewUser.setBounds(105, 133, 94, 14);
		lblNewUser.setText("New Username:");
		
		Button btnChangeUser = new Button(this, SWT.NONE);
		btnChangeUser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Account acc = stream.getAccount(text_1.getText());
				if(acc==null) {
					try {
						String old = account.Username();
						account.ChangeUsername(text.getText(), text_1.getText());
						stream.deleteAccount(old);
						stream.reAddAccount(account);
						File file = new File(old+".txt");
						file.delete();
						Profile back = new Profile(display,drive,stream,account);
						drive.changeShell(back,display);
						UserChangeSuccess next = new UserChangeSuccess(display);
						next.open();
					}
					catch(Exception ex) {
						PasswordError err = new PasswordError(display);
						err.open();
					}
				}
				else {
					UsernameError err = new UsernameError(display);
					err.open();
				}
			}
		});
		btnChangeUser.setBounds(149, 175, 139, 28);
		btnChangeUser.setText("Change Username");
		setDefaultButton(btnChangeUser);
		
		text = new Text(this, SWT.BORDER);
		text.setBounds(198, 82, 139, 19);
		
		text_1 = new Text(this, SWT.BORDER);
		text_1.setBounds(198, 130, 139, 19);
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Change Username");
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
