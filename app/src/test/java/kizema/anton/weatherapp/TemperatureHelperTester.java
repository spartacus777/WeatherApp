package kizema.anton.weatherapp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import kizema.anton.weatherapp.helpers.TemperatureHelper;
import kizema.anton.weatherapp.model.WeatherForcastDto;

import static org.junit.Assert.assertTrue;

public class TemperatureHelperTester {

    WeatherForcastDto sampleWeatherDto1;
    WeatherForcastDto sampleWeatherDto2;

    @Before
    public void init() {
        sampleWeatherDto1 = Mockito.mock(WeatherForcastDto.class);
        sampleWeatherDto1.temp_min = -30;
        sampleWeatherDto1.temp = 3;
        sampleWeatherDto1.temp_max = 12;

        sampleWeatherDto2 = Mockito.mock(WeatherForcastDto.class);
        sampleWeatherDto2.temp_min = 12;
        sampleWeatherDto2.temp = 10;
        sampleWeatherDto2.temp_max = 0;
    }

    @Test
    public void test_getDayName() throws Exception {
        String ans = TemperatureHelper.getTemperatureString(sampleWeatherDto1);
        assertTrue(ans.equals("-30 - 12 C"));

        String ans2 = TemperatureHelper.getTemperatureString(sampleWeatherDto2);
        assertTrue(ans2.equals("12 - 0 C"));
    }

}
