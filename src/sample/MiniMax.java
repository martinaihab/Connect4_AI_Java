import java.util.Arrays;

public class MiniMax {
	public static final int EMPTY = 0;
	public static final int COMPUTER = 1;
	public static final int PLAYER = 2;
	
	public static void initialize_board(int [][] B) {
		int rows = B.length;
		int cols = B[0].length;
		for(int i=0; i<rows; i++) {
			for(int j=0; j<cols; j++) {
				B[i][j] = EMPTY;
			}
		}
	}
	
	
	public static boolean is_winning_position(int [][] B) {
		//Tuna
	}
	
	
	public static double score_position(int [][] B) {
		//Tuna
	}
	
	
	public static void update_board(int[][] B) {
	}
	
	
	public static int next_move(int[][] B) {
	}
	
	
	public static void main(String[] args) {
		int[][] Board = new int[6][7];
		initialize_board(Board);
		System.out.println(Arrays.deepToString(Board).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
		
	}


}



