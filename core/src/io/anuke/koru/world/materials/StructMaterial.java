package io.anuke.koru.world.materials;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.items.Items;
import io.anuke.koru.world.MaterialType;

/**Artifical materials built by the player.*/
public class StructMaterial{
	
	//Tiles
	
	public static final BaseMaterial woodfloor = new BaseMaterial("woodfloor", MaterialType.tile){{
		addDrop(Items.wood, 2);
	}};
	
	public static final BaseMaterial stonefloor = new BaseMaterial("stonefloor", MaterialType.tile){{
		addDrop(Items.stone, 2);
	}};
	
	
	//Objects, blocks
	
	public static final BaseMaterial stonepillar = new BaseMaterial("stonepillar", MaterialType.block){{
		addDrop(Items.stone, 2); 
		color = new Color(0x717171ff);
		breaktime = 100;
	}};
	
	public static final BaseMaterial woodblock = new BaseMaterial("woodblock", MaterialType.block){{
		addDrop(Items.wood, 2); 
		color = new Color(0x744a28ff);
		breaktime = 60;
	}};
	
	public static final BaseMaterial torch = new BaseMaterial("torch", MaterialType.torch){{
		addDrop(Items.wood, 1);
		breaktime = 20;
	}};
	
	public static final BaseMaterial box = new BaseMaterial("box", MaterialType.chest){{
		addDrop(Items.wood, 10);
	}};
	
	public static final BaseMaterial workbench = new BaseMaterial("workbench", MaterialType.workbench){{
		addDrop(Items.wood, 10);
	}};

}
