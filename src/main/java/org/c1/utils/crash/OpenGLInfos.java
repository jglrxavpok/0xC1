package org.c1.utils.crash;

import org.c1.client.*;

public class OpenGLInfos implements CrashInfos
{

    @Override
    public String getInfos()
    {
        String header = SECTION_START + " OpenGL " + SECTION_END;
        return header + "\n\tGraphics Card: " + OpenGLHelper.getOpenGLRendererInfo() + "\n\tVersion: " + OpenGLHelper.getOpenGLVersion() + "\n\tVendor: " + OpenGLHelper.getOpenGLVendor();
    }

}
