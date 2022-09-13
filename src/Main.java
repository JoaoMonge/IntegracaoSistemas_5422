import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) throws JRException, ClassNotFoundException, SQLException {



        final int[] count = {0};


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                DatabaseConnector db = new DatabaseConnector();
                count[0] = db.getReservas(count[0]);

            }
        }, 0, 120 * 1000);


        while(true) {}
}
}
