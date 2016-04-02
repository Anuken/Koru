package net.pixelstatic.koru.renderers;


public class PlayerRenderer extends EntityRenderer{
	
	@Override
	public void render(){
		render.layers.update(entity.getX(), entity.getY());
	}
	
	@Override
	public void initRender(){
		render.layer("player");
	}

}
