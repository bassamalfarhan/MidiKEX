import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileManager {
//	public static ArrayList<Course> loadCourseList() {
//		BufferedReader input = null;
//		try {
//			input = new BufferedReader(new InputStreamReader(
//					new FileInputStream(new File("data/courseList"))));
//
//			ArrayList<Course> list = new ArrayList<Course>();
//			while (true) {
//				String s = input.readLine();
//				if (s.equals("###"))
//					break;
//
//				String[] eList = s.split(";");
//				list.add(new Course(eList[0], Double.parseDouble(eList[1]),
//						Integer.parseInt(eList[2]), Double
//								.parseDouble(eList[3]), Integer
//								.parseInt(eList[4])));
//			}
//
//			if (input != null) {
//				try {
//					input.close();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//			}
//
//			return list;
//		} catch (Exception e) {
//			if (input != null) {
//				try {
//					input.close();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//			}
//			e.printStackTrace();
//		}
//		if (input != null) {
//			try {
//				input.close();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//		}
//		return null;
//	}

//	public static int[][] loadDifficultyMatrix() {
//		BufferedReader input = null;
//		try {
//			input = new BufferedReader(new InputStreamReader(
//					new FileInputStream(new File("resources/difficulty_matrix"))));
//
//			int[][] matrix = new int[10][21];
//			int rowCounter = -2;
//			while (true) {
//				String s = input.readLine();
//				
//				if (s.startsWith("###")){
//					break;
//				}
//				if(++rowCounter == -1){
//					continue;
//				}
//				
//				String[] eList = s.split(";");
//				for(int i = 0; i < eList.length-1; ++i){
//					eList[i+1] = eList[i+1].trim();
//					matrix[rowCounter][i] = eList[i+1].equals("#")?-1:Integer.parseInt(eList[i+1]);
//				}
//			}
//
//			if (input != null) {
//				try {
//					input.close();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//			}
//
//			return matrix;
//		} catch (Exception e) {
//			if (input != null) {
//				try {
//					input.close();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//			}
//			e.printStackTrace();
//		}
//		if (input != null) {
//			try {
//				input.close();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//		}
//		return null;
//	}
	
	public static int[][] loadHandSpan(String filename) {
		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File("resources/"+filename))));

			int[][] matrix = new int[6][10];
			
			int rowCounter = -2;
			while (true) {
				String s = input.readLine();
				
				if (s.startsWith("###")){
					break;
				}
				if(++rowCounter == -1){
					continue;
				}
				
				String[] eList = s.split(";");
				for(int i = 0; i < eList.length-1; ++i){
					eList[i+1] = eList[i+1].trim();
					
					boolean negation = false;
					if(eList[i+1].contains("-")){
						eList[i+1].replace("-", "");
						negation = true;
					}
						
					matrix[i][rowCounter] = (negation?(-1):1)*Integer.parseInt(eList[i+1]);
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
		}finally{
			try {
				if(input != null){
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	
//	public static void saveCourseList(ArrayList<Course> courseList) {
//		BufferedWriter output = null;
//		try {
//			output = new BufferedWriter(new OutputStreamWriter(
//					new FileOutputStream(new File("data/courseList"))));
//
//			for (Course c : courseList) {
//				output.write(c.toString());
//			}
//			output.write("###");
//		} catch (Exception e) {
//		} finally {
//			if (output != null) {
//				try {
//					output.close();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//			}
//		}
//	}
}