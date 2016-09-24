package kizema.anton.weatherapp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import kizema.anton.weatherapp.helpers.TimeHelper;
import kizema.anton.weatherapp.model.WeatherForcastDto;

import static org.junit.Assert.assertTrue;


public class HelperTests {

    WeatherForcastDto sampleWeatherDto1;
    WeatherForcastDto sampleWeatherDto2;

    @Before
    public void init(){
        sampleWeatherDto1 = Mockito.mock(WeatherForcastDto.class);
        sampleWeatherDto1.dayOfMonth = 3;
        sampleWeatherDto1.monthOfYear = 3;
        sampleWeatherDto1.hour = 12;
        sampleWeatherDto1.minute = 5;

        sampleWeatherDto2 = Mockito.mock(WeatherForcastDto.class);
        sampleWeatherDto2.dayOfMonth = 12;
        sampleWeatherDto2.monthOfYear = 10;
        sampleWeatherDto2.hour = 0;
        sampleWeatherDto2.minute = 52;
    }

    @Test
    public void test_getDayName() throws Exception {
        String ans = TimeHelper.getDayName(sampleWeatherDto1);
        assertTrue(ans.equals("03.03"));

        String ans2 = TimeHelper.getDayName(sampleWeatherDto2);
        assertTrue(ans2.equals("12.10"));
    }

    @Test
    public void test_getProperTimeInt() throws Exception {
        assertTrue("09".equals(TimeHelper.getProperTimeInt(9)));
        assertTrue("01".equals(TimeHelper.getProperTimeInt(1)));
        assertTrue("00".equals(TimeHelper.getProperTimeInt(0)));
        assertTrue("20".equals(TimeHelper.getProperTimeInt(20)));
        assertTrue("100".equals(TimeHelper.getProperTimeInt(100)));
    }

    @Test
    public void test_getTime() throws Exception {
        String ans = TimeHelper.getTime(sampleWeatherDto1);
        assertTrue(ans.equals("12:05"));

        String ans2 = TimeHelper.getTime(sampleWeatherDto2);
        assertTrue(ans2.equals("00:52"));
    }
}
