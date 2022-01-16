package com.weatherapp.WeatherApplication.View;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.weatherapp.WeatherApplication.Serivce.WeatherService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@SpringUI(path = "")
public class MainView extends UI {

    @Autowired
    private WeatherService weatherService;


    private VerticalLayout mainLayout;
    private NativeSelect<String> unitSelect;
    private TextField cityTextField;
    private Button searchButton;
    private HorizontalLayout dashboard;
    private Label location;
    private Label currentTemp;
    private HorizontalLayout mainDescription;
    private Label weatherDescription;
    private Label minWeather;
    private Label maxWeather;
    private Label humidity;
    private Label pressure;
    private Label wind;
    private Label feesLike;
    private Image iconImage;


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        mainLayout();
        setHeader();
        setLogo();
        setForm();
        dashboardTitle();
        dashboardDetails();

        searchButton.addClickListener(clickEvent -> {
            if(!cityTextField.getValue().equals("") && unitSelect.getValue()!=null){
                    try{
                        updateUI();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
            }else{
                Notification.show("Please provide the city name And/or unit");
            }
        });
    }

    public void mainLayout(){
        iconImage = new Image();
        mainLayout = new VerticalLayout();
        mainLayout.setWidth("100%");
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setContent(mainLayout);
    }

    private void setHeader(){
        HorizontalLayout header = new HorizontalLayout();
        header.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Label title = new Label("Welcome to the Weather app");
        title.setStyleName(ValoTheme.LABEL_H1);
        header.addComponent(title);
        mainLayout.addComponent(header);
    }

    private void setLogo(){
        HorizontalLayout logo = new HorizontalLayout();
        logo.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Image img= new Image(null,new ClassResource("/static/logo.png"));

        logo.setWidth("240px");
        logo.setHeight("240px");
        logo.setVisible(true);

        logo.addComponent(img);
        mainLayout.addComponent(logo);
    }

    private void setForm() {
        HorizontalLayout formLayout = new HorizontalLayout();
        formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        formLayout.setSpacing(true);
        formLayout.setMargin(true);

        //unite field
        unitSelect = new NativeSelect<String>();
        ArrayList<String> items = new ArrayList<>();
        items.add("Celcius");
        items.add("Farhenite");
        unitSelect.setEmptySelectionCaption("Select");
        unitSelect.setItems(items);
        unitSelect.setValue(items.get(0));
        formLayout.addComponent(unitSelect);

        //Text field
        cityTextField = new TextField();
        cityTextField.setWidth("80%");
        formLayout.addComponent(cityTextField);


        //Search button
        searchButton = new Button();
        searchButton.setIcon(VaadinIcons.SEARCH);
        formLayout.addComponent(searchButton);


        mainLayout.addComponent(formLayout);
    }

    private void dashboardTitle(){
            dashboard = new HorizontalLayout();
            dashboard.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

            //location
            location = new Label("Currently in Pune");
            location.addStyleName(ValoTheme.LABEL_H2);
            location.addStyleName(ValoTheme.LABEL_LIGHT);

            //Current temperature
            currentTemp = new Label();
            currentTemp.addStyleName(ValoTheme.LABEL_H1);
            currentTemp.addStyleName(ValoTheme.LABEL_BOLD);

            dashboard.addComponents(location,iconImage,currentTemp);



    }

    private void dashboardDetails(){
        mainDescription=new HorizontalLayout();
        mainDescription.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        //Description layout
        VerticalLayout descriptionLayout = new VerticalLayout();
        descriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        //Weather Description
        weatherDescription = new Label("clear skies");
        weatherDescription.setStyleName(ValoTheme.LABEL_SUCCESS);

        descriptionLayout.addComponent(weatherDescription);

        //Min weather
        minWeather = new Label("Min : 55");
        descriptionLayout.addComponent(minWeather);

        //Max weather
        maxWeather = new Label("Min : 55");
        descriptionLayout.addComponent(maxWeather);

        VerticalLayout pressureLayout =new VerticalLayout();
        pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        pressure = new Label("pressure : 555");
        pressureLayout.addComponent(pressure);

        humidity = new Label("humidity : 555");
        pressureLayout.addComponent(humidity);

        wind = new Label("pressure : 555");
        pressureLayout.addComponent(wind);

        feesLike = new Label("FeesLike : 555");
        pressureLayout.addComponent(feesLike);

        mainDescription.addComponents(descriptionLayout,pressureLayout);

    }

    private void updateUI()  {

        String city = cityTextField.getValue();

        String defaultUnit;

        weatherService.setCityName(city);

        if(unitSelect.getValue().equals("Farhenite")){
            weatherService.setUnit("imperials");
        }else{
            weatherService.setUnit("metric");
        }

        if(weatherService.checkWeather()==true){

            if(unitSelect.getValue().equals("Farhenite")){
                weatherService.setUnit("imperials");
                unitSelect.setValue("F");
                defaultUnit = "\u00b0"+"F";
            }else{
                weatherService.setUnit("metric");
                unitSelect.setValue("C");
                defaultUnit = "\u00b0"+"C";
            }

            JSONObject mainObject = weatherService.getMain();
            int t = mainObject.getInt("temp");
            currentTemp.setValue(t +defaultUnit);

            String iconCode=null;
            String weatherDescriptionNew = null;
            JSONArray jsonArray = weatherService.getWeatherArray();
            for(int i=0;i<jsonArray.length();i++){
                JSONObject weatherObj= jsonArray.getJSONObject(i);
                iconCode = weatherObj.getString("icon");
                weatherDescriptionNew = weatherObj.getString("description");
            }

            iconImage.setSource(new ExternalResource("http://openweathermap.org/img/wn/"+iconCode+"@2x.png"));

            weatherDescription.setValue("Description : "+weatherDescriptionNew);
            minWeather.setValue("Min Tempeature "+weatherService.getMain().getFloat("temp_min")+unitSelect.getValue());
            maxWeather.setValue("Max Tempeature "+weatherService.getMain().getFloat("temp_max")+unitSelect.getValue());

            pressure.setValue("Pressure : "+ weatherService.getMain().getFloat("pressure"));
            humidity.setValue("Himdity : "+weatherService.getMain().getFloat("humidity"));
            wind.setValue("Wind : "+weatherService.getWind().getFloat("speed"));
            feesLike.setValue("Feels Like : "+weatherService.getMain().getFloat("feels_like"));

            location.setValue("Currently in "+city);

            mainLayout.addComponents(dashboard,mainDescription);
        }else{
            mainLayout.removeComponent(dashboard);
            mainLayout.removeComponent(mainDescription);
            Notification.show("Please provide valid city name");
        }

    }
}
