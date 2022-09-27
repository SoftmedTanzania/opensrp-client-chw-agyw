package org.smartregister.chw.agyw.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.smartregister.agyw.R;
import org.smartregister.chw.agyw.adapter.BaseServiceCardAdapter;
import org.smartregister.chw.agyw.dao.AGYWDao;
import org.smartregister.chw.agyw.domain.MemberObject;
import org.smartregister.chw.agyw.domain.ServiceCard;
import org.smartregister.chw.agyw.handlers.BaseServiceActionHandler;
import org.smartregister.chw.agyw.util.Constants;
import org.smartregister.view.activity.SecuredActivity;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BaseAGYWServicesActivity extends SecuredActivity {
    protected BaseServiceCardAdapter serviceCardAdapter;
    protected TextView tvTitle;
    protected MemberObject memberObject;
    protected String baseEntityId;

    public static void startMe(Activity activity, String baseEntityID) {
        Intent intent = new Intent(activity, BaseAGYWServicesActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityID);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agyw_services);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            baseEntityId = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);
        }
        memberObject = AGYWDao.getMember(baseEntityId);
        setupViews();
        initializeMainServiceContainers();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeMainServiceContainers();
    }

    protected void setupViews() {
        initializeRecyclerView();
        View cancelButton = findViewById(R.id.undo_button);
        cancelButton.setOnClickListener(v -> finish());
        tvTitle = findViewById(R.id.top_patient_name);
        tvTitle.setText(MessageFormat.format("{0}, {1}", memberObject.getFullName(), memberObject.getAge()));
    }

    protected void initializeRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        serviceCardAdapter = new BaseServiceCardAdapter(this, new ArrayList<>(), getServiceHandler(), baseEntityId);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(serviceCardAdapter);
    }

    //TODO: cleanup -> move files to follow pattern

    @SuppressLint("NotifyDataSetChanged")
    protected void initializeMainServiceContainers() {
        List<ServiceCard> serviceCards = new ArrayList<>();

        ServiceCard bioMedicalService = new ServiceCard();
        bioMedicalService.setServiceName(getString(R.string.bio_medical_services));
        bioMedicalService.setId(Constants.SERVICES.AGYW_BIO_MEDICAL_SERVICES);
        bioMedicalService.setServiceIcon(R.drawable.ic_bio_medical);
        bioMedicalService.setBackground(R.drawable.purple_bg);
        bioMedicalService.setFormName(Constants.FORMS.AGYW_BIO_MEDICAL);
        serviceCards.add(bioMedicalService);

        ServiceCard behavioralService = new ServiceCard();
        behavioralService.setServiceName(getString(R.string.behavioral_services));
        behavioralService.setId(Constants.SERVICES.AGYW_BEHAVIORAL_SERVICES);
        behavioralService.setServiceIcon(R.drawable.ic_behavioral);
        behavioralService.setBackground(R.drawable.orange_bg);
        behavioralService.setFormName(Constants.FORMS.AGYW_BEHAVIORAL);
        serviceCards.add(behavioralService);

        ServiceCard structuralService = new ServiceCard();
        structuralService.setServiceName(getString(R.string.structural_services));
        structuralService.setId(Constants.SERVICES.AGYW_STRUCTURAL_SERVICES);
        structuralService.setServiceIcon(R.drawable.ic_structural);
        structuralService.setBackground(R.drawable.dark_blue_bg);
        structuralService.setFormName(Constants.FORMS.AGYW_STRUCTURAL);
        serviceCards.add(structuralService);


        serviceCardAdapter.setServiceCards(serviceCards);
        serviceCardAdapter.notifyDataSetChanged();
    }

    public BaseServiceActionHandler getServiceHandler() {
        return new BaseServiceActionHandler();
    }

    @Override
    protected void onCreation() {
        //
    }

    @Override
    protected void onResumption() {
        initializeMainServiceContainers();
    }
}
