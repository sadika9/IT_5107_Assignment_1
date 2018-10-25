package core;

import java.util.ArrayList;

public class GameLogic {
    private Board m_board;
    private Die m_die;
    private Player m_players[];
    private int m_currentPlayer = 0;
    private int m_nextPlayer = 0;
    boolean m_isWon = false;

    public void init(int moveTos[], int players) {
        m_board = new Board(moveTos);
        m_die = new Die();

        m_players = new Player[players];
        for (int i = 0; i < players; i++) {
            Player player = new Player();
            player.setName("Player " + (i + 1));

            m_players[i] = player;
        }

        m_currentPlayer = 0;
    }

    public Board board() {
        return m_board;
    }

    public Die die() {
        return m_die;
    }

    public Player[] players() {
        return m_players;
    }

    public Player currentPlayer() {
        return m_players[m_currentPlayer];
    }

    public boolean goToNextState() {
        if (m_isWon) {
            return false;
        }

        m_currentPlayer = m_nextPlayer;
        Player player = m_players[m_currentPlayer];

        m_die.roll();

        // decide next player
        if (player.isEnteredToBoard() && (m_die.result() == 1 || m_die.result() == 6)) {
            m_nextPlayer = m_currentPlayer;
        } else {
            m_nextPlayer = (m_currentPlayer + 1) % m_players.length;
        }

        // new player
        if (!player.isEnteredToBoard()) {
            if (m_die.result() == 1 || m_die.result() == 6) {
                player.setPosition(0);
            } else { // player cannot enter into the board
                System.out.println("Cannot enter into the board");

                return true;
            }
        }

        // not a legal move, player's turn complete
        if (!isLegalMove(player.position(), m_die.result())) {
            System.out.println("Not a legal move");
            return true;
        }

        ArrayList playerMovements = new ArrayList();
        playerMovements.add(player.position()); // save starting position

        player.setPosition(player.position() + m_die.result());
        playerMovements.add(player.position()); // save position after adding the die value

        if (player.position() == 99) {
            m_isWon = true;
        } else {
            Cell cell = m_board.cells()[player.position()];

            if ((player.position() + 1) != cell.moveTo()) { // cell has a ladder or a snake
                player.setPosition(cell.moveTo());
                playerMovements.add(player.position());
            }
        }

        player.setLastSteps(playerMovements);

        return true;
    }

    public void printState() {
        System.out.println("Player Position\t" + currentPlayer().position());
        System.out.println("Current Player\t" + m_players[m_currentPlayer].name());
        System.out.println("Next Player\t" + m_players[m_nextPlayer].name());
        System.out.println("Die\t\t" + m_die.result());
        System.out.println("--------------------------------------------");
    }

    private boolean isLegalMove(int current, int dieResult) {
        return ((current + dieResult) <= 99);
    }
}
