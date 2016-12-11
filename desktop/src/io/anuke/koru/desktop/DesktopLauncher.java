package io.anuke.koru.desktop;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import io.anuke.koru.Koru;
import io.anuke.koru.network.KryoClient;

public class DesktopLauncher {
	public static void main (String[] arg) throws Exception{
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		config.setMaximized(true);
		config.setDecorated(true);
		config.setWindowedMode(d.width, d.height-100);
		config.setTitle("Koru");
		config.useVsync(true);
		
		
		new Lwjgl3Application(new Koru(new KryoClient()), config);
		
	}
	
	  public static void setIcon(long window, BufferedImage img) {
	        GLFWImage image = GLFWImage.malloc();
	        image.set(img.getWidth(), img.getHeight(), loadImageToByteBuffer(img));

	        GLFWImage.Buffer images = GLFWImage.malloc(1);
	        images.put(0, image);

	        GLFW.glfwSetWindowIcon(window, images);

	        images.free();
	        image.free();
	    }

	    private static ByteBuffer loadImageToByteBuffer(final BufferedImage image) {
	        final byte[] buffer = new byte[image.getWidth() * image.getHeight() * 4];
	        int counter = 0;
	        for (int i = 0; i < image.getHeight(); i++) {
	            for (int j = 0; j < image.getWidth(); j++) {
	                final int c = image.getRGB(j, i);
	                buffer[counter + 0] = (byte) (c << 8 >> 24);
	                buffer[counter + 1] = (byte) (c << 16 >> 24);
	                buffer[counter + 2] = (byte) (c << 24 >> 24);
	                buffer[counter + 3] = (byte) (c >> 24);
	                counter += 4;
	            }
	        }
	        ByteBuffer b = ByteBuffer.allocateDirect(buffer.length);
	        b.put(buffer);
	        return b;
	    }
}
