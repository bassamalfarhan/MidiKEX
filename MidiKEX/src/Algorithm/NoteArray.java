package Algorithm;

import java.util.ArrayList;

public class NoteArray {
	private ArrayList<Note> sequence;

	public NoteArray() {
		sequence = new ArrayList<Note>();
	}

	public void addNote(Note note) {
		sequence.add(sequence.size(), note);
	}

	/**
	 * Calculates and returns the number of semitones between two notes in the
	 * sequence. The numbering of the notes are zero based!
	 */
	public int getStretch(int n1, int n2) {
		if (n1 == n2) {
			return 0;
		} else if (n1 > n2) {
			int temp = n2;
			n2 = n1;
			n1 = temp;
		}
		return sequence.get(n2).getValue() - sequence.get(n1).getValue();
	}

	public int getBlackCount() {
		int count = 0;
		for (Note note : sequence)
			if (note.isBlack())
				++count;
		return count;
	}

	public int getWhiteCount() {
		return length() - getBlackCount();
	}

	public int getStaccatoCount() {
		int count = 0;
		for (Note note : sequence)
			if (note.isStaccato())
				++count;
		return count;
	}

	// HOTLINKS TO THE NOTE METHODS
	public boolean isStaccato(int n1) {
		return sequence.get(n1).isStaccato();
	}

	public boolean isBlack(int n1) {
		return sequence.get(n1).isBlack();
	}

	// TODO implement a toString method for a sequence...
	// @Override
	// public String toString(){
	// return "TODO";
	// }

	public int length() {
		return sequence.size();
	}

}
