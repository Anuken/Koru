package io.anuke.koru.world.materials;

import static io.anuke.koru.world.materials.MaterialTypes.*;

import com.badlogic.gdx.graphics.Color;

import io.anuke.koru.Koru;
import io.anuke.koru.entities.KoruEntity;
import io.anuke.koru.items.Items;
import io.anuke.koru.network.IServer;
import io.anuke.koru.network.packets.MenuOpenPacket;
import io.anuke.koru.ui.CraftingMenu;
import io.anuke.koru.world.Tile;
import io.anuke.koru.world.TileData;

/**Artifical materials built by the player.*/
public class StructMaterials{
	
	//Tiles
	
	public static final Material woodfloor = new Material("woodfloor", tile){{
		addDrop(Items.wood, 1);
		color = new Color(0x744a28ff);
		breaktime = 20;
	}};
	
	public static final Material stonefloor = new Material("stonefloor", tile){{
		addDrop(Items.stone, 1);
		color = new Color(0x717171ff);
		breaktime = 20;
	}};
	
	
	//Objects, blocks
	
	public static final Material stonepillar = new Material("stonepillar", block){{
		addDrop(Items.stone, 2); 
		color = new Color(0x717171ff);
		breaktime = 100;
	}};
	
	public static final Material woodblock = new Material("woodblock", block){{
		addDrop(Items.wood, 2); 
		color = new Color(0x744a28ff);
		breaktime = 60;
	}};
	
	public static final Material torch = new Material("torch",  StructMaterialTypes.torch){{
		addDrop(Items.wood, 1);
		breaktime = 20;
	}};
	
	public static final Material box = new Material("box", StructMaterialTypes.chest){{
		addDrop(Items.wood, 10);
		
	}};
	
	public static final Material workbench = new Material("workbench",  StructMaterialTypes.workbench){
		{
			addDrop(Items.wood, 10);
			interactable = true;
		}
		
		public void onInteract(Tile tile, int x, int y, KoruEntity entity){
			MenuOpenPacket packet = new MenuOpenPacket();
			packet.type = CraftingMenu.class;
			IServer.instance().sendTCP(entity.connection().connectionID, packet);
			Koru.log("Sent packet");
		}
		
		class Data extends TileData{
			
		}
	};
	
	public static void load(){}
}
