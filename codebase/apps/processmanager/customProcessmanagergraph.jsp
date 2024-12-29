<%@ page
        import="wt.util.HTMLEncoder"
        import="wt.help.HelpLinkHelper"
        import="java.util.regex.*,wt.util.WTException"
        import="java.util.Locale"
        import="wt.session.SessionHelper"
        import="wt.util.WTMessage"
        import="wt.workflow.work.ProcessManagerResource"
%>
<%
    Locale locale = SessionHelper.getLocale();
    Pattern p1 = Pattern.compile("^OR:+wt.workflow.engine.WfProcess+:\\d+$");//. represents single character
    Pattern p2 = Pattern.compile("^OR:+wt.workflow.engine.WfBlock+:\\d+$");
    String oid = request.getParameter("oid");
    boolean b = p1.matcher(oid).matches() || p2.matcher(oid).matches();
    if (!b) {
        throw new WTException(new WTMessage("wt.workflow.work.ProcessManagerResource", ProcessManagerResource.PROC_INVALID_PROC_REF, null).getLocalizedMessage(locale));
    }%>

<%@ taglib uri="http://www.ptc.com/windchill/taglib/components" prefix="jca" %>

<html ng-app="com.ptc.processmanager">


<head>
    <%@ page errorPage="/netmarkets/jsp/util/error.jsp" %>
    <title></title>
    <script type="text/javascript">var ie8_404_fix = 1;</script>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

    <base href="<%= pageContext.getServletContext().getContextPath() %>/">

    <script> (function () {
        var baseTag = document.getElementsByTagName('base')[0];
        baseTag.href = baseTag.href;
    })();

    </script>

    <jca:enableCorePlatform version="2.2" applications="processmanager" fullPage="true"/>

    <script type="text/javascript">
        if (!PTC.util) {
            PTC.util = {};
        }
        <%@ include file="/netmarkets/javascript/util/jsfrags/HTMLEncoder.jsfrag"%>
    </script>

    <link href='apps/processmanager/resources/css/ptc-processmanager.css' rel='stylesheet' type='text/css'/>

<body>
<div ng-controller="ProcessManagerGraphController"
     ng-init="init('<%=HTMLEncoder.encodeForHTMLAttribute(request.getParameter("oid"))%>')">

    <div class="graph-toolbar" ng-if="translationsDone">
        <button ng-click="graph.scale = (((graph.scale * 1.05) > 1.5) ? 1.5 : graph.scale * 1.05)"
                ng-show="zoomInButtonAccess" ng-disabled="canDisableZoomInButton()"
                style="float:left;margin-right: 0px;" class="icon-zoom-in"
                title="{{'ZOOMIN_BUTTON_TOOLTIP' | translate}}"></button>

        <button ng-click="graph.scale = (((graph.scale * 0.95) <= 0.70) ? 0.70 : graph.scale * 0.95)"
                class="icon-zoom-out" ng-show="zoomOutButtonAccess" ng-disabled="canDisableZoomOutButton()"
                style="float:left;margin-left: 0px;margin-right: 0px"
                title="{{'ZOOMOUT_BUTTON_TOOLTIP' | translate}}"></button>
    </div>
    <div id="ProcessManagerGraph" ng-mousedown="OnMouseDown($event)" ng-mouseup="OnMouseUp($event)"
         ng-mousemove="OnMouseMove($event)" ng-click="graphClicked();" ptc-graph="dataModel" scale="{{graph.scale}}"
         connect="createLink(from, to);"
         node-template-url="'apps/processmanager/templates/processmanager-graph-node-template.html'"
         class="graphframeCustom">
    </div>
</div>
</body>

</html>