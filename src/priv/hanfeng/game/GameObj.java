package priv.hanfeng.game;

import java.awt.*;

public class GameObj {
    //坐标
    int x;
    int y;
    double speed;//速度
    //长宽
    int height;
    int width;

    public GameObj() {
    }

    GameObj(int x, int y, double speed, int height, int width) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.height = height;
        this.width = width;
    }

    public void paint(Graphics g) {}

}
