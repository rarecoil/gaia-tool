package nl.grauw.gaia_tool.messages;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

public class IdentityReply extends SysexMessage {
	
	final static int GENERAL_INFORMATION = 0x06;
	final static int IDENTITY_REPLY = 0x02;
	
	public IdentityReply(SysexMessage sem) throws InvalidMidiDataException {
		super();
		byte[] data = sem.getData();
		setMessage(sem.getStatus(), data, data.length);
	}
	
	public int getDeviceId() {
		return getData()[1];
	}
	
	public int getIdNumber() {
		return getData()[4];
	}
	
	public int[] getDeviceFamilyCode() {
		int[] rv = {getData()[5], getData()[6]};
		return rv;
	}
	
	public int[] getDeviceFamilyNumberCode() {
		int[] rv = {getData()[7], getData()[8]};
		return rv;
	}
	
	public int[] getSoftwareRevisionLevel() {
		int[] rv = {getData()[9], getData()[10], getData()[11], getData()[12]};
		return rv;
	}
	
	public String toString() {
		int[] dfc = getDeviceFamilyCode();
		int[] dfnc = getDeviceFamilyNumberCode();
		int[] srl = getSoftwareRevisionLevel();
		
		return "Identity reply message. " +
				String.format("Device ID: %02XH. ", getDeviceId()) +
				String.format("ID number: %02XH. ",getIdNumber()) +
				String.format("Device family code: %02XH %02XH. ", dfc[0], dfc[1]) +
				String.format("Device family number code: %02XH %02XH. ", dfnc[0], dfnc[1]) +
				String.format("Software revision level: %d.%d.%d.%d.", srl[0], srl[1], srl[2], srl[3]);
	}
	
}
