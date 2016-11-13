package net.pixelstatic.koru.behaviors.tasks;

import net.pixelstatic.koru.entities.Effects;
import net.pixelstatic.koru.world.Material;

public class ParticleWaitTask extends WaitTask{
	final int x,y;
	final Material material;
	private boolean particled = false;

	public ParticleWaitTask(int x, int y, Material material, float duration){
		super(duration);
		this.x = x;
		this.y = y;
		this.material = material;
	}
	
	@Override
	protected void update(){
		super.update();
		if(this.duration > 1 && (int)(time) >= (int)duration/3 && !particled){
			Effects.blockParticle(x, y, material);
			particled = true;
		}
			
	}

}
