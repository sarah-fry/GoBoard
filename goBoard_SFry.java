import java.util.Scanner;

/// OVERALL NOTES:
/// Scoring is kinda funky, it "works" but I don't think its right
/// IDK HOW TO HANDLE SUICIDE!!! see below:
/// like if a piece gets captured, its vanishes (good)
/// but then you can place another piece in that space, and it never gets captured so it counts toward scoring (bad)
/// 

public class goBoard_SFry {

// make variables in the public class
    private int[][] board;
    private int boardSize;
    private String currentPlayer;
    private boolean gameOn;
    private boolean validMove;
    private int moveX, moveY;
    private Scanner scn = new Scanner(System.in);
    private final int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public goBoard_SFry() {
        System.out.print("Enter the size of the Go board: ");
        boardSize = scn.nextInt();
        board = new int[boardSize][boardSize]; // Initialize board
        currentPlayer = "black(X)";
        gameOn = true;
    }

    public void playerTurn() {
        currentPlayer = currentPlayer.equals("black(X)") ? "white(O)" : "black(X)";
    }

    public boolean isBoardFull() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void getMove() {
        validMove = false;
        while (!validMove) {
            System.out.print("Player " + currentPlayer + ", enter X coord (0-" + (boardSize - 1) + ") or -1 to exit: ");
            moveX = scn.nextInt();
            if (moveX == -1) {
                System.out.println("You entered -1: Exiting the game...");
                gameOn = false;
                return;
            }
            while (moveX < 0 || moveX >= boardSize) {
                System.out.println("Invalid X coordinate. Try again.");
                moveX = scn.nextInt();
                if (moveX == -1) {
                    System.out.println("You entered -1: Exiting the game...");
                    gameOn = false;
                    return;
                }
            }
    
            System.out.print("Player " + currentPlayer + ", enter Y coord (0-" + (boardSize - 1) + ") or -1 to exit: ");
            moveY = scn.nextInt();
            if (moveY == -1) {
                System.out.println("You entered -1: Exiting the game...");
                gameOn = false;
                return;
            }
            while (moveY < 0 || moveY >= boardSize) {
                System.out.println("Invalid Y coordinate. Try again.");
                moveY = scn.nextInt();
                if (moveY == -1) {
                    System.out.println("You entered -1: Exiting the game...");
                    gameOn = false;
                    return;
                }
            }
    
            if (board[moveY][moveX] == 0) {
                validMove = true;
            } else {
                System.out.println("This intersection is occupied. Try again.");
            }
        }
    }    

    public void placePiece() {
        if (!gameOn) return;

        int playerColor = currentPlayer.equals("black(X)") ? 1 : 2;
        int opponentColor = playerColor == 1 ? 2 : 1;

        if (currentPlayer.equals("black(X)")) {
            board[moveY][moveX] = 1; // Black stone- X
        } else {
            board[moveY][moveX] = 2; // White stone- O
        }
            for (int[] dir : directions) {
            int ny = moveY + dir[0];
            int nx = moveX + dir[1];
            if (ny >= 0 && ny < boardSize && nx >= 0 && nx < boardSize && board[ny][nx] == opponentColor) {
                boolean[][] visited = new boolean[boardSize][boardSize];
                if (!hasLiberty(ny, nx, opponentColor, visited)) {
                    removeGroup(ny, nx, opponentColor);
                }
            }
        }
        validMove = true;
    }

    public void displayBoard() {
        System.out.println("Current Board:");
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                if (board[y][x] == 1) {
                    System.out.print(" X ");
                } else if (board[y][x] == 2) {
                    System.out.print(" O ");
                } else {
                    System.out.print(" + ");
                }
                    if (x < boardSize - 1) {
                    System.out.print("-");
                }
            }
            System.out.println();
                if (y < boardSize - 1) {
                for (int x = 0; x < boardSize; x++) {
                    System.out.print(" | ");
                    if (x < boardSize - 1) {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
        }
    }


// CAPTURE  //
    private boolean hasLiberty(int y, int x, int color, boolean[][] visited) {
        if (y < 0 || y >= boardSize || x < 0 || x >= boardSize || visited[y][x]) return false;
        visited[y][x] = true;

        if (board[y][x] == 0) return true;
        if (board[y][x] != color) return false;

        for (int[] dir : directions) {
            if (hasLiberty(y + dir[0], x + dir[1], color, visited)) return true;
        }
        return false;
    }

    private void removeGroup(int y, int x, int color) {
        if (y < 0 || y >= boardSize || x < 0 || x >= boardSize || board[y][x] != color) return;
        if (board[y][x] != color) return;

        board[y][x] = 0;

        for (int[] dir : directions) {
            removeGroup(y + dir[0], x + dir[1], color);
        }
    }

    private int countTerritory(int y, int x, boolean[][] visited, int[] borderingColor) {
        if (y < 0 || y >= boardSize || x < 0 || x >= boardSize || visited[y][x]) return 0;
        visited[y][x] = true;

        if (board[y][x] == 1) {
            borderingColor[0] = 1;
            return 0;
        } else if (board[y][x] == 2) {
            borderingColor[1] = 1;
            return 0;
        }

        int count = 1;
        for (int[] dir : directions) {
            count += countTerritory(y + dir[0], x + dir[1], visited, borderingColor);
        }
        return count;
    }

    public void calculateScore() {
        // okay, I know I need to use directions here to find out is pieces are 'bordered' 
        // but I think the way I designed my board maybe messed that up?
        // also not sure if this counts captured stones by accident?
        boolean[][] checked = new boolean[boardSize][boardSize];
        int scoreX = 0;
        int scoreO = 0;
    
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (board[row][col] == 1) {
                    scoreX++;
                } else if (board[row][col] == 2) {
                    scoreO++;
                }
            }
        }
    
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                if (board[y][x] == 0 && !checked[y][x]) {
                    int[] whoTouches = new int[2]; // 0: black touched it, 1: white touched it
                    int area = countTerritory(y, x, checked, whoTouches);
    
                    if (whoTouches[0] == 1 && whoTouches[1] == 0) {
                        scoreX += area;
                    } else if (whoTouches[1] == 1 && whoTouches[0] == 0) {
                        scoreO += area;
                    } else {
                        
                    }
                }
            }
        }
    
        System.out.println("Final Scoring:");
        System.out.println("Black (X) score: " + scoreX);
        System.out.println("White (O) score: " + scoreO);
    
        if (scoreX > scoreO) {
            System.out.println("Black wins!");
        } else if (scoreO > scoreX) {
            System.out.println("White wins!");
        } else {
            System.out.println("It's a tie!");
        }
    }
    
    public void playGame() {
        while (gameOn && !isBoardFull()) {
            displayBoard();
            getMove();
            if (!gameOn) break;
            placePiece();
            if (validMove) playerTurn();
        }
        System.out.println("Game ended.");
        displayBoard();
        calculateScore();
    }

    public static void main(String[] args) {
        goBoard_SFry game = new goBoard_SFry();
        game.playGame();
    }
}