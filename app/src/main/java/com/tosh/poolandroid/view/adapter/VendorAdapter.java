package com.tosh.poolandroid.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tosh.poolandroid.R;
import com.tosh.poolandroid.model.Vendor;

import java.util.ArrayList;
import java.util.List;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.VendorView> {

 private List<Vendor> vendorModel = new ArrayList<>();
    private Context context;

    public VendorAdapter(Context context, List<Vendor> vendorModel){
        this.vendorModel = vendorModel;
        this.context = context;

    }

    @NonNull
    @Override
    public VendorView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_vendor, parent, false);
        return new VendorView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorView holder, int position) {
        holder.vendorName.setText(vendorModel.get(position).getName());

        Picasso.get()
                .load(vendorModel.get(position).getImg_url())
                .fit()
                .centerCrop()
                .into(holder.vendorImage);
    }

    @Override
    public int getItemCount() {
        return vendorModel.size();
    }

    public class VendorView extends RecyclerView.ViewHolder{

        ImageView vendorImage;
        TextView vendorName;
        public VendorView(@NonNull View itemView) {
            super(itemView);

            vendorImage = (ImageView) itemView.findViewById(R.id.vendor_image);
            vendorName = itemView.findViewById(R.id.vendor_name);
        }
    }
}
