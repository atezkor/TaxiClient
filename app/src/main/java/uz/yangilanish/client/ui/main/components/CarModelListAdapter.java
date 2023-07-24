package uz.yangilanish.client.ui.main.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uz.yangilanish.client.R;
import uz.yangilanish.client.models.CarModel;
import uz.yangilanish.client.utils.CacheData;


public class CarModelListAdapter extends RecyclerView.Adapter<CarModelViewHolder> {

    private final Context context;

    private final List<CarModel> carModelList;

    private OnItemClickListener itemClickListener;

    private int position = -1;

    private boolean disable;

    public CarModelListAdapter(Context context, OnItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;

        carModelList = CacheData.getCarModelList();
    }

    @NonNull
    @Override
    public CarModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_car_model, parent, false);

        return new CarModelViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull CarModelViewHolder holder, int position) {
        CarModel carModel = carModelList.get(position);

        itemClickListener = new OnItemClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(CarModel carModel) {
                if (disable) {
                    return;
                }

                for (CarModel model : CacheData.getCarModelList()) {
                    if (model.getId() == carModel.getId()) {
                        setPosition(holder.getAdapterPosition(), model.isSelected());
                        model.setSelected(!model.isSelected());
                    } else {
                        model.setSelected(false);
                    }
                }

                notifyDataSetChanged(); // notifyItemChanged(position);
            }
        };

        holder.setOnClickListener(itemClickListener, carModel);
        holder.setAttribute(context, carModel);
    }

    @Override
    public int getItemCount() {
        return carModelList.size();
    }


    public int getPosition() { // NO_ID = 0
        return position;
    }

    public void setPosition(int index, boolean isSelected) {
        if (isSelected) {
            this.position = -1;
        } else {
            this.position = index;
        }
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public CarModel getItem() {
        if (getPosition() >= 0) {
            return carModelList.get(getPosition());
        }

        return null;
    }
}
