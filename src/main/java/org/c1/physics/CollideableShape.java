package org.c1.physics;

public abstract class CollideableShape extends Shape {
	
	public boolean isSolid;
	
	public abstract AABB getAABB();
	

}
