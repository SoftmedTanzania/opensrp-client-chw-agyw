package org.smartregister.chw.agyw.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.smartregister.agyw.R;
import org.smartregister.chw.agyw.domain.ServiceCard;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class BaseServiceCardAdapter extends RecyclerView.Adapter<BaseServiceCardAdapter.ViewHolder> {
    private Context context;
    private List<ServiceCard> serviceCards;
    private String baseEntityId;

    private View.OnClickListener clickListener;

    public BaseServiceCardAdapter(Context context, List<ServiceCard> serviceCards, View.OnClickListener clickListener, String baseEntityId) {
        this.context = context;
        this.serviceCards = serviceCards;
        this.clickListener = clickListener;
        this.baseEntityId = baseEntityId;
    }

    public void setServiceCards(List<ServiceCard> serviceCards) {
        this.serviceCards = serviceCards;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_card_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ServiceCard serviceCard = serviceCards.get(position);

        holder.cardLayout.setBackgroundResource(serviceCard.getBackground());
        holder.cardLayout.setOnClickListener(clickListener);
        holder.cardLayout.setTag(serviceCard);
        holder.cardLayout.setTag(R.id.BASE_ENTITY_ID, baseEntityId);

        holder.name.setText(serviceCard.getServiceName());
        // holder.visitCount.setText(context.getString(R.string.service_visit_count, serviceCard.getVisitsCount()));

        if (serviceCard.getServiceIcon() != null) {
            Drawable service_icon = context.getResources().getDrawable(serviceCard.getServiceIcon());
            if (service_icon != null)
                holder.serviceIcon.setImageDrawable(service_icon);
        }
    }

    @Override
    public int getItemCount() {
        return serviceCards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View cardLayout;
        public TextView name;
        public TextView actionsCount;
        public TextView servicesStatus;
        public Button processVisitBtn;
        public ImageView serviceIcon;
        public TextView visitCount;
        public ImageView serviceStatusIcon;


        public ViewHolder(View view) {
            super(view);
            cardLayout = view.findViewById(R.id.card_layout);
            name = view.findViewById(R.id.service_title);
            actionsCount = view.findViewById(R.id.actions_count);
            servicesStatus = view.findViewById(R.id.service_status);
            processVisitBtn = view.findViewById(R.id.process_visit);
            serviceIcon = view.findViewById(R.id.service_icon);
            visitCount = view.findViewById(R.id.visit_count);
            serviceStatusIcon = view.findViewById(R.id.service_status_icon);
        }
    }
}
