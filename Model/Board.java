package Model;

import View.BoardView;

/**
 * Generates, updates, and calculates all game data
 *
 * @author csleys
 */
public class Board {

    private Piece[][] Board;
    private int moveCounter; //tracks turns
    private boolean debug = false;

    // start postions of pieces
    private int[] throneListX = {0, 10, 0, 10, 5};
    private int[] throneListY = {0, 0, 10, 10, 5};
    private int[] whiteStartX = {5, 4, 5, 6, 3, 4, 6, 7, 4, 5, 6, 5};
    private int[] whiteStartY = {3, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7};
    private int[] blackStartX = {0, 0, 0, 0, 0, 1, 3, 3, 4, 4, 5, 5, 5, 5, 6, 6,
        7, 7, 9, 10, 10, 10, 10, 10};
    private int[] blackStartY = {3, 4, 5, 6, 7, 5, 0, 10, 0, 10, 0, 10, 1, 9, 0,
        10, 0, 10, 5, 3, 4, 5, 6, 7};

    public Board() {
        newBoard();
    }

    /**
     * Returns a integer representation of the color of a given piece,
     *
     * @param x The X value of the piece to be checked
     * @param y The Y value of the piece to be checked
     * @return  0 for black, 1 for white, 2 for king, and 3 if there is no piece
     * at those coordinates.
     */
    public int getColor(int x, int y) {
        if (Board[y][x] != null) {
            if (Board[y][x].isKing()) {
                return 2;
            } else {
                return Board[y][x].getColor();
            }
        } else {
            return 3;
        }
    }

    /**
     * Updates the two-dimensional array Board with the values from the start
     * coordinate arrays defined above.
     */
    public final void newBoard() {
        Logger("Initilizing Board");
        Board = new Piece[11][11];
        moveCounter = 1; // keeps track of whose move it is
        Board[5][5] = new King(1);
        for (int count = 0; count < whiteStartX.length; count++) {
            Logger("Ininitilzing White x = " + whiteStartX[count] + " y = "
                    + whiteStartY[count]);
            Board[whiteStartY[count]][whiteStartX[count]] = new Piece(1);
        }
        for (int count = 0; count < blackStartX.length; count++) {
            Logger("Ininitilzing Black x = " + blackStartX[count] + " y = "
                    + blackStartY[count]);
            Board[blackStartY[count]][blackStartX[count]] = new Piece(0);
        }
    }

    /**
     * Validates and moves a piece from the start x,y to the end x,y
     *
     * @param startX    The x value of the start coordinate
     * @param startY    The y value of the start coordinate
     * @param endX      The x of the end coordinate
     * @param endY      The y of the end coordinate
     */
    public void movePiece(int startX, int startY, int endX, int endY) {
        Logger("Moving Piece start(" + startX + "," + startY + " end(" + endX
                + "," + endY + ")");
        Piece start = Board[startY][startX];

        if (startX == endX && startY == endY) { //Checks that the user is actually
            // attempting to move the piece
            Logger("Start and end values equal");
            BoardView.displayMessage("Invalid Move: You picked the same square");
            return;
        }

        if (start != null) { //checks that start value contains something
            Logger("Start value exists");
            if ((start.getColor() == 1 && moveCounter % 2 == 1)
                    || (start.getColor() == 0 && moveCounter % 2 == 0)) { // checks
                // that the piece is of the correct color
                Logger("Is players turn");
                if (startX == endX || startY == endY) { // checks that the piece
                    // is on a cardinal point
                    Logger("In line with cardinal point");
                    if (checkPath(startX, startY, endX, endY)) {// checks that the
                        // path is clear
                        Logger("Path and end point clear");
                        Board[endY][endX] = start; //copy start to end
                        Board[startY][startX] = null; //delete start
                        moveCounter++; //changes turn
                        checkKills(endX, endY); //checks if any piece is killed
                    } else {
                        BoardView.displayMessage("Invalid Move: There is"
                                + " something in the way");
                    }
                } else {
                    BoardView.displayMessage("Invalid Move: You have to move"
                            + " on cardinal points");
                }
            } else {
                BoardView.displayMessage("Invalid Move: Its not you move");
            }
        } else {
            BoardView.displayMessage("Invalid Move: There is nothing there!");
        }
    }

