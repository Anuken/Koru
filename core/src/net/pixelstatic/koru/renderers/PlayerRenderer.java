package net.pixelstatic.koru.renderers;


public class PlayerRenderer extends EntityRenderer{
	
	@Override
	public void render(){
		render.layers.layer("player").setPosition(entity.getX(), entity.getY()-0.5f).yLayer(false).addBlobShadow(-0.5f).add();
	}
	
	@Override
	public void initRender(){
		render.layer("player");
	}

}
