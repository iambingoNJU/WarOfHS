package Creature;

import BattleGround.BattleField;

import java.util.ArrayList;

public class Monster extends Creature {
    public Monster() {
        super();
    }

    public Monster(int x, int y, BattleField battleField) {
        super(x, y, battleField);
    }

    @Override
    public ArrayList<Creature> getEnamy() {
        return bf.getGoodGuys();
    }

    @Override
    public ArrayList<Creature> getFriend() {
        return bf.getBadGuys();
    }
}
