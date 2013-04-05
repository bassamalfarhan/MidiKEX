package UI;

import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import Utilities.MidiReceiver;
import Utilities.MidiTransmitter;

public class CmdController {
	/*
	 * private final int RECEIVER_ID = 1, TRANSMITTER_ID = 5;
	 */

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
		String line = null;
		while (s.hasNextLine()) {
			line = s.nextLine();
			if (line.trim().equals("exit"))
				break;

			processCommand(line);
		}
		s.close();
	}

	/**
	 * Prints out a list of available devices, both receivers and transmitters.
	 */
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
	 * Sets the MIDI IN port.
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
	 * sends a note on/off command to the keyboard. Some (cheaper MIDI
	 * keyboards) use velocity based note off, that is setting the velocity to 0
	 * to indicate the note to be off, this case is supported by the velocity
	 * based parameter.
	 * 
	 * @param toggle note state indicator
	 * @param velocityBased true : use note_on command + set velocity to 0 if toggle = false
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

	/**
	 * Sets the MIDI OUT port.
	 */
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

	public static void main(String[] args) {
		new CmdController();
	}

	/**
	 * Overrides the system.out.println for future compatibility issues
	 * 
	 * @param string The string to be passed thru to system.out.println
	 */
	public void println(Object string) {
		System.out.println(string);
	}

	/**
	 * Overrides the system.out.print for future compatibility issues.
	 * 
	 * @param string The string to be passed thru to system.out.print
	 */
	public void print(Object string) {
		System.out.print(string);
	}

	/*
	 * Command processor.
	 * 
	 * @param line The string to be processed
	 */
	private void processCommand(String line) {
		/*
		 * Textual responses and function calls are handled inside the Switch
		 * statement
		 */
		switch (line) {
		case "show devices":
			printOutAvailableDevices();
			break;
		case "exit":
			println("There is no way out atm, sorry...");
			break;
		default:
			String[] divider = null;
			if (line.startsWith("set transmitter")) {
				divider = line.split(" ");
				setMidiTransmitter(Integer.parseInt(divider[divider.length - 1]
						.trim()));
			} else if (line.startsWith("set receiver")) {
				divider = line.split(" ");
				setMidiReceiver(Integer.parseInt(divider[divider.length - 1]
						.trim()));
			}
			break;
		}
	}
}
