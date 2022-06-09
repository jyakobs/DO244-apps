package functions;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.nio.file.Paths;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import io.quarkus.funqy.Funq;

public class Function {

    @Funq
    public Output function(Input input) throws Exception
    {
        // Defining the variables:
        String fileName = "/home/shajain/RedHatTraining/GithubRepositories/DO244-apps/quarkus-weather-app/cities.json";

        // For Storing Tempertaure Values
        double tempInKelvin = 0.0d, tempInCelsius = 0.0d, tempInFahrenheit = 0.0d;

        // Object of class Methods
        Methods methods = new Methods();

        // JSONObjects for final JSON Output
        JSONObject cityDetailsJson =  new JSONObject(), tempDetailsJson =  new JSONObject();

        // Variables names needs to be figured out--------
        Map.Entry cityDetails,tempDetails = null;

        // Parsing the Query Parameter
        String city_name = input.getMessage();

        // flag to store result
        boolean isCityNamePresent = false;

        // Parsing the JSON file
        Object obj = new JSONParser().parse(new FileReader(fileName));

        // Typecasting Object to JSONObject
        JSONObject mainObj = (JSONObject) obj;

        /* First: We will fetch the value of Key 'city' and store it in a Map.
        After fetching, we will iterate the elements of Map using Iterator.*/
        Map cityDetailsMap = ((Map) mainObj.get("city"));
        Iterator<Map.Entry> cityDetailsItr = cityDetailsMap.entrySet().iterator();

        // Iterate over the Map
        while (cityDetailsItr.hasNext())
        {
            // Get the entry at this iteration
            cityDetails = cityDetailsItr.next();

            // Check if this key is the required key
            if(cityDetails.getKey().equals(city_name))
            {
                // Setting flag to true
                isCityNamePresent = true;

                // Fetch the value of the required key i.e. city_name
                JSONObject cityObj =  (JSONObject)cityDetails.getValue();

                // Fetch the value of Key 'main' and store it in a Map.
                Map tempDetailsMap = ((Map)cityObj.get("main"));
                Iterator<Map.Entry> tempDetailsItr = tempDetailsMap.entrySet().iterator();

                // Iterate over the Map
                while(tempDetailsItr.hasNext())
                {
                    // Get the entry at this iteration
                    tempDetails = tempDetailsItr.next();

                    // Check if this key is the required key
                    if(tempDetails.getKey().equals("temp"))
                    {
                        /* Fetch the value of the required key 'temp'
                        and it holds temp in degree kelvin */
                        tempInKelvin = (double) tempDetails.getValue();
                    }
                }
            }
        }

        // If city name cannot be found
        if(isCityNamePresent == false)
        {
            cityDetailsJson.put("city",city_name);
            cityDetailsJson.put("message","City cannot be found!!");

            return new Output(cityDetailsJson.toJSONString());
        }

        // Time for Conversions

        // Converting temp from kelvin to celsius
        tempInCelsius = methods.kelvin_to_celsius(tempInKelvin);
        // Converting temp from kelvin to fahrenheit
        tempInFahrenheit = methods.kelvin_to_fahrenheit(tempInKelvin);

        // Setting the final output
        cityDetailsJson.put("city",city_name);
        tempDetailsJson.put("celsius",tempInCelsius);
        tempDetailsJson.put("fahrenheit",tempInFahrenheit);
        tempDetailsJson.put("kelvin",tempInKelvin);
        cityDetailsJson.put("temperature",tempDetailsJson);

        return new Output(cityDetailsJson.toJSONString());
    }

}