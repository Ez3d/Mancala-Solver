//  Generates the move sequence to get the most points in one turn
//  NOTE: run GOOGLE_APPLICATION_CREDENTIALS must be set in the run console before running:
//  set GOOGLE_APPLICATION_CREDENTIALS=D:\Users\Ez3d\Documents\Cloud Vision\mancala-1639878949959-d9b6e998c679.json

//  Strange error occurs: The declared package "mancala" does not match the expected package "main.java.mancala"
//  To fix, change the package name to "main.java.mancala", save the file, then change it back to "mancala", and save again
package mancala;

import java.util.*;
import java.io.*;

public class MancalaGenerate {

    static ArrayList<Board> boards = new ArrayList<Board>();
    // static ArrayList<int[]> boards = new ArrayList<int[]>();

    static int turn = 0;
    // 0 is P1
    // 1 is P2
    
    // Selects the nth pocket
    public static Board move(int n, Board b) {
        int stones = b.getBoard()[n];
        b.getBoard()[n] = 0;
        for (int i = n + 1; i <= stones + n; i++) {
            if ((turn == 0 && i % 14 == 0) || (turn == 1 && i % 14 == 7)) {
                i++;
                stones++;
            }

            b.getBoard()[i % 14]++;

            // Recursive part
            if (i == stones + n) {
                // Lands in mancala
                if ((turn == 0 && i % 14 == 7) || (turn == 1 && i % 14 == 0)) {
                    b.getBoard()[14] = 1;
                    return b;
                }
                // Lands in non-empty pocket
                else if (b.getBoard()[i % 14] != 1) {
                    b.getBoard()[14] = 0;
                    move(i % 14, b);
                }
                else
                    return b;
            }
        }
        return b;
    }
    

    public static void tester(Board board) {
        for (int i = 1; i <= 6; i++) {
            if (board.getBoard()[i] != 0) {
                Board newBoard = move(i, board.clone());
                newBoard.addMove(i);
                if (newBoard.getBoard()[14] == 1) {
                    newBoard.getBoard()[14] = 0;
                    tester(newBoard.clone());
                }
                else
                    boards.add(newBoard.clone());
            }
        }
    }


    public static void main(String[] args) {
        
        int[] board;
        try {
            board = LoadGame.loadGame("D:/Users/Ez3d/Documents/VisualStudioCode/Java/Mancala/image.jpg");
        } catch (IOException e) {
            System.out.println("File not found!");
            return;
        }

        System.out.print("\nStarting board:");
        showBoard(board);
        tester(new Board(board, new ArrayList<Integer>()));

        Collections.sort(boards);

        // Print list of top 5 move sequences
        System.out.println("\nTOP 5 MOVE SEQUENCES:");
        for (int i = 0; i < 5; i++) {
            System.out.println("\nTotal score: " + boards.get(i).getBoard()[7]);
            System.out.print("Move order: ");
            for (int n : boards.get(i).getMoves())
                System.out.print(n + ", ");
            System.out.print("\nResulting board:");
            showBoard(boards.get(i).getBoard());
        }
    }

    // Prints board
    public static void showBoard(int[] board) {
        System.out.printf("%n%2d ", board[0]);
        for (int i = 13; i >= 7; i--)
            System.out.printf("%2d ", board[i]);
        System.out.printf("%n%2d ", board[0]);
        for (int i = 1; i <= 7; i++)
            System.out.printf("%2d ", board[i]);
        System.out.println();
    }
}
