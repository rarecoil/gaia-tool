package nl.grauw.gaia_tool.views;

import java.awt.Component;
import java.util.Vector;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JOptionPane;

import nl.grauw.gaia_tool.Gaia;

public class MIDIDeviceSelector {
	
	Gaia gaia;
	Component parent;
	
	public MIDIDeviceSelector(Gaia gaia, Component parent) {
		this.gaia = gaia;
		this.parent = parent;
	}
	
	public void show() {
		MidiDevice input = selectMIDIInputDevice();
		MidiDevice output = selectMIDIOutputDevice();
		
		if (gaia.isOpened())
			gaia.close();
		
		try {
			gaia.setMIDIDevices(input, output);
			if (gaia.canOpen())
				gaia.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	private MidiDevice selectMIDIInputDevice() {
		Vector<Object> inputDevices = new Vector<Object>();
		MidiDevice.Info[] devicesInfo = MidiSystem.getMidiDeviceInfo();
		for (MidiDevice.Info mdi : devicesInfo) {
			try {
				MidiDevice md = MidiSystem.getMidiDevice(mdi);
				if (md.getMaxTransmitters() != 0)
					inputDevices.add(mdi);
			} catch (MidiUnavailableException e) {
			}
		}
		
		Object selection = JOptionPane.showInputDialog(parent, "Please select a MIDI input device",
				"Select MIDI input device", JOptionPane.QUESTION_MESSAGE, null, inputDevices.toArray(),
				gaia.getMidiInput() != null ? gaia.getMidiInput().getDeviceInfo() : null);
		
		if (selection instanceof MidiDevice.Info) {
			try {
				return MidiSystem.getMidiDevice((MidiDevice.Info) selection);
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		}
		return gaia.getMidiInput();
	}
	
	private MidiDevice selectMIDIOutputDevice() {
		Vector<Object> outputDevices = new Vector<Object>();
		MidiDevice.Info[] devicesInfo = MidiSystem.getMidiDeviceInfo();
		for (MidiDevice.Info mdi : devicesInfo) {
			try {
				MidiDevice md = MidiSystem.getMidiDevice(mdi);
				if (md.getMaxReceivers() != 0)
					outputDevices.add(mdi);
			} catch (MidiUnavailableException e) {
			}
		}
		
		Object selection = JOptionPane.showInputDialog(parent, "Please select a MIDI output device",
				"Select MIDI output device", JOptionPane.QUESTION_MESSAGE, null, outputDevices.toArray(),
				gaia.getMidiOutput() != null ? gaia.getMidiOutput().getDeviceInfo() : null);
		
		if (selection instanceof MidiDevice.Info) {
			try {
				return MidiSystem.getMidiDevice((MidiDevice.Info) selection);
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		}
		return gaia.getMidiOutput();
	}
	
}
