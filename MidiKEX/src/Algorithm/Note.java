package Algorithm;

public class Note {
	private int value;
	private boolean staccato, black;
	private int chordIndex;

	/**
	 * Names of the notes listed in the order they occur in.
	 */
	public static final String[] noteNames = { "C", "C#", "D", "D#", "E", "F",
			"F#", "G", "G#", "A", "A#", "B" };

	public static final boolean[] blackIndex = { false, true, false, true,
			false, false, true, false, true, false, true, false };

	public Note(int value) {
		this.value = value;
		setBlackByValue(value);
	}

	public Note(int value, boolean staccato) {
		this.value = value;
		setBlackByValue(value);
		this.staccato = staccato;
	}

	public Note(int value, boolean staccato, boolean black) {
		this.value = value;
		this.staccato = staccato;
		this.black = black;
	}

	public Note(String note) {
		if (note.charAt(note.length() - 1) == '.')
			staccato = true;

		int value = 0;
		if (note.charAt(1) == '#') {
			value = getNameIndex(note.substring(0, 2));
			value += 12 * ((((int) note.charAt(2)) - 48) + 1);
			black = true;
		} else {
			value = getNameIndex(note.substring(0, 1));
			value += 12 * ((((int) note.charAt(1)) - 48) + 1);
		}

		this.value = value;
	}

	private int getNameIndex(String noteName) {
		int pos = -1;

		for (int i = 0; i < noteNames.length; ++i) {
			if (noteNames[i].equals(noteName)) {
				pos = i;
				break;
			}
		}

		return pos;
	}

	public boolean isStaccato() {
		return staccato;
	}

	private void setBlackByValue(int value) {
		black = blackIndex[value % 12];
	}

	public boolean isBlack() {
		return black;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return noteNames[value % 12] + ((value / 12) - 1)
				+ (staccato ? "." : "") + " (" + value + ")";
	}

	/**
	 * Class method in case any outsider wants to know the name of a note.
	 * 
	 * @param note
	 *            Integer representing the note.
	 * @return String textual representation of the specified note.
	 */
	public static String getNoteName(int note) {
		return noteNames[note % 12] + ((note / 12) - 1);
	}

	public int getChordIndex() {
		return chordIndex;
	}

	public void setChordIndex(int chordIndex) {
		this.chordIndex = chordIndex;
	}
}
