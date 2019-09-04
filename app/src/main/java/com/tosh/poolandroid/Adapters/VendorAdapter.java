package com.tosh.poolandroid.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tosh.poolandroid.R;
import com.tosh.poolandroid.Retrofit.Model.Vendor;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.VendorView> {

    List<Integer> vendorImgList = new ArrayList<>();
    List<String> vendorTitleList = new ArrayList<>();

    public VendorAdapter(List<Integer> vendorImgList, List<String> vendorTitleList) {
        this.vendorImgList = vendorImgList;
        this.vendorTitleList = vendorTitleList;
    }

    @NonNull
    @Override
    public VendorView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_vendor, parent, false);
        return new VendorView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorView holder, int position) {

        holder.vendorImage.setImageResource(vendorImgList.get(position));
        holder.vendorName.setText(vendorTitleList.get(position));

    }

    @Override
    public int getItemCount() {
        return vendorImgList.size();
    }

    public class VendorView extends RecyclerView.ViewHolder{

        CircleImageView vendorImage;
        TextView vendorName;
        public VendorView(@NonNull View itemView) {
            super(itemView);

            vendorImage = (CircleImageView) itemView.findViewById(R.id.vendor_image);
            vendorName = itemView.findViewById(R.id.vendor_name);
        }
    }
}
