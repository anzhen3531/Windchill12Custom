package com.ptc.windchill.enterprise.associativity.asb.dataUtilities;

import com.ptc.core.components.descriptor.ModelContext;
import com.ptc.core.components.factory.dataUtilities.AbstractAttributeDataUtility;
import com.ptc.core.components.rendering.guicomponents.NmActionGuiComponent;
import com.ptc.core.components.rendering.guicomponents.RichTextDisplayComponent;
import com.ptc.netmarkets.model.NmOid;
import com.ptc.netmarkets.util.misc.NmAction;
import com.ptc.netmarkets.util.misc.NmActionServiceHelper;
import com.ptc.windchill.associativity.equivalence.EquivalenceNetwork;
import com.ptc.windchill.associativity.service.AssociativityServiceLocator;
import com.ptc.windchill.baseserver.ServiceLocator;
import com.ptc.windchill.baseserver.dao.NavigationCriteriaDAO;
import com.ptc.windchill.enterprise.associativity.asb.server.filter.EquivalentStatusFilter;
import com.ptc.windchill.enterprise.associativity.asb.structure.ASBStructureHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import wt.associativity.Associative;
import wt.associativity.EquivalenceLink;
import wt.fc.ObjectReference;
import wt.fc.Persistable;
import wt.filter.NavCriteriaCacheId;
import wt.filter.NavCriteriaContext;
import wt.filter.NavigationCriteria;
import wt.filter.NavigationCriteriaHelper;
import wt.identity.IdentityFactory;
import wt.log4j.LogR;
import wt.method.MethodLocal;
import wt.part.WTPart;
import wt.util.HTMLEncoder;
import wt.util.WTException;
import wt.vc.Iterated;
import wt.vc.views.View;
import wt.vc.views.ViewManageable;
import wt.vc.views.ViewReference;

public class EquivalenceNetworkDataUtility extends AbstractAttributeDataUtility {
    private final Logger logger = LogR.getLoggerInternal(EquivalenceNetworkDataUtility.class.getName());
    protected Map<Associative, Set<EquivalenceNetwork>> equivalenceNetworkMap = new HashMap();
    protected static final String BOLD_TAG_START = "<b>";
    protected static final String BOLD_TAG_END = "</b>";
    private static final String DEFAULT_SEPARATOR = " - ";
    private static final String JSONTYPE = "type";
    private static final String JSONVALUE = "html";
    private static final String HTML_BREAK_TAG = "</br>";
    protected String viewToBold;
    private static MethodLocal<Map<Associative, Set<EquivalenceNetwork>>> equivalenceNetworkMapLocale = new MethodLocal();

    public EquivalenceNetworkDataUtility() {
    }

    public Object getPlainDataValue(String var1, Object var2, ModelContext var3) throws WTException {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Object is " + var2);
            this.logger.trace("component_id is " + var1);
            this.logger.trace("ModelContext is " + var3);
        }

