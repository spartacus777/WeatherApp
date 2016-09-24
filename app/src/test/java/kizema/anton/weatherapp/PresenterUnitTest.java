package kizema.anton.weatherapp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import kizema.anton.weatherapp.activities.stations.WeatherInteractor;
import kizema.anton.weatherapp.activities.stations.WeatherPresenterImpl;
import kizema.anton.weatherapp.activities.stations.WeatherView;
import kizema.anton.weatherapp.model.WeatherForcastDto;

import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class PresenterUnitTest {

    @Before
    public void init() {

    }

    @Test
    public void test_presenter_different_data_db_inet() throws Exception {

        final List<WeatherForcastDto> list = new ArrayList<>();
        WeatherForcastDto s = Mockito.mock(WeatherForcastDto.class);
        s.cityId = "DEMO";
        list.add(s);


        final List<WeatherForcastDto> dbList = new ArrayList<>();
        WeatherForcastDto s1 = Mockito.mock(WeatherForcastDto.class);
        s1.cityId = "DBDEMO";
        dbList.add(s1);

        WeatherForcastDto s2 = Mockito.mock(WeatherForcastDto.class);
        s2.cityId = "DBDEMO2";
        dbList.add(s2);


        WeatherPresenterImpl presenter = new WeatherPresenterImpl(new WeatherInteractor() {
            @Override
            public void loadData(OnCompletion listener) {
                listener.onComplete(list);
            }

            @Override
            public List<WeatherForcastDto> loadDataFromDB() {
                return dbList;
            }

            @Override
            public boolean shouldLoadFromLocalDb() {
                return true;
            }
        });

        WeatherView view = new WeatherView() {

            int counter = 0;

            @Override
            public void setData(List<WeatherForcastDto> list) {
                assertTrue(counter <= 1);

                if (counter == 0){
                    //DB load
                    assertTrue(list.size() == 2);
                    assertTrue(list.get(0).cityId.equals("DBDEMO"));
                    assertTrue(list.get(1).cityId.equals("DBDEMO2"));
                }

                if (counter == 1){
                    //DB load
                    assertTrue(list.size() == 1);
                    assertTrue(list.get(0).cityId.equals("DEMO"));
                }

                ++counter;
            }

            @Override
            public void showError() {

            }
        };


        presenter.setView(view);

    }

//    private StationModel mockStationModel(int id, String descr){
//        StationModel s = Mockito.mock(StationModel.class);
//        s.setTitle(descr);
//        s.setStationId(id);
//
//        Mockito.when(s.getStationId()).thenReturn(id);
//        Mockito.when(s.getTitle()).thenReturn(descr);
//
//        return s;
//    }

//    @Test
//    public void test_update_list() throws Exception {
//
//        final List<StationModel> list = new ArrayList<>();
//        final List<StationModel> list1 = new ArrayList<>();
//
//        StationModel s = mockStationModel(1, "test");
//        list.add(s);
//        list1.add(s);
//
//        StationModel s2 = mockStationModel(2, "ejdew");
//        list1.add(s2);
//
//        StationsPresenter presenter = new StationsPresenterImpl(new StationsInteractor() {
//            @Override
//            public void loadData(OnCompletion listener) {
//                listener.onComplete(list1);
//            }
//
//            @Override
//            public List<StationModel> loadDataFromDB() {
//                return list;
//            }
//        });
//
//        StationsView view = new StationsView() {
//
//            private int counter = 0;
//            private boolean first = true;
//
//            @Override
//            public void setData(List<StationModel> list) {
//                if (first){
//                    first = false;
//
//                    assertEquals(list.size(), 1);
//                    assertTrue(list.get(0).getStationId() == 1);
//                    assertTrue(list.get(0).getTitle().equals("test"));
//                } else {
//                    assertEquals(list.size(), 2);
//                    assertTrue(list.get(0).getStationId() == 1);
//                    assertTrue(list.get(1).getStationId() == 2);
//
//                    assertEquals(counter, 0);
//                    ++counter;
//                }
//            }
//
//            @Override
//            public void showError() {}
//        };
//
//        presenter.setView(view);
//    }

}