package io.anuke.koru.world.materials;

import static io.anuke.koru.world.materials.MaterialType.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import io.anuke.koru.items.Items;

/**Natural materials generated in the world.*/
public class Material{
	//Tiles
	
	public static final BaseMaterial air = new BaseMaterial("air", tile){{
		
	}};
	
	public static final BaseMaterial forestgrass = new BaseMaterial("grass",grass){{
		
	}};
	
	public static final BaseMaterial darkgrass = new BaseMaterial("grass",grass){{
		foilageTint = new Vector3(0.8f, 0.92f, 0.86f);
	}};
	
	public static final BaseMaterial swampgrass = new BaseMaterial("grass",grass){{
		
	}};
	
	public static final BaseMaterial drygrass = new BaseMaterial("grass",grass){{
		foilageTint = new Vector3(1.3f,1.1f,0.98f);
	}};
	
	public static final BaseMaterial burntgrass = new BaseMaterial("grass",grass){{
		foilageTint = new Vector3(1.6f, 1.2f, 0.94f);
	}};
	
	public static final BaseMaterial bluegrass = new BaseMaterial("grass",grass){{
		
	}};
	
	public static final BaseMaterial water = new BaseMaterial("water", MaterialType.water){{
		
	}};
	
	public static final BaseMaterial deepwater = new BaseMaterial("deepwater", MaterialType.water){{
		
	}};
	
	public static final BaseMaterial darkrock = new BaseMaterial("darkrock",tile){{
		
	}};
	
	public static final BaseMaterial magmarock = new BaseMaterial("magmarock",tile){{
		
	}};
	
	public static final BaseMaterial cobblestone = new BaseMaterial("cobblestone",tile){{
		
	}};
	
	public static final BaseMaterial sand = new BaseMaterial("sand",tile){{
		variants = 7;
	}};
	
	public static final BaseMaterial gravel = new BaseMaterial("gravel",tile){{
		
	}};
	
	public static final BaseMaterial ice = new BaseMaterial("gravel",tile){{
		variants = 7;
	}};
	
	public static final BaseMaterial riveredge = new BaseMaterial("riveredge",tile){{
		
	}};
	
	public static final BaseMaterial stone = new BaseMaterial("stone",tile){{
		variants = 7;
	}};
	
	
	//Objects, blocks
	
	
	public static final BaseMaterial grassblock = new BaseMaterial("grassblock", MaterialType.tallgrassblock){{
		
	}};
	
	public static final BaseMaterial shortgrassblock = new BaseMaterial("shortgrassblock", MaterialType.shortgrassblock){{
		
	}};
	
	public static final BaseMaterial bush = new BaseMaterial("bush",object){{
		offset = -1f;
	}};
	
	public static final BaseMaterial mossyrock = new BaseMaterial("mossyrock",object){{
		
	}};
	
	public static final BaseMaterial rock = new BaseMaterial("rock",object){{
		breaktime = 50; 
		addDrop(Items.stone, 2); 
		color = new Color(0x717171ff);
		offset = -1f;
	}};
	
	public static final BaseMaterial blackrock = new BaseMaterial("blackrock",object){{
		
	}};
	
	public static final BaseMaterial mushy = new BaseMaterial("mushy",object){{
		
	}};
	
	public static final BaseMaterial drybush = new BaseMaterial("drybush",object){{
		offset = -1f;
	}};
	
	public static final BaseMaterial deadtree = new BaseMaterial("deadtree",tree){{
		offset = -4f;
	}};
	
	public static final BaseMaterial oaktree = new BaseMaterial("oaktree",tree){{
		offset = -10f;
	}};
	
	public static final BaseMaterial willowtree = new BaseMaterial("willowtree",tree){{
		offset = -4f;
	}};
	
	public static final BaseMaterial burnedtree = new BaseMaterial("burnedtree",tree){{
		offset = -5f;
	}};
	
	public static final BaseMaterial pinetree = new BaseMaterial("pinetree",tree){{
		addDrop(Items.wood, 10);
		breaktime = 90;
		offset = -5f;
	}};
	
	public static final BaseMaterial pinesapling = new BaseMaterial("pinesapling",object){{
		addDrop(Items.pinecone, 1);
		collisions = false;
		breaktime = 1;
	}};
	
	public static final BaseMaterial stoneblock = new BaseMaterial("stoneblock",block){{
		addDrop(Items.stone, 5); 
		color = new Color(0x717171ff);
		breaktime = 50;
	}};
}
