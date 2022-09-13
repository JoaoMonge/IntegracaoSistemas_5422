import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
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
     * @param contagem_old_reservas -> Indica a partir de qual registo devemos verificar se foi inserida uma nova entrada
     * @return última contagem de reservas existentes na base de dados
     */
    public int getReservas(int contagem_old_reservas){
        try {
            //Carrega o driver do jdbc para conectar ao MySql
            Class.forName("com.mysql.cj.jdbc.Driver");

            //Conecta-se à base de dados
            Connection con = DriverManager.getConnection(
                    Constants.DATABASE_URL, Constants.DATABASE_USER, Constants.DATABASE_PASSWORD);

            //Executa uma query que devolve o conjunto Reserva,Reserva_Quartos,Hotel ordenado pela data de entrada no quarto.
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Reserva r,Reserva_Quartos rq, Hotel h \n" +
                    "WHERE r.Numero_Reserva = rq.Numero_Reserva AND rq.Sigla_Hotel = h.Sigla_Hotel ORDER BY r.Dia_Entrada ASC;");


            //O contagem_total_reservas a 0 vai servir para ignorar antigas reservas
            int contagem_total_reservas = 0;
            while (rs.next()) {
                ++contagem_total_reservas;
                //Deteta uma nova reserva sempre que o
                if(contagem_total_reservas > contagem_old_reservas){
                    //Detectada nova Reserva.

                    //1º Obter o geocode do hotel
                    ServicoGeocode geocode = new ServicoGeocode();
                    JSONObject coords = geocode.obterCoordenadasHotel(rs.getString(11),"Lisbon");
                    JSONObject location = (JSONObject) coords.get("location");

                    //2º Obter meteorologia para aquele hotel
                    ServicoMeteorologia meteo = new ServicoMeteorologia();
                    JSONObject previsao = meteo.obterPrevisao5Dias(String.valueOf(location.getDouble("lat")),String.valueOf(location.getDouble("lng")));


                    //3º Gerar o Report feito no JasperSoft
                    JasperReport jr = JasperCompileManager.compileReport(Constants.FILEPATH_JASPERREPORT);

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


                    //Jasperprint recebe o ficheiro JasperReport linha 64 mais os parametros e a conexao à base de dados
                    JasperPrint jp = JasperFillManager.fillReport(jr,params, con);
                    JasperViewer.viewReport(jp);
                }
            }
            con.close();
            return contagem_total_reservas;
        }catch(Exception e){
            System.out.println(e);
            return 0;
        }
    }
}
