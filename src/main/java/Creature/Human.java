package Creature;

import BattleGround.BattleField;

import java.util.ArrayList;

public class Human extends Creature {
    public Human() {
        super();
    }

    public  Human(int x, int y, BattleField battleField) {
        super(x, y, battleField);
    }

    @Override
    public ArrayList<Creature> getEnamy() {
        return bf.getBadGuys();
    }

    @Override
    public ArrayList<Creature> getFriend() {
        return bf.getGoodGuys();
    }
}
