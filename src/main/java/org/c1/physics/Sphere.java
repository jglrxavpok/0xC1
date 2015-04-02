package org.c1.physics;

import org.c1.maths.Vec3f;

public class Sphere {

	private Vec3f position;
	private float radius;

	public Sphere(){
		this(new Vec3f(), 0.0f);
	}
	
	public Sphere(Vec3f pos){
		this(pos, 0.0f);
	}
	
	public Sphere(Vec3f pos, float rad) {	
		this.position = pos;
		this.radius = rad;
	}
	
	public boolean collidesSphere(Sphere o){
		
		float xd = position.x() - o.position.x();
	    float yd = position.y() - o.position.y();
	    float zd = position.z() - o.position.z();
	    
	    float sumRadius = getRadius() + o.getRadius();
	    float sqrRadius = sumRadius * sumRadius;

	    float distSqr = (xd * xd) + (yd * yd) + (zd * zd);

	    if (distSqr <= sqrRadius)
	    {
	        return true;
	    }
		
		return false;
	}
	
	public boolean collidesAABB(AABB aabb){
		
		return false;
	}

	public Vec3f getPosition() {
		return position;
	}

	public void setPosition(Vec3f position) {
		this.position = position;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
}
