package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.databinding.ChildItemsBinding;
import com.example.myapplication.databinding.GroupItemsBinding;
import com.example.myapplication.models.MyChildItems;
import com.example.myapplication.models.MyGroupItem;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import java.util.List;

public class ExpandableViewAdapter extends AbstractExpandableItemAdapter<ExpandableViewAdapter.MyGroupViewHolder, ExpandableViewAdapter.MyChildViewHolder> {
    private List<MyGroupItem> items;

    RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;

    private final Context activityContext;


    public ExpandableViewAdapter(List<MyGroupItem> items, Context activityContext, RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager) {
        this.items = items;
        this.activityContext = activityContext;
        this.mRecyclerViewExpandableItemManager = mRecyclerViewExpandableItemManager;
        setHasStableIds(true);

    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return items.get(groupPosition).getChildren().size();
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return items.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return items.get(groupPosition).getChildren().get(childPosition).getId();
    }

//    public  abstract class MyBaseViewHolder extends AbstractExpandableItemViewHolder {
//        public MyBaseViewHolder(View v) {
//            super(v);
//
//        }
//    }

    public class MyGroupViewHolder extends AbstractExpandableItemViewHolder {
        TextView slotsAvailable;
        TextView dayTime;
        GroupItemsBinding groupItemsBinding;


        public MyGroupViewHolder(GroupItemsBinding groupItemsBinding) {
            super(groupItemsBinding.getRoot());
            this.groupItemsBinding = groupItemsBinding;
            slotsAvailable = groupItemsBinding.getRoot().findViewById(R.id.slotsAvailable);
            dayTime = groupItemsBinding.getRoot().findViewById(R.id.dayTime);
        }

    }

    public class MyChildViewHolder extends AbstractExpandableItemViewHolder {
        TextView slots;
        ChildItemsBinding childItemsBinding;

        public MyChildViewHolder(ChildItemsBinding childItemsBinding) {
            super(childItemsBinding.getRoot());
            this.childItemsBinding = childItemsBinding;
            slots = childItemsBinding.getRoot().findViewById(R.id.slots);
        }
    }

    @NonNull
    @Override
    public MyGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(this.activityContext).inflate(R.layout.group_items, parent, false);
        GroupItemsBinding groupItemsBinding = DataBindingUtil.bind(itemView);
        assert groupItemsBinding != null;
        return new MyGroupViewHolder(groupItemsBinding);
    }

    @NonNull
    @Override
    public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(this.activityContext)
                .inflate(R.layout.child_items, parent, false);
        ChildItemsBinding childItemsBinding = DataBindingUtil.bind(itemView);
        return new MyChildViewHolder(childItemsBinding);
    }

    @Override
    public void onBindGroupViewHolder(@NonNull MyGroupViewHolder holder, int groupPosition, int viewType) {
        MyGroupItem myGroupItem = items.get(groupPosition);
//        holder.slotsAvailable.setText("" + myGroupItem.getAvailableSlots());
//        holder.dayTime.setText(myGroupItem.getTitle());
        holder.groupItemsBinding.getRoot().setClickable(true);
        holder.groupItemsBinding.setGroup(myGroupItem);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerViewExpandableItemManager.collapseAll();
                if (!holder.getExpandState().isExpanded()) {
                    mRecyclerViewExpandableItemManager.collapseAll();
                    mRecyclerViewExpandableItemManager.expandGroup(groupPosition);
                } else {
                    mRecyclerViewExpandableItemManager.collapseGroup(groupPosition);

                }
            }
        });
        //this.groupItemsBinding.executePendingBindings();
    }

    @Override
    public void onBindChildViewHolder(@NonNull MyChildViewHolder holder, int groupPosition, int childPosition, int viewType) {
        MyChildItems myChildItem = items.get(groupPosition).getChildren().get(childPosition);
       // holder.slots.setText(myChildItem.getSlotStartTime() + " - " + myChildItem.getSlotEndTime());
        if (myChildItem.getStatus() == 0) {
            holder.slots.setTextColor(activityContext.getResources().getColor(R.color.lightblack));
            holder.slots.setBackgroundColor(Color.parseColor("#EFEFEF"));
        }
        holder.childItemsBinding.setChildItems(myChildItem);
        holder.childItemsBinding.executePendingBindings();

    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(@NonNull MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }
    


}
