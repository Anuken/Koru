package io.anuke.koru.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.bitfire.postprocessing.filters.Filter;
import com.bitfire.utils.ShaderLoader;

public final class LightFilter extends Filter<LightFilter>{
	private float width, height;

	public LightFilter(int width, int height) {
		super(ShaderLoader.fromFile("screenspace", "light"));
		this.width = width;
		this.height = height;
		rebind();
	}

	public void setSize(int width, int height){
		setParams("u_resolution", new Vector2(width, height));
		endParams();
	}

	public void setColor(float r, float g, float b, float a){
		setParams("u_color", new Color(r, g, b, a));
		endParams();
	}

	@Override
	public void rebind(){
		setParams("u_texture0", u_texture0);
		setParams("u_lightmap", 6);
		setParams("u_darkmap", 7);
		setParams("u_resolution", new Vector2(width, height));
		endParams();
	}

	@Override
	protected void onBeforeRender(){
		inputTexture.bind(u_texture0);
	}
}
