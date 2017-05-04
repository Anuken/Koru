package io.anuke.koru.graphics;

import io.anuke.koru.modules.World;
import io.anuke.koru.utils.Resources;
import io.anuke.ucore.renderables.*;

public class KoruRenderable extends SpriteRenderable{
	
	public KoruRenderable(){
		
	}
	
	public KoruRenderable(String region){
		super(Resources.region(region));
	}
	
	public KoruRenderable region(String name){
		return (KoruRenderable) super.region(Resources.region(name));
	}
	
	public KoruRenderable generateShadow(){
		return (KoruRenderable)RenderPool.get("shadow"
				+ (int) (sprite.getRegionWidth() * 0.8f / 2f + Math.pow(sprite.getRegionWidth(), 1.5f) / 200f) * 2)
						.set(sprite.getX() + (int) (sprite.getWidth() / 2), sprite.getY(), true, true)
						.color(0, 0, 0).layer(-999999);
	}

	public KoruRenderable shadow(){
		return (KoruRenderable) layer(Layers.shadow);
	}
	
	public KoruRenderable light(){
		return (KoruRenderable) layer(Layers.light);
	}
	
	public KoruRenderable dark(){
		return (KoruRenderable) layer(Layers.dark);
	}
	
	public KoruRenderable color(float r, float g, float b, float a){
		sprite.setColor(r, g, b, a);
		return this;
	}

	public KoruRenderable addShadow(RenderableGroup group){
		group.add("shadow", generateShadow());
		return this;
	}

	public KoruRenderable addShadow(RenderableList list){
		list.add(generateShadow());
		return this;
	}

	public KoruRenderable addShadow(RenderableList list, float offset){
		list.add(generateShadow().add(0, offset));
		return this;
	}
	
	public KoruRenderable addShadow(RenderableGroup list, float offset){
		list.add("shadow", generateShadow().add(0, offset));
		return this;
	}
	
	public KoruRenderable tile(int x, int y){
		super.set(x*World.tilesize+World.tilesize/2, y*World.tilesize+World.tilesize/2);
		return this;
	}
	
	public KoruRenderable utile(int x, int y){
		super.set(x*World.tilesize, y*World.tilesize);
		return this;
	}
	
	public KoruRenderable set(float x, float y){
		super.set(x, y);
		return this;
	}
	
	public KoruRenderable set(float x, float y, boolean center){
		return (KoruRenderable)super.set(x, y, center, false);
	}

	public KoruRenderable set(float x, float y, boolean center, boolean block){
		return (KoruRenderable)super.set(x, y, center, block);
	}

	public KoruRenderable centerX(){
		super.centerX();
		return this;
	}
	
	public KoruRenderable sort(Sorter provider){
		super.sort(provider);
		return this;
	}
	
	public KoruRenderable layer(float layer){
		super.layer(layer);
		return this;
	}

	public KoruRenderable centerY(){
		super.centerY();
		return this;
	}

	public KoruRenderable center(){
		super.center();
		return this;
	}

	
	@Override
	public void onFree(){
		RenderPool.free(this);
	}

}