        if (!"equivalenceNetwork".equals(var1)) {
            this.logger.debug("Component id doesn't match with equivalenceNetwork");
            return null;
        } else {
            return this.getDataValueForObject(var2);
        }
    }

    protected Object getDataValueForObject(Object var1) throws WTException {
        if (var1 instanceof WTPart) {
            JSONObject var2 = new JSONObject();

            try {
                String var3 = this.getNetworkStringInHtmlFormat(var1);
                var2.put("type", "html");
                var2.put("html", var3);
            } catch (JSONException var4) {
                this.logger.error("JSONException : " + var4);
                throw new WTException(var4);
            }

            this.logger.trace("JSON HTML is : " + var2);
            return var2;
        } else {
            this.logger.debug("Object is not of type WTPart");
            return null;
        }
    }

    private String getNetworkStringInHtmlFormat(Object var1) throws WTException {
        StringBuilder var2 = new StringBuilder();
        if (var1 instanceof Associative) {
            List var3 = this.getEquivalenceNetworkString((Associative)var1);
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
                String var5 = (String)var4.next();
                var2.append(var5 + "</br>");
            }
        }

        this.logger.debug("HTML is : " + var2);
        return var2.toString();
    }

    private List<String> getEquivalenceNetworkString(Associative var1) throws WTException {
        ArrayList var2 = new ArrayList();
        this.equivalenceNetworkMap = (Map)equivalenceNetworkMapLocale.get();
        if (this.equivalenceNetworkMap != null && !this.equivalenceNetworkMap.isEmpty()) {
            Set var3 = (Set)this.equivalenceNetworkMap.get(var1);
            if (null == var3 || var3.isEmpty()) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("No Equivalence network found for " + IdentityFactory.getDisplayIdentity(var1).toString());
                }

                return var2;
            }

            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
                EquivalenceNetwork var5 = (EquivalenceNetwork)var4.next();
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("Creating network string for   " + var5.toString());
                }

                String var6 = this.getNetworkString(var1, var5);
                this.logger.trace("Created network string is " + var6);
                if (!var2.contains(var6)) {
                    var2.add(var6);
                }
            }
        }

        return var2;
    }

    public Object getDataValue(String var1, Object var2, ModelContext var3) throws WTException {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Object is " + var2);
            this.logger.trace("component_id is " + var1);
            this.logger.trace("ModelContext is " + var3);
        }

        if (!"equivalenceNetwork".equals(var1) && !"enterpriseVersion".equals(var1)) {
            this.logger.debug("Component id doesn't match with equivalenceNetwork or enterpriseVersion");
            return null;
        } else {
            String var4 = this.getNetworkStringInHtmlFormat(var2);
            RichTextDisplayComponent var5 = new RichTextDisplayComponent("", true);
            var5.setValue(var4);
            return var5;
        }
    }

    private NavigationCriteria getNavigationCriteria(ModelContext var1) throws WTException {
        HashMap var2 = var1.getNmCommandBean().getMap();
        String var3 = (String)var2.get("ncId");
        String var4 = (String)var2.get("sessionId");
        String var5 = (String)var2.get("moduleName");
        String var6 = (String)var2.get("remoteAddr");
        NavigationCriteria var7 = null;
        if (var3 != null && var4 != null && var5 != null && var6 != null) {
            NavCriteriaCacheId var8 = new NavCriteriaCacheId(var6, var4, var5);
            var7 = NavigationCriteriaHelper.service.getCachedNavigationCriteria(var3, var8);
        }

        if (null != var7) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug(NavigationCriteriaHelper.service.getJSONFromNavigationCriteria(var7));
            }

            var7 = ServiceLocator.getInstance().getNavigationCriteriaDAO().getDeepCopy(var7);
        }

        return var7;
    }

    public void setModelData(String var1, List<?> var2, ModelContext var3) throws WTException {
        if (!"equivalenceNetwork".equals(var1)) {
            this.logger.debug("Component id doesn't match with equivalenceNetwork");
        } else {
            this.setModelDataValue(var2, var3);
        }
    }

    protected void setModelDataValue(List<?> var1, ModelContext var2) throws WTException {
        Set var3 = (Set)var1.stream().filter((var0) -> {
            return var0 instanceof Associative;
        }).collect(Collectors.toSet());
        if (var3.isEmpty()) {
            this.logger.debug("No Objects qualified for datautility");
        } else {
            NavigationCriteria var4 = this.getNavigationCriteria(var2);
            EquivalentStatusFilter var5 = ASBStructureHelper.getEquivalentStatusFilterFromNC(var4);
            NavigationCriteria var6 = null;
            NavigationCriteria var7 = null;
            if (null != var5) {
                NavigationCriteriaDAO var8 = ServiceLocator.getInstance().getNavigationCriteriaDAO();
                var6 = ServiceLocator.getInstance().getNavigationCriteriaDAO().getDeepCopy(var8.getNavigationCriteria(var5.getNavCriteriaCacheId(), var5.getUpstreamNCID()));
                var7 = ServiceLocator.getInstance().getNavigationCriteriaDAO().getDeepCopy(var8.getNavigationCriteria(var5.getNavCriteriaCacheId(), var5.getDownstreamNCID()));
                ViewReference var9 = var5.isFilterOnUpstreamNC() ? var8.getFirstValidViewFromConfigSpecs(var6) : var8.getFirstValidViewFromConfigSpecs(var7);
                if (var9 != null) {
                    this.viewToBold = var9.getName();
                    this.logger.debug("View to bold is : " + this.viewToBold);
                }
            } else {
                NavCriteriaContext var10 = new NavCriteriaContext();
                var3.forEach((var1x) -> {
                    var10.setApplicableType(((WTPart)var1x).getClass());
                });
                var6 = ServiceLocator.getInstance().getNavigationCriteriaDAO().getDeepCopy(ServiceLocator.getInstance().getNavigationCriteriaDAO().getDefaultNavigationCriteria((Persistable)var1.get(0), var2.getNmCommandBean().getTextParameter("NC_APP_NAME")));
                var7 = ServiceLocator.getInstance().getNavigationCriteriaDAO().getDeepCopy(ServiceLocator.getInstance().getNavigationCriteriaDAO().getDefaultNavigationCriteria((Persistable)var1.get(0), var2.getNmCommandBean().getTextParameter("NC_APP_NAME")));
            }

            this.fillEquivalenceNetworkMap(var3, var6, var7);
        }
    }

    private void fillEquivalenceNetworkMap(Set<Associative> var1, NavigationCriteria var2, NavigationCriteria var3) throws WTException {
        this.equivalenceNetworkMap = (Map)equivalenceNetworkMapLocale.get();
        if (this.equivalenceNetworkMap == null) {
            this.equivalenceNetworkMap = new HashMap();
        }

        Iterator var4 = var1.iterator();

        while(var4.hasNext()) {
            Associative var5 = (Associative)var4.next();
            if (this.equivalenceNetworkMap.get(var5) == null) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Finding Equivalence Network for " + IdentityFactory.getDisplayIdentity(var5));
                }

                Set var6 = AssociativityServiceLocator.getInstance().getAssociativeEquivalenceManager().getEquivalenceNetwork(var5, var2, var3);
                this.logger.debug("Found " + var6.size() + " number of Equivalence Networks");
                this.equivalenceNetworkMap.put(var5, var6);
            }
        }

        equivalenceNetworkMapLocale.set(this.equivalenceNetworkMap);
    }

    protected String getNetworkString(Associative var1, EquivalenceNetwork var2) throws WTException {
        List var3 = var2.getAllLinks();
        StringBuilder var4 = new StringBuilder();
        String var5 = ((ViewManageable)var1).getView().getName();
        if (var3.isEmpty()) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Network empty for " + IdentityFactory.getDisplayIdentity(var1));
            }

            return var4.toString();
        } else {
            EquivalenceNetwork var6 = var2.getStartingNode();

            for(boolean var7 = true; null != var6; var6 = var6.getDownstreamNetwork()) {
                EquivalenceLink var8 = var6.getLink();
                String var9 = "";
                Iterated var10 = null;
                if (var7) {
                    if (var8 != null) {
                        var9 = this.getContextNameFromLink(var8, !var7);
                    }

                    var10 = var6.getData().getUpstreamIteration();
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Found latest iteration for Role A : " + IdentityFactory.getDisplayIdentity(var10).toString());
                    }

                    var4.append(this.formatViewNameIfMatchesWithCurrentView(var9, var5, var10));
                    var7 = false;
                }

                if (var8 != null) {
                    var9 = this.getContextNameFromLink(var8, true);
                }

                var10 = var6.getData().getDownstreamIteration();
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Found latest iteration for Role B : " + IdentityFactory.getDisplayIdentity(var10).toString());
                }

                var4.append(this.formatViewNameIfMatchesWithCurrentView(var9, var5, var10));
            }

            return var4.substring(0, var4.length() - " - ".length());
        }
    }

    protected String getContextNameFromLink(EquivalenceLink var1, boolean var2) {
        View var3 = var2 ? var1.getDownstreamContext() : var1.getUpstreamContext();
        return var3 != null ? var3.getName() : "";
    }

    private String formatViewNameIfMatchesWithCurrentView(String var1, String var2, Persistable var3) {
        String var4 = this.appendLinkToViewName(var1, var3);
        return (null == this.viewToBold || !this.viewToBold.equals(var1)) && (null != this.viewToBold || !var2.equals(var1)) ? var4 + " - " : "<b>" + var4 + "</b> - ";
    }

    private String appendLinkToViewName(String var1, Persistable var2) {
        String var3 = this.getInfoPageLink(var2);
        return this.formLinkForViewName(var1, var3);
    }

    protected String getInfoPageLink(Persistable var1) {
        String var2 = "";

        try {
            NmOid var3 = new NmOid();
            var3.setWtRef(ObjectReference.newObjectReference(var1));
            NmAction var4 = NmActionServiceHelper.service.getAction("object", "view");
            var4.setToolTip((String)null);
            var4.setIcon((String)null);
            var4.setContextObject(var3);
            NmActionGuiComponent var5 = new NmActionGuiComponent(var4);
            var2 = var5.getAction().getActionUrlExternal();
        } catch (Exception var6) {
            this.logger.error("Exception while crating infopage link : ", var6);
        }

        this.logger.debug("Info page link : " + var2);
        return var2;
    }

    private String formLinkForViewName(String var1, String var2) {
        return "<a target=\"_top\" href=\"" + var2 + "\">" + HTMLEncoder.encodeAndFormatForHTMLContent(var1) + "</a>";
    }
}
