package io.anuke.koru.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import io.anuke.koru.items.Item;
import io.anuke.koru.items.ItemStack;
import io.anuke.ucore.graphics.Hue;

public enum Materials implements Material{
	air, 
	grass(Hue.rgb(69, 109, 29,0.04f)),
	blackrock,
	magmarock,
	cobblestone,
	burntgrass(new Color(0x799126ff)),
	burntgrass2(new Color(0x938725ff)),
	swampgrass,
	sand,
	gravel,
	bluegrass,
	ice,
	water(MaterialType.water, 1, Hue.blend(41, 97, 155, 102, 102, 102, 0.3f)){{addDrop(Item.water, 1);}},
	riveredge,
	stone(MaterialType.tile, 120, Hue.rgb(115, 115, 115, 0.09f)){{addDrop(Item.stone, 1);}}, 
	woodfloor{{addDrop(Item.wood, 2);}},
	stonefloor{{addDrop(Item.stone, 2);}},
	pinecones(MaterialType.overlay){{addDrop(Item.pinecone, 1);}}, 
	grassblock(MaterialType.tallgrassblock),
	shortgrassblock(MaterialType.shortgrassblock),
	tallgrass1(MaterialType.grass),
	tallgrass2(MaterialType.grass),
	tallgrass3(MaterialType.grass),
	wheatgrass1(MaterialType.grass),
	wheatgrass2(MaterialType.grass),
	wheatgrass3(MaterialType.grass),
	fern1(MaterialType.grass),
	fern2(MaterialType.grass),
	fern3(MaterialType.grass),
	koru1(MaterialType.grass),
	koru2(MaterialType.grass),
	koru3(MaterialType.grass),
	bush1(MaterialType.object),
	bush2(MaterialType.object),
	bush3(MaterialType.object),
	mossyrock1(MaterialType.object),
	mossyrock2(MaterialType.object),
	rock1(MaterialType.object),
	rock2(MaterialType.object),
	rock3(MaterialType.object),
	rock4(MaterialType.object),
	blackrock1(MaterialType.object),
	blackrock2(MaterialType.object),
	blackrock3(MaterialType.object),
	blackrock4(MaterialType.object),
	mushy1(MaterialType.object),
	mushy2(MaterialType.object),
	mushy3(MaterialType.object),
	mushy4(MaterialType.object),
	mushy5(MaterialType.object),
	mushy6(MaterialType.object),
	mushy7(MaterialType.object),
	mushy8(MaterialType.object),
	drybush1(MaterialType.object),
	drybush2(MaterialType.object),
	drybush3(MaterialType.object),
	deadtree1(MaterialType.tree),
	deadtree2(MaterialType.tree),
	deadtree3(MaterialType.tree),
	deadtree4(MaterialType.tree),
	oaktree1(MaterialType.tree),
	oaktree2(MaterialType.tree),
	oaktree3(MaterialType.tree),
	oaktree4(MaterialType.tree),
	oaktree5(MaterialType.tree),
	oaktree6(MaterialType.tree),
	willowtree1(MaterialType.tree),
	willowtree2(MaterialType.tree),
	willowtree3(MaterialType.tree),
	willowtree4(MaterialType.tree),
	burnedtree1(MaterialType.tree),
	burnedtree2(MaterialType.tree),
	burnedtree3(MaterialType.tree),
	burnedtree4(MaterialType.tree),
	pinetree1(MaterialType.tree, 1){{addDrop(Item.wood, 5); addDrop(Item.pinecone, 1);}}, 
	pinetree2(MaterialType.tree, 1){{addDrop(Item.wood, 5); addDrop(Item.pinecone, 2);}}, 
	pinetree3(MaterialType.tree, 1){{addDrop(Item.wood, 5); addDrop(Item.pinecone, 2);}}, 
	pinetree4(MaterialType.tree, 1){{addDrop(Item.wood, 5);}}, 
	pinesapling(MaterialType.tree, 1, false){
		{addDrop(Item.pinecone, 1);}
		
		public boolean growable(){
			return true;
		}
		
		public Material growMaterial(){
			return random(Materials.pinetree1, Materials.pinetree2, Materials.pinetree3, Materials.pinetree4);
		}
		
		public TileData getDefaultData(){
			return new PinetreeTileData();
		}
		
		public Class<? extends TileData> getDataClass(){
			return PinetreeTileData.class;
		}
	}, 
	stoneblock(MaterialType.block){{addDrop(Item.stone, 6);}},
	woodblock(MaterialType.block){{addDrop(Item.wood, 5);}},
	hatcher(MaterialType.hatcher),
	box(MaterialType.chest){
		{addDrop(Item.wood, 10);}
		public TileData getDefaultData(){
			return new InventoryTileData(5,5);
		}
		
		public Class<? extends TileData> getDataClass(){
			return InventoryTileData.class;
		}
	};
	private MaterialType type = MaterialType.tile;
	private Color foilageColor = Hue.rgb(69, 109, 29, 0.04f);
	private int breaktime;
	private boolean enablecollisions = true;
	private Array<ItemStack> drops = new Array<ItemStack>();
	private Color color = Color.CLEAR;
	
