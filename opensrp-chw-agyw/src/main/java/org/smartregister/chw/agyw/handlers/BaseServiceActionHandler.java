package org.smartregister.chw.agyw.handlers;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import org.smartregister.agyw.R;
import org.smartregister.chw.agyw.domain.ServiceCard;
import org.smartregister.chw.agyw.util.Constants;


public class BaseServiceActionHandler implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        int i = view.getId();
        ServiceCard serviceCard = (ServiceCard) view.getTag();
        String baseEntityID = (String) view.getTag(R.id.BASE_ENTITY_ID);
        if (i == R.id.card_layout) {
            startVisitActivity(view.getContext(), serviceCard, baseEntityID);
        }
    }

    protected void startVisitActivity(Context context, ServiceCard serviceCard, String baseEntityId) {
        if (serviceCard.getServiceId().equals(Constants.SERVICES.AGYW_BEHAVIORAL_SERVICES)) {
            //something happened
            return;
        }
        Toast.makeText(context, serviceCard.getServiceName() + "Loading activity... ", Toast.LENGTH_SHORT).show();

    }


}
