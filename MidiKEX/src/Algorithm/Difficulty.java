package Algorithm;

import java.util.ArrayList;

import javax.sound.midi.Sequence;

import Utilities.FileManager;
import Algorithm.HandSpan.Type;

public class Difficulty {
	private HandSpan handspan = new HandSpan(
			FileManager.loadHandSpan("handspans"));
	private boolean[][] mBound = FileManager.loadBoundaries("boundaries");

	private Fingering fingering; // TODO, cleanup logic (not essential atm).
	private NoteArray seq;

	private ArrayList<Fingering> endPaths = new ArrayList<Fingering>();

	// The difficulty for all the allowed spans and fingers generated with the
	// help of the first three rules
	private int[][] iDifficulty = new int[10][21];

	/**
	 * Helper method that increases the stretch by 5 to get it within the bounds
	 * of the array
	 * 
	 * @param strech The stretch
	 * @return The stretch+5
	 */
	public int getStrechIndex(int strech) {
		if (strech < -5 || strech > 14)
			throw new IndexOutOfBoundsException(
					"Expected: [-5; 14], Received: " + strech);
		return strech + 5;
	}

	private void incrementDifficulty(int strechIndex, int fingerIndex, int value) {
		iDifficulty[fingerIndex][strechIndex] += value;
	}

	private boolean isBoundy(int stretchIndex, int fingerIndex) {
		// if(stretchIndex < 4)
		// System.err.println("+1");
		return mBound[fingerIndex][stretchIndex];
	}

	/**
	 * This private method handles the first the rules (STRECH RULE, SMALL &
	 * LARGE SPAN RULE)
	 */
	private void preReqRule() {
		int strechStart = -5;

		for (int i = 0; i < iDifficulty.length; ++i) {
			for (int j = 0; j < iDifficulty[i].length; ++j) {
				int strech = strechStart + j;

				/*
				 * STRECH RULE
				 * 
				 * Assign 2 points for each semitone that an interval exceeds
				 * MaxComf or is less than MinComf.
				 */
				if (handspan.getSpan(Type.MinComf, i) > strech) {
					incrementDifficulty(j, i,
							2 * (handspan.getSpan(Type.MinComf, i) - strech));
				} else if (handspan.getSpan(Type.MaxComf, i) < strech) {
					incrementDifficulty(j, i,
							2 * (strech - handspan.getSpan(Type.MaxComf, i)));
				}

				/*
				 * SMALL & LARGE SPAN RULE PART 1/2
				 * 
				 * For finger pairs including the thumb, assign 1 point for each
				 * semitone that an interval is less than MinRel/MaxRel.
				 */
				if (i < 4) {
					if (handspan.getSpan(Type.MinRel, i) > strech) {
						incrementDifficulty(j, i,
								handspan.getSpan(Type.MinRel, i) - strech);
					} else if (handspan.getSpan(Type.MaxRel, i) < strech) {
						incrementDifficulty(j, i,
								strech - handspan.getSpan(Type.MaxRel, i));
					}
				}
				/*
				 * SMALL & LARGE SPAN RULE PART 2/2
				 * 
				 * For finger pairs not including the thumb, assign 2 points per
				 * semitone.
				 */
				else {
					if (handspan.getSpan(Type.MinRel, i) > strech) {
						incrementDifficulty(j, i,
								2 * (handspan.getSpan(Type.MinRel, i) - strech));
					} else if (handspan.getSpan(Type.MaxRel, i) < strech) {
						incrementDifficulty(j, i,
								2 * (strech - handspan.getSpan(Type.MaxRel, i)));
					}
				}
			}
		}
	}

