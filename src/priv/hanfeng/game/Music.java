package priv.hanfeng.game;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Music {
    private AudioClip aau;
    private URL url;

    Music(String path) {
        try {
            url = getClass().getResource(path); //解析地址
            aau = Applet.newAudioClip(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loop() {
        aau.loop();
    }

    public void play(){aau.play();}

    public void stop() {
        aau.stop();
    }
}