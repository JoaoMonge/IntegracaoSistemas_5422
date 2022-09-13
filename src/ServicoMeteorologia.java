import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Consulta uma REST Api de Meteorologia
 */
public class ServicoMeteorologia {

    /**
     * Método Constructor da classe - Vai receber
     */
    public ServicoMeteorologia(){
        //Vazio de momento não é necessário colocar nada
    }

    /**
     * Consultar o servico de Meteorologia para obter uma previsão em 16 dias num ponto Geográfico através latitude longitude
     */
    public JSONObject obterPrevisao16Dias(String lat,String lon){

        /**
         * Consulta de uma REST API proveniente do site RapidAPI que contêm várias com o código exemplo
         * RETORNA um JSON Com os dados da Meteorologia
         */
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://weatherbit-v1-mashape.p.rapidapi.com/forecast/daily?lat="+lat+"&lon="+lon+"&lang=pt"))
                    .header("X-RapidAPI-Key", "2c919a96d7msh4b3c8f542ec9108p1b1e5fjsn5c0f938ee222")
                    .header("X-RapidAPI-Host", "weatherbit-v1-mashape.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            //Transformar o resultado do pedido GET em JSON para ser mais fácil de trabalhar em código JAVA
            JSONObject object = new JSONObject(response.body());
            return object;
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
    }

    /**
     * Consultar o servico de Meteorologia para obter uma previsão em 5 dias num ponto Geográfico através latitude longitude
    */
    public JSONObject obterPrevisao5Dias(String lat,String lon){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://weatherbit-v1-mashape.p.rapidapi.com/forecast/3hourly?lat="+lat+"&lon="+lon+"&lang=pt"))
                    .header("X-RapidAPI-Key", "2c919a96d7msh4b3c8f542ec9108p1b1e5fjsn5c0f938ee222")
                    .header("X-RapidAPI-Host", "weatherbit-v1-mashape.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            JSONObject object = new JSONObject(response.body());
            return object;
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
    }

}
