package io.anuke.koru.world.materials;

import static io.anuke.koru.world.materials.MaterialTypes.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import io.anuke.koru.items.Items;

/**Natural materials generated in the world.*/
public class Materials{
	//Tiles
	
	public static final Material air = new Material("air", tile){{
		
	}};
	
	public static final Material forestgrass = new Material("grass", grass){{
		
	}};
	
	public static final Material darkgrass = new Material("grass", grass){{
		foilageTint = new Vector3(0.8f, 0.92f, 0.86f);
	}};
	
	public static final Material swampgrass = new Material("grass", grass){{
		
	}};
	
	public static final Material drygrass = new Material("grass", grass){{
		foilageTint = new Vector3(1.3f, 1.1f, 0.98f);
	}};
	
	public static final Material burntgrass = new Material("grass", grass){{
		foilageTint = new Vector3(1.6f, 1.2f, 0.94f);
	}};
	
	public static final Material bluegrass = new Material("grass", grass){{
		
	}};
	
	public static final Material water = new Material("water",  MaterialTypes.water){{
		
	}};
	
	public static final Material deepwater = new Material("deepwater",  MaterialTypes.water){{
		
	}};
	
	public static final Material darkrock = new Material("darkrock", tile){{
		
	}};
	
	public static final Material magmarock = new Material("magmarock", tile){{
		
	}};
	
	public static final Material cobblestone = new Material("cobblestone", tile){{
		
	}};
	
	public static final Material sand = new Material("sand", tile){{
		variants = 7;
	}};
	
	public static final Material gravel = new Material("gravel", tile){{
		
	}};
	
	public static final Material ice = new Material("ice", tile){{
		variants = 7;
	}};
	
	public static final Material riveredge = new Material("riveredge", tile){{
		
	}};
	
	public static final Material stone = new Material("stone", tile){{
		variants = 7;
	}};
	
	
	//Objects,  blocks
	
	public static final Material sticks = new Material("sticks", overlay){{
		addDrop(Items.stick, 1);
		interactable = true;
	}};
	
	public static final Material grassblock = new Material("grassblock",  MaterialTypes.tallgrassblock){{
		
	}};
	
	public static final Material shortgrassblock = new Material("shortgrassblock",  MaterialTypes.shortgrassblock){{
		
	}};
	
	public static final Material mossyrock = new Material("mossyrock", object){{
		
	}};
	
	public static final Material rock = new Material("rock", object){{
		breaktime = 50; 
		addDrop(Items.stone, 2); 
		color = new Color(0x717171ff);
		offset = -1f;
		variants = 4;
	}};
	
	public static final Material blackrock = new Material("blackrock", object){{
		variants = 4;
	}};
	
	public static final Material mushy = new Material("mushy", object){{
		variants = 8;
	}};
	
	public static final Material drybush = new Material("drybush", object){{
		offset = -1f;
		variants = 3;
	}};
	
	public static final Material bush = new Material("bush", object){{
		offset = -1f;
		variants = 3;
	}};
	
	public static final Material deadtree = new Material("deadtree", tree){{
		variants = 4;
		offsets(-4, -3, -4, -1);
	}};
	
	public static final Material oaktree = new Material("oaktree", tree){{
		variants = 6;
		offsets(-10, -10, -10, -4, -4, -4);
	}};
	
	public static final Material willowtree = new Material("willowtree", tree){{
		variants = 4;
		offsets(-4, -3, -4, -3);
	}};
	
	public static final Material burnedtree = new Material("burnedtree", tree){{
		variants = 4;
		offsets(-4, -3, -4, -1);
	}};
	
	public static final Material pinetree = new Material("pinetree", tree){{
		addDrop(Items.wood, 10);
		breaktime = 90;
		variants = 4;
		offsets(-5f, -3f, -3f, -5f);
	}};
	
	public static final Material pinesapling = new Material("pinesapling", object){{
		addDrop(Items.pinecone, 1);
		collisions = false;
		breaktime = 1;
	}};
	
	public static final Material stoneblock = new Material("stoneblock", block){{
		addDrop(Items.stone, 5); 
		color = new Color(0x717171ff);
		breaktime = 50;
	}};
	
	public static void load(){}
}
