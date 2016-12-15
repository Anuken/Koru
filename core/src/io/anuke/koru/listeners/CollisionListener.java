package io.anuke.koru.listeners;

import io.anuke.koru.entities.KoruEntity;

public abstract class CollisionListener{
	abstract boolean accept(KoruEntity entity, KoruEntity other);
	void contact(KoruEntity entity, KoruEntity other){}
	void collision(KoruEntity entity, KoruEntity other){}
}
