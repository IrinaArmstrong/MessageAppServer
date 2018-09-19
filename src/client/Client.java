package client;

import java.io.*;
import java.net.Socket;

/*
*
*/

public class Client implements Runnable {

    // Переменная, содержащая сокет клиента
    static Socket socket;

    // Конструктор класса клиента
    public Client() {
        try {
            // Создаём сокет общения на стороне клиента по адресу localhost и порту 3345
            socket = new Socket("localhost", 3345);
            System.out.println("Client connected to socket");
            Thread.sleep(2000);
        }
        catch (Exception e) {
            System.err.println("Mistakes in initializing connection to Server from Client: " + e.getMessage());
            System.err.println("StackTrace: ");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            // Cоздаём объект для считывания консоли клиента
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // Cоздаём объекты для записи строк в данный созданный сокет, для чтения строк от сервера
            DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
            DataInputStream ois = new DataInputStream(socket.getInputStream());
            System.out.println("Client oos & ois initialized");

            // Проверяем открыт ли канал
            while(!socket.isOutputShutdown()){

                // Ждём появления данных в консоли клиента
                if(br.ready()){

                    // Если данные появились, то обрабатываем их
                    System.out.println("Client started writing in channel...");
                    Thread.sleep(1000);
                    String clientCommand = br.readLine();

                    // Пишем данные с консоли в канал сокета для сервера
                    oos.writeUTF(clientCommand); // todo Вставить сюда кодирование!
                    oos.flush();
                    System.out.println("Clien sent message: " + clientCommand + " to server.");
                    // Ждём чтобы сервер успел прочесть сообщение из сокета и ответить
                    Thread.sleep(1000);

                    // Проверка условия продолжения работы с сервером по этому сокету.
                    // Ищем кодовое слово quit
                    if(clientCommand.equalsIgnoreCase("quit")){

                        // Если кодовое слово получено, то инициализируем закрытие потока
                        System.out.println("Client close connections...");
                        Thread.sleep(2000);

                        // Смотрим что нам ответил сервер на последок перед закрытием ресурсов
                        if(ois.read() > -1)     {
                            System.out.println("Server answers before closing...");
                            String in = ois.readUTF(); // todo Читать по-другому, тк будет кодирование
                            System.out.println(in);
                        }

                        // Выходим из цикла записи чтения
                        break;
                    }
                }
            }

        }
        catch (IOException e) {
            System.err.println("Mistakes in Client: " + e.getMessage());
            System.err.println("StackTrace: ");
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            System.err.println("Mistakes in Client: " + e.getMessage());
            System.err.println("StackTrace: ");
            e.printStackTrace();
        }
    }

}
