package io.anuke.koru.world.materials;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import io.anuke.koru.items.Items;
import io.anuke.koru.world.MaterialType;

/**Natural materials generate in the world.*/
public class NaturalMaterial{
	//Tiles
	
	public static final BaseMaterial grass = new BaseMaterial("grass", MaterialType.grass){{
		
	}};
	
	public static final BaseMaterial darkgrass = new BaseMaterial("grass", MaterialType.grass){{
		foilageTint = new Vector3(0.8f, 0.92f, 0.86f);
	}};
	
	public static final BaseMaterial swampgrass = new BaseMaterial("grass", MaterialType.grass){{
		
	}};
	
	public static final BaseMaterial drygrass = new BaseMaterial("grass", MaterialType.grass){{
		foilageTint = new Vector3(1.3f,1.1f,0.98f);
	}};
	
	public static final BaseMaterial burntgrass = new BaseMaterial("grass", MaterialType.grass){{
		foilageTint = new Vector3(1.6f, 1.2f, 0.94f);
	}};
	
	public static final BaseMaterial bluegrass = new BaseMaterial("grass", MaterialType.grass){{
		
	}};
	
	public static final BaseMaterial water = new BaseMaterial("water", MaterialType.water){{
		
	}};
	
	public static final BaseMaterial deepwater = new BaseMaterial("deepwater", MaterialType.water){{
		
	}};
	
	public static final BaseMaterial darkrock = new BaseMaterial("darkrock", MaterialType.tile){{
		
	}};
	
	public static final BaseMaterial magmarock = new BaseMaterial("magmarock", MaterialType.tile){{
		
	}};
	
	public static final BaseMaterial cobblestone = new BaseMaterial("cobblestone", MaterialType.tile){{
		
	}};
	
	public static final BaseMaterial sand = new BaseMaterial("sand", MaterialType.tile){{
		variants = 7;
	}};
	
	public static final BaseMaterial gravel = new BaseMaterial("gravel", MaterialType.tile){{
		
	}};
	
	public static final BaseMaterial ice = new BaseMaterial("gravel", MaterialType.tile){{
		variants = 7;
	}};
	
	public static final BaseMaterial riveredge = new BaseMaterial("riveredge", MaterialType.tile){{
		
	}};
	
	public static final BaseMaterial stone = new BaseMaterial("stone", MaterialType.tile){{
		variants = 7;
	}};
	
	
	//Objects, blocks
	
	
	public static final BaseMaterial pinecones = new BaseMaterial("pinecones", MaterialType.overlay){{
		addDrop(Items.pinecone, 1);
	}};
	
	public static final BaseMaterial grassblock = new BaseMaterial("grassblock", MaterialType.tallgrassblock){{
		
	}};
	
	public static final BaseMaterial shortgrassblock = new BaseMaterial("shortgrassblock", MaterialType.shortgrassblock){{
		
	}};
	
	public static final BaseMaterial fern = new BaseMaterial("fern", MaterialType.foilage){{
		
	}};
	
	public static final BaseMaterial koru = new BaseMaterial("koru", MaterialType.foilage){{
		
	}};
	
	public static final BaseMaterial bush = new BaseMaterial("bush", MaterialType.object){{
		offset = -1f;
	}};
	
	public static final BaseMaterial mossyrock = new BaseMaterial("mossyrock", MaterialType.object){{
		
	}};
	
	public static final BaseMaterial rock = new BaseMaterial("rock", MaterialType.object){{
		breaktime = 50; 
		addDrop(Items.stone, 2); 
		color = new Color(0x717171ff);
		offset = -1f;
	}};
	
	public static final BaseMaterial blackrock = new BaseMaterial("blackrock", MaterialType.object){{
		
	}};
	
	public static final BaseMaterial mushy = new BaseMaterial("mushy", MaterialType.object){{
		
	}};
	
	public static final BaseMaterial drybush = new BaseMaterial("drybush", MaterialType.object){{
		offset = -1f;
	}};
	
	public static final BaseMaterial deadtree = new BaseMaterial("deadtree", MaterialType.tree){{
		offset = -4f;
	}};
	
	public static final BaseMaterial oaktree = new BaseMaterial("oaktree", MaterialType.tree){{
		offset = -10f;
	}};
	
	public static final BaseMaterial willowtree = new BaseMaterial("willowtree", MaterialType.tree){{
		offset = -4f;
	}};
	
	public static final BaseMaterial burnedtree = new BaseMaterial("burnedtree", MaterialType.tree){{
		offset = -5f;
	}};
	
	public static final BaseMaterial pinetree = new BaseMaterial("pinetree", MaterialType.tree){{
		addDrop(Items.wood, 10);
		breaktime = 90;
		offset = -5f;
	}};
	
	public static final BaseMaterial pinesapling = new BaseMaterial("pinesapling", MaterialType.object){{
		addDrop(Items.pinecone, 1);
		collisions = false;
		breaktime = 1;
	}};
	
	public static final BaseMaterial stoneblock = new BaseMaterial("stoneblock", MaterialType.block){{
		addDrop(Items.stone, 5); 
		color = new Color(0x717171ff);
		breaktime = 50;
	}};
}
