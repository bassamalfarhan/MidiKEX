package Algorithm;

import java.util.ArrayList;

public class Fingering implements Comparable<Fingering>{
	private int number, noteNumber, difficulty;

	private Fingering parent;
	private ArrayList<Fingering> children;

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
		if(parent != null){
			difficulty = parent.getDifficulty();
		}else{
			System.err.println("ERROR: Fingering->reParent Failed to inherit");
		}
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

	@Override
	public int compareTo(Fingering o) {
		if(o.getDifficulty() == this.difficulty){
			return 0;
		}
		if(o.getDifficulty()>this.difficulty){
			return -1;
		}
		if(o.getDifficulty()<this.difficulty){
			return 1;
		}
		return 0;
	}
}
