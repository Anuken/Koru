package io.anuke.koru.graphics;

import io.anuke.koru.world.Tile;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.facet.*;

public class KoruRenderable extends SpriteFacet{
	
	public KoruRenderable(){
		
	}
	
	public KoruRenderable(String region){
		super(region);
	}
	
	public KoruRenderable region(String name){
		return (KoruRenderable) super.region(Draw.region(name));
	}
	
	public KoruRenderable generateShadow(){
		return (KoruRenderable)RenderPool.get("shadow"
				+ (int) (sprite.getRegionWidth() * 0.8f / 2f + Math.pow(sprite.getRegionWidth(), 1.5f) / 200f) * 2)
						.set(sprite.getX() + (int) (sprite.getWidth() / 2), sprite.getY(), true, true)
						.color(0, 0, 0).layer(-999999);
	}

	public KoruRenderable shadow(){
		return (KoruRenderable) layer(Sorter.shadow);
	}
	
	public KoruRenderable light(){
		return (KoruRenderable) layer(Sorter.light);
	}
	
	public KoruRenderable dark(){
		return (KoruRenderable) layer(Sorter.dark);
	}
	
	public KoruRenderable color(float r, float g, float b, float a){
		sprite.setColor(r, g, b, a);
		return this;
	}

	public KoruRenderable addShadow(FacetMap group){
		group.add("shadow", generateShadow());
		return this;
	}

	public KoruRenderable addShadow(FacetList list){
		list.add(generateShadow());
		return this;
	}

	public KoruRenderable addShadow(FacetList list, float offset){
		list.add(generateShadow().add(0, offset));
		return this;
	}
	
	public KoruRenderable addShadow(FacetMap list, float offset){
		list.add("shadow", generateShadow().add(0, offset));
		return this;
	}
	
	public KoruRenderable tile(Tile tile){
		super.set(tile.worldx(), tile.worldy());
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
