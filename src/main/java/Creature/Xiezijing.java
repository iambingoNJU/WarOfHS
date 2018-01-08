package Creature;

import BattleGround.BattleField;

import javax.swing.ImageIcon;
import java.util.ArrayList;

public class Xiezijing extends Monster {

    public Xiezijing() {
        super();
    }

    public Xiezijing(int x, int y, BattleField battleField) {
        super(x, y, battleField);
        setImage(
                new ImageIcon(
                        getClass().getClassLoader().getResource("xzj.jpg")
                ).getImage()
        );
    }

}
