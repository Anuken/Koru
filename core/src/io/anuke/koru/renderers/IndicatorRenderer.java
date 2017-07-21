package io.anuke.koru.renderers;

import com.badlogic.gdx.utils.Align;

import io.anuke.koru.Koru;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.traits.ChildComponent;
import io.anuke.koru.traits.FadeComponent;
import io.anuke.koru.traits.TextTrait;
import io.anuke.ucore.core.Draw;

public class IndicatorRenderer extends EntityRenderer{
	
	@Override
	protected void render(){
	}

	@Override
	protected void init(){
		
		draw((p)->{
			ChildComponent child = entity.get(ChildComponent.class);
			KoruEntity parent = Koru.engine.getEntity(child.parent);
			if(parent == null) return;
			
			p.layer = parent.getY()-1;
			
			Draw.tcolor(1f-entity.get(FadeComponent.class).life/entity.get(FadeComponent.class).lifetime);
			Draw.text(entity.get(TextTrait.class).text, parent.getX(), parent.getY() + parent.collider().height*1.5f
					+ entity.get(FadeComponent.class).life/6f, Align.center);
			Draw.tcolor();
		});
	}

}
