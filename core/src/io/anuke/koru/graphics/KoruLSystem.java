package io.anuke.koru.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.lsystem.LSystem;
import io.anuke.ucore.lsystem.LSystemData;

public class KoruLSystem extends LSystem{
	private Array<Vector3> circles = new Array<>();
	private Color leafColor = Color.valueOf("366d2d");

	public KoruLSystem(LSystemData data) {
		super(data);
	}
	
	@Override
	protected void pop(){
		circles.add(new Vector3(x + lastx, y + lasty, (float)stack.size()/(maxstack)));
		super.pop();
	}
	
	@Override
	protected void drawLines(){
		Draw.color(data.start);
		Draw.rect("treebase", x, y);
		
		for(Line line : lines){
			float s = 1f - (float)line.stack/maxstack;
			
			Draw.thickness(data.thickness + s * 1f);
			Draw.color(data.start, data.end, (float)line.stack/maxstack);
			Draw.line(line.x1, line.y1, line.x2, line.y2);
		}
	}
	
	public void draw(){
		circles.clear();
		super.draw();
		
		for(Vector3 vec : circles){
			float rad = 3f + vec.z*3;
			Draw.color(leafColor);
			Draw.rect("circle", vec.x, vec.y, rad, rad);
		}
		
		Draw.color();
	}

}
