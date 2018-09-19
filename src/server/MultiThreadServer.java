package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
*
*/

public class MultiThreadServer {

    static ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {

        // Стартуем сервер на порту 3345, создаем и инициализируем переменную для обработки консольных команд с самого сервера
        try (ServerSocket server = new ServerSocket(3345);

             // Считываем консольные команды для сервера
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
             System.out.println("Server socket created. Command console reader is listening for commands...");

            // Проходим цикл до тех пор пока серверный сокет не закрыт
            while (!server.isClosed()) {

                // Если были ввделены команды из консоли сервера, то считываем поступившие комманды
                /*if (br.ready()) {
                    System.out.println("Main Server got messages in console...");

                    // Если команда - quit то инициализируем закрытие сервера и выходим из цикла
                    String serverCommand = br.readLine();
                    if (serverCommand.equalsIgnoreCase("quit")) {
                        System.out.println("Main Server is closing...");
                        server.close();
                        break; // fixme моветон!
                    }
                }*/

                // Если комманд от сервера нет то ожидаем подключения к сокету общения на серверной стороне
                Socket client = server.accept();

                // После получения запроса на подключение, сервер создаёт сокет
                // для общения с клиентом и отправляет его в отдельный поток Runnable
                // То есть объект MonoThreadClientHandler и тот продолжает общение от лица сервера
                executorService.execute(new MonoThreadClientHandler(client));
                System.out.print("Connection accepted.");
            }

            // Закрытие пула потоков после завершения работы всех до последнего
            executorService.shutdown();

        }
        catch (IOException e) {
            System.err.println("Mistakes in Main Server: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
