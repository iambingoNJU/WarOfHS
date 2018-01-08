package Launcher;

import BattleGround.BattleGround;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameLauncher extends JFrame {
    final int BTN_WIDTH = 100;
    final int BTN_HEIGHT = 30;
    final int WINDOW_WIDTH = 600;
    final int WINDOW_HEIGHT = 600;

    public void launch() {

        JPanel jPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                // 添加背景图片
                ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("bg.jpg"));
                Image img = icon.getImage();
                g.drawImage(img, 0, 0, g.getClipBounds().width, g.getClipBounds().height, null);
            }
        };

        // 添加按钮
        JButton startBtn = new JButton("开始游戏"),
                replayBtn = new JButton("回放游戏"),
                aboutBtn = new JButton("关于游戏"),
                exitBtn = new JButton("退出游戏");

        startBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new BattleGround().battle();
            }
        });

        replayBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO:
            }
        });

        aboutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Version: 1.0\nAuthor: bingo", "关于游戏", JOptionPane.PLAIN_MESSAGE);
            }
        });

        exitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JButton[] btnList = { startBtn, replayBtn, aboutBtn, exitBtn };

        jPanel.setLayout(null);

        for(int i = 0; i < btnList.length; i ++) {

            btnList[i].setBounds(
                    (WINDOW_WIDTH - BTN_WIDTH) / 2,
                    WINDOW_HEIGHT / 3 + i * (BTN_HEIGHT + 15),
                    BTN_WIDTH,
                    BTN_HEIGHT
            );
            jPanel.add(btnList[i]);
        }

        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);


        add(jPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

}
