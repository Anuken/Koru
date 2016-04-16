package net.pixelstatic.koru.components;

import net.pixelstatic.koru.ai.AIData;
import net.pixelstatic.koru.behaviors.Behavior;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.SnapshotArray;

public class BehaviorComponent implements Component{
	public ObjectMap<String, Behavior> behaviors = new ObjectMap<String, Behavior>();
	public SnapshotArray<Behavior> behaviorarray = new SnapshotArray<Behavior>();
	public AIData data;
	
	public BehaviorComponent(){
		data = new AIData();
	}

	@SuppressWarnings("unchecked")
	public <T extends Behavior> T addBehavior(Class<T> c, String name){
		try{
			Behavior t = (Behavior)c.newInstance();
			behaviors.put(name, t);
			behaviorarray.add(t);
			return (T)t;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public void removeBehavior(Behavior behavior){
		behaviorarray.removeValue(behavior, true);
		behaviors.remove(behavior.getClass().getSimpleName());
	}
	
	public void insertBehavior(int index, Behavior behavior){
		behaviors.put(behavior.getClass().getSimpleName(), behavior);
		behaviorarray.insert(0, behavior);
	}
	
	public <T extends Behavior> T addBehavior(Class<T> c){
		return addBehavior(c, c.getSimpleName());
	}
}
