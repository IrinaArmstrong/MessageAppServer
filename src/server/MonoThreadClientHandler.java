package server;

/*
*
*/

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Exchanger;

public class MonoThreadClientHandler implements Runnable {

    // Переменная, содержащая сокет текущего клиента
    private static Socket clientDialog;

    Exchanger<String> exchanger;
    String message;

    // Конструктор класса, принимает в качестве параметра сокет клиента
    public MonoThreadClientHandler(Socket client, Exchanger<String> ex) {
        this.exchanger = ex;
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

                if (MultiThreadServer.entry != null) {
                    System.out.println("One new message on server...");
                    System.out.println("Message:" + MultiThreadServer.entry);
                    out.writeUTF(MultiThreadServer.entry);
                    MultiThreadServer.entry = null;
                }
                else {


                    System.out.println("Server reading from channel...");

                    // Сервер ждёт в канале чтения (in) получения данных от клиента. После получения, считывает их
                    // todo Читать по-другому, тк будет кодирование
                    String my_entry = in.readUTF();

                    // Выводим в консоль
                    System.out.println("Got on server message: " + my_entry);
                    MultiThreadServer.entry = my_entry;

                    //message = my_entry;
                /*if (message == null)  message = exchanger.exchange(new String());
                else message = exchanger.exchange(my_entry);
                System.out.println("Client got message:  " + message);*/

                    // Проверка условия продолжения работы с клиентом по этому сокету.
                    // Ищем кодовое слово quit
                    // todo Надо как-то по другому обрывать диалог - как?
                /*if (my_entry.equalsIgnoreCase("quit")) {

                    // Если кодовое слово получено, то инициализируем закрытие потока
                    System.out.println("Client initialize connection's closing...");
                    out.writeUTF("Server reply - " + my_entry + " - OK");
                    Thread.sleep(3000);
                    break; // fixme моветон!
                }

                // Если слова quit для окончания работы не поступало, то продолжаем работу
                System.out.println("Server is trying to write to channel..");
                out.writeUTF("Server reply - " + my_entry + " - OK");
                System.out.println("Server wrote message to client");*/
                    Thread.sleep(3000);
                    out.flush();


                    // Освобождаем буфер сетевых сообщений
                    //out.flush();

                    // Возвращаемся в начало для считывания нового сообщения
                }
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
