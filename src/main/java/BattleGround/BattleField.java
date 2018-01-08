package BattleGround;

import Creature.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class BattleField extends JPanel implements Runnable {
    private int h = 0;
    private int w = 0;

    private int state = 0;
    private int ended = 0, winner = 0;

    private ArrayList<Creature> goodGuys = new ArrayList<Creature>();
    private ArrayList<Creature> badGuys = new ArrayList<Creature>();
    private ArrayList<Thread> creatureThreads = new ArrayList<Thread>();

    private String level =
            ".......................\n" +
            ".......................\n" +
            "..1.................X..\n" +
            "..2................L...\n" +
            "..3...............X....\n" +
            "..4..............L.....\n" +
            "..5...............X....\n" +
            "..6................L...\n" +
            "..7.................X..\n" +
            ".....0.........S.......\n" +
            ".......................\n" +
            ".......................\n";

    public BattleField() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        init();
        new Thread(this).start();
    }

    public void init() {
        int x = 0, y = 0;

        for(int i = 0; i < level.length(); i ++) {
            char item = level.charAt(i);

            if(item == '\n') {
                y++;
                if (w < x) w = x;
                x = 0;
            } else if(item == '.') {
                x ++;
            } else if(item == '0') {
                goodGuys.add(new Yeye(x, y, this));
                x ++;
            } else if(item == '1') {
                goodGuys.add(new Huluwa(x, y, this, COLOR.赤, SENIORITY.一));
                x ++;
            } else if(item == '2') {
                goodGuys.add(new Huluwa(x, y, this, COLOR.橙, SENIORITY.二));
                x ++;
            } else if(item == '3') {
                goodGuys.add(new Huluwa(x, y, this, COLOR.黄, SENIORITY.三));
                x ++;
            } else if(item == '4') {
                goodGuys.add(new Huluwa(x, y, this, COLOR.绿, SENIORITY.四));
                x ++;
            } else if(item == '5') {
                goodGuys.add(new Huluwa(x, y, this, COLOR.青, SENIORITY.五));
                x ++;
            } else if(item == '6') {
                goodGuys.add(new Huluwa(x, y, this, COLOR.蓝, SENIORITY.六));
                x ++;
            } else if(item == '7') {
                goodGuys.add(new Huluwa(x, y, this, COLOR.紫, SENIORITY.七));
                x ++;
            } else if(item == 'S') {
                badGuys.add(new Shejing(x, y, this));
                x ++;
            } else if(item == 'X') {
                badGuys.add(new Xiezijing(x, y, this));
                x ++;
            } else if(item == 'L') {
                badGuys.add(new Xiaolouluo(x, y, this));
                x ++;
            }
        }

        h = y;

        setLayout(new GridLayout(h, w));

    }

    public int getH() {
        return this.h;
    }

    public int getW() {
        return this.w;
    }

    public ArrayList<Creature> getGoodGuys() {
        return goodGuys;
    }

    public boolean allDead(ArrayList<Creature> cres) {
        for(int i = 0; i < cres.size(); i ++) {
            if(!cres.get(i).isDead())
                return false;
        }
        return true;
    }

    public ArrayList<Creature> getBadGuys() {
        return badGuys;
    }

    public void draw(Graphics g) {
        ArrayList<Creature> creatures = new ArrayList<Creature>(goodGuys);
        creatures.addAll(badGuys);

        for(int i = 0; i < creatures.size(); i ++) {
            Creature creature = creatures.get(i);
            if(!creature.isDead()) {
                int sx = g.getClipBounds().width / w,
                        sy = g.getClipBounds().height / h;
                g.drawImage(creature.getImage(), creature.getX() * sx, creature.getY() * sy, sx, sy, null);
            }
        }

        g.setColor(Color.red);
        g.setFont(new Font("Serif", Font.BOLD, 30));
        g.drawString("Tips: use SPACE to start/pause.", 30, 30);

        if(ended == 1) {
            if(winner == 1) {
                g.drawString("Game Over! The winner is Huluwa!!!", 30, 60);
            } else {
                g.drawString("Game Over! The winner is Monster...", 30, 60);
            }
        }

    }

    @Override
    public void paint(Graphics g) {
        // 添加背景图片
        super.paint(g);
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("battlebg.jpg"));
        Image img = icon.getImage();
        g.drawImage(img, 0, 0, g.getClipBounds().width, g.getClipBounds().height, null);
        draw(g);
    }

    // 每隔一定时间刷新屏幕
    public void run() {
        try {
            while(true) {
                if(allDead(badGuys)) {
                    ended = 1;
                    winner = 1;
                    creatureStop();

                } else if(allDead(goodGuys)) {
                    ended = 1;
                    winner = 2;
                    creatureStop();
                }
                Thread.sleep(50);
                repaint();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void creatureRun() {
        ArrayList<Creature> creatures = new ArrayList<Creature>(goodGuys);
        creatures.addAll(badGuys);
        for (int i = 0; i < creatures.size(); i++) {
            Thread t = new Thread(creatures.get(i));
            t.start();
            creatureThreads.add(t);
        }
    }

    public void creatureStop() {
        for (int i = 0; i < creatureThreads.size(); i++) {
            creatureThreads.get(i).interrupt();
        }
        creatureThreads.clear();
    }


    public boolean outOfBound(int y, int x) {
        return ((x < 0) || (x >= w) || (y < 0) || (y >= h));
    }

    public synchronized CreType[][] getMap() {
        CreType[][] map = new CreType[h][w];
        for(int i = 0; i < h; i ++) {
            for(int j = 0; j < w; j ++) {
                map[i][j] = CreType.NONE;
            }
        }

        ArrayList<Creature> creatures = new ArrayList<Creature>(goodGuys);
        creatures.addAll(badGuys);

        for(int i = 0; i < creatures.size(); i ++) {
            Creature cre = creatures.get(i);
            if(outOfBound(cre.getY(), cre.getX())) {
                continue;
            }

            if(!cre.isDead()) {
                if (cre instanceof Huluwa) {
                    map[cre.getY()][cre.getX()] = CreType.valueOf(
                            cre.getClass().getSimpleName().toUpperCase() + (((Huluwa) cre).getColor().ordinal() + 1)
                    );
                } else {
                    map[cre.getY()][cre.getX()] = CreType.valueOf(cre.getClass().getSimpleName().toUpperCase());
                }
            }
        }

        return map;
    }

    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if(key == KeyEvent.VK_SPACE) {
                if(state == 0) {
                    state = 1;
                    creatureRun();
                } else if(state == 1) {
                    state = 0;
                    creatureStop();
                }
            }
        }
    }

}
