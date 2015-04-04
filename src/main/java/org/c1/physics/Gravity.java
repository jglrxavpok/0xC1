package org.c1.physics;

import org.c1.maths.Vec3f;

public class Gravity {

	private float force;
	private Vec3f center;

	// Since gravity should be a sphere, it -should- only require a radius
	private float radius;
	private float attractRadius;
	
	private Sphere boundingBox;

	public Gravity(float force, Vec3f center, float radius,
			float attractionRadius) {
		this.force = force;
		this.center = center;
		this.radius = radius;
		this.attractRadius = attractionRadius;
		this.boundingBox = new Sphere(center, radius);
	}

	public float getForce() {
		return force;
	}

	public void setForce(float force) {
		this.force = force;
	}

	public Vec3f getCenter() {
		return center;
	}

	public void setCenter(Vec3f center) {
		this.center = center;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getAttractRadius() {
		return attractRadius;
	}

	public void setAttractRadius(float attractRadius) {
		this.attractRadius = attractRadius;
	}
	
	
	

}
