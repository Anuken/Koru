package io.anuke.koru.world.materials;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import io.anuke.koru.items.Item;
import io.anuke.koru.items.ItemStack;
import io.anuke.koru.modules.World;
import io.anuke.koru.traits.InventoryTrait;
import io.anuke.koru.world.BreakType;
import io.anuke.koru.world.Tile;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.facet.FacetList;
import io.anuke.ucore.util.Strings;
				
public abstract class Material{
	private static ArrayList<Material> materials = new ArrayList<Material>();
	private static int lastid;
	
	protected final int id;
	protected final String name;
	protected final MaterialLayer layer;
	protected String formalName;
	
	protected Vector3 foilageTint = new Vector3(1f, 1f, 1f);
	protected Color color = Color.CLEAR;
	
	protected float offset = 0;
	protected float[] offsets;
	protected int variants = 1;
	protected int cullSize = 24;
	
	protected boolean interactable = false;
	protected int breaktime;
	protected Array<ItemStack> drops = new Array<ItemStack>();
	protected BreakType breaktype = BreakType.stone;
	
	protected boolean solid;
	protected Rectangle hitbox = new Rectangle(0, 0, World.tilesize, World.tilesize);
	
	public static Material getMaterial(int id){
		return materials.get(id);
	}
	
	public static Iterable<Material> getAll(){
		return materials;
	}
	
	protected Material(String name, MaterialLayer layer){
		id = lastid++;
		this.name = name;
		this.layer = layer;
		formalName = Strings.capitalize(name);
		
		materials.add(this);
	}
	
	public String formalName(){
		return formalName;
	}
	
	public abstract void draw(Tile tile, FacetList list);
	
	public void changeEvent(Tile tile){}
	
	//collect the block be default
	public void onInteract(Tile tile, Spark spark){
		spark.get(InventoryTrait.class).addItems(tile.wall().getDrops());
		spark.get(InventoryTrait.class).sendUpdate(spark);
			
		tile.setWall(Materials.air);
	}
	
	public int id(){
		return id;
	}
	
	public String name(){
		return name;
	}
	
	//TODO
	public void setColor(Color color){
		this.color = color;
	}

	public Vector3 foilageTint(){
		return foilageTint;
	}
	
	public MaterialLayer layer(){
		return layer;
	}
	
	public boolean isLayer(MaterialLayer layer){
		return this.layer == layer;
	}
	
	public int breaktime(){
		return breaktime;
	}
	
	public boolean isBreakable(){
		return breaktime > 0;
	}
	
	public boolean interactable(){
		return interactable;
	}
	
	public float offset(){
		return offset;
	}
	
	public BreakType breakType(){
		return breaktype;
	}
	
	public ItemStack[] getDrops(){
		return drops.toArray(ItemStack.class);
	}
	
	public int variants(){
		return variants;
	}
	
	/**Offset at a certain location.*/
	public float variantOffset(Tile tile){
		return (offsets == null ? offset : offsets[tile.rand(variants)-1]);
	}
	
	public String variantString(Tile tile){
		return variants > 0 ? tile.rand(variants) + "" : "";
	}

	public Color getColor(){
		return color;
	}

	public boolean solid(){
		return solid;
	}
	
	public int cullSize(){
		return cullSize;
	}

	public Rectangle getHitbox(int x, int y, Rectangle rectangle){
		return rectangle.setSize(hitbox.width, hitbox.height)
				.setCenter(hitbox.x + x * World.tilesize, hitbox.y + y * World.tilesize);
	}
	
	//builder methods...
	protected void offsets(float... offsets){
		this.offsets = offsets;
	}
	
	protected void addDrops(ItemStack... stacks){
		drops.addAll(stacks);
	}
	
	protected void addDrop(Item item, int amount){
		drops.add(new ItemStack(item, amount));
	}
	
	@Override
	public String toString(){
		return name() + ":" + id();
	}
}
