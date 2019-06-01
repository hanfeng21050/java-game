package priv.hanfeng.game;

        import java.awt.*;

public class Barrier extends GameObj {
    public Barrier() {
    }

    public Barrier(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public void paint(Graphics g) {
        g.setColor(Color.magenta);
        g.drawRect(x-1,y-1,width+2,height+2);

        g.setColor(Color.decode("#5f3c23"));
        g.drawRect(x, y, width, height);
        g.fillRect(x, y, width, height);
    }
}
