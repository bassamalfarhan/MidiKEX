package Algorithm;

import java.util.ArrayList;
import java.util.Collections;

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
	 * @param strech
	 *            The stretch
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

					int sIndex = seq.getStretch(fing.getNoteNumber(),
							currentPos);

					// I've messed up the navigation routines, however, this
					// if-else statement should solve the issue...
					// TODO clean up the mess and remove this if-else
					// statement...
					try {
						if (((fing.getNumber() < j) && (seq
								.getStretchDirection(fing.getNoteNumber(),
										currentPos) < 0))
								|| ((fing.getNumber() > j) && (seq
										.getStretchDirection(
												fing.getNoteNumber(),
												currentPos) > 0))) {
							sIndex = getStrechIndex((sIndex > 0 ? -1 : 1)
									* sIndex);
						} else {
							sIndex = getStrechIndex((sIndex < 0 ? -1 : 1)
									* sIndex);
						}
					} catch (IndexOutOfBoundsException e) {
						continue;
					}
					/*
					 * This is a cryptic description of the mess above!
					 * 
					 * (fing.getNumber() < j) //1 2
					 * (seq.getStretchDirection(fing.getNoteNumber(),
					 * currentPos) > 0) //1 2
					 * 
					 * (fing.getNumber() > j) //2 1
					 * (seq.getStretchDirection(fing.getNoteNumber(),
					 * currentPos) < 0) //2 1
					 */

					// System.out.print(fing.getNumber() + " -> " + j + " ");

					if (isBoundy(sIndex, fIndex)) {
						// System.out.println("PASS");
						bruceLee = new Fingering(j, currentPos);
						fing.addChild(bruceLee);
						bruceLee.increaseDifficulty(iDifficulty[fIndex][sIndex]);
						/* ****************************************************** */
						/*
						 * START OF RULE IMPLEMENTATION
						 * *************************
						 */
						/* ****************************************************** */
						
						/*
						 * 4. Position-Change-Count Rule: Assign 2 points for every full change
						 * of hand position and 1 point for every half change. A change of hand
						 * position occurs whenever the first and third notes in a consecutive
						 * group of three span an interval that is greater than MaxComf or less
						 * than MinComf for the corresponding fingers. In a full change, three
						 * conditions are satisfied simultaneously: The finger on the second of
						 * the three notes is the thumb; 
						 * 
						 * the second pitch lies between the first
						 * and third pitches; and the interval between the first and third pitches
						 * is greater than MaxPrac or less than MinPrac. All other changes are
						 * half changes.
						 */
						if((currentPos > 1 && j != fing.getParent().getNumber()) && ((handspan.getSpan(Type.MinComf, j, fing.getParent().getNumber())>seq.getStretch(currentPos, fing.getParent().getNoteNumber())) || (seq.getStretch(currentPos, fing.getParent().getNoteNumber())>handspan.getSpan(Type.MaxComf, j, fing.getParent().getNumber())))){
							if(fing.getNumber() == 1
							&& seq.getStretchDirection(fing.getParent().getNoteNumber(), fing.getNoteNumber()) == seq.getStretchDirection(fing.getNoteNumber(), currentPos)
							&& (handspan.getSpan(Type.MinPrac, fing.getParent().getNumber(), j)
								>
								(seq.getStretchDirection(fing.getParent().getNoteNumber(), currentPos)*seq.getStretch(fing.getParent().getNoteNumber(), currentPos))
								||
								(seq.getStretchDirection(fing.getParent().getNoteNumber(), currentPos)*seq.getStretch(fing.getParent().getNoteNumber(), currentPos))
								>
								handspan.getSpan(Type.MaxPrac, fing.getParent().getNumber(), j))
								){ //Full change
								bruceLee.increaseDifficulty(2);
							}else{ //Half change
								bruceLee.increaseDifficulty(1);
							}
						}
						
						/*
						 * 5. Position-Change-Size Rule: If the interval spanned by the first and
						 * third notes in a group of three is less than MinComf, assign the
						 * difference between the interval and MinComf (expressed in
						 * semitones). Conversely, if the interval is greater than MaxComf,
						 * assign the difference between the interval and MaxComf.
						 */
						if(currentPos > 1 && j != fing.getParent().getNumber()){
							if(handspan.getSpan(Type.MinComf, j, fing.getParent().getNumber())>seq.getStretch(currentPos, fing.getParent().getNoteNumber())){	
								bruceLee.increaseDifficulty(handspan.getSpan(Type.MinComf, j, fing.getParent().getNumber())-
								seq.getStretch(currentPos, fing.getParent().getNoteNumber()));
							}else if(seq.getStretch(currentPos, fing.getParent().getNoteNumber())>handspan.getSpan(Type.MaxComf, j, fing.getParent().getNumber())){
								bruceLee.increaseDifficulty(seq.getStretch(currentPos, fing.getParent().getNoteNumber())-
								handspan.getSpan(Type.MaxComf, j, fing.getParent().getNumber()));
							}
						}
						
						/*
						 * 6. Weak-Finger Rule: Assign 1 point every time finger
						 * 4 or finger 5 is used.
						 */
						if (j == 4 || j == 5) {
							bruceLee.increaseDifficulty(1);
						}

						/*
						 * 7. Three-Four-Five Rule: Assign 1 point every time
						 * fingers 3, 4, and 5 occur consecutively in any order,
						 * even when groups overlap.
						 */
						if (currentPos > 1 && (j == 3 || j == 4 || j == 5)) {
							int rule7counter = j == 3 ? 1 : j == 4 ? 2 : 4;

							rule7counter += fing.getNumber() == 3 ? 1 : fing
									.getNumber() == 4 ? 2
									: fing.getNumber() == 5 ? 4 : 0;

							rule7counter += fing.getParent().getNumber() == 3 ? 1
									: fing.getParent().getNumber() == 4 ? 2
											: fing.getParent().getNumber() == 5 ? 4
													: 0;

							if (rule7counter == 7) {
								bruceLee.increaseDifficulty(1);
							}
						}

						/*
						 * 8. Three-to-Four Rule: Assign 1 point each time
						 * finger 3 is immediately followed by finger 4.
						 */
						if (j == 4 && fing.getNumber() == 3) {
							bruceLee.increaseDifficulty(1);
						}

						/*
						 * 9. Four-on-Black Rule: Assign 1 point each time
						 * fingers 3 and 4 occur consecutively in any order with
						 * 3 on white and 4 on black.
						 */
						if ((j == 3 && !seq.isBlack(currentPos)
								&& fing.getNumber() == 4 && seq.isBlack(fing
								.getNoteNumber()))
								|| (j == 4 && seq.isBlack(currentPos)
										&& fing.getNumber() == 3 && !seq
											.isBlack(fing.getNoteNumber()))) {
							bruceLee.increaseDifficulty(1);
						}

						/*
						 * 10. Thumb-on-Black Rule: Assign 1 point whenever the
						 * thumb plays a black key.
						 * 
						 * If the immediately preceding note is white, assign a
						 * further 2 points. If the immediately following note
						 * is white, assign a further 2 points.
						 */
						if (j == 1 && seq.isBlack(currentPos)) {
							if (!seq.isBlack(fing.getNoteNumber())) {
								bruceLee.increaseDifficulty(3);
							} else {
								bruceLee.increaseDifficulty(1);
							}
						} /* PART TWO ALONG WITH RULE 11 */

						/*
						 * 11. Five-on-Black Rule: If the fifth finger plays a
						 * black key and the immediately preceding and following
						 * notes are also black, assign 0 points.
						 * 
						 * If the immediately preceding note is white, assign 2
						 * points. If the immediately following key is white,
						 * assign 2 further points.
						 */
						if (j == 5 && seq.isBlack(currentPos)
								&& !seq.isBlack(fing.getNoteNumber())) {
							bruceLee.increaseDifficulty(2);
						}

						if (!seq.isBlack(currentPos)
								&& seq.isBlack(fing.getNoteNumber())
								&& (fing.getNumber() == 1 || fing
										.getNumber() == 5)) {
							bruceLee.increaseDifficulty(2);
						}

						/*
						 * 12. Thumb-Passing Rule: 
						 * Assign 1 point for each thumb- or fingerpass on the same 
						 * level (from white to white or black to black). 
						 * Assign 3 points if the lower note is white, played by a 
						 * finger other than the thumb, and the upper is black, 
						 * played by the thumb. 
						 */
						if(sIndex < 5){
							if(seq.isBlack(currentPos) == seq.isBlack(fing.getNoteNumber())){
								bruceLee.increaseDifficulty(1);
							}else if((seq.isBlack(currentPos) && j == 1) || (seq.isBlack(fing.getNoteNumber()) &&  fing.getNumber() == 1)) {
								bruceLee.increaseDifficulty(3);
							}
						}
						
						
						/*
						 * Because it's 2013...
						 * RULE 13!
						 * THE CHORD HANDLER 
						 */
						
						boolean twinDragonsEffect = false;
						if(seq.getChordIndex(currentPos) > 1){
							if(sIndex < 5){
								continue;
							}
							
							Fingering n2 = bruceLee,
									  n1 = fing;
							
							for(int u = seq.getChordIndex(currentPos)-1; u > 0; --u){
								Fingering localN1 = n1;
								for(int v = u; v > 0; --v, localN1 = localN1.getParent()){
//									System.err.println("f1: "+localN1.getNumber()+" f2: "+n2.getNumber()+" n1: "+localN1.getNoteNumber()+" n2: "+n2.getNoteNumber());
									if((handspan.getSpan(Type.MinRel, localN1.getNumber(), n2.getNumber()) > seq.getStretch(n2.getNoteNumber(), localN1.getNoteNumber()))
									|| (seq.getStretch(n2.getNoteNumber(), localN1.getNoteNumber()) > handspan.getSpan(Type.MaxRel, localN1.getNumber(), n2.getNumber()))){
										twinDragonsEffect = true;
									}
								}
								n1 = n1.getParent();
								n2 = n2.getParent();	
							}
						}
						if(twinDragonsEffect){
							continue;
						}
												
						/* ****************************************************** */
						/*
						 * END OF RULE IMPLEMENTATION
						 * ***************************
						 */
						/* ****************************************************** */
						recursivengine(bruceLee, currentPos + 1);
					}
					// else {
					// System.out.println("FAIL");
					// }
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

		/*
		// First 17 notes of Für Elise
		int notes[] = { 16, 15, 16, 15, 16, 11, 14, 12, 9, 0, 4, 9, 11, 4, 8,
				11, 12 };

		Note note = null;
		for (int i = 0; i < notes.length; ++i) {
			note = new Note(notes[i] + 12 * 4);
			seq.addNote(note);
		}
		*/

		String seqStr[] = "C4 G3 E3 C4 D4 C4 B3 G#3".split(" ");
		Note note = null;
		for (int i = 0; i < seqStr.length; ++i) {
			note = new Note(seqStr[i]);
			seq.addNote(note);
		}
		
		
		fingering = generateFingering();

		System.out.print("Sequence: ");
		for (Note n : seq) {
			System.out.print(n.getName() + " ");
		}
		System.out.println();

		Fingering best = endPaths.get(0);
		Fingering[] fff = new Fingering[seq.length()];
		for (Fingering f : endPaths) {
			if (f.getDifficulty() <= best.getDifficulty())
				best = f;
		}

		ArrayList<Fingering> bestOfTheBest = new ArrayList<Fingering>();
		for (Fingering f : endPaths) {
			if (f.getDifficulty() == best.getDifficulty())
				bestOfTheBest.add(f);
		}

		System.out.print("BEST: ");
		for (int i = seq.length(); i > 0; --i) {
			fff[i - 1] = best;
			best = best.getParent();
		}
		for (int i = 0; i < seq.length(); ++i) {
			System.out.print(fff[i].getNumber() + " ");
		}
		System.out.println();
		
		
		Collections.sort(endPaths);
		for (Fingering f : endPaths) {
			best = f;
			System.out.print(best.getDifficulty()+" -:- ");
			for (int i = seq.length(); i > 0; --i) {
				fff[i - 1] = best;
				best = best.getParent();
			}
			for (int i = 0; i < seq.length(); ++i) {
				System.out.print(fff[i].getNumber() + " ");
			}
			System.out.println();
		}
		

		/* DEBUG */System.out.println("endPath Size: " + bestOfTheBest.size());
	}

	public int[][] bestFingerings;
	public static int[][] getFingerings(NoteArray na) {
		return new Difficulty(na).bestFingerings;
	}

	/**
	 * Currently it reloads all the resources for every instance, which is NOT EFFICIENT, TODO FIX IT!
	 * @param na Sequence
	 */
	public Difficulty(NoteArray na) {
		preReqRule();
		this.seq = na;
		fingering = generateFingering();

		Fingering best = endPaths.get(0);
		for (Fingering f : endPaths) {
			if (f.getDifficulty() <= best.getDifficulty()){
				best = f;
			}
		}

		ArrayList<Fingering> bestOfTheBest = new ArrayList<Fingering>();
		for (Fingering f : endPaths) {
			if (f.getDifficulty() == best.getDifficulty())
				bestOfTheBest.add(f);
		}
		
		bestFingerings = new int[bestOfTheBest.size()][seq.length()];
		for(int i = 0; i < bestOfTheBest.size(); ++i){
			best = bestOfTheBest.get(i);
			for (int j = seq.length(); j > 0; --j) {
				bestFingerings[i][j - 1] = best.getNumber();
				best = best.getParent();
			}
		}	
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