package io.anuke.koru.graphics;

import com.badlogic.gdx.math.Vector2;
import com.bitfire.postprocessing.filters.Filter;
import com.bitfire.utils.ShaderLoader;
import com.bitfire.utils.Vector4;

public final class LightFilter extends Filter<LightFilter>{

	private float width, height;

	public enum Param implements Parameter{
		Texture("u_texture0", 0), Lightmap("u_lightmap", 0), Resolution("resolution", 0), Color("ambientColor", 0);

		private String mnemonic;
		private int elementSize;

		private Param(String mnemonic, int arrayElementSize) {
			this.mnemonic = mnemonic;
			this.elementSize = arrayElementSize;
		}

		@Override
		public String mnemonic(){
			return this.mnemonic;
		}

		@Override
		public int arrayElementSize(){
			return this.elementSize;
		}
	}

	public LightFilter(int width, int height) {
		super(ShaderLoader.fromFile("screenspace", "light"));
		this.width = width;
		this.height = height;
		rebind();
	}

	public void SetSize(int width, int height){
		setParams(Param.Resolution, new Vector2(width, height));
		endParams();
	}

	public void SetColor(float r, float g, float b, float a){
		setParam(Param.Color, new Vector4(r, g, b, a));
		endParams();
	}

	@Override
	public void rebind(){
		// reimplement super to batch every parameter
		setParams(Param.Texture, u_texture0);
		setParams(Param.Lightmap, 6);
		setParams(Param.Resolution, new Vector2(width, height));
	}

	@Override
	protected void onBeforeRender(){
		inputTexture.bind(u_texture0);
	}
}
