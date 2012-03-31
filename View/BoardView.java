package View;

/**
 * This class generates and organizes all visible elements of the program
 *
 * @author csleys
 */
import Model.Board;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BoardView extends JPanel {

    private BoardSquare[] boardPanel;
    private Board board;
    private BoardSquare prevousPiece = null;
    private static BoardView instance;

    /**
     * Generates and organizes the buttons and calls updateView() function
     * to add the game pieces
     */
    private BoardView() {
        setLayout(new GridLayout(11, 11, 0, 0));
        setVisible(true);
        boardPanel = new BoardSquare[121];

        board = new Board();

        int buttonCount = 0;
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                boardPanel[buttonCount] = new BoardSquare(x, y);
                boardPanel[buttonCount].addActionListener(new ActionEvent());
                add(boardPanel[buttonCount]);
                buttonCount++;
            }
        }
        updateView();
    }

    /**
     * Implements singleton design pattern
     * @return  the BoardView object
     */
    public static BoardView GetInstance() {
        if (instance == null) {
            instance = new BoardView();
        }

        return instance;
    }

    /**
     * Creates a JFrame and adds this class as a JPanel and creates the menu
     * bar
     */
    public void newView() {
        JFrame frame = new JFrame("Viking Chess");
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
        frame.setResizable(false);

        MenuBar menu = new MenuBar();
        Menu program = new Menu("Program");
        MenuItem newGame = new MenuItem("New Game");
        MenuItem exit = new MenuItem("Exit");
        MenuItem debug = new MenuItem("Toggle Debug Mode");

        newGame.addActionListener(new MenuEvent());
        exit.addActionListener(new MenuEvent());
        debug.addActionListener(new MenuEvent());

        program.add(newGame);
        program.add(debug);
        program.add(exit);
        menu.add(program);
        frame.setMenuBar(menu);
    }

    /**
     * Method to pass messages to the user
     *
     * @param m The string that is to be displayed
     */
    public static void displayMessage(String m) {
        JOptionPane.showMessageDialog(null, m);
    }

    /**
     * Updates the icons on each button to represent current game state
     */
    public void updateView() {
        for (int count = 0; count < boardPanel.length; count++) {
            boardPanel[count].setType(board.getColor(boardPanel[count].getXCoor(),
                    boardPanel[count].getYCoor()));
        }

        boardPanel[0].setIcon(new IconThrone());
        boardPanel[10].setIcon(new IconThrone());
        boardPanel[110].setIcon(new IconThrone());
        boardPanel[120].setIcon(new IconThrone());
        if (boardPanel[60].getIcon() == null) {
            boardPanel[60].setIcon(new IconCenterThrone());
        }
    }

    /**
     * Gets the x and y coordinates of the buttons the user selects and passes
     * start and end coordinates to the Model
     */
    private class ActionEvent implements ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            BoardSquare piece = (BoardSquare) e.getSource();
            if (prevousPiece != null && piece != null) {
                board.movePiece(prevousPiece.getXCoor(),
                        prevousPiece.getYCoor(), piece.getXCoor(),
                        piece.getYCoor());
                updateView();
                prevousPiece = null;
            } else {
                prevousPiece = piece;
            }
        }
    }

    /**
     * Takes input from the menu
     */
    private class MenuEvent implements ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent ae) {
            String com = ae.getActionCommand();

            if (com.equals("Exit")) {
                System.exit(0);
            } else if (com.equals("New Game")) {
                board.newBoard();
                updateView();
            } else if (com.equals("Toggle Debug Mode")) {
                board.toggleDebug();
            }
        }
    }
}
