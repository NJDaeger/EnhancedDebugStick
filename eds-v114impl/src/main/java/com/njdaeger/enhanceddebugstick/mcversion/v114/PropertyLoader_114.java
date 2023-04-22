package com.njdaeger.enhanceddebugstick.mcversion.v114;

import com.njdaeger.enhanceddebugstick.api.EnhancedDebugStickApi;
import com.njdaeger.enhanceddebugstick.mcversion.PropertyLoader;

public class PropertyLoader_114 implements PropertyLoader {
    
    @Override
    public void load(EnhancedDebugStickApi plugin) {
        Property_114.registerProperties();
    }
}
