package uz.yangilanish.client.dto.auth;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import uz.yangilanish.client.models.Address;
import uz.yangilanish.client.models.AppProperty;
import uz.yangilanish.client.models.CarModel;
import uz.yangilanish.client.models.Client;
import uz.yangilanish.client.models.Company;
import uz.yangilanish.client.models.Driver;


public class ResponseData {

    @SerializedName("update_required")
    private boolean requiredUpdate;

    @SerializedName("update_url")
    private String updateUrl;

    private Client client;

    private Company company;

    private AppProperty appProperty;

    private List<CarModel> carModelList = new ArrayList<>();

    private List<Driver.Tariff> tariffList = new ArrayList<>();

    private List<Address> addressList = new ArrayList<>();

    public boolean isRequiredUpdate() {
        return requiredUpdate;
    }

    public void setRequiredUpdate(boolean requiredUpdate) {
        this.requiredUpdate = requiredUpdate;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Company getCompany() {
        return company;
    }

    public AppProperty getAppProperty() {
        return appProperty;
    }

    public void setAppProperty(AppProperty appProperty) {
        this.appProperty = appProperty;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Driver.Tariff> getTariffList() {
        return tariffList;
    }

    public void setTariffList(List<Driver.Tariff> tariffList) {
        this.tariffList = tariffList;
    }

    public List<CarModel> getCarModelList() {
        return carModelList;
    }

    public void setCarModelList(List<CarModel> carModelList) {
        this.carModelList = carModelList;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}