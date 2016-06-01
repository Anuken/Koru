package net.pixelstatic.koru.renderers;


public class PlayerRenderer extends EntityRenderer{
	
	@Override
	public void render(){
		//render.layers.update(entity.getX(), entity.getY());
		render.layers.layer("player").setPosition(entity.getX(), entity.getY()).yLayer(false).addBlobShadow(-0.5f).add();
		render.layers.layer("player").addReflection();
	}
	
	@Override
	public void initRender(){
		render.layer("player");
	}

}
