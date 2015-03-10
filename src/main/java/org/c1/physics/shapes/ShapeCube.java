package org.c1.physics.shapes;

import org.c1.maths.Vec3f;
import org.c1.physics.AABB;
import org.c1.physics.CollideableShape;


public class ShapeCube extends CollideableShape {
	 
	public Vec3f size;
	
	public ShapeCube(Vec3f pos, Vec3f size, boolean isSolid){
		this.position = pos;
		this.size = size;
		this.isSolid = isSolid;
	}
	
	@Override
	public AABB getAABB() {
		return new AABB(this.position, this.size);
	}

	@Override
	public void render() {
		// TODO Make cube render
	}

}
