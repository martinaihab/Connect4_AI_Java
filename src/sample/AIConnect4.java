package sample;

import java.util.*;

public class AIConnect4 {
    private static final int EMPTY = 0;
    private static final int COMPUTER = 1;
    public static final int PLAYER = 2;
    private static final int COLUMN_COUNT = 7;
    private static final int ROW_COUNT = 6;
    private static final int DEPTH = 3;
    private static int[][] board;

    public static void initialize_board() {
        board = new int[ROW_COUNT][COLUMN_COUNT];
        int rows = board.length;
        int cols = board[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = EMPTY;
            }
        }
    }


    private static int get_first_available_row(int[][] B, int C) {
        int rows = B.length;
        int row_counter = 0;
        for (int i = 0; i < rows; i++) {
            if (B[i][C] == EMPTY)
                break;
            else
                row_counter += 1;
        }
        return row_counter;
    }


    private static List<Integer> get_all_available_columns(int[][] B) {
        List<Integer> valid_cols = new ArrayList<>();
        int rows = B.length;
        int cols = B[0].length;
        for (int i = 0; i < cols; i++) {
            if (B[rows - 1][i] == EMPTY)
                valid_cols.add(i);
        }
        return valid_cols;
    }


    private static boolean is_terminal(int[][] B) {
        return is_winning_position(B, PLAYER) || is_winning_position(B, COMPUTER) || get_all_available_columns(B).size() == 0;
    }


    private static int[][] try_drop_piece(int[][] B, int C, int piece) {
        int rows = B.length;
        int cols = B[0].length;
        int[][] temp_B = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp_B[i][j] = B[i][j];
            }
        }
        for (int i = 0; i < rows; i++) {
            if (temp_B[i][C] == EMPTY) {
                temp_B[i][C] = piece;
                break;
            }
        }
        return temp_B;
    }


    private static boolean is_winning_position(int[][] board, int piece) {
        //Tuna
        //Check horizontal locations for win
        for (int i = 0; i < ROW_COUNT; i++)
            for (int j = 0; j < COLUMN_COUNT - 3; j++)
                if (board[i][j] == piece && board[i][j + 1] == piece && board[i][j + 2] == piece && board[i][j + 3] == piece)
                    return true;

        //Check vertical locations for win
        for (int i = 0; i < ROW_COUNT - 3; i++)
            for (int j = 0; j < COLUMN_COUNT; j++)
                if (board[i][j] == piece && board[i + 1][j] == piece && board[i + 2][j] == piece && board[i + 3][j] == piece)
                    return true;
//
//	# Check positively sloped diagonals
        for (int i = 0; i < ROW_COUNT - 3; i++)
            for (int j = 0; j < COLUMN_COUNT - 3; j++)
                if (board[i][j] == piece && board[i + 1][j + 1] == piece && board[i + 2][j + 2] == piece && board[i + 3][j + 3] == piece)
                    return true;
//
//	# Check negatively sloped diagonals
        for (int i = 3; i < ROW_COUNT; i++)
            for (int j = 0; j < COLUMN_COUNT - 3; j++)
                if (board[i][j] == piece && board[i - 1][j + 1] == piece && board[i - 2][j + 2] == piece && board[i - 3][j + 3] == piece)
                    return true;
        return false;
    }


    private static double score_position(int[][] board) {
        int score = 0;

        //Score center column
        int centerCount = 0;
        for (int i = 0; i < COLUMN_COUNT; i++) {
            if (board[0][3] == COMPUTER)
                centerCount++;
        }
        score += centerCount * 3;

        //Score Horizontal
        //int[] window = new int [4];
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT - 3; j++) {
                score += evaluateWindow(board, i, j, LineType.HORIZONTAL);
            }
        }

        //Score Vertical
        for (int i = 0; i < ROW_COUNT - 3; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {
                score += evaluateWindow(board, i, j, LineType.VERTICAL);
            }
        }

        //Score Diagonal
        for (int i = 0; i < ROW_COUNT - 3; i++) {
            for (int j = 0; j < COLUMN_COUNT - 3; j++) {
                score += evaluateWindow(board, i, j, LineType.POSITIVE_DIAGONAL);
                score += evaluateWindow(board, i, j, LineType.NEGATIVE_DIAGONAL);
            }
        }
        return score;
    }


    enum LineType {
        HORIZONTAL, VERTICAL, POSITIVE_DIAGONAL, NEGATIVE_DIAGONAL
    }


    private static int evaluateWindow(int[][] board, int row, int column, LineType lineType) {
        int windowScore = 0;
        int empty = 0;
        int pieceCount = 0;
        int oppPieceCount = 0;
        if (lineType == LineType.HORIZONTAL) {
            for (int i = 0; i < 4; i++) {
                if (board[row][column + i] == COMPUTER)
                    pieceCount++;
                else if (board[row][column + i] == PLAYER)
                    oppPieceCount++;
                else
                    empty++;
            }
        } else if (lineType == LineType.VERTICAL) {
            for (int i = 0; i < 4; i++) {
                if (board[row + i][column] == COMPUTER)
                    pieceCount++;
                else if (board[row + i][column] == PLAYER)
                    oppPieceCount++;
                else
                    empty++;
            }
        } else if (lineType == LineType.POSITIVE_DIAGONAL) {
            for (int i = 0; i < 4; i++) {
                if (board[row + i][column + i] == COMPUTER)
                    pieceCount++;
                else if (board[row + i][column + i] == PLAYER)
                    oppPieceCount++;
                else
                    empty++;
            }
        } else if (lineType == LineType.NEGATIVE_DIAGONAL) {
            for (int i = 0; i < 4; i++) {
                if (board[row + 3 - i][column + i] == COMPUTER)
                    pieceCount++;
                else if (board[row + 3 - i][column + i] == PLAYER)
                    oppPieceCount++;
                else
                    empty++;
            }
        }

        if (pieceCount == 4)
            windowScore += 100;
        else if (pieceCount == 3 && empty == 1)
            windowScore += 5;
        else if (pieceCount == 2 && empty == 2)
            windowScore += 2;

        if (oppPieceCount == 3 && empty == 1)
            windowScore -= 4;

        return windowScore;
    }


    public static void update_board(int column, int playerOrComputerMove) {
        int rows = board.length;
        for (int i = 0; i < rows; i++) {
            if (board[i][column] == EMPTY) {
                board[i][column] = playerOrComputerMove;
                break;
            }
        }
    }


    public static int getAIMove() {
        double next = minimax(board, DEPTH, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true).get(1);
        update_board((int) next, COMPUTER);
        return (int) next;
    }


    private static List<Double> minimax(int[][] B, int depth, double alpha, double beta, boolean maximizing) {
        List<Double> ret = new ArrayList<>();
        if (is_terminal(B)) {
            if (is_winning_position(B, COMPUTER))
                ret.add(1000000.0);
            else if (is_winning_position(B, PLAYER))
                ret.add(-1000000.0);
            else
                ret.add(0.0);
        } else if (depth == 0) {
            double x = score_position(B);
            ret.add(x);
        } else {
            double value;
            int next_move = 0;
            List<Integer> valid_columns = get_all_available_columns(B);

            if (maximizing) {
                //maximizing
                value = Double.NEGATIVE_INFINITY;
                for (int c : valid_columns) {
                    int[][] temp_Board = try_drop_piece(B, c, COMPUTER);
                    double new_value = minimax(temp_Board, depth - 1, alpha, beta, false).get(0);
                    if (new_value > value) {
                        value = new_value;
                        next_move = c;
                    }
                    if (value > alpha)
                        alpha = value;
                    if (alpha >= beta)
                        break;
                }
            } else {
                //minimizing
                value = Double.POSITIVE_INFINITY;
                for (int c : valid_columns) {
                    int[][] temp_Board = try_drop_piece(B, c, PLAYER);
                    double new_value = minimax(temp_Board, depth - 1, alpha, beta, true).get(0);
                    if (new_value < value) {
                        value = new_value;
                        next_move = c;
                    }
                    if (value < beta)
                        beta = value;
                    if (alpha >= beta)
                        break;
                }
            }
            ret.add(value);
            ret.add((double) next_move);
        }
        return ret;
    }


    public static void main(String[] args) {
        initialize_board();
		
		
		/*
		while(true) {
			Scanner input = new Scanner(System.in);
	        
			int new_column = input.nextInt();
	        update_board(Board, new_column, PLAYER);
	        System.out.println(Arrays.deepToString(Board).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
	        if(is_terminal(Board))
	        	 break;
	       
	        System.out.println();
	        
	        new_column = getAIMove(Board);
	        update_board(Board, new_column, COMPUTER);
	        System.out.println(Arrays.deepToString(Board).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
	        if(is_terminal(Board))
	        	break;
		}
		 System.out.println("Game Over !");
*/
    }


}



