package BattleGround;

import Creature.*;
import Log.Reader;
import Log.Writer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.text.SimpleDateFormat;


public class BattleField extends JPanel implements Runnable {
    private int h = 0;
    private int w = 0;

    private int state = 0;
    private int ended = 0, winner = 0;

    private int timestamp = 0;
    private Writer writer;
    private String separator = ",";
    private HashMap<String, Image> imageMap = new HashMap<String, Image>();

    private boolean replayMode = false;
    private Reader reader;
    private String line;

    private String recDir = "GameRecords/";

    private ArrayList<Creature> goodGuys = new ArrayList<Creature>();
    private ArrayList<Creature> badGuys = new ArrayList<Creature>();
    private ArrayList<Thread> creatureThreads = new ArrayList<Thread>();

    private String level =
            ".......................\n" +
            ".......................\n" +
            "..1.................X..\n" +
            "..2................L...\n" +
            "..3...............X....\n" +
            "..4....0.........L...S.\n" +
            "..5...............X....\n" +
            "..6................L...\n" +
            "..7.................X..\n" +
            ".......................\n" +
            ".......................\n" +
            ".......................\n";

    public BattleField(boolean mode) {
        addKeyListener(new TAdapter());
        setFocusable(true);
        init();
        replayMode = mode;
        if(replayMode) {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int res = jFileChooser.showOpenDialog(this);
            if(res == JFileChooser.APPROVE_OPTION) {
                reader = new Reader(recDir + jFileChooser.getSelectedFile().getName());
            }
        } else {
            writer = new Writer(recDir + "record" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".log");
        }

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
            } else if((item >= '1') && (item <= '7')) {
                goodGuys.add(new Huluwa(x, y, this, COLOR.values()[item - '1'], SENIORITY.values()[item - '1']));
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


        imageMap.put("Huluwa1", new ImageIcon(getClass().getClassLoader().getResource("hlw1.jpg")).getImage());
        imageMap.put("Huluwa2", new ImageIcon(getClass().getClassLoader().getResource("hlw2.jpg")).getImage());
        imageMap.put("Huluwa3", new ImageIcon(getClass().getClassLoader().getResource("hlw3.jpg")).getImage());
        imageMap.put("Huluwa4", new ImageIcon(getClass().getClassLoader().getResource("hlw4.jpg")).getImage());
        imageMap.put("Huluwa5", new ImageIcon(getClass().getClassLoader().getResource("hlw5.jpg")).getImage());
        imageMap.put("Huluwa6", new ImageIcon(getClass().getClassLoader().getResource("hlw6.jpg")).getImage());
        imageMap.put("Huluwa7", new ImageIcon(getClass().getClassLoader().getResource("hlw7.jpg")).getImage());
        imageMap.put("Yeye", new ImageIcon(getClass().getClassLoader().getResource("yy.jpg")).getImage());
        imageMap.put("Shejing", new ImageIcon(getClass().getClassLoader().getResource("sj.jpg")).getImage());
        imageMap.put("Xiezijing", new ImageIcon(getClass().getClassLoader().getResource("xzj.jpg")).getImage());
        imageMap.put("Xiaolouluo", new ImageIcon(getClass().getClassLoader().getResource("xll.jpg")).getImage());

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
        if(replayMode == false) {
            ArrayList<Creature> creatures = new ArrayList<Creature>(goodGuys);
            creatures.addAll(badGuys);

            for (int i = 0; i < creatures.size(); i++) {
                Creature creature = creatures.get(i);
                if (!creature.isDead()) {
                    int sx = g.getClipBounds().width / w,
                            sy = g.getClipBounds().height / h;
                    g.drawImage(creature.getImage(), creature.getX() * sx, creature.getY() * sy, sx, sy, null);
                    if (ended == 0) {
                        if(creature instanceof Huluwa) {
                            writer.write(timestamp + separator +
                                    creature.getClass().getSimpleName() + (((Huluwa) creature).getColor().ordinal() + 1) + separator +
                                    creature.getX() + separator + creature.getY() + "\n");
                        } else {
                            writer.write(timestamp + separator + creature.getClass().getSimpleName() + separator +
                                    creature.getX() + separator + creature.getY() + "\n");
                        }
                    }
                }
            }

            g.setColor(Color.red);
            g.setFont(new Font("Serif", Font.BOLD, 30));
            g.drawString("Tips: use SPACE to start/pause.", 30, 30);

            if (ended == 1) {
                if (winner == 1) {
                    g.drawString("Game Over! The winner is Huluwa!!!", 30, 60);
                } else {
                    g.drawString("Game Over! The winner is Monster...", 30, 60);
                }
            }

            if(ended == 0) writer.write("\n");

        } else {
            int sx = g.getClipBounds().width / w,
                    sy = g.getClipBounds().height / h;
            line = reader.readLine();
            while((line != null) && (line.length() > 1)) {
                String[] parts = line.split(separator);
                try {
                    g.drawImage( imageMap.get(parts[1]),
                            Integer.valueOf(parts[2]) * sx,
                            Integer.valueOf(parts[3]) * sy,
                            sx, sy, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                line = reader.readLine();
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
            while(ended == 0) {
                if(allDead(badGuys)) {
                    ended = 1;
                    winner = 1;
                    creatureStop();
                    writer.close();
                } else if(allDead(goodGuys)) {
                    ended = 1;
                    winner = 2;
                    creatureStop();
                    writer.close();
                }

                Thread.sleep(50);
                repaint();
                timestamp ++;
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

            if((key == KeyEvent.VK_SPACE) && (ended == 0)) {
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
