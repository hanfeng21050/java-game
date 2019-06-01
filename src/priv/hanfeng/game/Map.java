package priv.hanfeng.game;


import java.awt.*;
import java.util.ArrayList;

public class Map {
    private ArrayList<Barrier> barriers = new ArrayList<>();
    Image backPng1 = GameUtil.getImage("images/background2.jpg");//开始后背景图片
    public Map(){
        barriers.add(new Barrier(100,780,10,200));
        barriers.add(new Barrier(400,650,10,200));
        barriers.add(new Barrier(700,520,10,200));
        barriers.add(new Barrier(1000,650,10,200));
        barriers.add(new Barrier(1300,780,10,200));
    }

    public ArrayList<Barrier> getBarriers() {
        return barriers;
    }

    public void paint(Graphics g)
    {
        g.drawImage(backPng1,0,0,null);
        for (int i = 0; i < barriers.size(); i++) {
            barriers.get(i).paint(g);
        }
    }
}
