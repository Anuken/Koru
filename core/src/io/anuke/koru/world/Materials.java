package io.anuke.koru.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import io.anuke.koru.items.Item;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.items.Items;
import io.anuke.ucore.graphics.Hue;

public enum Materials implements Material{
	air, 
	grass(MaterialType.grass, Hue.lightness(1f)),
	darkgrass(MaterialType.grass, color(0.8f,0.92f,0.86f)),
	swampgrass(MaterialType.grass),
	burntgrass(MaterialType.grass, color(1.3f,1.1f,0.98f)),
	burntgrass2(MaterialType.grass, color(1.6f,1.2f,0.94f)),
	bluegrass(MaterialType.grass),
	water(MaterialType.water, 1, Hue.blend(41, 97, 155, 102, 102, 102, 0.3f)){{addDrop(Items.water, 1);}},
	deepwater(MaterialType.water, 1, Hue.blend(41, 97, 155, 102, 102, 102, 0.3f)){{addDrop(Items.water, 1);}},
	blackrock,
	magmarock,
	cobblestone,
	sand{{vary(7);}},
	gravel,
	ice{{vary(7);}},
	riveredge,
	stone(MaterialType.tile){{vary(7);}}, 
	woodfloor{{addDrop(Items.wood, 2);}},
	stonefloor{{addDrop(Items.stone, 2);}},
	pinecones(MaterialType.overlay){{addDrop(Items.pinecone, 1);}}, 
	grassblock(MaterialType.tallgrassblock),
	shortgrassblock(MaterialType.shortgrassblock),
	tallgrass1(MaterialType.foilage, -10f),
	tallgrass2(MaterialType.foilage, -10f),
	tallgrass3(MaterialType.foilage, -10f),
	wheatgrass1(MaterialType.foilage),
	wheatgrass2(MaterialType.foilage),
	wheatgrass3(MaterialType.foilage),
	fern1(MaterialType.foilage),
	fern2(MaterialType.foilage),
	fern3(MaterialType.foilage),
	koru1(MaterialType.foilage),
	koru2(MaterialType.foilage),
	koru3(MaterialType.foilage),
	bush1(MaterialType.object, -1f),
	bush2(MaterialType.object, -1f),
	bush3(MaterialType.object, -1f),
	mossyrock1(MaterialType.object),
	mossyrock2(MaterialType.object),
	rock1(MaterialType.object, -1f){{breakt(50);addDrop(Items.stone, 2);}},
	rock2(MaterialType.object, -1f){{breakt(50);addDrop(Items.stone, 2);}},
	rock3(MaterialType.object, -1f){{breakt(50);addDrop(Items.stone, 2);}},
	rock4(MaterialType.object, -2f){{breakt(50);addDrop(Items.stone, 2);}},
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
	drybush1(MaterialType.object, -1f),
	drybush2(MaterialType.object, -1f),
	drybush3(MaterialType.object, -1f),
	deadtree1(MaterialType.tree, -3f),
	deadtree2(MaterialType.tree, -2f),
	deadtree3(MaterialType.tree, -3f),
	deadtree4(MaterialType.tree),
	oaktree1(MaterialType.tree, -10f),
	oaktree2(MaterialType.tree, -10f),
	oaktree3(MaterialType.tree, -10f),
	oaktree4(MaterialType.tree, -4f),
	oaktree5(MaterialType.tree, -4f),
	oaktree6(MaterialType.tree, -4f),
	willowtree1(MaterialType.tree, -4f),
	willowtree2(MaterialType.tree, -3f),
	willowtree3(MaterialType.tree, -4f),
	willowtree4(MaterialType.tree, -3f),
	burnedtree1(MaterialType.tree),
	burnedtree2(MaterialType.tree),
	burnedtree3(MaterialType.tree),
	burnedtree4(MaterialType.tree),
	pinetree1(MaterialType.tree, -5f){{addDrop(Items.wood, 50);breakt(90);}}, 
	pinetree2(MaterialType.tree, -3f){{addDrop(Items.wood, 50);breakt(90);}}, 
	pinetree3(MaterialType.tree, -3f){{addDrop(Items.wood, 50); breakt(90);}}, 
	pinetree4(MaterialType.tree, -5f){{addDrop(Items.wood, 50); breakt(90);}}, 
	pinesapling(MaterialType.object, 1, false){{addDrop(Items.pinecone, 1);}}, 
	stonepillar(MaterialType.block, 100){{addDrop(Items.stone, 6); color = new Color(0x744a28ff);}},
	stoneblock(MaterialType.block, 50){{addDrop(Items.stone, 6); color = new Color(0x744a28ff);}},
	woodblock(MaterialType.block, 60){{addDrop(Items.wood, 5); color = new Color(0x744a28ff);}},
	hatcher(MaterialType.hatcher),
	torch(MaterialType.torch, 20){{addDrop(Items.wood, 1);}},
	box(MaterialType.chest){{addDrop(Items.wood, 10);}};
	private MaterialType type = MaterialType.tile;
	private Color foilageColor = Hue.rgb(69, 109, 29, 0.04f);
	private int breaktime;
	private boolean enablecollisions = true;
	private Array<ItemStack> drops = new Array<ItemStack>();
	public Color color = Color.CLEAR;
	private float offset = 0;
	private int variants = 1;
	
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
		this(type, breaktime, null);
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
	
	private Materials(MaterialType type, float offset){
		this.type = type;
		this.offset = offset;
		color = new Color(0x5a391eff);
	}
	
	void vary(int i){
		this.variants = i;
	}
	
	private Materials(Color foilageColor){
		this.foilageColor = foilageColor;
	}
	
	public Color foilageColor(){ // the color that plants have on this tile
		return foilageColor;
	}
	
	public void breakt(int t){
		this.breaktime = t;
	}
	
	public final int breaktime(){ // how much time it takes to break the block
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
	
	public static Color color(float r, float g, float b){
		Color color = new Color();
		color.r = r;
		color.g = g;
		color.b = b;
		color.a = 1f;
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
	
	public float offset(){
		return offset;
	}
	
	public int variants(){
		return variants;
	}
	
	public static Material random(Material... materials){
		return materials[MathUtils.random(materials.length-1)];
	}
	
	public static Material next(Materials mat, int max){
		return values()[mat.ordinal() + MathUtils.random(max-1)];
	}
}
