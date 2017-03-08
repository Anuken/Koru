package io.anuke.koru.desktop;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import io.anuke.koru.Koru;

public class DesktopLauncher {
	public static void main (String[] arg) throws Exception{
		
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		config.setMaximized(true);
		config.setDecorated(true);
		config.setWindowedMode(d.width, d.height-100);
		config.setWindowIcon("sprites/icon.png");
		config.setTitle("Koru");
		//config.useVsync(false);
		
		new Lwjgl3Application(new Koru(), config);
		
	}
}
