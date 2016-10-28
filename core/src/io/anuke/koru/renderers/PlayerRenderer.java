package io.anuke.koru.renderers;

import io.anuke.ucore.g3d.ModelTransform;
import io.anuke.ucore.g3d.Models;

public class PlayerRenderer extends EntityRenderer{
	
	@Override
	public void render(){
		render.models.get("player").position(entity.getX(), 5, entity.getY());
		//render.models.get("player").rotate(Vector3.Y, 1);
	}
	
	@Override
	public void initRender(){
		ModelTransform t = Models.geti("char");
        
        render.models.add("player", t);
	}

}
