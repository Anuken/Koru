package io.anuke.koru;

import static io.anuke.koru.utils.Text.*;

import java.util.Scanner;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.MathUtils;

import io.anuke.koru.entities.EntityType;
import io.anuke.koru.entities.KoruEntity;


public class CommandHandler{
	final KoruServer server;
	Scanner scan;
	
	public CommandHandler(KoruServer server){
		this.server = server;
		
		scan = new Scanner(System.in);
		
		new Thread(()->{
			while(true){
				try{
					process(scan.nextLine().toLowerCase());
				}catch (Exception e){
					print(PURPLE + "Command error!");
					e.printStackTrace();
				}
			}
		}){{setDaemon(true);}}.start();
	}

	private void process(String s){
		
		String c = s.indexOf(" ") == -1 ? "" : s.split(" ")[0];
		String[] args = s.substring(c.length()+1).split(" ");
		
		if(c.isEmpty()) c = s;
		
		if(c.equals("spawn")){
			for(int i = 0; i < 30; i++){
				KoruEntity entity = new KoruEntity(EntityType.testmob);
				entity.position().set(MathUtils.random(-40, 40), MathUtils.random(-40, 40));
				entity.addSelf().sendSelf();
			}
			
			print("spawning 30 entities. (now "+PURPLE+server.updater.engine.getEntities().size()+LIGHT_CYAN+" entities at "+PURPLE+ 1f/(server.updater.delta/60f)+LIGHT_CYAN +" FPS.)");
		}else if(c.equals("systems")){
			for(EntitySystem system : server.updater.engine.getSystems()){
				print(YELLOW + "- " + system.getClass().getSimpleName()  + (system.checkProcessing() ? GREEN + " [Enabled]" : RED  +" [Disabled]"));
			}
		}else if(c.equals("sysdisable")){
			String name = args[0];
			
			for(EntitySystem system : server.updater.engine.getSystems()){
				if(name.equals(system.getClass().getSimpleName().toLowerCase())){
					system.setProcessing(false);
					print(YELLOW + "System '"+name+"' disabled.");
					return;
				}
			}
			
			print(YELLOW + "No system '"+name+"' found.");
		}else if(c.equals("sysenable")){
			String name = args[0];
			
			for(EntitySystem system : server.updater.engine.getSystems()){
				if(name.equals(system.getClass().getSimpleName().toLowerCase())){
					system.setProcessing(true);
					print(YELLOW + "System '"+name+"' enabled.");
					return;
				}
			}
			
			print(YELLOW + "No system '"+name+"' found.");
		}else if(c.equals("entities")){
			print(YELLOW + server.updater.engine.getEntities().size());
		}else if(c.equals("fps")){
			print(YELLOW + 1f/(server.updater.delta/60f));
		}else if(c.equals("cells")){
			print(YELLOW + server.updater.engine.map().getCellAmount());
		}else{
			print(PURPLE + "Invalid command.");
		}
	}

	private void print(String s){
		System.out.println(LIGHT_CYAN + s + RED);
	}
}
