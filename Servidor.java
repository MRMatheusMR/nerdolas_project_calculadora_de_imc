import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Servidor {
    // Mapeamento sobre as informações
    private static Map<String, String> usuarios = new HashMap<>();
    private static Map<String, Double> alturas = new HashMap<>();
    private static Map<String, Double> pesos = new HashMap<>();
    private static Map<String, Integer> idades = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234); // Inicia o servidor
            System.out.println("Servidor iniciado. Aguardando conexões...");

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Aceita conexao 
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress()); // Obtem o ip e faz um print de conectado

                Thread clientThread = new Thread(() -> {
                    try {
                        InputStream inputStream = clientSocket.getInputStream();
                        OutputStream outputStream = clientSocket.getOutputStream();

                        byte[] buffer = new byte[1024];
                        int bytesRead;

                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            String message = new String(buffer, 0, bytesRead); // define a mensagem como o que o cliente enviar
                            System.out.println("Mensagem recebida do cliente: " + message);

                            // Realizar ação com base na mensagem recebida
                            String response = processMessage(message);

                            outputStream.write(response.getBytes());
                            outputStream.flush();
                        }

                        clientSocket.close();
                        System.out.println("Cliente desconectado: " + clientSocket.getInetAddress());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                clientThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processMessage(String message) {
        String[] parts = message.split(",");
        String funcionalidade = parts[0];

        // Mapear a mensagem para uma funcionalidade funcional
        Function<String[], String> functionality = switch (funcionalidade) {
            case "1" -> Servidor::cadastrarUsuario;
            case "2" -> Servidor::inserirAltura;
            case "3" -> Servidor::inserirPeso;
            case "4" -> Servidor::inserirIdade;
            case "5" -> Servidor::calcularIMC;
            default -> Servidor::mensagemInvalida;
        };

        // Executar a funcionalidade e retornar o resultado
        return functionality.apply(parts);
    }

    private static String cadastrarUsuario(String[] parts) {
        if (parts.length != 3) {
            return "Formato inválido. Use: 1,usuario,senha";
        }

        String usuario = parts[1];
        String senha = parts[2];

        if (usuarios.containsKey(usuario)) {
            return "Usuário já cadastrado.";
        }

        usuarios.put(usuario, senha);
        return "Usuário cadastrado com sucesso.";
    }

    private static String inserirAltura(String[] parts) {
        if (parts.length != 3) {
            return "Formato inválido. Use: 2,usuario,altura";
        }

        String usuario = parts[1];
        double altura;

        try {
            altura = Double.parseDouble(parts[2]);
        } catch (NumberFormatException e) {
            return "Altura inválida.";
        }

        alturas.put(usuario, altura);
        return "Altura inserida com sucesso.";
    }

    private static String inserirPeso(String[] parts) {
        if (parts.length != 3) {
            return "Formato inválido. Use: 3,usuario,peso";
        }

        String usuario = parts[1];
        double peso;

        try {
            peso = Double.parseDouble(parts[2]);
        } catch (NumberFormatException e) {
            return "Peso inválido.";
        }

        pesos.put(usuario, peso);
        return "Peso inserido com sucesso.";
    }

    private static String inserirIdade(String[] parts) {
        if (parts.length != 3) {
            return "Formato inválido. Use: 4,usuario,idade";
        }

        String usuario = parts[1];
        int idade;

        try {
            idade = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return "Idade inválida.";
        }

        idades.put(usuario, idade);
        return "Idade inserida com sucesso.";
    }

    private static String calcularIMC(String[] parts) {
        if (parts.length != 2) {
            return "Formato inválido. Use: 5,usuario";
        }

        String usuario = parts[1];

        if (!usuarios.containsKey(usuario) || !alturas.containsKey(usuario) || !pesos.containsKey(usuario)) {
            return "Usuário não encontrado ou dados incompletos.";
        }

        double altura = alturas.get(usuario);
        double peso = pesos.get(usuario);

        double imc = peso / (altura * altura);
        String imcFormatado = String.format("%.2f", imc);

        return "O IMC do usuário " + usuario + " é: " + imcFormatado + " % ";
    }

    private static String mensagemInvalida(String[] parts) {
        return "Mensagem inválida.";
    }
}
