package web;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import net.Account;
import net.Streamer;

import movElem.*;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class Home extends Shell {
	private Text GENRE;
	private Text MovName_Key;
	private static Driver drive;
	private static Streamer stream;
	private static Account account;
	private boolean all;
	private ArrayList<Movie>Out;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			Home shell = new Home(display,drive,stream,account);
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
	public Home(Display display,Driver Drive,Streamer Stream,Account Account) {
		super(display, SWT.SHELL_TRIM);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		
		drive = Drive;
		stream = Stream;
		account = Account;
		
		Label lblStreamweb = new Label(this, SWT.NONE);
		lblStreamweb.setFont(SWTResourceManager.getFont("Lucida Grande", 20, SWT.NORMAL));
		lblStreamweb.setAlignment(SWT.CENTER);
		lblStreamweb.setBounds(95, 30, 263, 29);
		lblStreamweb.setText("STREAMWEB");
		
		Button btnMyProfile = new Button(this, SWT.NONE);
		btnMyProfile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Profile next = new Profile(display,drive,stream,account);
				drive.changeShell(next,display);
			}
		});
		btnMyProfile.setBounds(346, 10, 94, 28);
		btnMyProfile.setText("My Profile");
		
		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(0, 65, 450, 2);
		
		Label label_1 = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(0, 127, 450, 2);
		
		Label lblGenre = new Label(this, SWT.NONE);
		lblGenre.setAlignment(SWT.CENTER);
		lblGenre.setBounds(47, 76, 356, 20);
		lblGenre.setText("Genre (enter \"all\" to search ALL genres): ");
		
		Label label_2 = new Label(this, SWT.SEPARATOR | SWT.VERTICAL);
		label_2.setBounds(330, 127, 13, 151);
		
		Label lblOrderedSearch = new Label(this, SWT.NONE);
		lblOrderedSearch.setAlignment(SWT.CENTER);
		lblOrderedSearch.setBounds(346, 135, 94, 14);
		lblOrderedSearch.setText("Ordered Search");
		
		Spinner spinner = new Spinner(this, SWT.BORDER);
		spinner.setMinimum(1);
		spinner.setBounds(361, 168, 59, 22);
		
		Label lblListSize = new Label(this, SWT.NONE);
		lblListSize.setAlignment(SWT.CENTER);
		lblListSize.setBounds(361, 155, 59, 14);
		lblListSize.setText("List Size");
		
		Label lblSearchBy = new Label(this, SWT.NONE);
		lblSearchBy.setAlignment(SWT.CENTER);
		lblSearchBy.setBounds(343, 200, 94, 14);
		lblSearchBy.setText("Search By:");
		
		Label lblNamekeyword = new Label(this, SWT.NONE);
		lblNamekeyword.setAlignment(SWT.CENTER);
		lblNamekeyword.setBounds(10, 135, 322, 27);
		lblNamekeyword.setText("Movie Search: Enter movie name.");
		
		List list = new List(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent event) {
	        	MoviePage next = new MoviePage(display,drive,stream,account,Out.get(list.getSelectionIndex()),all);
	        	drive.changeShell(next,display);
	        }});
		list.setBounds(38, 183, 255, 85);
		
		GENRE = new Text(this, SWT.BORDER);
		GENRE.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				String gen = GENRE.getText().toLowerCase();
				if(gen.equals("all")) {
					all = true;
				}
				else {
					all = false;
				}
				list.removeAll();
				Out = stream.KeywordSearch(MovName_Key.getText(), GENRE.getText());
				if(Out!=null) {
					int i = 1;
					for(Movie m:Out) {
						list.add(i+". "+m.Name());
						i++;
					}
				}
			}
		});
		GENRE.setBounds(137, 95, 175, 19);
		
		MovName_Key = new Text(this, SWT.BORDER);
		MovName_Key.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				list.removeAll();
				Out = stream.KeywordSearch(MovName_Key.getText(), GENRE.getText());
				if(Out!=null) {
					int i = 1;
					for(Movie m:Out) {
						list.add(i+". "+m.Name());
						i++;
					}
				}
			}
		});
		MovName_Key.setBounds(38, 154, 255, 19);
		
		Button btnStreams = new Button(this, SWT.NONE);
		btnStreams.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ArrayList<Movie>out = stream.Order(GENRE.getText().toLowerCase(), spinner.getSelection(), "p");
				if(out==null) {
					InvalidGenre next = new InvalidGenre(display);
					next.open();
				}
				else {
					ListPop next = new ListPop(display,drive,stream,account,out,all);
					drive.changeShell(next,display);
				}
			}
		});
		btnStreams.setBounds(343, 215, 94, 28);
		btnStreams.setText("Streams");
		
		Button btnReviews = new Button(this, SWT.NONE);
		btnReviews.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ArrayList<Movie>out = stream.Order(GENRE.getText().toLowerCase(), spinner.getSelection(), "r");
				if(out==null) {
					InvalidGenre next = new InvalidGenre(display);
					next.open();
				}
				else {
					ListRat next = new ListRat(display,drive,stream,account,out,all);
					drive.changeShell(next,display);
				}
			}
		});
		btnReviews.setBounds(343, 240, 94, 28);
		btnReviews.setText("Reviews");
		
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Home");
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
