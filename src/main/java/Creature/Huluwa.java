package Creature;

import BattleGround.BattleField;

import javax.swing.ImageIcon;
import java.util.ArrayList;

public class Huluwa extends Human {
    private COLOR color;
    private SENIORITY seniority;

    public Huluwa() {
        super();
    }

    public Huluwa(int x, int y, BattleField battleField, COLOR color, SENIORITY seniority) {
        super(x, y, battleField);
        this.color = color;
        this.seniority = seniority;

        setImage(
                new ImageIcon(
                        getClass().getClassLoader().getResource(
                        "hlw" + (getSeniority().ordinal() + 1) + ".jpg")
                ).getImage()
        );
    }

    public COLOR getColor() {
        return color;
    }

    public void setColor(COLOR color) {
        this.color = color;
    }

    public SENIORITY getSeniority() {
        return seniority;
    }

    public void setSeniority(SENIORITY seniority) {
        this.seniority = seniority;
    }

}
