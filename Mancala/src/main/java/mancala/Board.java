// Implements the Board object

package mancala;

import java.util.*;

public class Board implements Comparable<Board>{
    
    private int[] board;
    private ArrayList<Integer> moves;


    // Constructor
    public Board(int[] b, ArrayList<Integer> moves) {
        this.board = b.clone();
        this.moves = new ArrayList<Integer>(moves);
    }


    public int compareTo(Board board) {
        return board.board[7] - this.board[7];
    }

    public Board clone() {
        return new Board(this.board.clone(), new ArrayList<Integer>(this.moves));
    }

    public int[] getBoard() {
        return board;
    }

    public ArrayList<Integer> getMoves() {
        return moves;
    }

    public void addMove(int n) {
        moves.add(n);
    }
}
