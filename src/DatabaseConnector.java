import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;


/**
 * Conector para se ligar à base de dados do Hóteis
 */
public class DatabaseConnector {

    public DatabaseConnector(){

    }

    /**
     * Este método obtêm todas as reservas e contêm lógica para obter apenas as inseridas recentemente.
     * @param count -> Indica a partir de qual registo devemos verificar se foi inserida uma nova entrada
     * @return última contagem de reservas existentes na base de dados
     */
    public int getReservas(int count){
        try {
            //Carrega o driver do jdbc para conectar ao MySql
            Class.forName("com.mysql.cj.jdbc.Driver");

            //Conecta-se à base de dados
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Hotel_5086?characterEncoding=utf8", "root", "p@ssw0rd");

            //Executa uma query que devolve o conjunto Reserva,Reserva_Quartos,Hotel ordenado pela data de entrada no quarto.
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Reserva r,Reserva_Quartos rq, Hotel h \n" +
                    "WHERE r.Numero_Reserva = rq.Numero_Reserva AND rq.Sigla_Hotel = h.Sigla_Hotel ORDER BY r.Dia_Entrada ASC;");


            //O counter a 0 vai servir para ignorar antigas reservas
            int counter = 0;
            while (rs.next()) {
                ++counter;

                //Detetar uma nova reserva
                if(counter > count){
                    //Detectada nova Reserva.

                    //1º Obter o geocode do hotel
                    ServicoGeocode geocode = new ServicoGeocode();
                    JSONObject coords = geocode.obterCoordenadasHotel(rs.getString(11),"Lisbon");
                    JSONObject location = (JSONObject) coords.get("location");

                    //2º Obter meteorologia para aquele hotel
                    ServicoMeteorologia meteo = new ServicoMeteorologia();
                    JSONObject previsao = meteo.obterPrevisao5Dias(String.valueOf(location.getDouble("lat")),String.valueOf(location.getDouble("lng")));

                    System.out.println(previsao.toString());

                    //3º Gerar o Report feito no JasperSoft
                    JasperReport jr = JasperCompileManager.compileReport("/Users/joaomonge/JaspersoftWorkspace/MyReports/ConfirmacaoReserva.jrxml");

                    JSONArray array = (JSONArray) previsao.get("data");
                    JSONObject prev1 = (JSONObject) array.get(0);
                    JSONObject prev1_desc = (JSONObject) prev1.get("weather");
                    String description = prev1_desc.getString("description");

                    //Preenchimento dos parametros do Report
                    HashMap<String,Object> params = new HashMap<String,Object>();
                    params.put("Designacao",rs.getString(11));
                    params.put("Dia_Entrada", rs.getString(3) );
                    params.put("Dia_Saida", rs.getString(4));
                    params.put("Dia1_Meteo",description);
                    params.put("Dia2_Meteo",description);
                    params.put("Dia3_Meteo",description);
                    params.put("Dia4_Meteo",description);
                    params.put("Dia5_Meteo",description);


                    JasperPrint jp = JasperFillManager.fillReport(jr,params, con);
                    JasperViewer.viewReport(jp);

                }

            }
            con.close();
            return counter;
        }catch(Exception e){
            System.out.println(e);
            return 0;
        }
    }
}
