package io.anuke.koru.entities;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;

public class EntityTypes{
	private static ObjectMap<Class<?>, EntityType> types = new ObjectMap<Class<?>, EntityType>();

	public static EntityType get(Class<? extends EntityType> c){
		try{
			if(!types.containsKey(c)){
				types.put(c, ClassReflection.newInstance(c));
			}
			return types.get(c);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
