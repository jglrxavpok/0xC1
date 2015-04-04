package org.c1.tests;

import java.io.IOException;
import java.util.List;

import org.c1.client.Model;
import org.c1.client.gui.editor.ShipEditorComponent;
import org.c1.client.render.Texture;
import org.c1.level.GameObject;
import org.c1.maths.Vec3f;

import com.google.common.collect.Lists;

public class ModularShipObject extends GameObject {

    private List<ShipEditorComponent> components;
    private Model model;
    private Texture texture;
    
    public ModularShipObject(String id) {
        super(id);
        this.model = new Model();
        this.components = Lists.newArrayList();
        try {
            this.texture = new Texture("textures/logo.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Made for walls only - YET -
     * @param x xCoord 
     * @param y yCoord
     */
    public void addShipComponent(ShipEditorComponent comp){
        components.add(comp);
    }
    
    public void createShipModel(){
        for(ShipEditorComponent comp : components){
            model.addBox(new Vec3f(0, 0, 0), new Vec3f(1, 1, 1));
        }
    }

    @Override
    public void update(double delta) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render(double delta) {
        texture.bind(0);
        model.render();
    }

    @Override
    public boolean shouldDie() {
        // TODO Auto-generated method stub
        return false;
    }

}
