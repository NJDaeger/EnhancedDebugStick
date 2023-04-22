package com.njdaeger.enhanceddebugstick.mcversion.v113;

import com.njdaeger.enhanceddebugstick.api.EnhancedDebugStickApi;
import com.njdaeger.enhanceddebugstick.mcversion.PropertyLoader;

public class PropertyLoader_113 implements PropertyLoader {
    
    @Override
    public void load(EnhancedDebugStickApi plugin) {
        Property_113.registerProperties();
    }
}
