package web;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import movElem.*;
import net.*;
import sort.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class AccountSettings extends Shell {
	
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
			AccountSettings shell = new AccountSettings(display,drive,stream,account);
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
	public AccountSettings(Display display, Driver Drive, Streamer Stream, Account Account) {
		super(display, SWT.SHELL_TRIM);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		setLayout(null);
		
		account = Account;
		stream = Stream;
		drive = Drive;
		
		Button btnBack = new Button(this, SWT.NONE);
		btnBack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Profile back = new Profile(display,drive,stream,account);
				drive.changeShell(back,display);
			}
		});
		btnBack.setBounds(21, 10, 94, 28);
		btnBack.setText("Back");
		
		Button btnChangeUsername = new Button(this, SWT.NONE);
		btnChangeUsername.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ChangeUser next = new ChangeUser(display,drive,stream,account);
				drive.changeShell(next,display);
			}
		});
		btnChangeUsername.setBounds(10, 62, 142, 51);
		btnChangeUsername.setText("Change Username");
		
		Button btnDeleteAccount = new Button(this, SWT.NONE);
		btnDeleteAccount.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DeleteAccount next = new DeleteAccount(display,drive,stream,account);
				drive.changeShell(next,display);
			}
		});
		btnDeleteAccount.setBounds(164, 62, 122, 51);
		btnDeleteAccount.setText("Delete Account");
		
		Button btnChangePassword = new Button(this, SWT.NONE);
		btnChangePassword.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ChangePass next = new ChangePass(display,drive,stream,account);
				drive.changeShell(next,display);
			}
		});
		btnChangePassword.setBounds(298, 62, 142, 51);
		btnChangePassword.setText("Change Password");
		
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Account Settings");
		setSize(450, 225);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
