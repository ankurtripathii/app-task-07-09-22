package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.enums.Times;
import com.example.myapplication.models.APIResponseModel;
import com.example.myapplication.models.MyChildItems;
import com.example.myapplication.models.MyGroupItem;
import com.example.myapplication.retrofit.APIInteface;
import com.example.myapplication.retrofit.RetrofitClientInstance;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity implements RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener {

    ProgressDialog progressDoalog;
    APIInteface apiInteface;
    ExpandableViewAdapter expandableViewAdapter;
    List<MyGroupItem> myGroupItems = new ArrayList<>();
    RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;
    RecyclerView.Adapter mWrappedAdapter;

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");
    LocalTime morningEndTime = LocalTime.parse("12:00:00", DateTimeFormatter.ISO_TIME);
    LocalTime afternoonEndTime = LocalTime.parse("15:00:00", DateTimeFormatter.ISO_TIME);
    LocalTime eveningEndTime = LocalTime.parse("18:00:00", DateTimeFormatter.ISO_TIME);
    LocalTime lateEveningEndTime = LocalTime.parse("22:00:00", DateTimeFormatter.ISO_TIME);
    LocalTime allEndTime = LocalTime.parse("20:00:00", DateTimeFormatter.ISO_TIME);

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_expand);
        callApi();
    }

    private void callApi() {
        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
        apiInteface = RetrofitClientInstance.getRetrofitInstance().create(APIInteface.class);
        Call<APIResponseModel> call = apiInteface.getResponseModel();
        call.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<APIResponseModel> call, @NonNull Response<APIResponseModel> response) {
                progressDoalog.dismiss();

                List<MyChildItems> dataList = response.body().getResponse();
                formatDataForAdapter(dataList);
                Collections.sort(myGroupItems, Comparator.comparingInt(MyGroupItem::getId));
                setAdapter();
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                progressDoalog.dismiss();
                System.out.println(t.getMessage());

            }
        });
    }

    private void setAdapter() {
        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(null);
        expandableViewAdapter = new ExpandableViewAdapter(myGroupItems, MainActivity.this,mRecyclerViewExpandableItemManager);
        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(expandableViewAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(mWrappedAdapter);
        recyclerView.setHasFixedSize(false);

    }

    public void formatDataForAdapter(List<MyChildItems> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            MyChildItems myChildItem = dataList.get(i);
            MyGroupItem myGroupItem;
            LocalTime currentItemOpenTime = LocalTime.parse(myChildItem.getSlotStartTime(), DateTimeFormatter.ISO_TIME);
            if (currentItemOpenTime.isBefore(morningEndTime)) {
                myGroupItem = new MyGroupItem();
                myGroupItem.setId(1);
                setItemsToGroupArray(myChildItem, Times.MORNING.name(), myGroupItem);
            } else if (currentItemOpenTime.isBefore(afternoonEndTime)) {
                myGroupItem = new MyGroupItem();
                myGroupItem.setId(2);
                setItemsToGroupArray(myChildItem, Times.AFTERNOON.name(), myGroupItem);
            } else if (currentItemOpenTime.isBefore(eveningEndTime)) {
                myGroupItem = new MyGroupItem();
                myGroupItem.setId(3);
                setItemsToGroupArray(myChildItem, Times.EVENING.name(), myGroupItem);
            } else if (currentItemOpenTime.isBefore(lateEveningEndTime)) {
                myGroupItem = new MyGroupItem();
                myGroupItem.setId(4);
                setItemsToGroupArray(myChildItem, Times.LATE_EVENING.name(), myGroupItem);
            }
            if (currentItemOpenTime.isBefore(allEndTime)) {
                myGroupItem = new MyGroupItem();
                myGroupItem.setId(5);
                setItemsToGroupArray(myChildItem, Times.ALL.name(), myGroupItem);
            }
        }


    }

    private void setItemsToGroupArray(MyChildItems myChildItem, String dayPhase, MyGroupItem myGroupItem) {
        List<MyChildItems> myChildItems;

        myGroupItem.setTitle(dayPhase);
        if (myGroupItems.contains(myGroupItem)) {
            myGroupItem = myGroupItems.get(myGroupItems.indexOf(myGroupItem));
            myChildItems = myGroupItem.getChildren();
        } else {
            myChildItems = new ArrayList<>();
            myGroupItems.add(myGroupItem);
        }
        if (myChildItem.getStatus() == 1) {
            myGroupItem.setAvailableSlots(myGroupItem.getAvailableSlots() + 1);
        }
        myChildItem.setSlotStartTime(formatTimeToHumanReadable(myChildItem.getSlotStartTime()));
        myChildItem.setSlotEndTime(formatTimeToHumanReadable(myChildItem.getSlotEndTime()));
        myChildItem.setId(myChildItems.size() + 1);
        myChildItems.add(myChildItem);
        myGroupItem.setChildren(myChildItems);

    }

    private String formatTimeToHumanReadable(String time) {
        String formattedTime = "";
        try {
            formattedTime = LocalTime.parse(time, DateTimeFormatter.ISO_TIME).format(FORMATTER);
        } catch (Exception e) {
            formattedTime = time;
            //   System.out.println(e);
        }
        return formattedTime;
    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser, Object payload) {

    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser, Object payload) {
        if (fromUser)
            adjustScrollPositionOnGroupExpanded(groupPosition);
    }

    private void adjustScrollPositionOnGroupExpanded(int groupPosition) {
        int childItemHeight = this.getResources().getDimensionPixelSize(R.dimen.list_item_height);
        int topMargin = (int) (this.getResources().getDisplayMetrics().density * 16); // top-spacing: 16dp
        int bottomMargin = topMargin; // bottom-spacing: 16dp

        mRecyclerViewExpandableItemManager.scrollToGroup(groupPosition, childItemHeight, topMargin, bottomMargin);
    }

}