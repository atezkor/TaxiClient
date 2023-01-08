package uz.yangilanish.client.ui.components;

import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import uz.yangilanish.client.R;
import uz.yangilanish.client.models.Address;


public class AddressViewHolder extends RecyclerView.ViewHolder {

    private final RelativeLayout addressBox;

    private final AppCompatTextView name;

    public AddressViewHolder(@NonNull View itemView) {
        super(itemView);

        addressBox = itemView.findViewById(R.id.address_button);
        name = itemView.findViewById(R.id.tv_address);
    }

    public void setOnClickListener(final OnItemClickListener clickListener, final Address address) {
        addressBox.setOnClickListener(v -> clickListener.onItemClick(address));
    }

    public void setAttribute(Address address) {
        name.setText(address.getName());
    }
}
