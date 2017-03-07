package io.anuke.koru.entities;

import com.badlogic.gdx.utils.Array;

import io.anuke.koru.components.KoruComponent;

public class ComponentList{
	Array<KoruComponent> list = new Array<KoruComponent>();
	
	public ComponentList(KoruComponent...components){
		list = new Array<KoruComponent>(components);
	}
	
	public ComponentList remove(Class<?> c){
		for(KoruComponent k : list){
			if(k.getClass().isAssignableFrom(c)){
				list.removeValue(k, true);
				break;
			}
		}
		return this;
	}
	
	public ComponentList add(KoruComponent c){
		list.add(c);
		return this;
	}
}
