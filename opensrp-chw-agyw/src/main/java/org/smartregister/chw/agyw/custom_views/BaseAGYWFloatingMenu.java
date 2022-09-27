package org.smartregister.chw.agyw.custom_views;

import android.app.Activity;
import android.content.Context;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;
import android.widget.LinearLayout;

import org.smartregister.chw.agyw.domain.MemberObject;
import org.smartregister.chw.agyw.fragment.BaseAGYWCallDialogFragment;
import org.smartregister.agyw.R;

public class BaseAGYWFloatingMenu extends LinearLayout implements View.OnClickListener {
    private MemberObject MEMBER_OBJECT;

    public BaseAGYWFloatingMenu(Context context, MemberObject MEMBER_OBJECT) {
        super(context);
        initUi();
        this.MEMBER_OBJECT = MEMBER_OBJECT;
    }

    protected void initUi() {
        inflate(getContext(), R.layout.view_agyw_floating_menu, this);
        FloatingActionButton fab = findViewById(R.id.agyw_fab);
        if (fab != null)
            fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.agyw_fab) {
            Activity activity = (Activity) getContext();
            BaseAGYWCallDialogFragment.launchDialog(activity, MEMBER_OBJECT);
        }  else if (view.getId() == R.id.refer_to_facility_layout) {
            Activity activity = (Activity) getContext();
            BaseAGYWCallDialogFragment.launchDialog(activity, MEMBER_OBJECT);
        }
    }
}