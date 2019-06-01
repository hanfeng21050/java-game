package priv.hanfeng.game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * 用于加载图片的工具类
 */
public class GameUtil {
    public GameUtil() {
    }

    public static Image getImage(String path) {
        BufferedImage bi = null;
        URL u = GameUtil.class.getClassLoader().getResource(path);
        try {
            bi = ImageIO.read(u);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bi;
    }
}
