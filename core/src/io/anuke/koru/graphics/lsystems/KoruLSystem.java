package io.anuke.koru.graphics.lsystems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.Graphics;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.lsystem.LSystem;
import io.anuke.ucore.lsystem.LSystemData;
import io.anuke.ucore.util.Mathf;

public class KoruLSystem extends LSystem implements Disposable{
	public static final int frames = 20;
	public static boolean writeOut = false;
	private Array<Vector3> circles = new Array<>();

	private boolean drawn = false;
	private boolean cache = true;
	private TextureRegion[] regions;
	private float time = 0f;
	private float offsetx = 0;
	private int seed = Mathf.random(9999);
	private boolean sortAscending = false;
	private boolean sortCircles = false;
	
	public float colorStep = 0.3f;
	public float trunkHeight = 0f;
	public Color leafColor;
	public Color leafColorEnd;
	public Color lineColor;

	public KoruLSystem(LSystemData data) {
		super(data);
	}

	public LSystemData getData(){
		return data;
	}

	public String getInstructions(){
		return string;
	}
	
	public boolean isDrawn(){
		return drawn;
	}

	@Override
	protected void drawForward(){
		boolean z = lines.size == 0;
		if(z){
			data.len += trunkHeight;
		}

		super.drawForward();

		if(z){
			data.len -= trunkHeight;
		}
	}

	@Override
	protected void pop(){
		if(leafColor != null && !drawn)
			circles.add(new Vector3(lastx, lasty, (float) stack.size() / (maxstack)));
		super.pop();
	}

	@Override
	protected void drawLines(){
		Draw.color(data.start);
		Draw.rect("treebase", x, y);

		for(Line line : lines){
			float s = 1f - (float) line.stack / maxstack;

			Draw.thickness(data.thickness + s * 1f);
			Draw.color(data.start, data.end, (float) line.stack / maxstack);
			Draw.line(line.x1 + x, line.y1 + y, line.x2 + x, line.y2 + y);
		}
	}

	@Override
	protected float getTime(){
		return time;
	}

	public void draw(){

		if(!drawn){
			float phase = MathUtils.PI2 * data.swayphase;
			regions = new TextureRegion[frames];
			int boundGrow = 7;
			float minx = 999,  maxx = -999, maxy = -999;
			int screenX = 0, screenY = 0, screenWidth = 0, screenHeight = 0;
			
			Graphics.setScreen();
			Graphics.surface("trees");
			Graphics.clear(Color.CLEAR);
			
			for(int i = -1; i < frames; i++){
				time = (float) Math.max(i, 0) / frames * phase;

				recache();

				if(i != -1){
					Graphics.clear(Color.CLEAR);
					
					float xoff = Gdx.graphics.getWidth()/2;
					float yoff = Gdx.graphics.getHeight()/2;
					Draw.color(data.start);
					Draw.rect("treebase", 0 + xoff, 0 + yoff);

					for(Line line : lines){
						float s = 1f - (float) line.stack / maxstack;
						
						Draw.thickness(data.thickness + s * 1f);
						Draw.color(data.start, data.end, (float) line.stack / maxstack);
						Draw.line(line.x1 + xoff, line.y1 + yoff, line.x2 + xoff, line.y2 + yoff);
					}

					for(Vector3 vec : circles){
						float rad = 3f + vec.z * 3;
						if(leafColorEnd != null){
							Draw.color(leafColor, leafColorEnd, Mathf.round(vec.z, colorStep));
						}else{
							Draw.color(leafColor);
						}
						Draw.rect("circle", vec.x + xoff, vec.y + yoff, rad, rad);
					}

					Draw.color();
					Graphics.flush();
					
					TextureRegion region = ScreenUtils.getFrameBufferTexture(screenX, screenY, screenWidth, screenHeight);
					regions[i] = region;
					if(i == 0 && writeOut){
						PixmapIO.writePNG(Gdx.files.local("trees/tree" + Mathf.random(99999)), ScreenUtils.getFrameBufferPixmap(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
					}
					
				}else{
					for(Line line : lines){
						minx = Math.min(line.x1, minx);
						minx = Math.min(line.x2, minx);
						
						maxx = Math.max(line.x1, maxx);
						maxx = Math.max(line.x2, maxx);
						maxy = Math.max(line.y1, maxy);
						maxy = Math.max(line.y2, maxy);
					}
					
					screenX = (int)minx - boundGrow + Gdx.graphics.getWidth()/2;
					screenY = Gdx.graphics.getHeight()/2 - 2;
					screenWidth = (int)(maxx - minx) + boundGrow * 2;
					screenHeight = (int)maxy + boundGrow;
					offsetx = ((screenX + screenWidth) + screenX)/2 - Gdx.graphics.getWidth()/2;
				}
			}
			
			Graphics.end();
			Graphics.beginCam();
			Graphics.surface();

			drawn = true;
		}else{
			if(regions == null){
				throw new RuntimeException("Disposed!");
			}
			float time = 100f / data.swayphase;
			TextureRegion region = regions[(int)((Timers.time() + this.timeOffset) / time) % regions.length];
			Draw.rect(region, x + offsetx, y + region.getRegionHeight()/2 - 2);
		}
	}

	private void recache(){
		circles.clear();
		lines.clear();

		angle = 90;
		lastx = lasty = 0;

		for(int i = 0; i < string.length(); i++){
			drawc(string.charAt(i));
		}

		lines.sort();
		if(sortCircles) circles.sort((v1, v2) -> sortAscending ? Float.compare(v1.z, v2.z) : Float.compare(v2.z, v1.z));
	}

	@Override
	public void dispose(){
		if(regions != null)
		for(TextureRegion region : regions){
			region.getTexture().dispose();
		}
		regions = null;
	}

}
