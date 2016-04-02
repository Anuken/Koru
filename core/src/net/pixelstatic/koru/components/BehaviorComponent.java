package net.pixelstatic.koru.components;

import net.pixelstatic.koru.behaviors.Behavior;
import net.pixelstatic.koru.behaviors.PathfindBehavior;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.ObjectMap;

public class BehaviorComponent implements Component{
	public ObjectMap<String, Behavior> behaviors = new ObjectMap<String, Behavior>();

	{
		this.addBehavior(PathfindBehavior.class);
	}

	@SuppressWarnings("unchecked")
	public <T extends Behavior> T addBehavior(Class<T> c, String name){
		try{
			Behavior t = (Behavior)c.newInstance();
			behaviors.put(name, t);
			return (T)t;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	public <T extends Behavior> T addBehavior(Class<T> c){
		return addBehavior(c, c.getSimpleName());
	}
}
