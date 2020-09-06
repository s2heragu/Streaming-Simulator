package web;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import movElem.*;
import net.*;

public class Driver {

	protected Shell shell;
	private Streamer stream;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Driver window = new Driver();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public void changeShell(Shell Shell,Display display) {
		shell.dispose();
		shell = Shell;
		shell.open();
		shell.layout();
		shell.addListener(SWT.Close, new Listener() {
		      public void handleEvent(Event event){
		    	  String movs = stream.moviesTXT();
					ArrayList<ArrayList<String>>accTXTs = stream.ACCStxt();
					if(accTXTs==null) {
						File file = new File("STREAMWEB.txt");
						FileWriter fw = null;
						try {
							fw = new FileWriter(file);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						PrintWriter pw = new PrintWriter(fw);
						pw.println(movs);
						pw.print("0");
						pw.close();
					}
					else {
						ArrayList<String>names = accTXTs.get(0);
						ArrayList<String>txts = accTXTs.get(1);
						for(int j = 0;j<names.size();j++) {
							File file = new File(names.get(j)+".txt");
							FileWriter fw = null;
							try {
								fw = new FileWriter(file);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							PrintWriter pw = new PrintWriter(fw);
							pw.print(txts.get(j));
							pw.close();
						}
						int accSize = names.size();
						File file1 = new File("STREAMWEB.txt");
						FileWriter fw1 = null;
						try {
							fw1 = new FileWriter(file1);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						PrintWriter pw1 = new PrintWriter(fw1);
						pw1.println(movs);
						pw1.println(accSize);
						for(int i = 0; i<accSize;i++) {
							pw1.println(names.get(i)+".txt");
						}
						pw1.close();
					}
		      }
		    });
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(){
		try {
			stream = new Streamer("STREAMWEB.txt");
			Display display = Display.getDefault();
			shell = new Login(display,stream,this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
