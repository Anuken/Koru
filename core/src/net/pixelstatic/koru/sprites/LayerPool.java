package net.pixelstatic.koru.sprites;

import com.badlogic.gdx.utils.Pool;

public class LayerPool extends Pool<Layer>{

	@Override
	protected Layer newObject(){
		return new Layer();
	}

}
