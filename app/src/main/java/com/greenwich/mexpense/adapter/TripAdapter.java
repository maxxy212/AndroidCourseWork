package com.greenwich.mexpense.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.greenwich.mexpense.BR;
import com.greenwich.mexpense.R;
import com.greenwich.mexpense.activity.EditTripActivity;
import com.greenwich.mexpense.activity.ShowActivity;
import com.greenwich.mexpense.model.Trip;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Package com.greenwich.mexpense.adapter in
 * <p>
 * Project MExpense
 * <p>
 * Created by Maxwell on 15/11/2021
 */
public class TripAdapter extends RealmRecyclerViewAdapter<Trip, RecyclerView.ViewHolder> {

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;
    private Realm realm;
    UploadAllTripsListener uploadAllTripsListener;

    public TripAdapter(@Nullable OrderedRealmCollection<Trip> data, Realm realm, UploadAllTripsListener uploadAllTripsListener) {
        super(data, true, true);
        this.realm = realm;
        this.uploadAllTripsListener = uploadAllTripsListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER){
            ViewDataBinding _binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.header_view, parent, false);
            return new HeaderItemHolder(_binding);
        }else {
            ViewDataBinding _binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.trip_card, parent, false);
            return new TripItemHolder(_binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderItemHolder){
            ((HeaderItemHolder) holder).setData();
        }
        else if (holder instanceof TripItemHolder){
            final Trip trip = getItem(position - 1);
            ((TripItemHolder) holder).setTrip(trip);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
        {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    class HeaderItemHolder extends RecyclerView.ViewHolder implements SearchView.OnQueryTextListener{
        ViewDataBinding dataBinding;
        RealmQuery<Trip> query = realm.where(Trip.class);

        public HeaderItemHolder(ViewDataBinding itemView) {
            super(itemView.getRoot());
            dataBinding = itemView;
        }

        void setData(){
            View view = dataBinding.getRoot();
            SearchView searchView = view.findViewById(R.id.simpleSearchView);
            searchView.setOnQueryTextListener(this);
            MaterialButton upload = view.findViewById(R.id.upload);
            MaterialButton delete = view.findViewById(R.id.delete);
            upload.setOnClickListener(view1 -> uploadAllTripsListener.onUploadClick());
            delete.setOnClickListener(view1 -> realm.delete(Trip.class));
        }

        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (s.isEmpty()) {
                updateData(realm.where(Trip.class).findAllAsync());
            }else {
                query.contains("name", s, Case.INSENSITIVE)
                .or().contains("destination", s, Case.INSENSITIVE)
                .or().contains("date_of_trip", s, Case.INSENSITIVE);
                updateData(query.findAllAsync());
            }
            return false;
        }
    }

    class TripItemHolder extends RecyclerView.ViewHolder{
        ViewDataBinding dataBinding;

        public TripItemHolder(ViewDataBinding itemView) {
            super(itemView.getRoot());
            dataBinding = itemView;
        }

        void setTrip(final Trip trip){
            dataBinding.setVariable(BR.trip, trip);
            View view = dataBinding.getRoot();
            MaterialButton del = view.findViewById(R.id.delete);
            MaterialButton show = view.findViewById(R.id.show);
            MaterialButton edit = view.findViewById(R.id.edit);
            del.setOnClickListener(view1 -> realm.executeTransaction(realm1 -> trip.deleteFromRealm()));
            show.setOnClickListener(view1 -> ShowActivity.start(view.getContext(), trip.id));
            edit.setOnClickListener(view1 -> EditTripActivity.start(view.getContext(), trip.id));
        }
    }

    public interface UploadAllTripsListener{
        void onUploadClick();
    }

}
