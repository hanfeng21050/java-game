package priv.hanfeng.game;

import java.awt.*;

public class Bullet extends GameObj {
    private int damage;
    private Image[] bullet = new Image[2];//武器
    private int[] damages = new int[2];//每种武器的伤害
    private int[] speeds = new int[2];//每种武器子弹的速度
    private int type;
    private String direction;
    public Bullet() {
    }

    public Bullet(int x,int y,int width,int type,String direction) {
        bullet[0] = GameUtil.getImage("images/bullet.png");
        bullet[1] = GameUtil.getImage("images/bullet2.png");

        //伤害值
        damages[0] = 5;
        damages[1] = 33 ;

        //子弹速度
        speeds[0] = 20;
        speeds[1] = 43;

        this.direction = direction;
        this.y = y;
        if("R".equals(direction))
            this.x = x + width;
        if("L".equals(direction))
            this.x = x;
        this.type = type;
        speed = speeds[this.type];
        damage = damages[this.type];
    }


    public void move() {

        if("R".equals(direction))
            this.x += speed;
        if("L".equals(direction))
            this.x -= speed;
    }

    public boolean dead() {
        if (x > Constant.GAME_WIDTH || x < 0) return true;
        return false;
    }

    @Override
    public void paint(Graphics g) {
        move();
        g.drawImage(bullet[type], x, y, null);
    }

    public int[] getDamages() {
        return damages;
    }

    public int getType() {
        return type;
    }

    public String getDirection() {
        return direction;
    }
}
