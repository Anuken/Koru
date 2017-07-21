package io.anuke.koru.renderers;

import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.traits.RenderComponent;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.renderables.FuncRenderable.DrawFunc;
import io.anuke.ucore.renderables.Sorter;

public abstract class EntityRenderer{
	protected boolean init;
	protected KoruEntity entity;
	protected RenderComponent render;
	public boolean walking;
	
	protected void render(){}
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
	
	public void draw(DrawFunc d){
		render.list.add(0f, Sorter.object, d);
	}
	
	public void drawShadow(String region, KoruEntity e){
		render.list.add(Sorter.shadow, Sorter.tile, (l)->{
			l.layer = Sorter.shadow;
			Draw.rect("shadow"
					+ (int) (Draw.region(region).getRegionWidth() * 0.8f / 2f + Math.pow(Draw.region(region).getRegionWidth(), 1.5f) / 200f) * 2, e.getX(), e.getY());
		});
	}
	
	public void drawShadow(String region, float yoffset, KoruEntity e){
		render.list.add(Sorter.shadow, Sorter.tile, (l)->{
			l.layer = Sorter.shadow;
			Draw.color();
			Draw.rect("shadow"
					+ (int) (Draw.region(region).getRegionWidth() * 0.8f / 2f + Math.pow(Draw.region(region).getRegionWidth(), 1.5f) / 200f) * 2, e.getX(), yoffset+e.getY());
		});
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
