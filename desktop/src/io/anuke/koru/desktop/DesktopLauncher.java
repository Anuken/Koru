package io.anuke.koru.desktop;

import io.anuke.koru.Koru;

import java.awt.Toolkit;

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
