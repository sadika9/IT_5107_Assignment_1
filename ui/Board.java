package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.BasicStroke;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import core.Player;

public class Board extends JPanel {

    private int m_moveTos[];
    private int m_cellWidth = 50;
    private int m_boardPadding = 15;
    private Player m_players[];

    public Board(int moveTos[], Player[] players) {
        m_moveTos = moveTos;
        m_players = players;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCells(g);
    }

    private void drawCells(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        BasicStroke bs1 = new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        g2d.setStroke(bs1);
        for (int c1 = 0; c1 < 99; c1++) {
            int c2 = m_moveTos[c1]; // cell-1's "move to" cell

            if (c1 == c2) { // next cell is same as cell-1's "move to" cell
                continue;
            }

            if (c1 < c2) { // ladder
                g2d.setColor(Color.GREEN);
            } else {
                g2d.setColor(Color.RED);
            }

            int x1 = m_boardPadding + cellIndexToRectPosX(c1) + m_cellWidth / 2;
            int y1 = cellIndexToRectPosY(c1) + m_cellWidth / 2;

            int x2 = m_boardPadding + cellIndexToRectPosX(c2) + m_cellWidth / 2;
            int y2 = cellIndexToRectPosY(c2) + m_cellWidth / 2;

            g2d.drawLine(x1, y1, x2, y2);
        }

        BasicStroke bs2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        g2d.setStroke(bs2);
        g2d.setColor(Color.BLACK);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                int px = m_boardPadding + (x * m_cellWidth);
                int py = m_boardPadding + (y * m_cellWidth);

                g2d.drawRect(px, m_boardPadding + py, m_cellWidth, m_cellWidth);

                String label;
                if (y % 2 == 0) {
                    label = "" + (100 - (x + (y * 10)));
                } else {
                    label = "" + (((10 - y - 1) * 10) + (x + 1));
                }
                g2d.drawString(label, px + 5, py + 30);
            }
        }

        g2d.setFont(new Font("default", Font.BOLD, 16));
        for (Player p : m_players) {
            int pos = p.position();

            int x = m_boardPadding + cellIndexToRectPosX(pos) + m_cellWidth / 2;
            int y = cellIndexToRectPosY(pos) + m_cellWidth / 2;

            String name = p.name().replace("layer ", "");
            g2d.drawString(name, x - 10, y + 15);
        }

        g2d.dispose();
    }

    private int cellIndexToRectPosX(int index) {
        boolean inverse = false;

        if (index >= 10 && index < 20) {
            inverse = true;
        } else if (index >= 30 && index < 40) {
            inverse = true;
        } else if (index >= 50 && index < 60) {
            inverse = true;
        } else if (index >= 70 && index < 80) {
            inverse = true;
        } else if (index >= 90 && index < 100) {
            inverse = true;
        }

        int pos = (index % 10) * m_cellWidth;
        if (inverse) {
            pos = 10 * m_cellWidth - pos - m_cellWidth;
        }

        return pos;
    }

    private int cellIndexToRectPosY(int index) {
        int mul = 0;

        if (index < 10) {
            mul = 10;
        } else if (index < 20) {
            mul = 9;
        } else if (index < 30) {
            mul = 8;
        } else if (index < 40) {
            mul = 7;
        } else if (index < 50) {
            mul = 6;
        } else if (index < 60) {
            mul = 5;
        } else if (index < 70) {
            mul = 4;
        } else if (index < 80) {
            mul = 3;
        } else if (index < 90) {
            mul = 2;
        } else if (index < 100) {
            mul = 1;
        }
        int pos = mul * m_cellWidth - m_cellWidth / 2;

        return pos;
    }
}
