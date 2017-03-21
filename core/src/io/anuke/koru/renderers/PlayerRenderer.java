package io.anuke.koru.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;

import io.anuke.koru.components.ConnectionComponent;
import io.anuke.koru.graphics.Draw;
import io.anuke.koru.utils.Resources;

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
			
			if(!entity.get(ConnectionComponent.class).local){
				Resources.font2().setColor(Color.YELLOW);
				
				Resources.font2().getData().setScale(1f/2f);
				Resources.font2().draw(Resources.batch(), entity.get(ConnectionComponent.class).name, entity.getX(), entity.getY() + 14, 0, Align.center, false);
			
				Resources.font2().setColor(Color.WHITE);
			}
			
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
