package com.example.rentaride;

public class Utils {
    public static final int COCHE = 0, MOTOCICLETA = 1, BICICLETA = 2, VIEW_TYPE_MESSAGE_SENT = 3, VIEW_TYPE_MESSAGE_RECEIVED = 4;

    public static final Vehiculo cocheprueba = new Vehiculo(COCHE, "Tesla S", "2019", "Color blanco", "636666663", "3874FBD","600 cv", "Eléctrico", "https://fotografias.lasexta.com/clipping/cmsimages01/2018/02/28/1728D227-6B90-470B-8300-20C5EC7B4F9C/58.jpg",false);
    public static final Vehiculo motoprueba = new Vehiculo(MOTOCICLETA, "Kawasaki Z800", "2020", "Color negro mate", "666626663", "1174FPD","120 cv", "Gasolina", "https://img.clasf.es/2019/09/17/Kawasaki-Z800-PERFORMANCE-20190917155352.4997120015.jpg");
    public static final Vehiculo biciprueba = new Vehiculo(BICICLETA, "Mondraker", "2017", "Bicicleta profesional de montaña", "666566663", "https://img.milanuncios.com/fg/3084/39/308439057_8.jpg?VersionId=JGuACnbtRxJ48FhiHQE3GkJPKJwJjmgi");
}
