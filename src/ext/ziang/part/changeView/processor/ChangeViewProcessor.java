package ext.ziang.part.changeView.processor;

import com.ptc.core.components.beans.ObjectBean;
import com.ptc.core.components.forms.DefaultObjectFormProcessor;
import com.ptc.core.components.forms.FormProcessingStatus;
import com.ptc.core.components.forms.FormResult;
import com.ptc.netmarkets.model.NmOid;
import com.ptc.netmarkets.util.beans.NmCommandBean;
import com.ptc.windchill.enterprise.util.PartManagementHelper;
import ext.common.util.PartUtil;
import wt.part.WTPart;
import wt.pom.Transaction;
import wt.util.WTException;

import java.util.List;

/**
 * 创建多视图BOM
 */
public class ChangeViewProcessor extends DefaultObjectFormProcessor {

    @Override
    public FormResult doOperation(NmCommandBean nmCommandBean, List<ObjectBean> list) throws WTException {
        NmOid nmOid = ((ObjectBean) list.get(0)).getActionOid();
        WTPart part = (WTPart) nmOid.getRef();
        Transaction transaction = new Transaction();
        transaction.start();
        try {
            String selectedViewName = getSelectedViewName(nmCommandBean);
            List<WTPart> allChildPart = PartUtil.findAllChildPart(part);
            for (WTPart wtPart : allChildPart) {
                // 创建多视图
                PartUtil.createNewView(wtPart, selectedViewName);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            throw new WTException(e.getLocalizedMessage());
        }
        return new FormResult(FormProcessingStatus.SUCCESS);
    }

    /**
     * 获取选择视图名称
     *
     * @param commandBean
     * @return
     * @throws WTException
     */
    private static String getSelectedViewName(NmCommandBean commandBean) throws WTException {
        String view = PartManagementHelper.getSelectedComboBoxValueFromForm(commandBean, "newVersionView");
        if (view == null) {
            throw new WTException("com.ptc.windchill.enterprise.part.partResource", "part.newViewVersion.UNEXPECTED_FAILURE", (Object[]) null);
        } else {
            return view;
        }
    }


}
