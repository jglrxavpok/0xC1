package org.c1;

import org.c1.resources.*;
import org.c1.utils.crash.*;

public interface C1Instance
{
    AssetLoader getAssetsLoader();

    void crash(CrashReport crashReport);
}
