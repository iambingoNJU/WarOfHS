package Creature;

import BattleGround.BattleField;

import javax.swing.ImageIcon;
import java.util.ArrayList;

public class Yeye extends Human {

    public Yeye(int x, int y, BattleField battleField) {
        super(x, y, battleField);
        setImage(
                new ImageIcon(
                        getClass().getClassLoader().getResource("yy.jpg")
                ).getImage()
        );
    }

}
