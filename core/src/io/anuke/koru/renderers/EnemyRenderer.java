package io.anuke.koru.renderers;

public class EnemyRenderer extends EntityRenderer{

	@Override
	protected void render(){
		//render.group.get("monster").sprite().setPosition(entity.getX(), entity.getY()).centerX();
		//render.group.get("shadow").sprite().setPosition(entity.getX(), entity.getY()).center();
	}

	@Override
	protected void init(){
		//new SpriteRenderable(Resources.region("genericmonster"))
		//.addShadow(render.group, Resources.atlas())
		//.setProvider(Sorter.object)
		//.add("monster", render.group);
	}

}
