package core;

public class Board {
    private Cell m_cells[] = new Cell[100];

    public Board(int moveTos[]) {
        for (int i = 0; i < 100; i++) {
            m_cells[i] = new Cell(moveTos[i]);
        }
    }

    protected Cell[] cells() {
        return m_cells;
    }
}
