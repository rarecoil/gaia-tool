package nl.grauw.gaia.tool.messages;

public class Sysex extends Message {
	
	public static final int SYSTEM_EXCLUSIVE = 0xF0;
	public static final int SPECIAL_SYSTEM_EXCLUSIVE = 0xF7;
	public static final int END_OF_EXCLUSIVE = 0xF7;
	
	public Sysex(byte[] message) {
		super(message);
	}
	
	public Sysex(int status, byte[] data) {
		super(createMessage(status, data));
		
		if (status != SYSTEM_EXCLUSIVE && status != SPECIAL_SYSTEM_EXCLUSIVE)
			throw new IllegalArgumentException("Invalid status code.");
	}
	
	private static byte[] createMessage(int status, byte[] data) {
		byte[] message = new byte[data.length + 1];
		message[0] = (byte)status;
		System.arraycopy(data, 0, message, 1, data.length);
		return message;
	}
	
}
