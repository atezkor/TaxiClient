package uz.yangilanish.client.actions;

import java.util.List;

import uz.yangilanish.client.dto.auth.CheckAuthResponse;
import uz.yangilanish.client.dto.auth.ResponseData;
import uz.yangilanish.client.dto.auth.SmsConfirmResponse;
import uz.yangilanish.client.models.Address;
import uz.yangilanish.client.models.AppProperty;
import uz.yangilanish.client.models.CarModel;
import uz.yangilanish.client.models.Client;
import uz.yangilanish.client.models.Company;
import uz.yangilanish.client.models.Driver;
import uz.yangilanish.client.utils.AppPref;
import uz.yangilanish.client.utils.CacheData;


public class AuthAction {

    public void smsConfirm(SmsConfirmResponse responseBody) {
        ResponseData response = responseBody.getData();
        setAppData(response.getClient(), response.getCarModelList(), response.getTariffList(), response.getAddressList(), response.getCompany(), response.getAppProperty());
    }

    public void clientAuth(CheckAuthResponse responseBody) {
        ResponseData response = responseBody.getData();
        setAppData(response.getClient(), response.getCarModelList(), response.getTariffList(), response.getAddressList(), response.getCompany(), response.getAppProperty());
    }

    private void setAppData(Client client, List<CarModel> carModelList, List<Driver.Tariff> tariffList, List<Address> addressList, Company company, AppProperty appProperty) {
        AppPref.setPhone(client.getPhone());
        AppPref.setSecretKey(client.getSecretKey());

        CacheData.setClient(client);
        CacheData.setCarModelList(carModelList);
        CacheData.setTariffList(tariffList);
        CacheData.setAddressList(addressList);
        CacheData.setCompany(company);
        CacheData.setAppProperty(appProperty);
    }

    public static void logout() {
        AppPref.setPhone(null);
        AppPref.setSecretKey(null);
    }
}
