package Algorithm;

import Utilities.FileManager;
import Algorithm.HandSpan.Type;

public class Difficulty {
	HandSpan handspan = new HandSpan(FileManager.loadHandSpan("handspans"));
	boolean[][] mBound = FileManager.loadBoundaries("boundaries");

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

	public void incrementDifficulty(int strechIndex, int fingerIndex, int value) {
		iDifficulty[fingerIndex][strechIndex] += value;
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

	public void generateFingerings(NoteArray sequence) {
		int counter = 0;

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				System.out.println(counter++);
			}
		}
	}

	/**********************************************************************
	 ** QUALITY CONTROL CRAP
	 **********************************************************************/
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

		generateFingerings(null);
	}

	public static void main(String args[]) {
		new Difficulty();
	}
}