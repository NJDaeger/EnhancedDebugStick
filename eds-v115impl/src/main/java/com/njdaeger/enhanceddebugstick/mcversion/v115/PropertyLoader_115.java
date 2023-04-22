package com.njdaeger.enhanceddebugstick.mcversion.v115;

import com.njdaeger.enhanceddebugstick.api.EnhancedDebugStickApi;
import com.njdaeger.enhanceddebugstick.mcversion.PropertyLoader;

public class PropertyLoader_115 implements PropertyLoader {
    
    @Override
    public void load(EnhancedDebugStickApi plugin) {
        Property_115.registerProperties();
    }
}
