package io.anuke.koru.traits;


import io.anuke.koru.input.InputHandler;
import io.anuke.koru.network.Net;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.Trait;

public class InputTrait extends Trait{
	public InputHandler input;// = new InputHandler(null);
	
	@Override
	public void added(Spark spark){
		input = new InputHandler(spark);
	}
	
	@Override
	public void update(Spark spark){
		if(Net.server()){
			input.update(Timers.delta());
		}
	}
}
