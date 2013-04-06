package Algorithm;

import java.util.ArrayList;

public class Fingering {
	private int number, noteNumber, difficulty;

	Fingering parent;
	ArrayList<Fingering> children;

	public Fingering(int number, int noteNumber) {
		this.number = number;
		this.noteNumber = noteNumber;
		this.difficulty = 0;
	}

	public void addChild(Fingering child) {
		if(children == null){
			children = new ArrayList<Fingering>();
		}
		child.reParent(this);
		children.add(child);
	}

	public void reParent(Fingering parent) {
		this.parent = parent;
		if(parent != null)
			difficulty = parent.getDifficulty();
	}

	public void increaseDifficulty(int difficulty) {
		this.difficulty += difficulty;
	}

	
	public int getNumber() {
		return number;
	}

	public int getNoteNumber() {
		return noteNumber;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public Fingering getParent() {
		return this.parent;
	}
}
