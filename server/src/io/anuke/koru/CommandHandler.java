package io.anuke.koru;

import static io.anuke.koru.utils.Text.*;

import java.util.Scanner;


public class CommandHandler{
	final KoruServer server;
	Scanner scan;
	
	public CommandHandler(KoruServer server){
		this.server = server;
		
		scan = new Scanner(System.in);
		
		new Thread(()->{
			while(true){
				process(scan.nextLine().toLowerCase());
			}
		}){{setDaemon(true);}}.start();
	}
	
	private void process(String s){
		if(s.equals("spawn")){
			print("spawning");
		}else{
			print(PURPLE + "Invalid command.");
		}
	}
	
	private void print(String s){
		System.out.println(LIGHT_CYAN + s + YELLOW);
	}
}
