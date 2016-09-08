package net.pixelstatic.koru.utils;

import com.badlogic.gdx.utils.ObjectMap;

public class ValueMap{
	private ObjectMap<String, Object> values;
	
	public ValueMap(){
		values = new ObjectMap<String, Object>();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue(String name, Class<T> c){
		return (T)(values.get(name));
	}
	
	public void addValue(String name, Object object){
		values.put(name, object);
	}
}
