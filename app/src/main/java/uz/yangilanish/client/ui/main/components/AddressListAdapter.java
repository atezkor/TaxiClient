package uz.yangilanish.client.ui.main.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uz.yangilanish.client.R;
import uz.yangilanish.client.models.Address;
import uz.yangilanish.client.utils.CacheData;


public class AddressListAdapter extends RecyclerView.Adapter<AddressViewHolder> {

    private final Context context;

    private final OnItemClickListener onItemClickListener;

    private final List<Address> addressList;

    public AddressListAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;

        addressList = CacheData.getClient().getLastBookingAddresses();
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addressList.get(position);

        holder.setAttribute(address);
        holder.setOnClickListener(onItemClickListener, address);
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }
}
