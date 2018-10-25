package core;

import java.util.Random;

public class Die {
    private int m_result = 1;
    private Random generator = new Random();

    public int result() {
        return m_result;
    }

    protected void roll() {
        m_result = generator.nextInt(6) + 1;
    }
}
