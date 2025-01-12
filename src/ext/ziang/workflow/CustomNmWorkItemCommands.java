package ext.ziang.workflow;

import com.ptc.core.components.forms.FormProcessingStatus;
import com.ptc.core.components.forms.FormResult;
import com.ptc.core.components.util.FeedbackMessage;
import com.ptc.core.ui.resources.FeedbackType;
import com.ptc.netmarkets.util.beans.NmCommandBean;
import com.ptc.netmarkets.work.NmWorkItemCommands;
import wt.session.SessionHelper;
import wt.util.WTException;

public class CustomNmWorkItemCommands extends NmWorkItemCommands {

    /**
     * 保存任务
     * 
     * @param commandBean
     * @return
     * @throws WTException
     */
    public static FormResult save(NmCommandBean commandBean) throws WTException {
        try {
            FormResult formResult = NmWorkItemCommands.save(commandBean);
            if (formResult.getStatus().equals(FormProcessingStatus.SUCCESS)) {
                FeedbackMessage feedbackMessage =
                    new FeedbackMessage(FeedbackType.SUCCESS, SessionHelper.getLocale(), "保存成功", null,
                            "");
                formResult.addFeedbackMessage(feedbackMessage);
                return formResult;
            }
            return formResult;
        } catch (Exception e) {
            throw new WTException("保存失败 e:message" + e.getLocalizedMessage());
        }
    }
}
