package Algorithm;

import java.util.ArrayList;
import java.util.Iterator;

public class NoteArray implements Iterable<Note> {
	private ArrayList<Note> sequence;

	public NoteArray() {
		sequence = new ArrayList<Note>();
	}

	public NoteArray(ArrayList<Note> al) {
		sequence = al;
	}

	public void addNote(Note note) {
		sequence.add(sequence.size(), note);
	}
	
	public void addNote(Note note, int chordIndex) {
		int index = sequence.size();
		sequence.add(index, note);
		sequence.get(index).setChordIndex(chordIndex);
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

	public int getStretchDirection(int n1, int n2) {
		if (n1 == n2) {
			return 0;
		} else if (sequence.get(n1).getValue() > sequence.get(n2).getValue()) {
			return -1;
		} else {
			return 1;
		}
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

	public NoteArray[] getStaccatoSeparated() {
		NoteArray[] na = new NoteArray[getStaccatoCount()
				+ (sequence.get(sequence.size() - 1).isStaccato() ? 0 : 1)];

		na[0] = new NoteArray();
		for (int i = 0, stc = 0; i < sequence.size(); ++i) {
			na[stc].addNote(sequence.get(i));
			if (sequence.get(i).isStaccato() && (++stc) != na.length) {
				na[stc] = new NoteArray();
			}
		}

		return na;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		boolean chord = false;
		for (int i = 0; i < sequence.size(); ++i) {
			
			if(i != 0)
				sb.append(" ");
			
			if(!chord && sequence.get(i).getChordIndex() > 0){
				sb.append("{");
				chord = true;
			}	
			
			sb.append(sequence.get(i).getName());
			
//			sb.append("("+sequence.get(i).getValue()+")");
			
			if(chord && 
				((sequence.size() != i+1 && sequence.get(i+1).getChordIndex() == 0)
				|| sequence.size() == i+1)
				){
				sb.append("}");
				chord = false;
			}
		}
		return sb.toString();
	}

	public int length() {
		return sequence.size();
	}

	@Override
	public Iterator<Note> iterator() {
		return sequence.iterator();
	}

	public int getChordIndex(int noteIndex){
		return sequence.get(noteIndex).getChordIndex();
	}
}
