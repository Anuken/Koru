package net.pixelstatic.koru.listeners;

import net.pixelstatic.koru.entities.KoruEntity;

public abstract class CollisionListener{
	abstract boolean accept(KoruEntity entity, KoruEntity other);
	abstract void collision(KoruEntity entity, KoruEntity other);
}
