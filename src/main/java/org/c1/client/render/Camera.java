package org.c1.client.render;

import org.c1.client.*;
import org.c1.maths.*;

public class Camera
{

    private Matrix4   projection;

    private String    name = "unknown cam";

    private Transform transform;

    public Camera(Matrix4 projection)
    {
        this.projection = projection;
        transform = new Transform();
    }

    public Camera(float fov, float aspect, float zNear, float zFar)
    {
        super();
        this.projection = new Matrix4().initPerspective(fov, aspect, zNear, zFar);
    }

    public Camera setProjection(Matrix4 projection)
    {
        this.projection = projection;
        return this;
    }

    public Matrix4 getViewProjection()
    {
        Matrix4 cameraRotation = getRotationMatrix();
        Vector3 cameraPos = getTransform().getTransformedPos().mul(-1);
        Matrix4 cameraTranslation = new Matrix4().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
        return projection.mul(cameraRotation.mul(cameraTranslation));
    }

    public Matrix4 getRotationMatrix()
    {
        return getTransform().getTransformedRotation().conjugate().toRotationMatrix();
    }

    public Vector3 getLeft()
    {
        return getTransform().getRotation().getLeft();
    }

    public Vector3 getRight()
    {
        return getTransform().getRotation().getRight();
    }

    public Vector3 getPos()
    {
        return getTransform().getTransformedPos();
    }

    public void setPos(Vector3 pos)
    {
        getTransform().setPosition(pos);
    }

    public Vector3 getForward()
    {
        return getTransform().getTransformedRotation().getForward();
    }

    public Vector3 getUp()
    {
        return getTransform().getTransformedRotation().getUp();
    }

    public String getName()
    {
        return name;
    }

    public Camera setName(String name)
    {
        this.name = name;
        return this;
    }

    public Transform getTransform()
    {
        return transform;
    }

}
