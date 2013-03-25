
public class Difficulty {
	
	/**************************************************************************************
	  #; –5; –4; –3; –2; –1;  0;  1;  2;  3;  4;  5;  6;  7;  8;  9; 10; 11; 12; 13; 14; 15
	1-2; 10;  7;  4;  3;  2;  #;  0;  0;  0;  0;  0;  1;  2;  3;  6;  9;  #;  #;  #;  #;  #
	1-3;  #; 11;  8;  5;  4;  #;  2;  1;  0;  0;  0;  0;  0;  1;  2;  3;  6;  9;  #;  #;  #
	1-4;  #;  #; 12;  9;  6;  #;  4;  3;  2;  1;  0;  0;  0;  0;  0;  1;  2;  3;  6;  9;  #
	1-5;  #;  #;  #;  #; 12;  #;  6;  5;  4;  3;  2;  1;  0;  0;  0;  0;  1;  2;  3;  6;  9
	2-3;  #;  #;  #;  #;  #;  #;  0;  0;  2;  6; 10;  #;  #;  #;  #;  #;  #;  #;  #;  #;  #
	2-4;  #;  #;  #;  #;  #;  #;  4;  2;  0;  0;  2;  6; 10;  #;  #;  #;  #;  #;  #;  #;  #
	2-5;  #;  #;  #;  #;  #;  #;  #;  6;  4;  2;  0;  0;  2;  4;  8; 12;  #;  #;  #;  #;  #
	3-4;  #;  #;  #;  #;  #;  #;  0;  0;  4;  8;  #;  #;  #;  #;  #;  #;  #;  #;  #;  #;  #
	3-5;  #;  #;  #;  #;  #;  #;  4;  2;  0;  0;  2;  6; 10;  #;  #;  #;  #;  #;  #;  #;  #
	4-5;  #;  #;  #;  #;  #;  #;  0;  0;  2;  6; 10;  #;  #;  #;  #;  #;  #;  #;  #;  #;  #
	**************************************************************************************/
	
	
	
//	private int[][] matrix;
//	public Difficulty(){
//		matrix = FileManager.loadDifficultyMatrix();
//	}
	
	
	public class MatrixNavigator{
		public int getArrayPosition(int f1, int f2){
			if(f1 != f2 && Math.min(f1, f2)>0 && Math.max(f1, f2)<6){
				if(f1 > f2){
					int temp = f1;
					f1 = f2;
					f2 = temp;
				}
				
				int pos = 0;
				switch(f1){
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
			throw new IndexOutOfBoundsException("Finger index expected: [1-5], received: "+f1+" and "+f2);
		}	
	}
	
	
	private final int[] MinPrac, 
				  		MinComf, 
				  		MinRel, 
				  		MaxRel, 
				  		MaxComf, 
				  		MaxPrac;
	
	public Difficulty() {
		int[][] spans = FileManager.loadHandSpan("handspans");
		MinPrac = spans[0];
  		MinComf = spans[1];
  		MinRel = spans[2];
  		MaxRel = spans[3];
  		MaxComf = spans[4];
  		MaxPrac = spans[5];
		
		MatrixNavigator mn = new MatrixNavigator();
		for(int i = 0; i < spans.length;  ++i){
			for(int j = 0; j < spans[i].length; ++j){
				System.out.print(spans[i][j]+"\t");
			}
			System.out.println();
		}
	}
	
	
	public static void main(String args[]){
		new Difficulty();
	}
}