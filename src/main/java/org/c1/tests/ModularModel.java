package org.c1.tests;

import java.util.List;

import org.c1.client.Model;
import org.c1.client.model.ModelCube;
import org.c1.maths.Vec3f;

public class ModularModel extends Model {
    
    public List<Model> components;
    
    public ModularModel(){
        
    }
    
    public void addComponent(Model model) {
        this.components.add(model);
    }
    
    public void addBox(Vec3f pos, Vec3f size){
        this.addComponent(new ModelCube(pos, size));
    }

    @Override
    public void render(){
        super.render();
        for(Model m : components){
            m.render();
        }
    }
    
    
}
