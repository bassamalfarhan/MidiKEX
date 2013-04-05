package Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileManager {
	
	/**
	 * Loads the boundaries of the generated table.
	 * @param filename name of the file.
	 * @return boolean matrix containing the boundaries.
	 */
	public static boolean[][] loadBoundaries(String filename) {
		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File("resources/" + filename))));

			boolean[][] matrix = new boolean[10][21];
			int rowCounter = -2;
			while (true) {
				String s = input.readLine();

				if (s.startsWith("###")) {
					break;
				}
				if (++rowCounter == -1) {
					continue;
				}

				String[] eList = s.split(";");
				for (int i = 0; i < eList.length - 1; ++i) {
					matrix[rowCounter][i] = eList[i + 1].trim().equals("X") ? true
							: false;
				}
			}

			if (input != null) {
				try {
					input.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			return matrix;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Reads in the handspans from the given file.
	 * 
	 * @param filename
	 *            to be read.
	 * @return a matrix representation of the handspans.
	 */
	public static int[][] loadHandSpan(String filename) {
		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File("resources/" + filename))));

			int[][] matrix = new int[6][10];

			int rowCounter = -2;
			while (true) {
				String s = input.readLine();

				if (s.startsWith("###")) {
					break;
				}
				if (++rowCounter == -1) {
					continue;
				}

				String[] eList = s.split(";");
				for (int i = 0; i < eList.length - 1; ++i) {
					matrix[i][rowCounter] = Integer.parseInt(eList[i + 1]
							.trim());
				}
			}

			if (input != null) {
				try {
					input.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			return matrix;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}