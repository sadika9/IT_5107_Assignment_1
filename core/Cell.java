package core;

public class Cell {
    private int m_moveTo;

    public Cell(int moveTo) {
        m_moveTo = moveTo;
    }

    public int moveTo() {
        return m_moveTo;
    }
}
