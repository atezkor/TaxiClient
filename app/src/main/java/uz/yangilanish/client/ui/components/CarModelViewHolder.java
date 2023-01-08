package uz.yangilanish.client.ui.components;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import uz.yangilanish.client.R;
import uz.yangilanish.client.models.CarModel;
import uz.yangilanish.client.network.ApiConstants;
import uz.yangilanish.client.utils.CacheData;
import uz.yangilanish.client.utils.NumberFormat;


public class CarModelViewHolder extends RecyclerView.ViewHolder {

    private final RelativeLayout model;

    private final ImageView image;

    private final AppCompatTextView name, price;

    public CarModelViewHolder(@NonNull View itemView) {
        super(itemView);

        model = itemView.findViewById(R.id.rl_car_model);
        image = itemView.findViewById(R.id.iv_car_image);
        name = itemView.findViewById(R.id.tv_model_name);
        price = itemView.findViewById(R.id.tv_car_price);
    }

    public void setAttribute(Context context, CarModel carModel) {
        name.setText(carModel.getName());

        model.setSelected(carModel.isSelected()); // setBackground(ContextCompat.getDrawable(context, R.drawable.car_model_item_selected));
        if (carModel.isSelected()) {
            name.setTextColor(context.getResources().getColor(R.color.md_grey_100));
            price.setTextColor(context.getResources().getColor(R.color.md_grey_100));
        } else {
            name.setTextColor(context.getResources().getColor(R.color.md_grey_800));
            price.setTextColor(context.getResources().getColor(R.color.md_grey_800));
        }

        // Set image
        Picasso picasso = Picasso.get();
        picasso.load(ApiConstants.IMAGE_URL + carModel.getPhoto()).into(image);

        // Additional payment for particular address
        int payment = carModel.getPrice();
        if (CacheData.getCurrentAddress() != null) {
            payment += CacheData.getCurrentAddress().getAdditionalPayment();
        }

        price.setText(String.format(context.getString(R.string.initial_payment), NumberFormat.price(payment)));
    }

    public void setOnClickListener(final OnItemClickListener clickListener, final CarModel m) {
        model.setOnClickListener(v -> clickListener.onItemClick(m));
    }
}
