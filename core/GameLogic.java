package core;

import java.util.ArrayList;

public class GameLogic {
    public enum GameState {
        Fresh,
        UnknownError,
        PlayerNotAllowedIn,
        CannotMove,
        Finish,
        Ok,
    }

    private Board m_board;
    private Die m_die;
    private Player m_players[];
    private int m_currentPlayerIndex = 0;
    private int m_nextPlayerIndex = 0;
    private boolean m_isWon = false;
    private GameState m_gameState = GameState.Fresh;

    public void init(int moveTos[], int players) {
        m_board = new Board(moveTos);
        m_die = new Die();

        m_players = new Player[players];
        for (int i = 0; i < players; i++) {
            Player player = new Player();
            player.setName("Player " + (i + 1));

            m_players[i] = player;
        }

        m_currentPlayerIndex = 0;
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

    public int currentPlayerIndex() {
        return m_currentPlayerIndex;
    }

    public Player currentPlayer() {
        return m_players[m_currentPlayerIndex];
    }

    public boolean isWon() {
        return m_isWon;
    }

    public void goToNextState() {
        m_gameState = GameState.Ok;

        if (m_isWon) {
            m_gameState = GameState.Finish;
            return;
        }

        m_currentPlayerIndex = m_nextPlayerIndex;
        Player player = currentPlayer();

        m_die.roll();

        // decide next player
        if (player.isEnteredToBoard() && (m_die.result() == 1 || m_die.result() == 6)) {
            m_nextPlayerIndex = m_currentPlayerIndex;
        } else {
            m_nextPlayerIndex = (m_currentPlayerIndex + 1) % m_players.length;
        }

        // new player
        if (!player.isEnteredToBoard()) {
            boolean canStart = (m_die.result() == 1 || m_die.result() == 6);

            if (!canStart) { // player cannot enter into the board
                System.out.println("Cannot enter into the board");

                m_gameState = GameState.PlayerNotAllowedIn;
                return;
            }
        }

        // not a legal move, player's turn complete
        if (!isLegalMove(player.position(), m_die.result())) {
            System.out.println("Not a legal move");

            m_gameState = GameState.CannotMove;
            return;
        }

        ArrayList playerMovements = new ArrayList();
        playerMovements.add(player.position()); // save starting position

        player.setPosition(player.position() + m_die.result());
        playerMovements.add(player.position()); // save position after adding the die value

        if (player.position() == 99) {
            m_gameState = GameState.Finish;
            m_isWon = true;
        } else {
            Cell cell = m_board.cells()[player.position()];

            if ((player.position() + 1) != cell.moveTo()) { // cell has a ladder or a snake
                player.setPosition(cell.moveTo());
                playerMovements.add(player.position());
            }

            if (player.position() == 99) { // to handle ladders directly to winning cells
                m_gameState = GameState.Finish;
                m_isWon = true;
            }
        }

        player.setLastSteps(playerMovements);

        return;
    }

    public GameState gameState() {
        return m_gameState;
    }

    public void printState() {
        System.out.println("Player Position\t" +  currentPlayer().position());
        System.out.println("Current Player\t" + currentPlayer().name());
        System.out.println("Next Player\t" + m_players[m_nextPlayerIndex].name());
        System.out.println("Die\t\t" + m_die.result());
        System.out.println("--------------------------------------------");
    }

    private boolean isLegalMove(int current, int dieResult) {
        return ((current + dieResult) <= 99);
    }
}
