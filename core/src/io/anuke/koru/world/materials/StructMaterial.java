package io.anuke.koru.world.materials;

import static io.anuke.koru.world.materials.MaterialType.*;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.items.Items;

/**Artifical materials built by the player.*/
public class StructMaterial{
	
	//Tiles
	
	public static final BaseMaterial woodfloor = new BaseMaterial("woodfloor", tile){{
		addDrop(Items.wood, 1);
		color = new Color(0x744a28ff);
		breaktime = 20;
	}};
	
	public static final BaseMaterial stonefloor = new BaseMaterial("stonefloor", tile){{
		addDrop(Items.stone, 2);
		color = new Color(0x717171ff);
		breaktime = 20;
	}};
	
	
	//Objects, blocks
	
	public static final BaseMaterial stonepillar = new BaseMaterial("stonepillar", block){{
		addDrop(Items.stone, 2); 
		color = new Color(0x717171ff);
		breaktime = 100;
	}};
	
	public static final BaseMaterial woodblock = new BaseMaterial("woodblock", block){{
		addDrop(Items.wood, 2); 
		color = new Color(0x744a28ff);
		breaktime = 60;
	}};
	
	public static final BaseMaterial torch = new BaseMaterial("torch",  StructMaterialType.torch){{
		addDrop(Items.wood, 1);
		breaktime = 20;
	}};
	
	public static final BaseMaterial box = new BaseMaterial("box", StructMaterialType.chest){{
		addDrop(Items.wood, 10);
	}};
	
	public static final BaseMaterial workbench = new BaseMaterial("workbench",  StructMaterialType.workbench){{
		addDrop(Items.wood, 10);
	}};
	
	public static void load(){}
}
