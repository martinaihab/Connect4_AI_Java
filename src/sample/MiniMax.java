import java.util.*;

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
	
	
	public static int get_first_available_row(int[][] B, int C) {
		int rows = B.length;
		int row_counter = 0;
		for(int i=0; i<rows; i++) {
			if(B[i][C] == EMPTY)
				break;
			else
				row_counter+=1;
		}
		return row_counter;
	}
	
	
	public static List<Integer> get_all_available_columns(int[][] B){
		List<Integer> valid_cols = new ArrayList<>();
		int rows = B.length;
		int cols = B[0].length;
		for(int i=0; i<cols; i++) {
			if(B[rows-1][i] == EMPTY)
				valid_cols.add(i);
		}
		return valid_cols;
	}
	
	
	public static boolean is_terminal(int[][] B) {
		int rows = B.length;
		int cols = B[0].length;
			for(int j=0; j<cols; j++) {
				if(B[rows-1][j] == EMPTY)
					return false;
			}
		return true;
	}
	
	
	public static int[][] drop_piece(int[][] B, int C, int piece) {
		int rows = B.length;
		int cols = B[0].length;
		int [][]temp_B = new int[rows][cols];
		for(int i=0; i<rows; i++)
		{
			temp_B[i][C] = B[i][C];
			if(temp_B[i][C] == EMPTY) {
				temp_B[i][C] = piece;
				break;
			}
		}
		return temp_B;
	}
	
	
	
	
	
	public static boolean is_winning_position(int [][] B, int piece) {
		//Tuna
		return true;
	}
	
	
	public static double score_position(int [][] B) {
		//Tuna
		return 1;
	}
	
	
	public static void update_board(int[][] B) {
	}
	
	
	public static int next_move(int[][] B) {
		double next = minimax(B, 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true).get(1);
		return (int)next;
	}
	
	
	public static List<Double> minimax(int [][] B, int depth, double alpha, double beta, boolean maximizing) {
		List<Double> ret = new ArrayList<>(2);
		if(is_terminal(B)) {
			if(is_winning_position(B, COMPUTER))
				ret.set(0, 1000000.0);
			else if(is_winning_position(B, PLAYER))
				ret.set(0, -1000000.0);
			else
				ret.set(0, 0.0);
		}
		else if(depth==0) {
			ret.set(0, score_position(B));
		}
		else {
			double value;
			int next_move = 0;
			List<Integer> valid_columns = get_all_available_columns(B);
			
			if(maximizing) {
				//maximizing
				value = Double.NEGATIVE_INFINITY;
				for(int c: valid_columns) {
					int[][] temp_Board = drop_piece(B, c, COMPUTER);
					double new_value = minimax(temp_Board, depth-1, alpha, beta, false).get(0);
					if(new_value > value) {
						value=new_value;
						next_move = c;
					}
					if(value>alpha)
						alpha = value;
					if(alpha >= beta)
						break;
				}
			}
			else {
				//minimizing
				value = Double.POSITIVE_INFINITY;
				for(int c: valid_columns) {
					int[][] temp_Board = drop_piece(B, c, COMPUTER);
					double new_value = minimax(temp_Board, depth-1, alpha, beta, true).get(0);
					if(new_value < value) {
						value=new_value;
						next_move = c;
					}
					if(value<beta)
						beta = value;
					if(alpha >= beta)
						break;
				}
			}
			ret.set(0, value);
			ret.set(1, (double)next_move);	
		}
		
		return ret;
	}
	
	
	public static void main(String[] args) {
		int[][] Board = new int[6][7];
		initialize_board(Board);
		Board[0][0] = PLAYER;
		Board[1][1] = COMPUTER;
		Board[1][1] = COMPUTER;
		Board[1][1] = COMPUTER;
		Board[5][1] = COMPUTER;
		
		
		System.out.println(get_first_available_row(Board, 0));
		System.out.println(get_all_available_columns(Board));
		System.out.println(Arrays.deepToString(Board).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
		int[][] Toard = drop_piece(Board, 0, COMPUTER);
		System.out.println("Board");
		System.out.println(Arrays.deepToString(Board).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
		System.out.println("Toard");
		System.out.println(Arrays.deepToString(Toard).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
	}


}



