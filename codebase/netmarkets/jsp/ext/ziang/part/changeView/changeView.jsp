<%@ taglib prefix="jca" uri="http://www.ptc.com/windchill/taglib/components" %>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/fmt" prefix="fmt" %>

<%@ include file="/netmarkets/jsp/components/beginWizard.jspf" %>
<%@ include file="/netmarkets/jsp/components/includeWizBean.jspf" %>

<jca:initializeItem operation="${createBean.create}"/>

<jca:wizard buttonList="DefaultWizardButtonsNoApply">
    <jca:wizardStep action="changeView_step" type="extPart"/>
</jca:wizard>

<%@include file="/netmarkets/jsp/util/end.jspf" %>