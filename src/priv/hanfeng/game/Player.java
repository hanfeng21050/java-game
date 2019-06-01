package priv.hanfeng.game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player extends GameObj {
    private boolean up;
    private boolean left;
    private boolean right;
    private boolean k;//判断是否正在跳跃的过程中
    private double speedY;//跳跃向上的初速度
    private String direction;//表示角色面向左边还是右边
    private int health;//生命值
    private int player;//角色
    private ArrayList<Barrier> barriers;
    private Image[] playerImage = new Image[2];
    private Barrier barrier;//跳板集合
    private Gun gun;
    private ArrayList<Bullet> enemyBullets = new ArrayList<>();//表示对手的子弹集合

    public Player() {

    }

    public Player(int x, int y, double speed, int height, int width, ArrayList<Barrier> barriers, int player) {
        super(x, y, speed, height, width);
        initPlayer(x, y, speed, height, width, barriers, player);
        new Thread(r_jump).start();
        t_left.start();
        t_right.start();
    }

    /**
     * 用于初始化玩家角色
     *
     * @param x
     * @param y
     * @param speed
     * @param height
     * @param width
     * @param barriers
     * @param player
     */
    public void initPlayer(int x, int y, double speed, int height, int width, ArrayList<Barrier> barriers,int player) {
        this.x = x;
        this.y = y;
        this.player = player;
        this.barriers = barriers;
        gun = new Gun(x, y, width, height);
        //根据玩家加载图片
        if (player == 1) {
            this.direction = "R";
            playerImage[0] = GameUtil.getImage("images/playerR.png");
            playerImage[1] = GameUtil.getImage("images/playerL.png");
        } else if (player == 2) {
            this.direction = "L";
            playerImage[0] = GameUtil.getImage("images/player2R.png");
            playerImage[1] = GameUtil.getImage("images/player2L.png");
        }
        up = true;
        left = false;
        right = false;
        k = true;
        health = Constant.HEALTH;
    }

    public int getPlayer() {
        return player;
    }

    public Gun getGun() {
        return gun;
    }
    public String getDirection() {
        return direction;
    }

    /**
     * 按键监听
     *
     * @param e
     */
    public void PressAction(KeyEvent e) {
        gun.pressAction(e);//对枪的监听
        int key = e.getKeyCode();
        //玩家1
        if (player == 1) {
            if (key == KeyEvent.VK_A) {
                left = true;
                direction = "L";
            }
            if (key == KeyEvent.VK_D) {
                right = true;
                direction = "R";
            }
            if (key == KeyEvent.VK_W) {
                //如果角色没落地，则不允许再次跳跃
                if (k) {
                    up = true;
                    k = false;
                    speedY = Constant.SPEED;
                }
            }
        }
        //玩家2
        if (player == 2) {
            if (key == KeyEvent.VK_LEFT) {
                direction = "L";
                left = true;

            }
            if (key == KeyEvent.VK_RIGHT) {
                direction = "R";
                right = true;
            }
            if (key == KeyEvent.VK_UP) {
                //如果角色没落地，则不允许再次跳跃
                if (k) {
                    up = true;
                    k = false;
                    speedY = Constant.SPEED;
                }
            }
        }
    }

    /**
     * 按键监听
     *
     * @param e
     */
    public void LeaveAction(KeyEvent e) {
        int key = e.getKeyCode();
        if (player == 1) {
            if (key == KeyEvent.VK_A) {
                left = false;
            }
            if (key == KeyEvent.VK_D) {
                right = false;
            }
            if (key == KeyEvent.VK_W) {
            }
        }
        if (player == 2) {
            if (key == KeyEvent.VK_LEFT) {
                left = false;
            }
            if (key == KeyEvent.VK_RIGHT) {
                right = false;
            }
            if (key == KeyEvent.VK_UP) {
            }
        }
    }

    /**
     * 移动
     */
    public void move() {
        //更新枪的位置

        if (left) {
            this.x -= speed;
        }
        if (right) {
        this.x += speed;
         }
        if (barrier != null) {
        //如果角色的坐标没有在挡板的x坐标之内，下落
        if (x + width < barrier.x || x > barrier.x + barrier.width) {
            up = true;
        }
        gun.move(this);
    }

    }

    /**
     * 跳跃过程
     */
    public void jump(ArrayList<Barrier> barriers) {
        speedY -= (double) Constant.GRAVITY / 30;
        y -= speedY;
        if (speedY < 0) {
            if (barrier != null)
                if (isCrash(barriers) && y + height - barrier.y < 5)//判断是否跳到了跳板上
                {
                    k = true;
                    up = false;
                    y = barrier.y - height;//纠正角色的坐标
                }
        }

        //角色落到了下边界
        if (y > 900 - height) {
            k = true;
            up = false;
            y = 900 - height;
        }
        //更新枪的位置
        gun.move(this);
    }

    /**
     * 当被子弹命中时，被击退一小段距离
     */
    public void repel(Bullet bullet)
    {
        //搁置
    }

    /**
     * 检测是否被击中
     *
     * @param bullets
     */
    public void hitted(ArrayList<Bullet> bullets) {
        if (bullets != null) {
            enemyBullets = bullets;
            for (int i = 0; i < bullets.size(); i++) {
                if (bullets.get(i).y + bullets.get(i).width > y && bullets.get(i).y < y + height) {
                    if (bullets.size() != i && bullets.size() != 0) {
                        //aSaaystem.out.println(Math.abs(x + width / 2 - bullets.get(i).x) +"--"+"---"+ width / 2);
                        if (Math.abs(x + width / 2 - bullets.get(i).x) < width / 2) {
                           // System.out.println(1);
                            health -= bullets.get(i).getDamages()[bullets.get(i).getType()];
                            bullets.remove(i);
                            i--;
                        } else if (bullets.get(i).dead()) {
                            bullets.remove(i);
                            i--;
                        }
                    }
                }
            }
        }
    }

    /**
     * 检测是否死亡
     */
    public boolean dead() {
        if (health <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否和地图障碍相碰
     */
    public boolean isCrash(ArrayList<Barrier> barriers) {
        for (int i = 0; i < barriers.size(); i++) {
            //如果角色的横坐标在一块挡板之间
            if (x + width > barriers.get(i).x && x < barriers.get(i).x + barriers.get(i).width) {
                //如果角色的在木板之上，且木板与角色的距离大于木板的宽度
                if (y + height > barriers.get(i).y - barriers.get(i).height) {
                    barrier = barriers.get(i);
                    return true;
                }

            }
        }
        return false;
    }

    /**
     * 线程控制跳跃
     */
    Runnable r_jump = new Runnable() {
        @Override
        public void run() {
            while (true) {
                isCrash(barriers);
                try {
                    if (up) {
                        jump(barriers);
                    }
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    Thread t_right = new Thread() {
        @Override
        public void run() {
            while (true) {
                //移动
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (right == true && x < 1600 - width) {
                    move();
                }
            }
        }
    };
    Thread t_left = new Thread() {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (left == true && x > 0) {
                    move();
                }
            }
        }
    };

    @Override
    public void paint(Graphics g) {
        //检测角色是否被子弹击中
        hitted(enemyBullets);
        //检测角色是否死亡
        dead();


        if ("R".equals(direction)) {
            //画角色
            g.drawImage(playerImage[0], this.x, this.y, null);
        } else if ("L".equals(direction)) {
            //画角色
            g.drawImage(playerImage[1], this.x, this.y, null);

        }
        gun.paint(g);//画枪
        //画生命值条
        g.setColor(Color.GREEN);
        g.drawRect(this.x, this.y - 35, Constant.SAVE / 10, 10);
        g.fillRect(this.x, this.y - 35, health, 10);
    }

}
