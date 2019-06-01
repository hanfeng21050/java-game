package priv.hanfeng.game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

/**
 * 滑稽大对决1.0
 * 完成时间 2019.5.15
 * 韩锋
 */
public class GameFrame extends Frame implements KeyListener {
    boolean isStart;
    boolean isEnd;
    private Image offScreenImage = null;
    Image backPng = GameUtil.getImage("images/background.jpg");//背景图片1
    Music pauseBGM;//背景音乐
    Music startBGM;//背景音乐
    Player player1;//玩家1
    Player player2;//玩家1
    Map map;
    Vector<Funny> fun = new Vector<Funny>();//储存滑稽的容器;

    public GameFrame() throws HeadlessException {
        setTitle("滑稽大对决1.0");
        setVisible(true);
        setSize(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
        setLocation(150, 100);
        setResizable(false);
        setAlwaysOnTop(true);
        addWindowListener(new WindowAdapter() {//确保按下叉叉的时候退出程序
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });//关闭窗口
        isStart = false;
        isEnd = false;
        pauseBGM = new Music("pauseBGM.wav");
        startBGM = new Music("startBGM.wav");
        map = new Map();
        player1 = new Player(100, Constant.GAME_HEIGHT - 96, 5, 96, 80, map.getBarriers(), 1);
        player2 = new Player(1500, Constant.GAME_HEIGHT - 96, 5, 96, 80, map.getBarriers(), 2);
        player1.hitted(player2.getGun().bullets);
        player2.hitted(player1.getGun().bullets);
        pauseBGM.loop();
        paintThread.start();
        this.addKeyListener(this);//添加监听
    }

    /**
     * 初始化
     */
    public void initFrame() {
        pauseBGM.loop();//播放音乐
        isStart = false;
        isEnd = false;
        player1.getGun().bullets.clear();
        player2.getGun().bullets.clear();
        player1.initPlayer(100, Constant.GAME_HEIGHT - 96, 5, 96, 80, map.getBarriers(), 1);
        player2.initPlayer(1500, Constant.GAME_HEIGHT - 96, 5, 96, 80, map.getBarriers(), 2);
        player1.hitted(player2.getGun().bullets);
        player2.hitted(player1.getGun().bullets);

    }

    /**
     * 双缓冲解决闪屏
     */
    public void update(Graphics g) {
        if (offScreenImage == null)
            offScreenImage = this.createImage(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);// 这是游戏窗口的宽度和高度
        Graphics gOff = offScreenImage.getGraphics();
        paint(gOff);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    public void paint(Graphics g) {
        //防止空指针异常
        if (player1 == null && player1 == null) {
            return;
        }
        //判断游戏结束
        if (player1.dead() || player2.dead()) {
            isEnd = true;
        }


        /**
         * 判断游戏是否结束
         */
        if (isEnd) {
            startBGM.stop();
            g.setColor(Color.decode("#f58220"));
            g.setFont(new Font("", Font.BOLD, 50));
            g.drawImage(backPng, 0, 0, Constant.GAME_WIDTH, Constant.GAME_HEIGHT, null);//绘制背景图
            if (player1.dead())
                g.drawString("player2 win!", 650, 400);
            if (player2.dead())
                g.drawString("player1 win!", 650, 400);
            g.drawString("Press R To Restart", 550, 450);
        }

        /**
         * 画滑稽表情、开始之前的界面
         */
        if (!isStart && !isEnd) {
            //每10ms产生一个Funny对象，并添加到容器
            if (System.currentTimeMillis() % 10 == 0) {
                Funny e = new Funny(1, 700, 0, 0, 0);
                fun.add(e);
            }
            //画开始/暂停背景
            g.drawImage(backPng, 0, 0, Constant.GAME_WIDTH, Constant.GAME_HEIGHT, null);//绘制背景图
            g.setColor(Color.decode("#f58220"));
            g.setFont(new Font("", Font.BOLD, 50));
            g.drawString("Press Space To Start!", 500, 400);
            //画Funny对象
            for (int i = 0; i < fun.size(); i++) {
                fun.get(i).paint(g);
                if (fun.get(i).x <= 0 || fun.get(i).x > Constant.GAME_WIDTH) fun.remove(i);
            }

        }

        /**
         * 游戏开始
         */
        if (isStart && !isEnd) {
            /**
             * 画地图
             */
            map.paint(g);

            /**
             * 画玩家
             */
            player1.paint(g);
            player2.paint(g);
        }


    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (keycode == KeyEvent.VK_SPACE) {
            if (!isStart) {
                pauseBGM.stop();
                startBGM.play();
                isStart = true;
            }
        }
        if (keycode == KeyEvent.VK_R && isEnd) {
            initFrame();
        }
        player1.PressAction(e);
        player2.PressAction(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player1.LeaveAction(e);
        player2.LeaveAction(e);
    }

    /**
     * 控制绘图的线程
     */
    Thread paintThread = new Thread() {
        @Override
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(15); // 1s = 1000ms 参数设置为15时帧率大约是60帧，大部分屏幕刷新率是60hz
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    public static void main(String[] args) {
        GameFrame f = new GameFrame();
    }
}
