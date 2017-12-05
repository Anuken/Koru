package io.anuke.koru.world.materials;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.items.impl.Items;
import io.anuke.koru.world.materials.MaterialTypes.Floor;
import io.anuke.koru.world.materials.MaterialTypes.Prop;
import io.anuke.koru.world.materials.MaterialTypes.Wall;
import io.anuke.koru.world.materials.StructMaterialTypes.Torch;

/**Artifical materials built by the player.*/
public class StructMaterials{
	public static final Material
	
	//tiles
	
	woodfloor = new Floor("woodfloor"){{
		addDrop(Items.wood, 1);
		color = new Color(0x744a28ff);
		breaktime = 20;
		variants = 0;
	}},
	
	stonefloor = new Floor("stonefloor"){{
		addDrop(Items.stone, 1);
		color = new Color(0x717171ff);
		variants = 0;
		breaktime = 20;
	}},
	
	
	//Objects, blocks
	
	stonepillar = new Wall("stonepillar"){{
		addDrop(Items.stone, 2); 
		color = new Color(0x717171ff);
		breaktime = 100;
	}},
	
	woodblock = new Wall("woodblock"){{
		addDrop(Items.wood, 2); 
		color = new Color(0x744a28ff);
		breaktime = 60;
	}},
	
	torch = new Torch("torch"){{
		addDrop(Items.wood, 1);
		breaktime = 20;
	}},
	
	box = new Prop("box"){{
		addDrop(Items.wood, 10);
	}},
	barrel = new Prop("barrel"){{
		addDrop(Items.wood, 10);
		variants = 0;
		offset = -3;
	}},
	table = new Prop("table"){{
		addDrop(Items.wood, 10);
		variants = 0;
		offset = -1;
	}};
	
	public static void load(){}
}
