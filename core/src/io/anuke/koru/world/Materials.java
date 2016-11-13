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
	water(MaterialType.water, 1, Hue.blend(41, 97, 155, 102, 102, 102, 0.3f)){
		{addDrop(Item.water, 1);}
	},
	riveredge,
	stone(MaterialType.tile, 120, Hue.rgb(115, 115, 115, 0.09f)){
		{addDrop(Item.stone, 1);}
	}, 
	woodfloor{{addDrop(Item.wood, 2);}},
	stonefloor{{addDrop(Item.stone, 2);}},
	pinecones(MaterialType.overlay){{addDrop(Item.pinecone, 1);}}, 
	tallgrass1(MaterialType.grass),
	tallgrass2(MaterialType.grass),
	tallgrass3(MaterialType.grass),
	fern1(MaterialType.grass),
	fern2(MaterialType.grass),
	fern3(MaterialType.grass),
	koru1(MaterialType.grass),
	koru2(MaterialType.grass),
	koru3(MaterialType.grass),
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
