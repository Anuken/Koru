package io.anuke.koru.renderers;

import io.anuke.ucore.spritesystem.SpriteRenderable;

public class PlayerRenderer extends EntityRenderer{
	AnimationType animation;
	float duration;
	
	@Override
	public void render(){
		/*
		render.group.get("crab").sprite().setPosition(entity.getX(), entity.getY()).centerX();
		
		render.group.get("crab").sprite().sprite.setRegion(Resources.region("crab" + dir() + 
				(render.walkframe > 0.001f ? ("-"+ ((int)(render.walkframe/7)%3)) : "")));
		render.group.get("crab").sprite().sprite.setFlip(flip(), false);
		
		
		SpriteRenderable item = render.group.get("item").sprite();
		
		item.setRegion(Resources.region(entity.getComponent(InventoryComponent.class).hotbarStack() == null ? 
				"clear" : entity.getComponent(InventoryComponent.class).hotbarStack().item.name() + "item"));
		
		item.setLayer(entity.getY() + 0.0001f)
		.setPosition(entity.getX() + shift(1), entity.getY()+6).centerX().sprite().sprite.setFlip(flip2(), false);
		
		float angle = entity.get(InputComponent.class).input.mouseangle + (flip2() ? 45 : - 45);
		
		item.sprite.setOrigin(!this.flip2() ? 1.5f : 12-1.5f, 1.5f);
		item.sprite.setRotation((flip2() ? angle-180 : angle));
		if(render.direction == 0) item.sprite.setRotation(item.sprite.getRotation()-180);
		
		render.group.get("shadow").sprite().setPosition(entity.getX(), entity.getY()+1).center();
		render.group.get("name").setPosition(entity.getX(), entity.getY());
		
		if(duration > 0){
			duration -= Gdx.graphics.getDeltaTime()*60;
			animate();
		}
		*/
	}
	
	private void animate(){
		if(animation == AnimationType.attack){
			SpriteRenderable item = render.group.get("item").sprite();
			item.sprite.rotate(fscl()*(duration*8));
		}
	}
	
	@Override
	public void init(){
		draw(l->{
			l.layer = entity.getY();
			
			
		});
		/*
		new SpriteRenderable(Resources.region("crab"))
		.addShadow(render.group, Resources.atlas())
		.setProvider(Sorter.object)
		.add("crab", render.group);
		
		SpriteRenderable item = new SpriteRenderable(Resources.region("woodaxeitem"))
		.setProvider(Sorter.object).sprite();
		
		item.add("item", render.group);
		
		new TextRenderable(Resources.font(), entity.getComponent(ConnectionComponent.class).local ? "" : entity.getComponent(ConnectionComponent.class).name)
		.align(Align.center)
		.setColor(Color.CORAL)
		.setProvider(Sorter.object)
		.add("name", render.group);
		*/
		
	}
	
	@Override
	public void onAnimation(AnimationType type){
		if(type == AnimationType.attack){
			animation = type;
			duration = 10;
		}
	}
}
