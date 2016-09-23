package kizema.anton.weatherapp.helpers;


public class FahrenheitToCelsius {

    public static int farenheitToCelsius(double far){
        return (int) (((far - 32)*5)/9);
    }

    public static int kelvinToCelsius(double kel){
        return (int) (kel - 273.15);
    }
}
