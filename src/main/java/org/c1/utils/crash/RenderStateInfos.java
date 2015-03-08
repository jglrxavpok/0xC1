package org.c1.utils.crash;

import java.util.*;

import org.c1.client.*;
import org.c1.client.render.*;

public class RenderStateInfos implements CrashInfos
{

    private RenderEngine renderEngine;

    public RenderStateInfos(RenderEngine engine)
    {
        this.renderEngine = engine;
    }

    @Override
    public String getInfos()
    {
        String renderState = SECTION_START + " Current RenderState " + SECTION_END;
        RenderState current = renderEngine.getRenderState();
        Iterator<Integer> it = current.getGLCaps().keySet().iterator();
        String glCaps = "";
        while(it.hasNext())
        {
            int cap = it.next();
            boolean isEnabled = current.getGLCaps().get(cap);
            glCaps += "\tglCaps[" + OpenGLHelper.getCapName(cap) + "] = " + isEnabled + "\n";
        }
        String funcs = "\tAlpha func: " + current.getAlphaFunc() + ", ref: " + current.getAlphaRef() + "\n";
        funcs += "\tBlend Func src: " + OpenGLHelper.getCapName(current.getBlendFuncSrc()) + ", Blend func dst: " + OpenGLHelper.getCapName(current.getBlendFuncDst()) + "\n";
        return renderState + "\n" + glCaps + funcs;
    }

}
