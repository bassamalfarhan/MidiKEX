package Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import Algorithm.Note;
import Algorithm.NoteArray;

public class FileManager {

	/**
	 * Loads the boundaries of the generated table.
	 * 
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
	 * @param filename to be read.
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

	/**
	 * TODO Comment
	 * @param filename
	 * @return
	 */
	public static NoteArray loadSequence(String filename) {
		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File("resources/" + filename))));

			NoteArray na = new NoteArray();

			while (true) {
				String s = input.readLine();

				if (s.startsWith("###")) {
					break;
				}

				String[] eList = s.split(" ");
				int chordCounter = 0;
				boolean chord = false;
				for (int i = 0; i < eList.length; ++i) {
					eList[i] = eList[i].trim();
					
					/* Chord control */
					if(!chord && eList[i].contains("{")){
						eList[i] = eList[i].replace("{", "");
						chord = true;
						++chordCounter;
					}
					
					na.addNote(new Note(eList[i]), chordCounter);
					
					if(chord){
						++chordCounter;
						if(eList[i].contains("}")){
							eList[i] = eList[i].replace("}", "");
							chord = false;
							chordCounter = 0;
						}
					}
					
					/* eocc */
				}
			}

			if (input != null) {
				try {
					input.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			return na;
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