	private void recursivengine(Fingering fing, int currentPos) {
		if (currentPos == seq.length()) {
			endPaths.add(fing);
		} else {
			Fingering bruceLee = null;
			for (int j = 1; j <= 5; ++j) {
				if (fing.getNumber() != j) { // TODO RESOLVE REPEATING NOTES
					int fIndex = handspan.getArrayPosition(fing.getNumber(), j);

					int sIndex = seq.getStretch(fing.getNoteNumber(), currentPos);
			
					//I've messed up the navigation routines, however, this if-else statement should solve the issue...
					//TODO clean up the mess and remove this if-else statement...
					try{
						if(((fing.getNumber() < j) && (seq.getStretchDirection(fing.getNoteNumber(), currentPos) < 0)) || 
								((fing.getNumber() > j) &&(seq.getStretchDirection(fing.getNoteNumber(), currentPos) > 0))){
							sIndex = getStrechIndex((sIndex>0?-1:1)*sIndex);
						}else{
							sIndex = getStrechIndex((sIndex<0?-1:1)*sIndex);
						}
					}catch(IndexOutOfBoundsException e){
						continue;
					}
					/*
					 * This is a cryptic description of the mess above!
					 * 
					(fing.getNumber() < j) //1 2
					(seq.getStretchDirection(fing.getNoteNumber(), currentPos) > 0) //1 2
					
					(fing.getNumber() > j) //2 1
					(seq.getStretchDirection(fing.getNoteNumber(), currentPos) < 0) //2 1 
 					*/
					
//					System.out.print(fing.getNumber() + " -> " + j + " ");

					if (isBoundy(sIndex, fIndex)) {
//						System.out.println("PASS");
						bruceLee = new Fingering(j, currentPos);
						fing.addChild(bruceLee);
						bruceLee.increaseDifficulty(iDifficulty[fIndex][sIndex]);
						/* *******************************************************/
						/* START OF RULE IMPLEMENTATION **************************/
						/* *******************************************************/
						
						/*
						 * 6. Weak-Finger Rule: 
						 * Assign 1 point every time finger 4 or finger 5 is used.
						 */
						if(j == 4 || j == 5){
							bruceLee.increaseDifficulty(1);
						}
						
						/*
						 * 7. Three-Four-Five Rule: 
						 * Assign 1 point every time fingers 3, 4, and 5 occur 
						 * consecutively in any order, even when groups overlap.
						 */
						if (currentPos > 1 && (j == 3 || j == 4 || j == 5)) {
							int rule7counter = j == 3?1:
												j == 4?2:
													4;
							
							rule7counter += fing.getNumber() == 3?1:
												fing.getNumber() == 4?2:
													fing.getNumber() == 5?4:
														0;
							
							rule7counter += fing.getParent().getNumber() == 3?1:
												fing.getParent().getNumber() == 4?2:
													fing.getParent().getNumber() == 5?4:
														0;
																		
							if(rule7counter == 7){
								bruceLee.increaseDifficulty(1);
							}
						}
						
						/* 
						 * 8. Three-to-Four Rule: 
						 * Assign 1 point each time finger 3 is immediately 
						 * followed by finger 4. 
						 */
						if(j == 4 && fing.getNumber() == 3){
							bruceLee.increaseDifficulty(1);
						}
						
						/*
						 * 9. Four-on-Black Rule: 
						 * Assign 1 point each time fingers 3 and 4 occur 
						 * consecutively in any order with 3 on white and 4 on black.
						 */
						if((j == 3 && !seq.isBlack(currentPos) && fing.getNumber() == 4 && seq.isBlack(fing.getNoteNumber()))
						|| (j == 4 && seq.isBlack(currentPos) && fing.getNumber() == 3 && !seq.isBlack(fing.getNoteNumber()))){
							bruceLee.increaseDifficulty(1);
						}
						
						/*
						 * 10. Thumb-on-Black Rule: 
						 * Assign 1 point whenever the thumb plays a black key. 
						 * 
						 * If the immediately preceding note is white, assign a further 2 points. 
						 * If the immediately following note is white, assign a further 2 points.
						 */
						if(j == 1 && seq.isBlack(currentPos)){
							if(!seq.isBlack(fing.getNoteNumber())){
								bruceLee.increaseDifficulty(3);
							}else{
								bruceLee.increaseDifficulty(1);
							}
						} /* PART TWO ALONG WITH RULE 11 */
						
						/*
						 * 11. Five-on-Black Rule: 
						 * If the fifth finger plays a black key and the immediately preceding 
						 * and following notes are also black, assign 0 points.
						 *  
						 * If the immediately preceding note is white, assign 2 points. 
						 * If the immediately following key is white, assign 2 further points.
						 */
						if(j == 5 && seq.isBlack(currentPos) && !seq.isBlack(fing.getNoteNumber())){
							bruceLee.increaseDifficulty(2);
						}
						
						if(!seq.isBlack(currentPos) && seq.isBlack(fing.getNoteNumber()) && (fing.getNoteNumber() == 1 || fing.getNoteNumber() == 5)){
							bruceLee.increaseDifficulty(2);
						}
						
						/*
						 * 12. Thumb-Passing Rule: 
						 * Assign 1 point for each thumb- or fingerpass on the same level (from white to white or black to black). 
						 * Assign 3 points if the lower note is white, played by a finger other than the thumb, and the upper is black, played by the thumb.
						 TODO */
						
						/* *******************************************************/
						/* END OF RULE IMPLEMENTATION ****************************/
						/* *******************************************************/
						recursivengine(bruceLee, currentPos + 1);
					} 
//					else {
//						System.out.println("FAIL");
//					}
				}
			}
		}
	}

