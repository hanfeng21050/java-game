package priv.hanfeng.game;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Gun extends GameObj {
    static Image[] gunsR = new Image[2];
    static Image[] gunsL = new Image[2];
    static int[] gunLoading = new int[2];//每种枪的子弹装弹速度
    String direction;
    //当前坐标
    private Player player;
    private int type;//当前枪的种类
    ArrayList<Bullet> bullets = new ArrayList<>();
    private int bulletSave;//子弹加载
    private int loadingTime;//枪的子弹加载速度
    private boolean isLoading;//装弹
    //下面两个变量保证在枪发射子弹 马上切枪时 子弹类型不会变化
    private int flag;
    private int ttype;
    Image currentGun;
    Music shooting1;
    Music shooting2;

    static {
        gunsR[0] = GameUtil.getImage("images/gun1R.png");
        gunsL[0] = GameUtil.getImage("images/gun1L.png");
        gunsR[1] = GameUtil.getImage("images/gun2R.png");
        gunsL[1] = GameUtil.getImage("images/gun2L.png");
        gunLoading[0] = 30;
        gunLoading[1] = 8;
    }

    public Gun() {
    }

    public Gun(int x,int y,int width,int height) {
        currentGun = gunsR[0];
        type = 0;
        this.width = 150;
        this.height = 90;
        direction = null;
        player = null;
        isLoading = false;
        bulletSave = 1;
        loadingTime = gunLoading[0];
        this.x = x + width / 3;
        this.y = y + height / 3;
        shooting1 = new Music("shooting1.wav");
        shooting2 = new Music("shooting2.wav");
        t_gun.start();
        loadingThread.start();
    }

    /**
     * 射击
     */
    public void Shoot() {
        if (!isLoading) {
            flag = 1;
            isLoading = true;
        }

    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    /**
     * 依靠玩家的坐标和方向来更新自己的位置及方向
     * @param player
     */
    public void move(Player player) {
        if(player != null) {
            this.player = player;
            this.direction = player.getDirection();
            if ("R".equals(direction)) {

                currentGun = gunsR[type];
                x = player.x + player.width / 3;
                y = player.y + player.height / 3;
            } else if ("L".equals(direction)) {
                currentGun = gunsL[type];
                x = player.x - width + player.width / 3 * 2;
                y = player.y + player.height / 3;
            }
        }
    }

    //按下换枪
    public void pressAction(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(player != null)
        {
            if(player.getPlayer() == 1) {
                if (keyCode == KeyEvent.VK_J) {
                    Shoot();
                }
                /**
                 * 当切换枪时,重新加载
                 */
                if (keyCode == KeyEvent.VK_1 && type != 0) {
                    if (bullets.size() == 0) {
                        if("R".equals(direction)){
                            currentGun = gunsR[0];
                        }else {
                            currentGun = gunsL[0];
                        }

                        type = 0;
                        bulletSave = 0;
                    }
                }
                if (keyCode == KeyEvent.VK_2 && type != 1) {
                    if (bullets.size() == 0) {
                        if("R".equals(direction)){
                            currentGun = gunsR[1];
                        }else {
                            currentGun = gunsL[1];
                        }
                        type = 1;
                        bulletSave = 0;
                    }
                }
            }
            else if(player.getPlayer() == 2)
            {
                if (keyCode == KeyEvent.VK_NUMPAD0) {
                    Shoot();
                }
                /**
                 * 当切换枪时,重新加载
                 */
                if (keyCode == KeyEvent.VK_NUMPAD1 && type != 0) {
                    if (bullets.size() == 0) {
                        if("R".equals(direction)){
                            currentGun = gunsR[0];
                        }else {
                            currentGun = gunsL[0];
                        }

                        type = 0;
                        bulletSave = 0;
                    }
                }
                if (keyCode == KeyEvent.VK_NUMPAD2 && type != 1) {
                    if (bullets.size() == 0) {
                        if("R".equals(direction)){
                            currentGun = gunsR[1];
                        }else {
                            currentGun = gunsL[1];
                        }
                        type = 1;
                        bulletSave = 0;
                    }
                }
            }
        }

    }

    /**
     * 保证枪能随着角色移动
     */
    Thread t_gun = new Thread() {
        @Override
        public void run() {
            while (true) {
                move(player);
            }
        }
    };

    /**
     * 装弹的线程
     */
    Thread loadingThread = new Thread() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //这个if语句保证在枪1发射子弹 马上切枪时 子弹类型不会变化
                if(flag==1)
                {
                    ttype=type;
                    flag = 0;
                }

                //如果装弹完成
                if (isLoading) {
                    if (type == 0)//如果是枪0
                    {
                        if (bulletSave >= Constant.SAVE) {
                            shooting1.play();
                            bulletSave = 0;
                            for (int i = 0; i < 3; i++) {
                                try {
                                    currentThread().sleep(120);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                bullets.add(new Bullet(x,y,width,ttype,direction));
                            }
                        }
                        isLoading = false;
                    }
                    if (type == 1)//如果是枪1
                    {

                        if (bulletSave >= Constant.SAVE) {
                            shooting2.play();
                            bullets.add(new Bullet(x,y,width,type,direction));
                            bulletSave = 0;
                        }
                        isLoading = false;
                    }
                }
            }
        }
    };

    @Override
    public void paint(Graphics g) {
        if (type == 0 || type == 1) {
            /**
             * 更新子弹加载速度
             */
            loadingTime = gunLoading[type];

            /**
             * 更新子弹充能
             */
            if (bulletSave < Constant.SAVE)
                bulletSave += loadingTime;


            /**
             * 画枪
             */
            if(direction != null)
            {
                g.drawImage(currentGun, x, y, null);
            }



            /**
             * 画缓冲条
             */
            if(player != null)
            {
                g.setColor(Color.red);
                g.drawRect(player.x, player.y - 20, Constant.SAVE/10, 10);
                g.fillRect(player.x, player.y - 20, bulletSave/10, 10);
            }

        }

        /**
         * 画子弹
         */

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).paint(g);
            if (bullets.get(i).dead()) {
                bullets.remove(i);//子弹出边界，清除

            }
        }

    }

    public int getType() {
        return type;
    }

}
