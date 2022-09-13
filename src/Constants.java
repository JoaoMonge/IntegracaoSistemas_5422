public class Constants {

    public static String DATABASE_URL = "jdbc:mysql://localhost:3306/Hotel_5086?characterEncoding=utf8";
    public static String DATABASE_USER = "root";
    public static String DATABASE_PASSWORD = "p@ssw0rd";

    //Caminho para o ficheiro jasperreports.
    public static String FILEPATH_JASPERREPORT=  "ConfirmacaoReserva.jrxml";

    //Equivale a verificar de 2 em 2 minutos se existe uma nova reserva.
    public static int REFRESH_RATE = 720;

}
