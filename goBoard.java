import java.util.Scanner;

public class goBoard {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);

        System.out.print("Please define how large you want your board to be:");
        int size = scn.nextInt();        
        String[][] pieces = new String[size][size];

        String currentPlayer = "black";
        boolean gameOn = true;
        
        // Initialize board with + intersections
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                pieces[i][j] = "+";
            }
        }

        while (gameOn) {
            boolean validMove = false; // Reset validMove for each player's turn
            //this allows a player to put in a invalid intersection (already used) and still continue their turn

            while (!validMove) {
                System.out.print("Player " + currentPlayer + " Please enter an x coord (0-" + (size - 1) + "): ");
                int moveX = scn.nextInt();
                while (moveX >= size){
                    System.out.println("Oops, choose another X coordinate");
                    moveX = scn.nextInt();
                }
                System.out.print("Player " + currentPlayer + " Please enter a y coord (0-" + (size - 1) + "): ");
                int moveY = scn.nextInt();
                while (moveY >= size){
                    System.out.println("Oops, choose another Y coordinate");
                    moveY = scn.nextInt();
                }
                //A way to end the game, for now
                if (moveX <= -1 || moveY <= -1) {
                    System.out.println("Game over!");
                    gameOn = false;
                    break;
                }

                // Place a piece (black)
                if (currentPlayer == "black"){
                    switch (pieces[moveY][moveX]) {
                        case "X":
                        case "O":
                            System.out.println("This intersection is already occupied. Try again.");
                            break;
                        case "+":
                            pieces[moveY][moveX] = "X";
                            validMove = true;
                            break;
                        default:
                            System.out.println("Invalid value.");
                    }
                }
                //place a piece (white)
                if (currentPlayer == "white") {
                    switch (pieces[moveY][moveX]) {
                        case "X":
                        case "O":
                            System.out.println("This intersection is already occupied. Try again.");
                            break;
                        case "+":
                            pieces[moveY][moveX] = "O";
                            validMove = true;
                            break;
                        default:
                            System.out.println("Invalid value.");
                    }
                }
            }

            if (gameOn == false) break; // Exit the game loop if game is over

            // Print the board
            System.out.println("Current Board:");
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    System.out.print(" " + pieces[row][col] + " ");
                    if (col < size - 1) {
                        System.out.print("-");
                    }
                }
                System.out.println();
                if (row < size - 1) {
                    for (int col = 0; col < size; col++) {
                        System.out.print(" |  ");
                    }
                    System.out.println();
                }
            }

            // Switch players
            if (currentPlayer == "black") {
                currentPlayer = "white";
            } else {
                currentPlayer = "black";
            }
        }
        scn.close();
    }
}
