// Human-playable version of mancala

package mancala;

import java.util.*;

@SuppressWarnings("resource")
public class Mancala {

    static int[] board = new int[14];
    // 0 is P2 mancala, 7 is P1 mancala. Cycles in ascending order
    // 0 13 12 11 10  9  8  7
    // 0  1  2  3  4  5  6  7

    static int turn = 0;
    // 0 is P1
    // 1 is P2

    static boolean free = false;
    
    // Selects the nth pocket
    public static void move(int n) {
        int stones = board[n];
        board[n] = 0;
        for (int i = n + 1; i <= stones + n; i++) {
            if ((turn == 0 && i % 14 == 0) || (turn == 1 && i % 14 == 7)) {
                i++;
                stones++;
            }

            board[i % 14]++;

            // Recursive part
            if (i == stones + n) {
                // Lands in mancala
                if ((turn == 0 && i % 14 == 7) || (turn == 1 && i % 14 == 0))
                    free = true;
                // Lands in empty pocket
                else if (board[i % 14] == 1)
                    return;
                // Lands in non-empty pocket
                else
                    move(i % 14);
            }
        }
    }

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        // Board setup
        for (int i = 0; i < 14; i++)
            board[i] = 4;
        board[0] = 0;
        board[7] = 0;

        showBoard();
        
        // Game loop
        while (true) {
            // Win conditions
            int sum1 = 0;
            for (int i = 1; i <= 6; i++)
                sum1 += board[i];
            int sum2 = 0;
            for (int i = 8; i <= 13; i++)
                sum2 += board[i];

            if (sum1 == 0 || sum2 == 0) {
                showBoard();
                if (board[0] < board[7])
                    System.out.println("P1 Wins!");
                else if (board[0] > board[7])
                    System.out.println("P2 Wins!");
                else
                    System.out.println("Tie!");
                return;
            }

            // Turn alternation
            while (turn == 0) {
                System.out.println("\nP1 Turn. Choose a pocket: ");
                int n = Integer.parseInt(in.nextLine());
                free = false;
                move(n);
                showBoard();
                if (!free)
                    turn = 1;
            }

            while (turn == 1) {
                System.out.println("\nP2 Turn. Choose a pocket: ");
                int n = Integer.parseInt(in.nextLine());
                free = false;
                move(n);
                showBoard();
                if (!free)
                    turn = 0;
            }

        }
    }

    // Prints board
    public static void showBoard() {
        System.out.printf("%n%2d ", board[0]);
        for (int i = 13; i >= 7; i--)
            System.out.printf("%2d ", board[i]);
        System.out.printf("%n%2d ", board[0]);
        for (int i = 1; i <= 7; i++)
            System.out.printf("%2d ", board[i]);
    }
}
