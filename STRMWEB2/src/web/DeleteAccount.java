package web;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import net.Account;
import net.Streamer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class DeleteAccount extends Shell {
	private Text text;
	private static Driver drive;
	private static Streamer stream;
	private static Account account;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			DeleteAccount shell = new DeleteAccount(display,drive,stream,account);
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
	public DeleteAccount(Display display,Driver Drive, Streamer Stream,Account Account) {
		super(display, SWT.SHELL_TRIM);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		
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
		btnBack.setBounds(20, 20, 94, 28);
		btnBack.setText("Back");
		
		Label lblPassword = new Label(this, SWT.NONE);
		lblPassword.setBounds(112, 71, 61, 14);
		lblPassword.setText("Password: ");
		
		text = new Text(this, SWT.BORDER);
		text.setBounds(179, 68, 150, 19);
		
		Button btnDeleteAccount = new Button(this, SWT.NONE);
		btnDeleteAccount.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String pass = text.getText();
				if(pass.equals(account.Password())) {
					File file = new File(account.Username()+".txt");
					file.delete();
					stream.deleteAccount(account.Username());
					Login back = new Login(display,stream,drive);
					AccountDeleted next = new AccountDeleted(display);
					drive.changeShell(back,display);
					next.open();
				}
				else {
					PasswordError next = new PasswordError(display);
					next.open();
				}
			}
		});
		btnDeleteAccount.setBounds(166, 115, 121, 28);
		btnDeleteAccount.setText("Delete Account");
		setDefaultButton(btnDeleteAccount);
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Delete Account");
		setSize(450, 225);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
