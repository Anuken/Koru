package io.anuke.koru.world.materials;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import io.anuke.koru.items.impl.Items;
import io.anuke.koru.world.Tile;
import io.anuke.koru.world.materials.MaterialTypes.*;
import io.anuke.ucore.facet.FacetList;

/**Natural materials generated in the world.*/
public class Materials{
	public static final Material
	
	//Tiles
	
	air = new Floor("air"){
		@Override
		public void draw(Tile tile, FacetList list){}
	},
	
	forestgrass = new Grass("grass"){{
		
	}},
	
	darkgrass = new Grass("grass"){{
		foilageTint = new Vector3(0.8f, 0.92f, 0.86f);
	}},
	
	swampgrass = new Grass("grass"){{
		
	}},
	
	drygrass = new Grass("grass"){{
		foilageTint = new Vector3(1.3f, 1.1f, 0.98f);
	}},
	
	burntgrass = new Grass("grass"){{
		foilageTint = new Vector3(1.6f, 1.2f, 0.94f);
	}},
	
	bluegrass = new Grass("grass"){{
		
	}},
	
	water = new Water("water"){{
		variants = 0;
		solid = true;
		hitbox.setSize(10, 10);
	}},
	
	deepwater = new Water("deepwater"){{
		variants = 0;
		solid = true;
		hitbox.setSize(10, 10);
	}},
	
	darkrock = new Floor("darkrock"){{
		
	}},
	
	magmarock = new Floor("magmarock"){{
		
	}},
	
	cobblestone = new Floor("cobblestone"){{
		
	}},
	
	sand = new Floor("sand"){{
		variants = 7;
	}},
	
	gravel = new Floor("gravel"){{
		
	}},
	
	ice = new Floor("ice"){{
		variants = 7;
	}},
	
	riveredge = new Floor("riveredge"){{
		
	}},
	
	stone = new Floor("stone"){{
		variants = 7;
	}},
	
	
	//Objects,  blocks
	
	sticks = new FloorItem("sticks"){{
		addDrop(Items.stick, 1);
	}},
	
	rocks = new FloorItem("rocks"){{
		addDrop(Items.stone, 1);
	}},
	
	grassblock = new TallGrassWall("grassblock"){{
		
	}},
	
	shortgrassblock = new ShortGrassWall("shortgrassblock"){{
		
	}},
	
	mossyrock = new Prop("mossyrock"){{
		
	}},
	
	rock = new Prop("rock"){{
		breaktime = 50; 
		addDrop(Items.stone, 2); 
		color = new Color(0x717171ff);
		offset = -1f;
		variants = 4;
	}},
	
	blackrock = new Prop("blackrock"){{
		variants = 4;
	}},
	
	mushy = new Prop("mushy"){{
		variants = 8;
		interactable = true;
		offsets(-1, -1, -1, -1, -1, -1, -1, -1);
		addDrop(Items.mushroom, 1);
	}},
	
	drybush = new Prop("drybush"){{
		offset = -1f;
		variants = 3;
	}},
	
	bush = new Prop("bush"){{
		offset = -1f;
		variants = 3;
	}},
	
	deadtree = new Tree("deadtree"){{
		variants = 4;
		offsets(-4, -3, -4, -1);
		addDrop(Items.wood, 10);
		breaktime = 120;
	}},
	
	oaktree = new Tree("oaktree"){{
		variants = 6;
		offsets(-10, -10, -10, -4, -4, -4);
		addDrop(Items.wood, 20);
		breaktime = 120;
	}},
	
	willowtree = new Tree("willowtree"){{
		variants = 4;
		offsets(-4, -3, -4, -3);
		addDrop(Items.wood, 20);
		breaktime = 120;
	}},
	
	burnedtree = new Tree("burnedtree"){{
		variants = 4;
		offsets(-4, -3, -4, -1);
		addDrop(Items.wood, 10);
		breaktime = 120;
	}},
	
	pinetree = new Tree("pinetree"){{
		addDrop(Items.wood, 10);
		breaktime = 90;
		variants = 4;
		offsets(-5f, -3f, -3f, -5f);
	}},
	
	pinesapling = new Prop("pinesapling"){{
		addDrop(Items.pinecone, 1);
		solid = false;
		breaktime = 1;
		variants = 0;
	}},
	
	pinecones = new Overlay("pinecones"){{
		addDrop(Items.pinecone, 1);
		solid = false;
		variants = 0;
		interactable = true;
	}},
	
	floweryellow = new Flower("floweryellow"){{
		
	}},
	flowerblue = new Flower("flowerblue"){{
		
	}},
	flowerpurple = new Flower("flowerpurple"){{
				
	}},
	flowerred = new Flower("flowerred"){{
				
	}},
	stoneblock = new Wall("stoneblock"){{
		addDrop(Items.stone, 5); 
		color = new Color(0x717171ff);
		breaktime = 50;
	}};
	
	public static void load(){
		StructMaterials.load();
	}
}
