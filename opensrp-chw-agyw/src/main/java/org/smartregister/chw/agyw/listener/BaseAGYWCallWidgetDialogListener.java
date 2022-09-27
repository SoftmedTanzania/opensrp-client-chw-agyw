package org.smartregister.chw.agyw.listener;


import android.view.View;

import org.smartregister.chw.agyw.fragment.BaseAGYWCallDialogFragment;
import org.smartregister.chw.agyw.util.AGYWUtil;
import org.smartregister.agyw.R;

import timber.log.Timber;

public class BaseAGYWCallWidgetDialogListener implements View.OnClickListener {

    private BaseAGYWCallDialogFragment callDialogFragment;

    public BaseAGYWCallWidgetDialogListener(BaseAGYWCallDialogFragment dialogFragment) {
        callDialogFragment = dialogFragment;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.agyw_call_close) {
            callDialogFragment.dismiss();
        } else if (i == R.id.agyw_call_head_phone) {
            try {
                String phoneNumber = (String) v.getTag();
                AGYWUtil.launchDialer(callDialogFragment.getActivity(), callDialogFragment, phoneNumber);
                callDialogFragment.dismiss();
            } catch (Exception e) {
                Timber.e(e);
            }
        } else if (i == R.id.call_agyw_client_phone) {
            try {
                String phoneNumber = (String) v.getTag();
                AGYWUtil.launchDialer(callDialogFragment.getActivity(), callDialogFragment, phoneNumber);
                callDialogFragment.dismiss();
            } catch (Exception e) {
                Timber.e(e);
            }
        }
    }
}
