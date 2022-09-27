package org.smartregister.chw.agyw.handlers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import org.smartregister.agyw.R;
import org.smartregister.chw.agyw.activity.BaseServicesFormActivity;
import org.smartregister.chw.agyw.dao.AGYWDao;
import org.smartregister.chw.agyw.domain.MemberObject;
import org.smartregister.chw.agyw.domain.ServiceCard;
import org.smartregister.chw.agyw.util.Constants;


public class BaseServiceActionHandler implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        int i = view.getId();
        ServiceCard serviceCard = (ServiceCard) view.getTag();
        String baseEntityID = (String) view.getTag(R.id.BASE_ENTITY_ID);
        if (i == R.id.card_layout) {
            MemberObject member = AGYWDao.getMember(baseEntityID);
            startVisitActivity(view.getContext(), serviceCard, member);
        }
    }

    protected void startVisitActivity(Context context, ServiceCard serviceCard, MemberObject memberObject) {
        if (serviceCard.getServiceId().equals(Constants.SERVICES.AGYW_BEHAVIORAL_SERVICES)) {
            BaseServicesFormActivity.startMe((Activity) context, serviceCard.getFormName(), memberObject.getBaseEntityId(), memberObject.getAge());
            return;
        }
        Toast.makeText(context, serviceCard.getServiceName() + "Loading activity... ", Toast.LENGTH_SHORT).show();

    }


}
