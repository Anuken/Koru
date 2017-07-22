package io.anuke.koru.network;

import io.anuke.koru.network.syncing.SyncData;
import io.anuke.koru.traits.DirectionTrait;
import io.anuke.koru.traits.InputTrait;
import io.anuke.koru.traits.SyncTrait;
import io.anuke.ucore.ecs.Spark;

public enum SyncType{
	position{
		public SyncData write(Spark spark){
			return new SyncData(spark, spark.pos().x, spark.pos().y);
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
			return new SyncData(spark, spark.pos().x, spark.pos().y);
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
					spark.pos().x, spark.pos().y, 
					spark.get(InputTrait.class).input.mouseangle, 
					spark.get(DirectionTrait.class).direction);
		}

		public void read(SyncData data, Spark spark){
			spark.get(DirectionTrait.class).direction = data.get(3);
			float x = data.get(0);
			float y = data.get(1);
			
			spark.get(DirectionTrait.class).walking = 
					spark.pos().dst(x, y) > 0.05f;
			
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
