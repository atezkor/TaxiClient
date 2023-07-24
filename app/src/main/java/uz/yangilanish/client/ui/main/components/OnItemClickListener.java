package uz.yangilanish.client.ui.main.components;

import uz.yangilanish.client.models.Address;
import uz.yangilanish.client.models.CarModel;


public interface OnItemClickListener {

    default void onItemClick(CarModel carModel) {

    }

    default void onItemClick(Address address) {

    }
}
