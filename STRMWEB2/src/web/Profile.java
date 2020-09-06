package web;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import net.*;
import movElem.*;
import sort.*;
import org.eclipse.swt.widgets.Composite;
import java.util.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.List;

public class Profile extends Shell {

	private static Account account;
	private static Streamer stream;
	private static Driver drive;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			Profile shell = new Profile(display,drive,stream,account);
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
	public Profile(Display display, Driver Drive, Streamer Stream, Account Account) {
		super(display, SWT.SHELL_TRIM);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		setLayout(null);
		
		stream = Stream;
		drive = Drive;
		account = Account;
		
		ArrayList<ArrayList<Movie>> tot = account.favorites(stream);
		
		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(0, 41, 450, 2);
		
		
		Label label_1 = new Label(this, SWT.SEPARATOR | SWT.VERTICAL);
		label_1.setBounds(318, 0, 2, 43);
		
		CLabel lblMyProfile = new CLabel(this, SWT.NONE);
		lblMyProfile.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		lblMyProfile.setBounds(10, 8, 292, 19);
		lblMyProfile.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		lblMyProfile.setText("My Profile: " + account.Username());
		
		Button btnHome = new Button(this, SWT.NONE);
		btnHome.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Home next = new Home(display,drive,stream,account);
				drive.changeShell(next,display);
			}
		});
		btnHome.setBounds(325, 7, 115, 28);
		btnHome.setText("Home");
		
		Label lblFavorites = new Label(this, SWT.NONE);
		lblFavorites.setBounds(10, 50, 73, 14);
		lblFavorites.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 13, SWT.NORMAL));
		lblFavorites.setText("Favorites:");
		
		Label label_2 = new Label(this, SWT.SEPARATOR | SWT.VERTICAL);
		label_2.setBounds(318, 41, 2, 91);
		
		Label label_3 = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_3.setBounds(0, 155, 320, 2);
		
		Label label_4 = new Label(this, SWT.SEPARATOR | SWT.VERTICAL);
		label_4.setBounds(318, 103, 2, 175);
		
		Label lblRecommended = new Label(this, SWT.NONE);
		lblRecommended.setBounds(10, 163, 120, 19);
		lblRecommended.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 13, SWT.NORMAL));
		lblRecommended.setText("Recommended:");
		
		Label lblStreams = new Label(this, SWT.NONE);
		lblStreams.setBounds(355, 49, 59, 28);
		lblStreams.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 13, SWT.NORMAL));
		lblStreams.setAlignment(SWT.CENTER);
		lblStreams.setText("Streams:");
		
		Label label_6 = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_6.setBounds(318, 155, 132, 2);
		
		Button btnSettings = new Button(this, SWT.NONE);
		btnSettings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AccountSettings next = new AccountSettings(display,drive,stream,account);
				drive.changeShell(next,display);
			}
		});
		btnSettings.setBounds(325, 173, 114, 28);
		btnSettings.setText("Settings");
		
		Label label_7 = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_7.setBounds(318, 218, 132, 2);
		
		Button btnLogOut = new Button(this, SWT.NONE);
		btnLogOut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Login next = new Login(display,stream,drive);
				drive.changeShell(next,display);
			}
		});
		btnLogOut.setBounds(326, 235, 114, 28);
		btnLogOut.setText("Log Out");
		
		Label lblStreamNum = new Label(this, SWT.NONE);
		lblStreamNum.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 24, SWT.NORMAL));
		lblStreamNum.setAlignment(SWT.CENTER);
		lblStreamNum.setText(""+account.Streams());
		lblStreamNum.setBounds(327, 83, 115, 34);
		
		List favs = new List(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		favs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MoviePage next = new MoviePage(display,drive,stream,account,tot.get(0).get(favs.getSelectionIndex()),false);
				drive.changeShell(next,display);
			}
		});
		favs.setBounds(20, 75, 277, 66);
		
		List recs = new List(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		recs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MoviePage next = new MoviePage(display,drive,stream,account,tot.get(1).get(recs.getSelectionIndex()),false);
				drive.changeShell(next,display);
			}
		});
		recs.setBounds(20, 190, 277, 66);
		createContents();
		
		if(tot!=null) {
			if(!tot.get(0).isEmpty()) {
				int i = 1;
				for(Movie m:tot.get(0)) {
					favs.add(i+". "+m.Name());
					i++;
				}
			}
			if(!tot.get(1).isEmpty()) {
				int i = 1;
				for(Movie m:tot.get(1)) {
					recs.add(i+". "+m.Name());
					i++;
				}
			}
		}
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Profile");
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
