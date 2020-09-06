package web;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import movElem.Movie;
import net.Account;
import net.Streamer;

public class ListRat extends Shell {

	private static Driver drive;
	private static Streamer stream;
	private static Account account;
	private static ArrayList<Movie>in;
	private static boolean all;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			ListRat shell = new ListRat(display,drive,stream,account,in,all);
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
	public ListRat(Display display, Driver Drive, Streamer Stream, Account Account, ArrayList<Movie>In, boolean All) {
		super(display, SWT.SHELL_TRIM);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		
		drive = Drive;
		stream = Stream;
		account = Account;
		in = In;
		all = All;
		
		List list = new List(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MoviePage next = new MoviePage(display,drive,stream,account,in.get(list.getSelectionIndex()),all);
				drive.changeShell(next,display);
			}
		});
		list.setBounds(51, 93, 348, 160);
		int i = 1;
		for(Movie m:in) {
			list.add(i+". "+m.Name()+": "+m.strScore()+"%");
			i++;
		}
		
		Label lblMostPopularMovies = new Label(this, SWT.NONE);
		lblMostPopularMovies.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		lblMostPopularMovies.setAlignment(SWT.CENTER);
		lblMostPopularMovies.setBounds(40, 44, 370, 25);
		lblMostPopularMovies.setText("Highest Rated Movies");
		
		Button btnHome = new Button(this, SWT.NONE);
		btnHome.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Home next = new Home(display,drive,stream,account);
				drive.changeShell(next,display);
			}
		});
		btnHome.setBounds(20, 10, 94, 28);
		btnHome.setText("Home");
		
		Button btnMyProfile = new Button(this, SWT.NONE);
		btnMyProfile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Profile next = new Profile(display,drive,stream,account);
				drive.changeShell(next,display);
			}
		});
		btnMyProfile.setBounds(335, 10, 94, 28);
		btnMyProfile.setText("My Profile");
		createContents();
		
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Highest Rated Movies");
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
