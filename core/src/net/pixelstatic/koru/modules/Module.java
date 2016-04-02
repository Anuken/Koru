package net.pixelstatic.koru.modules;

import net.pixelstatic.koru.Koru;

public abstract class Module {
	Koru koru;

	public abstract void update();
	
	public void init(){}

	public Module(Koru k) {
		this.koru = k;
	}

	public <T extends Module> T getModule(Class<T> c) {
		return koru.getModule(c);
	}
	
	public void resize(int width, int height){}
	
}
