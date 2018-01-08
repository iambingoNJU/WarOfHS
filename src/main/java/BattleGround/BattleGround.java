package BattleGround;

import javax.swing.*;
import java.awt.*;

public class BattleGround extends JFrame {
    public BattleGround() {
    }

    public void battle(boolean mode) {

        BattleField battleField = new BattleField(mode);
        add(battleField);

        setTitle("葫芦娃大战蛇精");
        setUndecorated(false);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setVisible(true);
    }
}
