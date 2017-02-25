package io.anuke.koru.generation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import io.anuke.koru.utils.Resources;

//TODO cleanup or remove class
public abstract class GeneratedObject{
	public final String name;
	private transient Pixmap pixmap;
	
	public GeneratedObject(String name){
		this.name = name;
	}
	
	abstract String getObjectType();
	
	/**Server-side only.*/
	public Pixmap getPixmap(){
		return pixmap;
	}
	
	/**Server-side only.*/
	public void loadPixmap(){
		pixmap = new Pixmap(getImageFile());
	}
	
	/**Client-side only.*/
	public void loadTexture(){
		Resources.atlas().addTexture(name, new Texture(getImageFile()));
	}
	
	public void loadPixmap(Pixmap pixmap){
		this.pixmap = pixmap;
	}
	
	public FileHandle getImageFile(){
		return Gdx.files.local("objects/" + getObjectType() + "s/" + name + ".png");
	}
}
