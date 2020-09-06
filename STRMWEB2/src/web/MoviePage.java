package web;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import movElem.*;
import sort.*;
import net.*;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class MoviePage extends Shell {
	private static Movie movie;
	private static Streamer stream;
	private static Account account;
	private static Driver drive;
	private static boolean all;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			MoviePage shell = new MoviePage(display,drive,stream,account,movie,all);
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
	public MoviePage(Display display, Driver Drive, Streamer Stream, Account Account, Movie Movie, boolean All) {
		super(display, SWT.SHELL_TRIM);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		setLayout(null);
		
		movie = Movie;
		account = Account;
		stream = Stream;
		drive = Drive;
		all = All;
		
		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(0, 50, 450, 2);
		
		Label label_1 = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(0, 100, 450, 2);
		
		Label label_2 = new Label(this, SWT.SEPARATOR | SWT.VERTICAL);
		label_2.setBounds(150, 100, 2, 177);
		
		Label label_3 = new Label(this, SWT.SEPARATOR | SWT.VERTICAL);
		label_3.setBounds(300, 100, 2, 177);
		
		Label lblName = new Label(this, SWT.NONE);
		lblName.setFont(SWTResourceManager.getFont("Luminari", 17, SWT.NORMAL));
		lblName.setBounds(10, 10, 430, 32);
		lblName.setText(movie.Name());
		
		Label lblGenre = new Label(this, SWT.BORDER | SWT.HORIZONTAL);
		lblGenre.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 17, SWT.NORMAL));
		lblGenre.setBounds(10, 64, 253, 21);
		lblGenre.setText(movie.genre().toUpperCase());
		
		Label lblStreamTitle = new Label(this, SWT.NONE);
		lblStreamTitle.setAlignment(SWT.CENTER);
		lblStreamTitle.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 17, SWT.NORMAL));
		lblStreamTitle.setBounds(30, 124, 89, 21);
		lblStreamTitle.setText("Streams:");
		
		Label lblStreamNum = new Label(this, SWT.NONE);
		lblStreamNum.setAlignment(SWT.CENTER);
		lblStreamNum.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 30, SWT.NORMAL));
		lblStreamNum.setBounds(30, 162, 89, 36);
		lblStreamNum.setText(""+movie.Streams());
		
		Button btnStream = new Button(this, SWT.NONE);
		btnStream.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				account.Stream(stream, movie, all);
				lblStreamNum.setText(""+movie.Streams());
			}
		});
		btnStream.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		btnStream.setBounds(29, 216, 93, 32);
		btnStream.setText("Stream");
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 17, SWT.NORMAL));
		lblNewLabel.setBounds(345, 124, 59, 21);
		lblNewLabel.setText("Rating:");
		
		Label lblNewLabel_1 = new Label(this, SWT.NONE);
		lblNewLabel_1.setAlignment(SWT.CENTER);
		lblNewLabel_1.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 30, SWT.NORMAL));
		lblNewLabel_1.setBounds(333, 162, 89, 41);
		lblNewLabel_1.setText(movie.strScore()+"%");
		
		Button btnReview = new Button(this, SWT.NONE);
		btnReview.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ReviewPage next = new ReviewPage(display,drive,stream,account,movie,all);
				drive.changeShell(next,display);
			}
		});
		btnReview.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		btnReview.setBounds(333, 216, 94, 32);
		btnReview.setText("Review");
		
		Button btnMyProfile = new Button(this, SWT.NONE);
		btnMyProfile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Profile next = new Profile(display,drive,stream,account);
				drive.changeShell(next,display);
			}
		});
		btnMyProfile.setBounds(345, 75, 94, 28);
		btnMyProfile.setText("My Profile");
		
		Button btnHome = new Button(this, SWT.NONE);
		btnHome.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Home next = new Home(display,drive,stream,account);
				drive.changeShell(next,display);
			}
		});
		btnHome.setBounds(345, 50, 94, 28);
		btnHome.setText("Home");
		
		
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText(movie.Name());
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
