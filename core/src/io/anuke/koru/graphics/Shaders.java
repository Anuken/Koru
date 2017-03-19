package io.anuke.koru.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.bitfire.utils.ShaderLoader;

public class Shaders{
	/**Parameters: RGBA (4 floats)*/
	public static ShaderProgram outline;
	
	public static void loadAll(){
		outline = load("outline", "outline");
	}
	
	public static void setParams(ShaderProgram shader, Object... params){
		if(shader == outline){
			shader.begin();
			shader.setUniformf("u_color", new Color((float)params[0], (float)params[1], (float)params[2], (float)params[3]));
			shader.end();
		}
	}
	
	private static ShaderProgram load(String vertl, String fragl){
		return ShaderLoader.fromFile(vertl, fragl);
	}
}