	private Materials(){}
	
	private Materials(MaterialType type){
		this.type = type;
	}
	
	private Materials(MaterialType type, int breaktime, Color color){
		this.type = type;
		this.breaktime = breaktime;
		this.color = color;
	}
	
	private Materials(MaterialType type, int breaktime){
		this(type, breaktime, Color.WHITE);
	}

	private Materials(MaterialType type, int breaktime, boolean collisions){
		this.type = type;
		this.breaktime = breaktime;
		this.enablecollisions = collisions;
	}
	
	private Materials(MaterialType type, Color foilageColor){
		this.type = type;
		this.foilageColor = foilageColor;
	}
	
	private Materials(Color foilageColor){
		this.foilageColor = foilageColor;
	}
	
	public Color foilageColor(){ // the color that plants have on this tile
		return foilageColor;
	}
	
	public final boolean breakable(){ // if the block is breakable (controls whether or not it is searched)
		return breaktime >= 0;
	}
	
	public final int breakTime(){ // how much time it takes to break the block
		return breaktime;
	}
	
	public boolean collisionsEnabled(){ //if collisions are enabled
		return enablecollisions;
	}
	
	public MaterialType getType(){ //the type
		return type;
	}
	
	public void harvestEvent(Tile tile){ //called when the block is harvested/destroyed
		if(getType().tile()){
			//tile.tile = Material.air;
		}else{
			//tile.blockid = 0;
		}
	}
	
	public void changeEvent(Tile tile){ //called when the block is changed/updated
		
	}
	
	public TileData getDefaultData(){
		return null;
	}
	
	public Class<? extends TileData> getDataClass(){
		return null;
	}
	
	public boolean growable(){
		return false;
	}
	
	public Material growMaterial(){
		return null;
	}
	
	public void growEvent(Tile tile){
		//tile.block = (Materials)growMaterial();
	}
	
	public Color getColor(){
		if(type.getColor() != null) return type.getColor();
		return color;
	}
	
	protected void addDrop(Item item, int amount){
		if(drops == null) drops = new Array<ItemStack>();
		drops.add(new ItemStack(item, amount));
	}
	
	public ItemStack[] getDrops(){
		return drops.toArray(ItemStack.class);
	}
	
	public boolean dropsItem(Item item){
		for(ItemStack stack : drops){
			if(stack.item == item) return true;
		}
		return false;
	}
	
	public final int id(){
		return ordinal();
	}
	
	public boolean solid(){
		return type.solid() && collisionsEnabled();
	}
	
	public static Material random(Material... materials){
		return materials[MathUtils.random(materials.length-1)];
	}
	
	public static Material next(Materials mat, int max){
		return values()[mat.ordinal() + MathUtils.random(max-1)];
	}
}
