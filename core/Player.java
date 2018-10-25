package core;

import java.util.ArrayList;

public class Player {
    private int m_position = -1;
    private ArrayList m_lastSteps = new ArrayList();
    private String m_name;

    public boolean isEnteredToBoard() {
        return (m_position != -1);
    }

    public int position() {
        return m_position;
    }

    protected void setPosition(int position) {
        m_position = position;
    }

    public ArrayList lastSteps() {
        return m_lastSteps;
    }

    protected void setLastSteps(ArrayList lastSteps) {
        m_lastSteps = lastSteps;
    }

    public String name() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }
}
