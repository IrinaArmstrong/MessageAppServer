package server;

/*
*
*/

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MonoThreadClientHandler implements Runnable {

    // Переменная, содержащая сокет текущего клиента
    private static Socket clientDialog;

    // Конструктор класса, принимает в качестве параметра сокет клиента
    public MonoThreadClientHandler(Socket client) {

        MonoThreadClientHandler.clientDialog = client;

    }

    @Override
    public void run() {

        try {
            // Инициируем каналы общения в сокете для сервера

            // Инициируем канал записи в серверный сокет
            DataOutputStream out = new DataOutputStream(clientDialog.getOutputStream());
            System.out.println("DataOutputStream created");

            // Инициируем канал чтения из серверного сокета
            DataInputStream in = new DataInputStream(clientDialog.getInputStream());
            System.out.println("DataInputStream created");

            // Начинаем диалог с подключенным клиентом в цикле, пока сокет не будет закрыт клиентом
            while (!clientDialog.isClosed()) {

                System.out.println("Server reading from channel...");

                // Сервер ждёт в канале чтения (in) получения данных от клиента. После получения, считывает их
                // todo Читать по-другому, тк будет кодирование
                String entry = in.readUTF();

                // Выводим в консоль
                System.out.println("Got on server message: " + entry);

                // Проверка условия продолжения работы с клиентом по этому сокету.
                // Ищем кодовое слово quit
                // todo Надо как-то по другому обрывать диалог - как?
                if (entry.equalsIgnoreCase("quit")) {

                    // Если кодовое слово получено, то инициализируем закрытие потока
                    System.out.println("Client initialize connection's closing...");
                    out.writeUTF("Server reply - " + entry + " - OK");
                    Thread.sleep(3000);
                    break; // fixme моветон!
                }

                // Если слова quit для окончания работы не поступало, то продолжаем работу
                System.out.println("Server is trying to write to channel..");
                out.writeUTF("Server reply - " + entry + " - OK");
                System.out.println("Server wrote message to client");

                // Освобождаем буфер сетевых сообщений
                out.flush();

                // Возвращаемся в начало для считывания нового сообщения
            }

            // Если кодовое слово quit получено, то закрываем соединения
            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels...");

            // Закрываем сначала каналы сокета
            in.close();
            out.close();

            // Закрываем сокет общения с клиентом в потоке моносервера
            clientDialog.close();

            System.out.println("Closing connections & channels - DONE.");
        }
        catch (IOException e) {
            System.err.println("Mistakes in Mono Server: " + e.getMessage());
            System.err.println("StackTrace: ");
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            System.err.println("Mistakes in Mono Server: " + e.getMessage());
            System.err.println("StackTrace: ");
            e.printStackTrace();
        }
    }

}
