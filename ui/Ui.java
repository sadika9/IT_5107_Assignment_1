package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import core.Die;
import core.GameLogic;
import core.Player;

public class Ui {
    private JFrame m_mainFrame;
    private Board m_gameBoardPanel;
    private JPanel m_gameInfoPanel;
    private JLabel[] m_cellLabels;
    private JLabel m_dieResultLabel;
    private JButton m_rollButton;
    private JList m_playerList;

    private int m_moveTos[];

    DefaultListModel<String> m_playerListModel;

    private GameLogic m_gameLogic;

    public Ui(GameLogic gameLogic, int moveTos[]) {
        m_gameLogic = gameLogic;
        m_moveTos = moveTos;

        setupUi();
    }

    public static int[] askGameSettings() {
        SpinnerNumberModel playerModel = new SpinnerNumberModel(1, 1, 10, 1);
        SpinnerNumberModel ladderModel = new SpinnerNumberModel(5, 1, 10, 1);
        SpinnerNumberModel snakeModel = new SpinnerNumberModel(3, 1, 10, 1);

        JSpinner nPlayersSpinner = new JSpinner(playerModel);
        JSpinner nLaddersSpinner = new JSpinner(ladderModel);
        JSpinner nSnakesSpinner = new JSpinner(snakeModel);

        JPanel playersPanel = new JPanel();
        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.X_AXIS));
        playersPanel.add(new JLabel("# players: "));
        playersPanel.add(nPlayersSpinner);

        JPanel laddersPanel = new JPanel();
        laddersPanel.setLayout(new BoxLayout(laddersPanel, BoxLayout.X_AXIS));
        laddersPanel.add(new JLabel("# ladders: "));
        laddersPanel.add(nLaddersSpinner);

        JPanel snakesPanel = new JPanel();
        snakesPanel.setLayout(new BoxLayout(snakesPanel, BoxLayout.X_AXIS));
        snakesPanel.add(new JLabel("# snakes: "));
        snakesPanel.add(nSnakesSpinner);

        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.add(playersPanel);
        group.add(laddersPanel);
        group.add(snakesPanel);

        JOptionPane.showMessageDialog(null, group, "Settings", JOptionPane.QUESTION_MESSAGE);

        int settings[] = new int[3];
        settings[0] = (int) nPlayersSpinner.getValue();
        settings[1] = (int) nLaddersSpinner.getValue();
        settings[2] = (int) nSnakesSpinner.getValue();

        return settings;
    }

    private void setupUi() {
        m_mainFrame = new JFrame("Snakes and Ladders");
        m_mainFrame.setSize(800, 600);
        m_mainFrame.setLayout(new BoxLayout(m_mainFrame.getContentPane(), BoxLayout.X_AXIS));

        setupGameBoardPanel();
        setupGameInfoPanel();

        m_mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        m_mainFrame.add(m_gameBoardPanel);
        m_mainFrame.add(m_gameInfoPanel);
        m_mainFrame.setVisible(true);
    }

    private void setupGameBoardPanel() {
        m_gameBoardPanel = new Board(m_moveTos, m_gameLogic.players());
        m_gameBoardPanel.setPreferredSize(new Dimension(500, 600));
    }

    private void setupGameInfoPanel() {
        m_dieResultLabel = new JLabel("Die: -");

        m_playerListModel = new DefaultListModel<>();
        updatePlayerListModel();

        m_playerList = new JList(m_playerListModel);
        m_playerList.setSelectedIndex(m_gameLogic.currentPlayerIndex());
        m_playerList.setEnabled(false);

        m_rollButton = new JButton("Roll");
        m_rollButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playRound();
            }
        });

        m_gameInfoPanel = new JPanel();
        m_gameInfoPanel.setLayout(new BoxLayout(m_gameInfoPanel, BoxLayout.Y_AXIS));
        m_gameInfoPanel.add(new JScrollPane(m_playerList));
        m_gameInfoPanel.add(Box.createVerticalGlue());
        m_gameInfoPanel.add(m_rollButton);
        m_gameInfoPanel.add(m_dieResultLabel);
        m_gameInfoPanel.add(Box.createVerticalGlue());
    }

    private void playRound() {
        m_gameLogic.goToNextState();

        Player player = m_gameLogic.currentPlayer();
        Die die = m_gameLogic.die();

        m_dieResultLabel.setText("Die: " + die.result());
        updatePlayerListModel();
        m_playerList.setSelectedIndex(m_gameLogic.currentPlayerIndex());

        m_gameBoardPanel.repaint();

        if (m_gameLogic.isWon()) {
            m_rollButton.setEnabled(false);

            JOptionPane.showMessageDialog(m_mainFrame, player.name() + " won!");
        }
    }

    private void updatePlayerListModel() {
        m_playerListModel.clear();

        for (Player player : m_gameLogic.players()) {
            m_playerListModel.addElement(playerListItemText(player));
        }
    }

    private String playerListItemText(Player player) {
        StringBuilder text = new StringBuilder(player.name());

        text.append(" ");
        if (player.position() == -1) {
            text.append("(waiting)");
        } else {
            text.append("(");
            text.append(player.position() + 1);
            text.append(")");
        }

        return text.toString();
    }
}
