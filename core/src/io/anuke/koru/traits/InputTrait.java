package io.anuke.koru.traits;


import io.anuke.koru.input.InputHandler;
import io.anuke.koru.network.IServer;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.Trait;
import io.anuke.ucore.util.Mathf;

public class InputTrait extends Trait{
	public InputHandler input;// = new InputHandler(null);
	
	@Override
	public void added(Spark spark){
		input = new InputHandler(spark);
	}
	
	@Override
	public void update(Spark spark){
		if(IServer.active()){
			input.update(Mathf.delta());
		}
	}
}
