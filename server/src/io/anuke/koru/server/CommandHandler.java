package io.anuke.koru.server;

import static io.anuke.ucore.util.ColorCodes.*;

import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

import com.badlogic.gdx.math.MathUtils;

import io.anuke.koru.entities.Prototypes;
import io.anuke.koru.network.Net;
import io.anuke.koru.systems.EntityMapper;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.ecs.Processor;
import io.anuke.ucore.ecs.Spark;


public class CommandHandler{
	final KoruServer server;
	private HashMap<String, Command> commands = new HashMap<String, Command>();
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
		
		register();
	}
	
	private void register(){
		cmd("help", ()->{
			print(YELLOW + " - Commands - ");
			for(Command c : commands.values()){
				print("* " + c.name + " " + c.params);
			}
		});
		
		cmd("spawn", ()->{
			for(int i = 0; i < 30; i++){
				Spark entity = new Spark(Prototypes.testEntity);
				entity.pos().set(MathUtils.random(-40, 40), MathUtils.random(-40, 40));
				Net.sendSpark(entity.add());
			}
			
			print("spawning 30 entities. (now "+PURPLE+server.updater.basis.getSparks().size+LIGHT_CYAN+" entities at "+PURPLE+ 1f/(Timers.delta()/60f)+LIGHT_CYAN +" FPS.)");
		});
		
		cmd("systems", ()->{
			for(Processor system : server.updater.basis.getProcessors()){
				print(YELLOW + "- " + system.getClass().getSimpleName()  + (system.isEnabled() ? GREEN + " [Enabled]" : RED  +" [Disabled]"));
			}
		});
		
		cmd("entities", ()->{
			print(YELLOW + server.updater.basis.getSparks().size);
		});
		
		cmd("fps", ()->{
			print(YELLOW + 1f/(Timers.delta()/60f));
		});
		
		cmd("cells", ()->{
			print(YELLOW + server.updater.basis.getProcessor(EntityMapper.class).getCellAmount());
		});
		
		cmd("sysdisable", "<name>", (args)->{
			String name = args[0];
			
			for(Processor system : server.updater.basis.getProcessors()){
				if(name.equals(system.getClass().getSimpleName().toLowerCase())){
					system.setEnabled(false);
					print(YELLOW + "System '"+name+"' disabled.");
					return;
				}
			}
			
			print(YELLOW + "No system '"+name+"' found.");
		});
		
		cmd("sysenable", "<name>", (args)->{
			String name = args[0];
			
			for(Processor system : server.updater.basis.getProcessors()){
				if(name.equals(system.getClass().getSimpleName().toLowerCase())){
					system.setEnabled(true);;
					print(YELLOW + "System '"+name+"' enabled.");
					return;
				}
			}
			
			print(YELLOW + "No system '"+name+"' found.");
		});
		
	}

	private void process(String s){
		
		String c = s.indexOf(" ") == -1 ? "" : s.split(" ")[0];
		String[] args = s.substring(c.length()+1).split(" ");
		
		if(c.isEmpty()){
			c = s;
			args = new String[]{};
		}
		
		if(commands.containsKey(c)){
			Command com = commands.get(c);
			if(args.length == com.paramLength){
				com.runner.accept(args);
			}else{
				print("Command usage: " + c + " " + com.params);
			}
		}else{
			print(PURPLE + "Invalid command.");
		}
		
	}

	private void print(String s){
		System.out.println(LIGHT_CYAN + s + RED);
	}
	
	private void cmd(String name, Runnable r){
		commands.put(name, new Command(name, "", s->{
			r.run();
		}));
	}
	
	private void cmd(String name, String params, Consumer<String[]> cons){
		commands.put(name, new Command(name, params, cons));
	}
	
	private class Command{
		String name, params;
		Consumer<String[]> runner;
		int paramLength;
		
		public Command(String name, String params, Consumer<String[]> runner){
			this.name = name;
			this.params = params;
			this.runner = runner;
			this.paramLength = params.length() == 0 ? 0 : (params.length() - params.replaceAll(" ", "").length() + 1);
		}
	}
}
