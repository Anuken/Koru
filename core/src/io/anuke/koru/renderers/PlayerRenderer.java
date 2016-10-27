package io.anuke.koru.renderers;

import com.badlogic.gdx.math.Vector3;

import io.anuke.ucore.g3d.ModelTransform;
import io.anuke.ucore.g3d.Models;

public class PlayerRenderer extends EntityRenderer{
	
	@Override
	public void render(){
		render.models.get("player").position(entity.getX(), entity.getY(), 0);
		render.models.get("player").rotate(Vector3.Y, 1);
	}
	
	@Override
	public void initRender(){
		ModelTransform t = Models.geti("char");
        t.rotate(Vector3.X, 45);
        t.add();
        
        render.models.add("player", t);
	}

}
