import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;


public class ConversorApp {
    private static final
    Scanner sc = new Scanner(System.in).useLocale(Locale.forLanguageTag("es-ES"));

    private double GestorApi(String moneda, String monedaDestino) throws IOException, InterruptedException {
        String clave = "8964cadb96cda9a90752145b";
        String url = "https://v6.exchangerate-api.com/v6/" + clave + "/latest/" + moneda;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);


        JsonObject rates = jsonObject.getAsJsonObject("conversion_rates");


        double valor = rates.get(monedaDestino).getAsDouble();

        return valor;
    }

    public double Conversor(double cantidad, String monedaBase, String monedaDestino) {
        double resultado = 0;
        try {
            ConversorApp ca = new ConversorApp();
            double tasaDeCambio = ca.GestorApi(monedaBase, monedaDestino);
            resultado = cantidad * tasaDeCambio;
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al consultar la API: " + e.getMessage());
        }
        return resultado;
    }

    private static int mostarMenu() {
        int opcion = 0;
        boolean validInput = false;
        System.out.println("""
                ========================================
                | BIENVENID@ al conversor de monedas =)|
                ========================================
                |seleccione una de las siguientes      |
                |opciones:                             |
                |(1) Dolar -> Pesos Colombianos        |
                |(2) Pesos Colombianos -> Dolar        |
                |(3) Dolar -> Real Brasileño           |
                |(4) Real Brasileño -> Dolar           |
                |(5) Dolar -> Pesos Chilenos           |
                |(6) Pesos Chilenos -> Dolar           |
                |(7) salir del sistema                 |
                ========================================
                |   desarrollado by pablo Mejia <3     |
                ========================================
                """);
        while (!validInput) {
            try {
                System.out.print("Ingrese una opcion: ");
                opcion = sc.nextInt();

                if (opcion > 0) {
                    validInput = true;
                } else {
                    System.out.println("Error: La opcion debe ser mayor que 0.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un numero entero.");
                sc.nextLine();
            }
        }
        return opcion;
    }

    public void procesoDeConvercion(String monedaBase, String monedaDestino) {
        ConversorApp capp = new ConversorApp();
        try {
            System.out.println(monedaBase + " -> " + monedaDestino);
            System.out.print("ingrese su monto a convertir: ");
            double cantida = sc.nextDouble();
            if (cantida > 0) {
                double r = capp.Conversor(cantida, monedaBase, monedaDestino);
                System.out.println("el valor de " + cantida + " " + monedaBase + " corresponde a -> " + r + " " +monedaDestino);
            } else {
                System.out.println("solo se permiten valores positivos :( \nvuelva a intentar hacer el proceso");
            }
        } catch (InputMismatchException e) {
            System.out.println("solo se permiten valores numericos \nvuelva a intentar hacer el proceso");
            sc.nextLine();
        }

    }


    public static void main(String[] args) {
        ConversorApp ca = new ConversorApp();
        while (true) {
            switch (mostarMenu()) {
                case 1 -> {
                    ca.procesoDeConvercion("USD", "COP");
                }
                case 2 -> {
                    ca.procesoDeConvercion("COP", "USD");
                }
                case 3 -> {
                    ca.procesoDeConvercion("USD", "BRL");
                }
                case 4 -> {
                    ca.procesoDeConvercion("BRL", "USD");
                }
                case 5 -> {
                    ca.procesoDeConvercion("USD", "CLP");
                }
                case 6 -> {
                    ca.procesoDeConvercion("CLP", "USD");
                }
                case 7 -> {
                    System.out.println("Gracias por usar nuestro sistema");
                    System.exit(0);
                }
                default -> {
                    System.out.println("Estimado Usuari@ \nporfavor ingrese una opcion valida en el sistema");
                }
            }

        }
    }


}