	public Fingering generateFingering() {
		Fingering fing = new Fingering(0, -1);

		Fingering bruceLee = null;
		for (int j = 1; j <= 5; ++j) {
			bruceLee = new Fingering(j, 0);
			fing.addChild(bruceLee);

			recursivengine(bruceLee, 1);
		}

		return fing;
	}


	/**********************************************************************
	 ** QUALITY CONTROL CRAP
	 **********************************************************************/
	private void testData() {
		seq = new NoteArray();

		//First 17 notes of Für Elise
		int notes[] = { 16, 15, 16, 15, 16, 11, 14, 12, 9, 0, 4, 9, 11, 4, 8, 11, 12};

		Note note = null;
		for (int i = 0; i < notes.length; ++i) {
			note = new Note(notes[i] + 12*4);
			seq.addNote(note);
		}

		fingering = generateFingering();

		System.out.print("Sequence: ");
		for (Note n : seq) {
			System.out.print(n.getName() + " ");
		}
		System.out.println();

		Fingering best = endPaths.get(0);
		Fingering [] fff = new Fingering[seq.length()];
		for (Fingering f : endPaths) {
			if(f.getDifficulty() <= best.getDifficulty())
				best = f;
			
//			for (int i = seq.length(); i > 0; --i) {
//				fff[i-1] = f;
//				f = f.getParent();
//			}
//			
//			for (int i = 0; i < seq.length(); ++i) {
//				System.out.print(fff[i].getNumber() + " (" + fff[i].getNoteNumber() + ") ");
//			}
//			System.out.println();
		}
		
		System.out.print("BEST: ");
		for (int i = seq.length(); i > 0; --i) {
			fff[i-1] = best;
			best = best.getParent();
		}
		for (int i = 0; i < seq.length(); ++i) {
			System.out.print(fff[i].getNumber() + " (" + fff[i].getNoteNumber() + ") ");
		}
		System.out.println();

		/* DEBUG */System.out.println("endPath Size: "+endPaths.size());
	}

	
	public Difficulty() {
		preReqRule();
		for (int i = 0; i < iDifficulty.length; ++i) {
			for (int j = 0; j < iDifficulty[i].length; ++j) {
				if (mBound[i][j]) {
					System.out.print(iDifficulty[i][j] + "\t");
				} else {
					System.out.print("\t");
				}
			}
			System.out.println();
		}

		long time = System.currentTimeMillis();
		testData();
		System.out.println("\nTime: " + (System.currentTimeMillis() - time)
				+ " ms");
	}

	public static void main(String args[]) {
		new Difficulty();
	}
}