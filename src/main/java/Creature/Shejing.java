package Creature;

import BattleGround.BattleField;

import javax.swing.ImageIcon;
import java.util.ArrayList;

public class Shejing extends Monster {

    public Shejing() {
        super();
    }

    public Shejing(int x, int y, BattleField battleField) {
        super(x, y, battleField);
        setImage(
                new ImageIcon(
                        getClass().getClassLoader().getResource("sj.jpg")
                ).getImage()
        );
    }

}