    /**
     * Method checks that there are no pieces at the end coordinates or
     * in between.
     *
     * @param startX    The x value of the start coordinate
     * @param startY    The y value of the start coordinate
     * @param endX      The x of the end coordinate
     * @param endY      The y of the end coordinate
     * @return
     */
    private boolean checkPath(int startX, int startY, int endX, int endY) {
        Logger("Checking Path");
        int lowX = startX;
        int lowY = startY;
        int highX = endX;
        int highY = endY;

        if (lowX > highX) {
            highX = startX;
            lowX = endX;
        } else if (lowY > highY) {
            highY = startY;
            lowY = endY;
        }

        for (int x = lowX; x <= highX; x++) {
            for (int y = lowY; y <= highY; y++) {
                if ((Board[y][x] == null && !isThrone(x, y))
                        || ((y == startY) && (x == startX))) { //checks that
                    // the square is not a throne, another piece, or the
                    // piece that is attempting to move
                    continue;
                }
                if (Board[startY][startX].isKing() && isThrone(endX, endY)) {
                    // checks if the king is moving on to throne
                    if (x != 5 && y != 5) {// making sure it is not the middle
                        // throne, if so white wins
                        Logger("White king moved to throne, white wins");
                        BoardView.displayMessage("White wins");
                        newBoard();
                        break;
                    }
                }
                Logger("Path not clear");
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the piece at x,y has killed any other piece
     *
     * @param x     The x of the piece
     * @param y     The y of the piece
     *
     * @exception   e   If one of the Board values is out of bounds then it must
     * be a wall and is counted against when calculating kills, or does not need
     * to be considered for opposite color
     */
    private void checkKills(int x, int y) {
        Logger("Checking kills");
        Piece north, south, east, west, twoNorth, twoSouth, twoEast, twoWest,
                current;

        current = Board[y][x];

        try {
            north = Board[y - 1][x];
        } catch (ArrayIndexOutOfBoundsException e) {
            north = null;
            Logger("North is wall, counted against");
        }
        try {
            south = Board[y + 1][x];
        } catch (ArrayIndexOutOfBoundsException e) {
            south = null;
            Logger("South is wall, counted against");
        }
        try {
            east = Board[y][x + 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            east = null;
            Logger("East is wall, counted against");
        }
        try {
            west = Board[y][x - 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            west = null;
            Logger("West is wall, counted against");
        }



        try {
            twoNorth = Board[y - 2][x];
        } catch (ArrayIndexOutOfBoundsException e) {
            twoNorth = new Piece(current.getColor());
            Logger("Two squares to the north is a wall, counts towards"
                    + " surrounding");
        }
        try {
            twoSouth = Board[y + 2][x];
        } catch (ArrayIndexOutOfBoundsException e) {
            twoSouth = new Piece(current.getColor());
            Logger("Two squares to the south is a wall, counts towards"
                    + " surrounding");
        }
        try {
            twoEast = Board[y][x + 2];
        } catch (ArrayIndexOutOfBoundsException e) {
            twoEast = new Piece(current.getColor());
            Logger("Two squares to the east is a wall, counts towards"
                    + " surrounding");
        }
        try {
            twoWest = Board[y][x - 2];
        } catch (ArrayIndexOutOfBoundsException e) {
            twoWest = new Piece(current.getColor());
            Logger("Two squares to the west is a wall, counts towards"
                    + " surrounding");
        }



        if (north != null && (north.getColor() != current.getColor())) {
            Logger("North is opposite color");
            if (north.isKing()) {
                Logger("North is King");
                checkKing(x, (y - 1));
            } else if (isThrone(x, y - 2)) { //Thrones count against
                Logger("Two squares north is a throne");
                Logger("North surrounded");
                Board[y - 1][x] = null;
            } else if (twoNorth != null && (twoNorth.getColor() == current.getColor())) {
                Logger("North surrounded");
                Board[y - 1][x] = null;
            }
        }
        if (south != null && (south.getColor() != current.getColor())) {
            Logger("South is opposite color");
            if (south.isKing()) {
                Logger("South is King");
                checkKing(x, (y + 1));
            } else if (isThrone(x, y + 2)) {
                Logger("Two squares south is a throne");
                Logger("South surrounded");
                Board[y + 1][x] = null;
            } else if (twoSouth != null && (twoSouth.getColor() == current.getColor())) {
                Logger("South surrounded");
                Board[y + 1][x] = null;
            }
        }
        if (east != null && (east.getColor() != current.getColor())) {
            Logger("East is opposite color");
            if (east.isKing()) {
                Logger("East is King");
                checkKing((x + 1), (y));
            } else if (isThrone(x + 2, y)) {
                Logger("Two squares east is a throne");
                Logger("East surrounded");
                Board[y][x + 2] = null;
            } else if (twoEast != null && (twoEast.getColor() == current.getColor())) {
                Logger("East is surrounded");
                Board[y][x + 1] = null;
            }
        }
        if (west != null && (west.getColor() != current.getColor())) {
            Logger("West is opposite color");
            if (west.isKing()) {
                Logger("West is King");
                checkKing((x - 1), (y));
            } else if (isThrone(x - 2, y)) {
                Logger("Two squares west is a throne");
                Logger("West surrounded");
                Board[y][x - 1] = null;
            } else if (twoWest != null && (twoWest.getColor() == current.getColor())) {
                Logger("West is surounded");
                Board[y][x - 1] = null;
            }
        }
    }

    /**
     * If a piece in danger is the king, then special rules apply
     *
     * @param x X value of king
     * @param y Y value of king
     *
     * @exception   e   if value is out of bounds then counted against
     */
    private void checkKing(int x, int y) {
        Logger("Checking if king is dead");
        Piece north, south, east, west;

        //thrones count against
        try {
            if (isThrone(x, y - 1)) {
                north = new Piece(0);
                Logger("North of King is throne, counted against");
            } else {
                north = Board[y - 1][x];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            north = new Piece(0);
            Logger("North of king is wall, counted against");
        }
        try {
            if (isThrone(x, y + 1)) {
                south = new Piece(0);
                Logger("South of King is throne, counted against");
            } else {
                south = Board[y + 1][x];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            south = new Piece(0);
            Logger("South of king is wall, counted against");
        }
        try {
            if (isThrone(x + 1, y)) {
                east = new Piece(0);
                Logger("East of King is throne, counted against");
            } else {
                east = Board[y][x + 1];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            east = new Piece(0);
            Logger("East of king is wall, counted against");
        }
        try {
            if (isThrone(x, y - 1)) {
                west = new Piece(0);
                Logger("West of King is throne, counted against");
            } else {
                west = Board[y][x - 1];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            west = new Piece(0);
            Logger("West of king is wall, counted against");
        }

        if (north != null && south != null && west != null && east != null) {
            // king surrounded on all sides
            if (north.getColor() == 0 && south.getColor() == 0
                    && east.getColor() == 0 && west.getColor() == 0) {
                // all sides are counted against, king is dead, black wins
                Logger("King is dead, black wins");
                BoardView.displayMessage("Black Wins");
                newBoard();
            }
        }
    }

    /**
     * Checks if square is a throne based on coordinates stored in arrays created
     * above
     *
     * @param x X value in question
     * @param y Y value in question
     * @return
     */
    public boolean isThrone(int x, int y) {
        for (int count = 0; count < throneListX.length; count++) {
            if (throneListX[count] == x && throneListY[count] == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Inverts the debug function
     */
    public void toggleDebug() {
        debug = !debug;
    }

    /**
     * Function that outputs logging information to the console
     *
     * @param message String to output
     */
    private void Logger(String message) {
        if (debug) {
            System.out.println(message);
        }
    }
}
