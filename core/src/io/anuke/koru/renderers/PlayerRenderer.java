package io.anuke.koru.renderers;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.components.ConnectionComponent;
import io.anuke.koru.graphics.Draw;

public class PlayerRenderer extends EntityRenderer{
	AnimationType animation;
	float duration;
	
	@Override
	public void render(){
		
	}
	
	@Override
	public void init(){
		
		draw(l->{
			l.layer = entity.getY();
			Draw.grect("crab-" + (render.direction == 1 || render.direction == 3 ? "s" : render.direction == 2 ? "b" : "f")
				+(render.walkframe > 0 ? "-"+((int)(render.walkframe/7))%3 : ""), 
					entity.getX() + (render.direction == 3 ? 12 : 0), entity.getY(), render.direction ==3 ? - 12 : 12, 12);
			
			Draw.tcolor(Color.CORAL);
			
			if(!entity.get(ConnectionComponent.class).local)
			Draw.text(entity.get(ConnectionComponent.class).name, entity.getX(), entity.getY() + 14);
			
			Draw.tcolor();
		});
		
		drawShadow("crab", 1, entity);
	}
	
	@Override
	public void onAnimation(AnimationType type){
		if(type == AnimationType.attack){
			animation = type;
			duration = 10;
		}
	}
}
