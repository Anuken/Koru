package io.anuke.koru.network;

import io.anuke.koru.network.syncing.SyncData;
import io.anuke.koru.traits.InputTrait;
import io.anuke.koru.traits.RenderComponent;
import io.anuke.koru.traits.SyncTrait;
import io.anuke.ucore.ecs.Spark;

public enum SyncType{
	position{
		public SyncData write(Spark spark){
			return new SyncData(spark, spark.getX(), spark.getY());
		}

		public void read(SyncData data, Spark spark){
			SyncTrait sync = spark.get(SyncTrait.class);
			
			if(sync.interpolator != null){
				sync.interpolator.push(spark, data.x(), data.y());
			}else{
				spark.pos().set(data.x(), data.y());
			}
		}
	},
	physics{
		public SyncData write(Spark spark){
			return new SyncData(spark, spark.getX(), spark.getY());
		}

		public void read(SyncData data, Spark spark){
			SyncTrait sync = spark.get(SyncTrait.class);
			
			if(sync.interpolator != null){
				sync.interpolator.push(spark, data.x(), data.y());
			}else{
				spark.pos().set(data.x(), data.y());
			}
		}
	},
	player{
		public SyncData write(Spark spark){
			return new SyncData(spark, 
					spark.getX(), spark.getY(), 
					spark.get(InputTrait.class).input.mouseangle, 
					spark.renderer().direction);
		}

		public void read(SyncData data, Spark spark){
			spark.get(RenderComponent.class).direction = data.get(3);
			float x = data.get(0);
			float y = data.get(1);
			
			//TODO
			spark.get(RenderComponent.class).renderer.walking = 
					spark.pos().dist(x, y) > 0.05f;
			
			spark.get(InputTrait.class).input.mouseangle = data.get(2);
			SyncTrait sync = spark.get(SyncTrait.class);
			
			if(sync.interpolator != null){
				sync.interpolator.push(spark, x, y);
			}else{
				spark.pos().set(x, y);
			}
		}
	};

	public abstract SyncData write(Spark spark);

	public abstract void read(SyncData buffer, Spark spark);

}
