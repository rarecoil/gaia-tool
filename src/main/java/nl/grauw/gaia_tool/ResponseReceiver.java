package nl.grauw.gaia_tool;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class ResponseReceiver implements Receiver {

	@Override
	public void send(MidiMessage message, long timeStamp) {
		System.out.println("Message with length " + message.getLength() + " received at " + timeStamp + ".");
		System.out.println("- Status code: " + String.format("0x%1$02X", message.getStatus()));
		System.out.print("- Message body: ");
		byte[] message_bytes = message.getMessage();
		for (byte message_byte : message_bytes) {
			System.out.print(String.format("0x%1$02X", message_byte & 0xFF) + " ");
		}
		System.out.println();
	}

	@Override
	public void close() {
	}

}
