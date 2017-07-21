package io.anuke.koru.ui;

import java.lang.reflect.Field;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import io.anuke.ucore.core.DrawContext;
import io.anuke.ucore.graphics.Atlas;
import io.anuke.ucore.scene.Skin;

public class StyleLoader{
	
	public static Skin loadStyles(){
		float s = 1f;
		FileHandle skinFile = Gdx.files.internal("ui/uiskin.json");
		Skin styles = new Skin();

		FileHandle atlasFile = skinFile.sibling(skinFile.nameWithoutExtension() + ".atlas");
		if(atlasFile.exists()){
			Atlas atlas = new Atlas(atlasFile);
			try{
				Field field = styles.getClass().getDeclaredField("atlas");
				field.setAccessible(true);
				field.set(styles, atlas);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
			styles.addRegions(atlas);
		}
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/smooth.ttf"));
		
		FreeTypeFontParameter normalparameter = new FreeTypeFontParameter();
		normalparameter.size = (int)(22 * s);

		FreeTypeFontParameter largeparameter = new FreeTypeFontParameter();
		largeparameter.size = (int)(26 * s);

		FreeTypeFontParameter borderparameter = new FreeTypeFontParameter();
		borderparameter.size = (int)(26 * s);
		borderparameter.borderWidth = 2*s;
		borderparameter.borderColor = Color.BLACK;
		borderparameter.spaceX = -2;

		BitmapFont font = generator.generateFont(normalparameter);
		BitmapFont largefont = generator.generateFont(largeparameter);
		BitmapFont borderfont = generator.generateFont(borderparameter);

		
		styles.add("default-font", font);
		styles.add("smooth-font", font);
		styles.add("large-font", largefont);
		styles.add("border-font", borderfont);
		
		FreeTypeFontGenerator pgenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/prose.ttf"));
		
		FreeTypeFontParameter pixelparameter = new FreeTypeFontParameter();
		pixelparameter.borderStraight = true;
		pixelparameter.mono = true;
		pixelparameter.size = 16;
		pixelparameter.borderWidth = 1;
		pixelparameter.borderColor = new Color(0,0,0,1f);
		pixelparameter.spaceX = -1;
		
		
		FreeTypeFontParameter pixelparameter2 = new FreeTypeFontParameter();
		pixelparameter2.borderStraight = true;
		pixelparameter2.mono = true;
		pixelparameter2.size = 16;
		
		BitmapFont pixelfont = pgenerator.generateFont(pixelparameter);
		pixelfont.getData().setScale(2f);
		pixelfont.getData().markupEnabled = true;
		styles.add("pixel-font", pixelfont);
		
		BitmapFont pixelfontm = pgenerator.generateFont(pixelparameter);
		pixelfontm.getData().setScale(2f);
		pixelfontm.getData().markupEnabled = false;
		styles.add("pixel-font-nomarkup", pixelfontm);
		
		BitmapFont pixelfont2 = pgenerator.generateFont(pixelparameter2);
		pixelfont2.getData().setScale(2f);
		pixelfont2.setUseIntegerPositions(false);
		styles.add("pixel-font-noborder", pixelfont2);

		styles.load(skinFile);

		DrawContext.skin = styles;

		generator.dispose();
		pgenerator.dispose();
		
		return styles;
	}
}
