package nl.grauw.gaia_tool;

import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class App extends Frame implements WindowListener, ActionListener {
	
	private static final long serialVersionUID = 4950285236879118899L;
	
	Gaia gaia;
	MenuItem playItem;
	MenuItem gm1SystemOn;
	MenuItem gm2SystemOn;
	MenuItem gmSystemOff;
	TextArea log;
	
	public App() throws MidiUnavailableException, InvalidMidiDataException {
		super();
		
		gaia = new Gaia();
		gaia.open();
		gaia.requestIdentity();
		
		// initialise UI
		addWindowListener(this);
		setTitle("Roland GAIA SH-01 tool — by Grauw");
		setSize(500, 400);
		MenuBar mb = new MenuBar();
		Menu testMenu = new Menu("Test");
		playItem = new MenuItem("Play test notes");
		playItem.addActionListener(this);
		testMenu.add(playItem);
		gm1SystemOn = new MenuItem("GM system on");
		gm1SystemOn.addActionListener(this);
		testMenu.add(gm1SystemOn);
		gm2SystemOn = new MenuItem("GM2 system on");
		gm2SystemOn.addActionListener(this);
		testMenu.add(gm2SystemOn);
		gmSystemOff = new MenuItem("GM system off");
		gmSystemOff.addActionListener(this);
		testMenu.add(gmSystemOff);
		mb.add(testMenu);
		setMenuBar(mb);
		log = new TextArea();
		add(log);

		log.append("This is the log. Use the menu to trigger stuff.\n");
	}

	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException {
		App app = new App();
		app.setVisible(true);
	}
	
	public void windowClosing(WindowEvent e) {
		try {
			gaia.close();
		} catch (MidiUnavailableException e1) {
			e1.printStackTrace();
		}
		dispose();
	}

	// unused window events
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == playItem) {
			try {
				gaia.playTestNote();
				gaia.playGMTestNote();
				gaia.playGM2TestNote();
			} catch (InvalidMidiDataException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == gm1SystemOn) {
			try {
				gaia.sendGM1SystemOn();
			} catch (InvalidMidiDataException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == gm2SystemOn) {
			try {
				gaia.sendGM2SystemOn();
			} catch (InvalidMidiDataException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == gmSystemOff) {
			try {
				gaia.sendGMSystemOff();
			} catch (InvalidMidiDataException e1) {
				e1.printStackTrace();
			}
		}
	}
	
}
