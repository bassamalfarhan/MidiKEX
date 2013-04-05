package Algorithm;

public class HandSpan {
	private int[][] spans;

	/**
	 * Constructs a handspan from matrix.
	 * @param spans the matrix to construct the handspan from.
	 */
	public HandSpan(int[][] spans) {
		this.spans = spans;
	}

	/**
	 * @param spanType  The span type to retrieve.
	 * @param f1 First finger.
	 * @param f2 Second finger.
	 * @return The span for the specified finger pair.
	 */
	public int getSpan(Type spanType, int f1, int f2) {
		return spans[spanType.getArrayIndex()][getArrayPosition(f1, f2)];
	}

	/**
	 * @param spanType The span type to retrieve.
	 * @param ap The array position to retrieve it from
	 * @return The span at the specified positions.
	 */
	public int getSpan(Type spanType, int ap) {
		return spans[spanType.getArrayIndex()][ap];
	}

	/**
	 * Defines the different span types!
	 */
	public enum Type {
		MinPrac(0), MinComf(1), MinRel(2), MaxRel(3), MaxComf(4), MaxPrac(5);

		private int index;

		private Type(int index) {
			this.index = index;
		}

		public int getArrayIndex() {
			return index;
		}
	}

	/**
	 * The order of the fingers doesn't matter!
	 * 
	 * @param f1 position of the first finger
	 * @param f2 position of the second finger
	 * @return array position in the handspans table
	 */
	public int getArrayPosition(int f1, int f2) {
		if (f1 != f2 && Math.min(f1, f2) > 0 && Math.max(f1, f2) < 6) {
			if (f1 > f2) {
				int temp = f1;
				f1 = f2;
				f2 = temp;
			}
			int pos = 0;
			switch (f1) {
			case 2:
				pos = 4;
				break;
			case 3:
				pos = 7;
				break;
			case 4:
				pos = 9;
				break;
			}
			pos += f2 - f1;
			return --pos;
		}
		throw new IndexOutOfBoundsException(
				"Finger index expected: [1-5], received: " + f1 + " and " + f2);
	}
}
