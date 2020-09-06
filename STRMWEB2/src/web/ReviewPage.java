package web;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.wb.swt.SWTResourceManager;

import movElem.Movie;
import net.Account;
import net.Streamer;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

public class ReviewPage extends Shell {
	
	private static Driver drive;
	private static Streamer stream;
	private static Account account;
	private static Movie movie;
	private static boolean all;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			ReviewPage shell = new ReviewPage(display,drive,stream,account,movie,all);
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
	public ReviewPage(Display display,Driver Drive, Streamer Stream,Account Account,Movie Movie,boolean All) {
		super(display, SWT.SHELL_TRIM);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		
		drive = Drive;
		stream = Stream;
		account = Account;
		movie = Movie;
		all = All;
		
		Label lblScale = new Label(this, SWT.NONE);
		lblScale.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 20, SWT.NORMAL));
		lblScale.setAlignment(SWT.CENTER);
		lblScale.setBounds(196, 104, 59, 27);
		lblScale.setText("0");
		
		Scale scale = new Scale(this, SWT.NONE);
		scale.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblScale.setText(""+scale.getSelection());
			}
		});
		scale.setBounds(107, 137, 237, 15);
		
		Button btnReview = new Button(this, SWT.NONE);
		btnReview.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				stream.Critique(movie,scale.getSelection(),all);
				MoviePage back = new MoviePage(display,drive,stream,account,movie,all);
				ThankYou next = new ThankYou(display);
				drive.changeShell(back,display);
				next.open();
			}
		});
		btnReview.setBounds(155, 183, 140, 28);
		btnReview.setText("Submit Review");
		setDefaultButton(btnReview);
		
		Button btnBack = new Button(this, SWT.NONE);
		btnBack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MoviePage back = new MoviePage(display,drive,stream,account,movie,all);
				drive.changeShell(back,display);
			}
		});
		btnBack.setBounds(22, 10, 94, 28);
		btnBack.setText("Back");
		
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
		
		Label lblInstruct = new Label(this, SWT.NONE);
		lblInstruct.setAlignment(SWT.CENTER);
		lblInstruct.setBounds(0, 60, 450, 27);
		lblInstruct.setText("Please critique " + movie.Name() + ".");
		
		Button btnHome = new Button(this, SWT.NONE);
		btnHome.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Home next = new Home(display,drive,stream,account);
				drive.changeShell(next,display);
			}
		});
		btnHome.setBounds(22, 32, 94, 28);
		btnHome.setText("Home");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Review Page");
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
