package io.anuke.koru.components;

import com.badlogic.ashley.core.Component;

import io.anuke.koru.entities.KoruEntity;

public interface KoruComponent extends Component{
	public default void update(KoruEntity entity){}
}
