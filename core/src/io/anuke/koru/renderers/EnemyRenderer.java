package io.anuke.koru.renderers;

import io.anuke.ucore.core.Draw;

public class EnemyRenderer extends EntityRenderer{

	@Override
	protected void render(){
		
	}

	@Override
	protected void init(){
		draw(l->{
			l.layer = entity.getY();
			Draw.grect("genericmonster", entity.getX(), entity.getY());
		});
		
		drawShadow("genericmonster", 0, entity);
	}

}
