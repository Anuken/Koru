package net.pixelstatic.koru.desktop;

import java.awt.Toolkit;

import net.pixelstatic.koru.Koru;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = true;
		config.width = Toolkit.getDefaultToolkit().getScreenSize().width;

		config.height = Toolkit.getDefaultToolkit().getScreenSize().height;
		new LwjglApplication(new Koru(new KryoClient()), config);
	}
}
