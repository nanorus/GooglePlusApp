package com.official.nanorus.googleplusapp.presentation.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.akashandroid90.imageletter.MaterialLetterIcon;
import com.official.nanorus.googleplusapp.R;
import com.official.nanorus.googleplusapp.entity.business.api.Businessman;
import com.official.nanorus.googleplusapp.model.data.ResourceManager;
import com.official.nanorus.googleplusapp.navigation.Router;

import java.util.ArrayList;
import java.util.List;

public class BusinessmenRecyclerViewAdapter extends RecyclerView.Adapter<BusinessmenRecyclerViewAdapter.BusinessmenRecyclerViewHolder> {

    private List<Businessman> dataList;
    private Router router;
    private ResourceManager resourceManager;
    private boolean suspendItemClickListener = true;

    public BusinessmenRecyclerViewAdapter() {
        dataList = new ArrayList<>();
        router = Router.getInstance();
        resourceManager = ResourceManager.getInstance();
    }

    public void clearList() {
        if (dataList != null)
            dataList.clear();
    }

    public void updateList(List<Businessman> list) {
        dataList.addAll(0, list);
    }

    public void addData(Businessman businessman) {
        dataList.add(businessman);
    }

    @NonNull
    @Override
    public BusinessmenRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.businessmen_list_item, parent, false);
        return new BusinessmenRecyclerViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (dataList == null)
            return 0;
        return dataList.size();
    }

    public void suspendItemClickListener(boolean suspend) {
        this.suspendItemClickListener = suspend;
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessmenRecyclerViewHolder holder, int position) {
        Businessman businessman = dataList.get(position);
        holder.name.setText(businessman.getName());
        holder.email.setText(businessman.getEmail());

        String name = businessman.getName();
        String initials = String.valueOf(name.charAt(0)) + String.valueOf(name.charAt(name.indexOf(" ") + 1));
        holder.letterIcon.setLetter(initials);
        holder.letterIcon.setShapeColor(resourceManager.getLetterIconShapeColor(position));

        holder.itemView.setOnClickListener(view -> {
            if (!suspendItemClickListener)
                router.startBusinessmanInfoActivity(holder.itemView.getContext(), businessman.getId());
        });
    }

    class BusinessmenRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView email;
        MaterialLetterIcon letterIcon;

        public BusinessmenRecyclerViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            email = itemView.findViewById(R.id.tv_email);
            letterIcon = itemView.findViewById(R.id.letterIcon);
        }
    }
}
