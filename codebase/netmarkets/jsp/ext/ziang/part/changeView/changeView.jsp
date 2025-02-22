<%@ taglib prefix="jca" uri="http://www.ptc.com/windchill/taglib/components" %>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/fmt" prefix="fmt" %>
<%@page pageEncoding="UTF-8"%>
<%@ include file="/netmarkets/jsp/components/beginWizard.jspf" %>
<%@ include file="/netmarkets/jsp/components/includeWizBean.jspf" %>


<jca:wizard buttonList="DefaultWizardButtonsNoApply" title="创建新的工厂BOM">
    <jca:wizardStep action="changeView_step" type="extPart"/>
</jca:wizard>

<%@include file="/netmarkets/jsp/util/end.jspf" %>