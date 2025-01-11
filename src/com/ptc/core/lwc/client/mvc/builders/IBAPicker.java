package com.ptc.core.lwc.client.mvc.builders;

import com.ptc.core.lwc.client.lwcResource;
import com.ptc.core.lwc.client.factory.dataUtilities.IbaPickerTreeHandler;
import com.ptc.jca.mvc.components.JcaComponentParams;
import com.ptc.jca.mvc.components.JcaTreeConfig;
import com.ptc.mvc.components.AbstractComponentBuilder;
import com.ptc.mvc.components.ColumnConfig;
import com.ptc.mvc.components.ComponentBuilder;
import com.ptc.mvc.components.ComponentConfig;
import com.ptc.mvc.components.ComponentConfigFactory;
import com.ptc.mvc.components.ComponentParams;
import com.ptc.mvc.components.ds.DataSourceMode;
import com.ptc.netmarkets.util.beans.NmCommandBean;
import java.io.IOException;
import java.util.Locale;
import org.apache.logging.log4j.Logger;
import wt.log4j.LogR;
import wt.util.WTException;
import wt.util.WTMessage;
import wt.util.WTProperties;

@ComponentBuilder({"lwc.attribute.ibaPicker"})
public final class IBAPicker extends AbstractComponentBuilder {
    private static final Logger LOGGER = LogR.getLoggerInternal(IBAPicker.class.getName());
    private static final String RESOURCE = lwcResource.class.getName();
    private static final String EXPANSION_LEVEL = getExpandProperty();

    public IBAPicker() {
    }

    public ComponentConfig buildComponentConfig(ComponentParams var1) throws WTException {
        LOGGER.trace("entering buildComponentConfig( params )");
        NmCommandBean var2 = ((JcaComponentParams)var1).getHelperBean().getNmCommandBean();
        Locale var3 = var2.getLocale();
        String var4 = WTMessage.getLocalizedMessage(RESOURCE, "CREATE_ATTR_IBA_LABEL", (Object[])null, var3);
        ComponentConfigFactory var5 = this.getComponentConfigFactory();
        JcaTreeConfig var6 = (JcaTreeConfig)var5.newTreeConfig();
        var6.setSelectable(true);
        var6.setSingleSelect(true);
        var6.setNodeColumn("OrgAndIbaNameCol");
        var6.setId("lwc.attribute.ibaPicker");
        var6.setConfigurable(false);
        var6.setDataSourceMode(DataSourceMode.SYNCHRONOUS);
        var6.setShowTreeLines(true);
        var6.setLabel(var4);
        var6.setFindInTableEnabled(true);
        var6.setExpansionLevel(EXPANSION_LEVEL);
        ColumnConfig var7 = var5.newColumnConfig("OrgAndIbaNameCol", false);
        var7.setDataUtilityId("ibaNameAndOrg");
        var7.setLabel(WTMessage.getLocalizedMessage(RESOURCE, "ATTRIBUTE_DISPLAY_NAME", (Object[])null, var3));
        var7.setDefaultSort(true);
        var6.addComponent(var7);
        // 添加内部名称展示
        ColumnConfig innerName = var5.newColumnConfig("name", false);
        innerName.setLabel(WTMessage.getLocalizedMessage(RESOURCE, "ATTRIBUTE_DISPLAY_NAME", (Object[])null, var3));
        innerName.setDefaultSort(true);
        var6.addComponent(innerName);
        LOGGER.trace("leaving buildComponentConfig( params )");
        return var6;
    }

    private static String getExpandProperty() {
        String var0 = "one";

        try {
            var0 = WTProperties.getLocalProperties().getProperty("lwc.iba.picker.expand.property", "one");
        } catch (IOException var3) {
            String var2 = "Cannot get value of lwc.iba.picker.expand.property. Value 'one' will be used as default value";
            LOGGER.error(var2, var3);
        }

        if ("none".equals(var0)) {
            return "none";
        } else {
            return "full".equals(var0) ? "full" : "one";
        }
    }

    public Object buildComponentData(ComponentConfig var1, ComponentParams var2) throws Exception {
        return new IbaPickerTreeHandler();
    }
}
