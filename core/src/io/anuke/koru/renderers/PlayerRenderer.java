package io.anuke.koru.renderers;

import io.anuke.layer3d.LayeredObject;

public class PlayerRenderer extends EntityRenderer{
	
	@Override
	public void render(){
		render.list.get(0).setPosition(entity.getX(), entity.getY());
		//render.models.get("player").rotate(Vector3.Y, 1);
	}
	
	@Override
	public void initRender(){
        render.list.add(new LayeredObject("player"));
	}

}
