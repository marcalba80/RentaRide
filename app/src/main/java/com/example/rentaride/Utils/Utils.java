package com.example.rentaride.Utils;

import android.location.Location;

import com.example.rentaride.Logica.Reserva;
import com.example.rentaride.Logica.Vehiculo;
import com.example.rentaride.R;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {
    public static final int COCHE = 0, MOTOCICLETA = 1, BICICLETA = 2, VIEW_TYPE_MESSAGE_SENT = 3, VIEW_TYPE_MESSAGE_RECEIVED = 4;
    public static final Vehiculo cocheprueba = new Vehiculo(COCHE, "Tesla", "S", "2019", "Color blanco", "636666663", "5815KDD","600 cv", "Electrico", "https://fotografias.lasexta.com/clipping/cmsimages01/2018/02/28/1728D227-6B90-470B-8300-20C5EC7B4F9C/58.jpg",false);
    public static final Vehiculo motoprueba = new Vehiculo(MOTOCICLETA, "Kawasaki", "Z800","2020", "Color negro mate", "666626663", "1174FPD","120 cv", "Gasolina", "https://img.clasf.es/2019/09/17/Kawasaki-Z800-PERFORMANCE-20190917155352.4997120015.jpg");
    public static final Vehiculo biciprueba = new Vehiculo(BICICLETA, "Mondraker", "","2017", "Bicicleta profesional de montaña", "666566663", "https://img.milanuncios.com/fg/3084/39/308439057_8.jpg?VersionId=JGuACnbtRxJ48FhiHQE3GkJPKJwJjmgi");

    static List<Event> listReservas = new ArrayList<>();

    public static List<Event> obtenerReservas(int c1, int c2, int c3){
        if(!listReservas.isEmpty()) return listReservas;
        long day = 86400000;
        Location l = new Location("");
        l.setLatitude(41.623237);
        l.setLongitude(0.615603);
        Location l2 = new Location("");
        l2.setLatitude(41.624556);
        l2.setLongitude(0.631703);
        Location l3 = new Location("");
        l3.setLatitude(41.629651);
        l3.setLongitude(0.626040);
        Location l4 = new Location("");
        l4.setLatitude(41.623237);
        l4.setLongitude(0.625603);
        Location l5 = new Location("");
        l5.setLatitude(41.624556);
        l5.setLongitude(0.651703);
        Location l6 = new Location("");
        l6.setLatitude(41.629651);
        l6.setLongitude(0.606040);
        listReservas.add(new Reserva(c1, new Date().getTime(),cocheprueba, 90.5,l));
        listReservas.add(new Reserva(c2, new Date().getTime(), motoprueba, 20,l2));
        listReservas.add(new Reserva(c3,  new Date().getTime(),biciprueba, 10.99,l3));
        listReservas.add(new Reserva(c1,  new Date().getTime()+day,cocheprueba, 102.5,l4));
        listReservas.add(new Reserva(c2,new Date().getTime()+day*2,motoprueba, 19.95,l5));
        listReservas.add(new Reserva(c3, new Date().getTime()+day*3,biciprueba, 9.5,l6));
        return listReservas;
    }
}
