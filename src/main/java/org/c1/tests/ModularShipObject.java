package org.c1.tests;

import java.util.List;

import org.c1.client.gui.editor.ShipEditorComponent;
import org.c1.level.GameObject;
import org.c1.maths.Vec3f;

public class ModularShipObject extends GameObject {

    private List<ShipEditorComponent> components;
    private ModularModel model;
    
    public ModularShipObject(String id) {
        super(id);
        this.model = new ModularModel();
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
            model.addBox(new Vec3f(comp.pos, 0), new Vec3f(1, 1, 1));
        }
    }

    @Override
    public void update(double delta) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render(double delta) {
        model.render();
    }

    @Override
    public boolean shouldDie() {
        // TODO Auto-generated method stub
        return false;
    }

}
