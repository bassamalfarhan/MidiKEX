import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

public class CmdController {
	/*
	 * private final int RECEIVER_ID = 1, TRANSMITTER_ID = 5;
	 */

	/**
	 * Names of the notes listed in the order they occur in.
	 */
	private final String[] noteNames = { "C", "C#", "D", "D#", "E", "F", "F#",
			"G", "G#", "A", "A#", "B" };

	/**
	 * A toggle to determine a state, it is either on or off
	 */
	private enum Toggle {
		ON(true), OFF(false);

		private final boolean state;

		private Toggle(boolean state) {
			this.state = state;
		}

		/**
		 * @return true if the state is off
		 */
		public boolean isOff() {
			if (!state)
				return true;
			else
				return false;
		}

		/**
		 * @return true if the state is on
		 */
		public boolean isOn() {
			if (state)
				return true;
			else
				return false;
		}
	}

	private MidiTransmitter transmitter;
	private MidiReceiver receiver;

	public CmdController() {
		// setMidiReceiver();
		// setMidiTransmitter();

		// long stopTime = System.currentTimeMillis()+1000;
		// playNote(53, toggle.ON, false);
		// while(stopTime > System.currentTimeMillis()){}
		// playNote(53, toggle.OFF, false);

		Scanner s = new Scanner(System.in);
		while (s.hasNextLine()) {
			processCommand(s.nextLine());
		}
	}

	private void printOutAvailableDevices() {
		int id = 0;
		println("ID\tDEVICE NAME");
		println("------------------------------------");
		for (MidiDevice.Info device : MidiSystem.getMidiDeviceInfo()) {
			println((id++) + "\t" + device);
		}
		println("------------------------------------");
		println("");
	}

	/**
	 * S
	 */
	public void setMidiReceiver(int id) {
		MidiDevice device;
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

		try {
			device = MidiSystem.getMidiDevice(infos[id]);
			print("RECEIVER Starting " + infos[id] + "... ");

			device.getTransmitter().setReceiver(
					new MidiReceiver(device.getDeviceInfo().toString(), this));

			device.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param toggle
	 *            true : note on; false : note off
	 * @param velocityBased
	 *            true : use note_on command + set velocity to 0 if toggle =
	 *            false
	 */
	public void playNote(int note, Toggle toggle, boolean velocityBased) {
		int noteOnOffVelocity = 64;
		int noteOnOffCommand = -112;

		if (toggle.isOff()) {
			if (velocityBased)
				noteOnOffVelocity = 0;
			else
				noteOnOffCommand = -128;
		}

		try {
			transmitter.send(new ShortMessage(noteOnOffCommand, note,
					noteOnOffVelocity), -1);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	public void setMidiTransmitter(int id) {
		MidiDevice device;
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

		try {
			device = MidiSystem.getMidiDevice(infos[id]);

			print("TRANSMITTER Starting " + infos[id] + "... ");

			transmitter = new MidiTransmitter(infos[id].toString(), this);
			transmitter.setReceiver(device.getReceiver());

			device.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param note Integer representing the note
	 * @return String repres
	 */
	public String getNoteName(int note) {
		return noteNames[note % 12] + ((note / 12) - 1);
	}

	public static void main(String[] args) {
		new CmdController();
	}

	public void println(Object string) {
		System.out.println(string);
	}

	public void print(Object string) {
		System.out.print(string);
	}

	private void processCommand(String line) {
		switch (line) {
		case "show devices":
			printOutAvailableDevices();
			break;
		case "exit":
			println("Bl‰‰‰‰‰‰h, no exit maaaaan....");
			break;
		}

		String[] divider = null;
		if (line.startsWith("set transmitter")) {
			divider = line.split(" ");
			setMidiTransmitter(Integer.parseInt(divider[divider.length - 1]
					.trim()));
		}
	}
}
