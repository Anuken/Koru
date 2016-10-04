package io.anuke.koru.generation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

public class GeneratedObject{
	public final String type, name;
	private Pixmap pixmap;
	
	public GeneratedObject(String type, String name){
		this.type = type;
		this.name = name;
	}
	
	/**Server-side only.*/
	public Pixmap getPixmap(){
		return pixmap;
	}
	
	/**Server-side only.*/
	public void loadPixmap(){
		pixmap = new Pixmap(getImageFile());
	}
	
	public void loadPixmap(Pixmap pixmap){
		this.pixmap = pixmap;
	}
	
	public FileHandle getImageFile(){
		return Gdx.files.local("downloaded/" + type + "s/" + name + ".png");
	}
}
