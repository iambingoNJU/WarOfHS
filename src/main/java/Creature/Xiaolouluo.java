package Creature;

import BattleGround.BattleField;

import javax.swing.ImageIcon;
import java.util.ArrayList;

public class Xiaolouluo extends Monster {

    public Xiaolouluo(int x, int y, BattleField battleField) {
        super(x, y, battleField);
        setImage(
                new ImageIcon(
                        getClass().getClassLoader().getResource("xll.jpg")
                ).getImage()
        );
    }

}
