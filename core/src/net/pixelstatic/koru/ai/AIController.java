package net.pixelstatic.koru.ai;

import com.badlogic.gdx.math.Vector2;

public class AIController{
	static Vector2 v = new Vector2();
	static int space = 14;
	static int skip = 1;
	static public final boolean debug = false;
	static public final float changerange = 11;

	public static Vector2 pathfindTo(AIData data, float x, float y, float targetx, float targety){

		if( !Cast2(x, y, targetx, targety) && false){
			v.set(targetx, targety);
		}else{
			if(data.lastpos.dst(v.set(targetx, targety)) > changerange || data.path == null){
				data.CreatePath(x, y, targetx, targety);
			}
			if(data.path.nodes.size <= 1) return v.set(targetx, targety);
			
			if(data.node + 2 >= data.path.nodes.size){
				return v.set(targetx, targety);
			}else if(data.path.getNodePosition(1 + data.node).dst(v.set(x, y)) <= 1f){
				data.node ++;
			}
			v.set(data.path.getNodePosition(1 + data.node));
		}
		return v;
		/*
		//Cast(x, y, x + 50, y + 50);
		if(data.aligns()){
		    // corner trap detected, now what?
		    data.lockedray = Math.max(data.first, data.middle);
		    data.timer.CountDown(100);
		    // Home.log("aligning");
		}

		/*
		if(data.aligns()){
		    // corner trap detected, now what?
		    //data.lockedray = Math.random() < 0.5 ? data.first : data.middle;
		    Vector2 r = new Vector2(targetx - x, targety - y);
		    r.setLength(r.len());
		    float ang = r.angle();
		    float f = 360 / (space);
		    r.setAngle((data.first / 2 + 1) * f * (float)Math.pow( -1, data.first) + ang);
		    


		    Vector2 relative_first = new Vector2(x, y).add(r);
		    
		    r.setAngle((data.middle / 2 + 1) * f * (float)Math.pow( -1, data.middle) + ang);
		    
		    Vector2 relative_middle = new Vector2(x, y).add(r);
		    
		    data.lockedray = relative_first.dst(new Vector2(targetx, targety)) < relative_middle.dst(new Vector2(targetx, targety)) ?
			    data.first : data.middle;
			
		    


		    data.timer.CountDown(50);
		    // Home.log("aligning");
		}
		
		

		if( !Cast2(x, y, targetx, targety)){
		    v.set(targetx, targety);
		}else if( !data.timer.Done()){
		    Vector2 r = new Vector2(targetx - x, targety - y);
		    float f = 360 / space;
		    float ang = r.angle();
		    r.setAngle((data.lockedray / 2 + 1) * f * (float)Math.pow( -1, data.lockedray) + ang);
		    v.set(x + r.x, y + r.y);
		    return v;
		}else{
		    Vector2 r = new Vector2(targetx - x, targety - y);
		    r.setLength(r.len());
		    float ang = r.angle();
		    for(int i = 0;i <= space;i ++){
			if( !data.timer.Done() && i <= data.lockedray){
			    //    i  = data.lockedray + 1;
			}
			float f = 360 / (space);
			//r.setAngle((i/2+1) * f * (float)Math.pow(-1, i) + ang);
			r.setAngle((i / 2 + 1) * f * (float)Math.pow( -1, i) + ang);

			if( !Cast2(x, y, x + r.x, y + r.y)){
			    v.set(x + r.x, y + r.y);
			    data.push(i);
			    return v;
			}
			if(i == 4){
			    v.set(x, y);
			}Pathfinder.GetNext(x, y, targetx, targety);
		    }
		}

		/*
			if( !Cast2(x, y, targetx, targety)){
			    v.set(targetx, targety);
			}else{
			    Vector2 r = new Vector2(targetx - x, targety - y);
			    float ang = r.angle();
			    r.setLength(r.len() * data.len);
			    r.setAngle(ang + data.spacing);

			    boolean both = false;

			    if(!Cast2(x, y, x + r.x, y + r.y)){
				v.set(x + r.x, y + r.y);
				return v;
			    }else{
				both = true;
			    }Pathfinder.GetNext(x, y, targetx, targety);

			    r.setAngle(ang - data.spacing);

			    if(!Cast2(x, y, x + r.x, y + r.y)){
				v.set(x + r.x, y + r.y);
				return v;
			    }else{
				both = both && true;
			    }

			    if(both){
				if(data.len > 0.6f) data.len -= 0.2f;
			    }else{
				data.len = 1f;
			    }

			}
		
		return v;//Pathfinder.GetNext(x, y, targetx, targety);
		*/
	}

	public static boolean solid(float x, float y){
		return false;
	}
/*
	public static Array<Vector2> line(int x0, int y0, int x1, int y1){
		Array<Vector2> line = new Array<Vector2>();

		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);

		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;

		int err = dx - dy;
		int e2;

		while(true){
			line.add(new Vector2(x0, y0));
			if((x0 + y0) % 10 == 0) new FramedEffect(x0, y0, "blank", 1, 5).SetColor(0, 0, 1).SetLayer( -1).SetShadow(false).SetNoFrame(true).SendSelf();

			if(x0 == x1 && y0 == y1) break;

			e2 = 2 * err;
			if(e2 > -dy){
				err = err - dy;
				x0 = x0 + sx;
			}

			if(e2 < dx){
				err = err + dx;
				y0 = y0 + sy;
			}
		}
		return line;
	}
*/
	public static boolean Cast2(float x0f, float y0f, float x1f, float y1f){
		int x0 = (int)x0f;
		int y0 = (int)y0f;
		int x1 = (int)x1f;
		int y1 = (int)y1f;
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);

		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;

		int err = dx - dy;
		int e2;
		int i = 0;
		while(true){

			if(i > skip && solid(x0, y0)){
				return true;
			}
			i ++;
			if(x0 == x1 && y0 == y1) break;

			e2 = 2 * err;
			if(e2 > -dy){
				err = err - dy;
				x0 = x0 + sx;
			}

			if(e2 < dx){
				err = err + dx;
				y0 = y0 + sy;
			}
		}
		return false;
	}

	public static Vector2 VectorCast(float x0f, float y0f, float x1f, float y1f){
		int x0 = (int)x0f;
		int y0 = (int)y0f;
		int x1 = (int)x1f;
		int y1 = (int)y1f;
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);

		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;

		int err = dx - dy;
		int e2;
		int i = 0;
		while(true){

			if(i > skip && solid(x0, y0)){
				return new Vector2(x0, y0);
			}
			i ++;
			if(x0 == x1 && y0 == y1) break;

			e2 = 2 * err;
			if(e2 > -dy){
				err = err - dy;
				x0 = x0 + sx;
			}

			if(e2 < dx){
				err = err + dx;
				y0 = y0 + sy;
			}
		}
		return null;
	}
}
