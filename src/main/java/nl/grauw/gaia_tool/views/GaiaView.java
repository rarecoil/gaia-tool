package nl.grauw.gaia_tool.views;

import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Observable;
import java.util.Observer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import nl.grauw.gaia_tool.Gaia;
import nl.grauw.gaia_tool.Log;

public class GaiaView extends Frame implements WindowListener, ActionListener, Observer {
	
	private static final long serialVersionUID = 4950285236879118899L;
	
	private Gaia gaia;
	
	private MenuItem playItem;
	private MenuItem gm1SystemOn;
	private MenuItem gm2SystemOn;
	private MenuItem gmSystemOff;
	private MenuItem systemDataRequest;
	private MenuItem patchDataRequest;
	private TextArea log;
	
	public GaiaView(Gaia g) {
		// assign model
		gaia = g;
		
		// initialise UI
		addWindowListener(this);
		setTitle("Roland GAIA SH-01 tool — by Grauw");
		setSize(800, 700);
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
		
		Menu dataMenu = new Menu("Data request");
		systemDataRequest = new MenuItem("System data");
		systemDataRequest.addActionListener(this);
		dataMenu.add(systemDataRequest);
		patchDataRequest = new MenuItem("Temporary patch data");
		patchDataRequest.addActionListener(this);
		dataMenu.add(patchDataRequest);
		mb.add(dataMenu);
		
		setMenuBar(mb);
		log = new TextArea("", 24, 80, TextArea.SCROLLBARS_VERTICAL_ONLY);
		add(log);
		
		// observe models
		gaia.getLog().addObserver(this);
		update(gaia.getLog(), null);
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
		try {
			if (e.getSource() == playItem) {
				gaia.playTestNote();
				gaia.playGMTestNote();
				gaia.playGM2TestNote();
			} else if (e.getSource() == gm1SystemOn) {
				gaia.sendGM1SystemOn();
			} else if (e.getSource() == gm2SystemOn) {
				gaia.sendGM2SystemOn();
			} else if (e.getSource() == gmSystemOff) {
				gaia.sendGMSystemOff();
			} else if (e.getSource() == systemDataRequest) {
				gaia.sendSystemDataRequest();
			} else if (e.getSource() == patchDataRequest) {
				gaia.sendPatchDataRequest();
			}
		} catch (InvalidMidiDataException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Log) {
			update((Log) o, arg);
		}
	}
	
	public void update(Log l, Object arg) {
		log.append(l.getLog().substring(log.getText().length()));
	}
	
}
