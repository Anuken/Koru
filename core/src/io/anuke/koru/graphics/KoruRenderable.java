package io.anuke.koru.graphics;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.utils.Resources;
import io.anuke.ucore.spritesystem.RenderableGroup;
import io.anuke.ucore.spritesystem.RenderableList;
import io.anuke.ucore.spritesystem.SpriteRenderable;

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
		return (KoruRenderable) new KoruRenderable("shadow"
				+ (int) (sprite.getRegionWidth() * 0.8f / 2f + Math.pow(sprite.getRegionWidth(), 1.5f) / 200f) * 2)
						.set(sprite.getX() + (int) (sprite.getWidth() / 2), sprite.getY(), true, true)
						.color(new Color(0, 0, 0, 1f)).layer(-999999);
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
	
	@Override
	public void onFree(){
		RenderPool.free(this);
	}

}
