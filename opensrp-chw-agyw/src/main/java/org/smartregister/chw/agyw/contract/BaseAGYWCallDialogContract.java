package org.smartregister.chw.agyw.contract;

import android.content.Context;

public interface BaseAGYWCallDialogContract {

    interface View {
        void setPendingCallRequest(Dialer dialer);
        Context getCurrentContext();
    }

    interface Dialer {
        void callMe();
    }
}
