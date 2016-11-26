package io.anuke.koru.renderers;

public class PlayerRenderer extends EntityRenderer{
	
	@Override
	public void render(){
		/*
		render.group.get("player").sprite().setPosition(entity.getX(), entity.getY()).centerX();
		render.group.get("shadow").sprite().setPosition(entity.getX(), entity.getY()).center();
		
		render.group.get("name").setPosition(entity.getX(), entity.getY());
		*/
	}
	
	@Override
	public void initRender(){
		/*
		new SpriteRenderable(Resources.region("player"))
		.addShadow(render.group, Resources.atlas())
		.setProvider(SortProviders.object)
		.add("player", render.group);
		
		new TextRenderable(Resources.font(), entity.getComponent(ConnectionComponent.class).local ? "" : entity.getComponent(ConnectionComponent.class).name)
		.center()
		.setColor(Color.CORAL)
		.setProvider(SortProviders.object)
		.add("name", render.group);
		*/
	}

}
