import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


/**
 * O Geocode permite obter uma morada com base numa localização ou morada.
 * Neste projeto foi implementado para obter as coordenadas de um hotel com base no nome do mesmo.
 * Essas coordenadas são obtidas depois para obter a meteorologia na zona do Hotel.
 */
public class ServicoGeocode {

    public ServicoGeocode(){
    }

    /**
     * Permite obter as coordenadas de um Hotel especifico numa Localidade
     * Devem usar os nomes em Ingles Exemplo Sheraton, Lisbon
     * @param nomeHotel
     * @param localidade
     * @return
     */
    public JSONObject obterCoordenadasHotel(String nomeHotel,String localidade){
        try {
            nomeHotel = nomeHotel.replace(" ","%20");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://geocode-forward-and-reverse.p.rapidapi.com/forward?address=Hotel%20"+nomeHotel+"%20in%20" + localidade))
                    .header("X-RapidAPI-Key", "34fc55e7c0mshe8b9d757743821cp1cd6d9jsn15c26f8282ba")
                    .header("X-RapidAPI-Host", "geocode-forward-and-reverse.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            //Transformar o resultado do pedido GET em JSON para ser mais fácil de trabalhar em código JAVA
            JSONObject coords = new JSONObject(response.body());
            return coords;
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
    }
}
