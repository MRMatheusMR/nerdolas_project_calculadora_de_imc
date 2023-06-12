import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            System.out.println("Conectado ao servidor.");

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            Scanner scanner = new Scanner(System.in);
            String message;

            do {
                System.out.println("-----------------------------------------");
                System.out.println("1 - Cadastrar Usuario");
                System.out.println("2 - Inserir Altura");
                System.out.println("3 - Inserir o Peso");
                System.out.println("4 - Inserir a Idade");
                System.out.println("5 - Consultar o IMC");
                System.out.println("-----------------------------------------");
                System.out.println("Digite a funcionalidade desejada (1 a 5):");
                message = scanner.nextLine();

                if (message.equals("1")) {
                    System.out.println("Digite o nome de usuário e a senha separados por vírgula:");
                    String[] parts = scanner.nextLine().split(",");
                    message += "," + parts[0] + "," + parts[1];
                } else if (message.equals("2")) {
                    System.out.println("Digite o nome de usuário e a altura separados por vírgula:");
                    String[] parts = scanner.nextLine().split(",");
                    message += "," + parts[0] + "," + parts[1];
                } else if (message.equals("3")) {
                    System.out.println("Digite o nome de usuário e o peso separados por vírgula:");
                    String[] parts = scanner.nextLine().split(",");
                    message += "," + parts[0] + "," + parts[1];
                } else if (message.equals("4")) {
                    System.out.println("Digite o nome de usuário e a idade separados por vírgula:");
                    String[] parts = scanner.nextLine().split(",");
                    message += "," + parts[0] + "," + parts[1];
                } else if (message.equals("5")) {
                    System.out.println("Digite o nome de usuário:");
                    String usuario = scanner.nextLine();
                    message += "," + usuario;
                }

                outputStream.write(message.getBytes());
                outputStream.flush();

                byte[] buffer = new byte[1024];
                int bytesRead = inputStream.read(buffer);

                String response = new String(buffer, 0, bytesRead);
                System.out.println("Resposta do servidor: " + response);

            } while (!message.equals("exit"));

            socket.close();
            System.out.println("Conexão encerrada.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
