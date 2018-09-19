
/*
*
*/

import client.Client;
import server.MultiThreadServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageAppMain {

    /*public static void main(String[] args) throws IOException, InterruptedException {

        MultiThreadServer multiThreadServer = new MultiThreadServer();
        multiThreadServer.main();

        //System.out.println("Number of clients: ");
        // todo Р’СЃС‚Р°РІРёС‚СЊ РІС‹Р±РѕСЂ РєРѕР»РёС‡РµСЃС‚РІР° РєР»РёРµРЅС‚РѕРІ
        int clientsNumber = 2;

        // РЎРѕР·РґР°РµРј РїСѓР» РїРѕС‚РѕРєРѕРІ РїРѕ РєРѕР»РёС‡РµСЃС‚РІСѓ РєР»РёРµРЅС‚РѕРІ
        ExecutorService exec = Executors.newFixedThreadPool(clientsNumber);

        // Р�РЅРёС†РёР°Р»РёР·РёСЂСѓРµРј РєР»РёРµРЅС‚РѕРІ

        ArrayList<Client> clients = new ArrayList<>();
        for (int i = 0; i < clientsNumber; i++)  {
            clients.add(new Client());
        }

        // Р�Р»Рё
        for (int i = 0; i < clientsNumber; i++)  {
            exec.execute(new Client());
        }

        // Р—Р°РєСЂС‹РІР°РµРј СЌРєР·РµРєСЊСЋС‚РµСЂ
        exec.shutdown();
    }*/

}
