package io.anuke.koru.entities;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.extend.traits.ProjectileTrait.ProjectileType;

public class ProjectileTypes{
	public static final ProjectileType 
	
	bolt = new ProjectileType(){
		@Override
		public void draw(Spark spark){
			Draw.alpha(spark.life().ifract());
			Draw.rect("bolt", spark.pos().x, spark.pos().y, spark.velocity().angle()-45);
			Draw.color();
		}
	}, 
	slash = new ProjectileType(){
		{
			lifetime = 12;
			speed = 2.4f;
			hitsize = 18f;
		}
		
		public void draw(Spark spark){
			Draw.alpha(spark.life().ifract());
			Draw.rect("slash", spark.pos().x, spark.pos().y, spark.velocity().angle()-45);
			Draw.color();
		}
	};
}
