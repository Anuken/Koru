package io.anuke.koru.renderers;

import io.anuke.koru.components.RenderComponent;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.ucore.spritesystem.LambdaRenderable.Drawable;
import io.anuke.ucore.spritesystem.Sorter;

public abstract class EntityRenderer{
	protected boolean init;
	protected KoruEntity entity;
	protected RenderComponent render;
	
	abstract protected void render();
	abstract protected void init();
	
	public final void renderInternal(KoruEntity entity, RenderComponent render){
		this.entity = entity;
		this.render = render;
		if(!init){
			init();
			init = true;
		}
		this.render();
	}
	
	public void draw(Drawable d){
		render.list.add(0f, Sorter.object, d);
	}
	
	float shift(float i){
		return flip2() ? -i : i;
	}
	
	public boolean flip(){
		return flip(render.direction);
	}
	
	public boolean flip2(){
		return flip(render.direction) || render.direction == 0;
	}
	
	public String dir(){
		return dir(render.direction);
	}
	
	public int fscl(){
		return flip2() ? 1 : -1;
	}
	
	public static boolean flip(int dir){
		return dir == 3;
	}
	
	public static String dir(int d){
		return d== 0 ? "-f" : ((d == 1 || d == 3) ? "-s" : "-b");
	}
	
	public void onAnimation(AnimationType type){
		
	}
}
