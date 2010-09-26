package nl.grauw.gaia_tool.messages;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

public class IdentityReply extends SysexMessage {

	public IdentityReply(SysexMessage sem) throws InvalidMidiDataException {
		super();
		byte[] data = sem.getData();
		setMessage(sem.getStatus(), data, data.length);
	}
	
	public int getDeviceId() {
		return (int)(getData()[1] & 0xFF);
	}
	
	public int getIdNumber() {
		return (int)(getData()[4] & 0xFF);
	}
	
	public int[] getDeviceFamilyCode() {
		int[] rv = {
				(int)(getData()[5] & 0xFF),
				(int)(getData()[6] & 0xFF)
			};
		return rv;
	}
	
	public int[] getDeviceFamilyNumberCode() {
		int[] rv = {
				(int)(getData()[7] & 0xFF),
				(int)(getData()[8] & 0xFF)
			};
		return rv;
	}
	
	public int[] getSoftwareRevisionLevel() {
		int[] rv = {
				(int)(getData()[9] & 0xFF),
				(int)(getData()[10] & 0xFF),
				(int)(getData()[11] & 0xFF),
				(int)(getData()[12] & 0xFF)
			};
		return rv;
	}
	
	protected static String toHex(int number) {
		return String.format("%1$02XH", number);
	}
	
	public String toString() {
		String s = "Identity reply message. Device ID: " + toHex(getDeviceId());
		s += ". ID number: " + toHex(getIdNumber());
		int[] dfc = getDeviceFamilyCode();
		s += ". Device family code: " + toHex(dfc[0]) + " " + toHex(dfc[1]);
		int[] dfnc = getDeviceFamilyNumberCode();
		s += ". Device family number code: " + toHex(dfnc[0]) + " " + toHex(dfnc[1]);
		int[] srl = getSoftwareRevisionLevel();
		s += ". Software revision level: " + srl[0] + "." + srl[1] + "." + srl[2] + "." + srl[3] + ".";
		return s;
	}
	
}
