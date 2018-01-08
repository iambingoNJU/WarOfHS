package Creature;

import BattleGround.BattleField;
import Thing2D.Thing2D;

import java.util.ArrayList;
import java.util.Random;

public class Creature extends Thing2D implements Runnable {
    protected BattleField bf;
    private Random rand;
    private boolean isDead;

    public Creature(int x, int y, BattleField battleField) {
        super(x, y);
        this.bf = battleField;
        rand = new Random();
        isDead = false;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public synchronized boolean move(int offx, int offy) {
        int x = getX() + offx;
        int y = getY() + offy;
        if((bf.getMap()[y][x] == CreType.NONE) &&
                (x >= 0 && x < bf.getW()) &&
                (y >= 0 && y < bf.getH())) {
            setX(x);
            setY(y);
            return true;
        }

        return false;
    }

    public ArrayList<Creature> getEnamy() {
        return null;
    }

    public ArrayList<Creature> getFriend() {
        return null;
    }

    public synchronized boolean go() {
        ArrayList<Creature> enamy = getEnamy();
        if((enamy == null) || (enamy.size() == 0)) {
            return false;
        }

        // 找到最近的敌人
        int idx = -1, minDis = Integer.MAX_VALUE;
        for(int i = 0; i < enamy.size(); i ++) {
            Creature en = enamy.get(i);
            if(!en.isDead()) {
                int diffx = getX() - en.getX();
                int diffy = getY() - en.getY();
                int dis = diffx * diffx + diffy * diffy;
                if (dis < minDis) {
                    minDis = dis;
                    idx = i;
                }
            }
        }

        if(idx == -1) {
            return false;
        }

        Creature target = enamy.get(idx);
        int diffx = target.getX() - getX();
        int diffy = target.getY() - getY();

        // 敌人在旁边
        if(((Math.abs(diffx) == 0) && (Math.abs(diffy) == 1)) ||
                ((Math.abs(diffx) == 1) && (Math.abs(diffy) == 0))) {
            if(rand.nextBoolean()) {
                target.setDead(true);
                return true;
            } else {
                this.setDead(true);
                return false;
            }
        }

        // 向最近的敌人走进
        if(Math.abs(diffx) < Math.abs(diffy)) {
            if(diffy < 0) {
                if(!move(0, -1)) {
                    if(diffx < 0) {
                        move(-1, 0);
                    } else if(diffx > 0) {
                        move(1, 0);
                    }
                }
            } else if(diffy > 0) {
                if(!move(0, 1)) {
                    if(diffx < 0) {
                        move(-1, 0);
                    } else if(diffx > 0) {
                        move(1, 0);
                    }
                }
            }
        } else {
            if(diffx < 0) {
                if(!move(-1, 0)) {
                    if(diffy < 0) {
                        move(0, -1);
                    } else if(diffy > 0) {
                        move(0, 1);
                    }
                }
            } else if(diffx > 0) {
                if(!move(1, 0)) {
                    if(diffy < 0) {
                        move(0, -1);
                    } else if(diffy > 0) {
                        move(0, 1);
                    }
                }
            }
        }
        return true;
    }

    public void run() {
        try {
            while(go()) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            //System.out.println("Interrupted!");
        }
    }
}
