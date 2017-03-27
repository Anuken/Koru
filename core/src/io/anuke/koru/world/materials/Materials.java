package io.anuke.koru.world.materials;

import static io.anuke.koru.world.materials.MaterialTypes.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import io.anuke.koru.items.Items;

/**Natural materials generated in the world.*/
public class Materials{
	public static final Material
	
	//Tiles
	
	air = new Material("air", tile){{
		
	}},
	
	forestgrass = new Material("grass", grass){{
		
	}},
	
	darkgrass = new Material("grass", grass){{
		foilageTint = new Vector3(0.8f, 0.92f, 0.86f);
	}},
	
	swampgrass = new Material("grass", grass){{
		
	}},
	
	drygrass = new Material("grass", grass){{
		foilageTint = new Vector3(1.3f, 1.1f, 0.98f);
	}},
	
	burntgrass = new Material("grass", grass){{
		foilageTint = new Vector3(1.6f, 1.2f, 0.94f);
	}},
	
	bluegrass = new Material("grass", grass){{
		
	}},
	
	water = new Material("water",  MaterialTypes.water){{
		
	}},
	
	deepwater = new Material("deepwater",  MaterialTypes.water){{
		
	}},
	
	darkrock = new Material("darkrock", tile){{
		
	}},
	
	magmarock = new Material("magmarock", tile){{
		
	}},
	
	cobblestone = new Material("cobblestone", tile){{
		
	}},
	
	sand = new Material("sand", tile){{
		variants = 7;
	}},
	
	gravel = new Material("gravel", tile){{
		
	}},
	
	ice = new Material("ice", tile){{
		variants = 7;
	}},
	
	riveredge = new Material("riveredge", tile){{
		
	}},
	
	stone = new Material("stone", tile){{
		variants = 7;
	}},
	
	
	//Objects,  blocks
	
	sticks = new CollectableMaterial("sticks", overlay){{
		addDrop(Items.stick, 1);
		interactable = true;
	}},
	
	grassblock = new Material("grassblock",  MaterialTypes.tallgrassblock){{
		
	}},
	
	shortgrassblock = new Material("shortgrassblock",  MaterialTypes.shortgrassblock){{
		
	}},
	
	mossyrock = new Material("mossyrock", object){{
		
	}},
	
	rock = new Material("rock", object){{
		breaktime = 50; 
		addDrop(Items.stone, 2); 
		color = new Color(0x717171ff);
		offset = -1f;
		variants = 4;
	}},
	
	blackrock = new Material("blackrock", object){{
		variants = 4;
	}},
	
	mushy = new CollectableMaterial("mushy", object){{
		variants = 8;
		offsets(-1, -1, -1, -1, -1, -1, -1, -1);
		interactable = true;
		addDrop(Items.mushroom, 1);
	}},
	
	drybush = new Material("drybush", object){{
		offset = -1f;
		variants = 3;
	}},
	
	bush = new Material("bush", object){{
		offset = -1f;
		variants = 3;
	}},
	
	deadtree = new Material("deadtree", tree){{
		variants = 4;
		offsets(-4, -3, -4, -1);
	}},
	
	oaktree = new Material("oaktree", tree){{
		variants = 6;
		offsets(-10, -10, -10, -4, -4, -4);
		addDrop(Items.wood, 16);
	}},
	
	willowtree = new Material("willowtree", tree){{
		variants = 4;
		offsets(-4, -3, -4, -3);
	}},
	
	burnedtree = new Material("burnedtree", tree){{
		variants = 4;
		offsets(-4, -3, -4, -1);
	}},
	
	pinetree = new Material("pinetree", tree){{
		addDrop(Items.wood, 10);
		breaktime = 90;
		variants = 4;
		offsets(-5f, -3f, -3f, -5f);
	}},
	
	pinesapling = new Material("pinesapling", object){{
		addDrop(Items.pinecone, 1);
		collisions = false;
		breaktime = 1;
	}},
	
	stoneblock = new Material("stoneblock", block){{
		addDrop(Items.stone, 5); 
		color = new Color(0x717171ff);
		breaktime = 50;
	}};
	
	public static void load(){}
}
