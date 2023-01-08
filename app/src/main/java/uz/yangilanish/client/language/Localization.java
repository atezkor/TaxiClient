package uz.yangilanish.client.language;

import uz.yangilanish.client.utils.AppPref;

public class Localization {

    static String UNKNOWN_ERROR_RU = "Произошла неизвестная ошибка!\nПопробуй снова";
    static String UNKNOWN_ERROR_UZ = "Noma\u2019lum xatolik yuz berdi!\nBoshidan urinib ko\u2018ring";

    static String language;
    static {
        language = AppPref.getLanguage();
    }

    public static String getUnknownError() {
        if (language.equals("uz"))
            return UNKNOWN_ERROR_UZ;

        if (language.equals("ru"))
            return UNKNOWN_ERROR_RU;

        return "";
    }
}
