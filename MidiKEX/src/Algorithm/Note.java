package Algorithm;

public class Note {
	private int value;
	private boolean staccato, black;

	/**
	 * Names of the notes listed in the order they occur in.
	 */
	public static final String[] noteNames = { "C", "C#", "D", "D#", "E", "F",
			"F#", "G", "G#", "A", "A#", "B" };

	public Note(int value) {
		this.value = value;
	}

	public Note(int value, boolean staccato) {
		this.value = value;
		this.staccato = staccato;
	}

	public Note(int value, boolean staccato, boolean black) {
		this.value = value;
		this.staccato = staccato;
		this.black = black;
	}

	public boolean isStaccato() {
		return staccato;
	}

	public boolean isBlack() {
		return black;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return noteNames[value % 12] + ((value / 12) - 1);
	}

	/**
	 * Class method in case any outsider wants to know the name of a note.
	 * 
	 * @param note Integer representing the note.
	 * @return String textual representation of the specified note.
	 */
	public static String getNoteName(int note) {
		return noteNames[note % 12] + ((note / 12) - 1);
	}
}
