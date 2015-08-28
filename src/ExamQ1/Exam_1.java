/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExamQ1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LukaszKrawczyk
 */
public class Exam_1 {

    public static void main(String[] args) throws IOException {

        Even e1 = new Even();
        String ip = "localhost";
        int port = 4444;
        int id = 0;
        HashMap<Integer, Exam_1> stiles = new HashMap();

        if (args.length == 2) {
            System.out.println("Args found -> Setting IP and PORT");
            ip = args[0];
            port = Integer.parseInt(args[1]);
        }

        for (Map.Entry<Integer, Exam_1> entrySet : stiles.entrySet()) {
            Integer key = entrySet.getKey();
            Exam_1 value = entrySet.getValue();

            if (key == id) {
                id++;
            }
        }

        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress(ip, port));

        while (true) {
            TSThread ts = new TSThread(ss.accept(), id, e1);
            Thread t1 = new Thread(ts);
            t1.start();
            id++;
        }
    }

    public static class TSThread implements Runnable {

        Socket s;
        BufferedReader in;
        PrintWriter out;
        Even even;
        int turnStile_Id;
        long tsNumber = 0;
        HashMap<Integer, Exam_1> stiles;

        public TSThread(Socket socket, int id, Even even) {
            s = socket;
            turnStile_Id = id;
            this.even = even;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(s.getOutputStream(), true);

                out.println("TURNSTILE-" + turnStile_Id);

                while (true) {
                    String temp = in.readLine().toLowerCase();
                    switch (temp) {
                        case "pass":
                            tsNumber++;
                            even.setVisitorsNumber();

                            out.println("Number from this turnStile: " + tsNumber);
                            break;
                        case "total":
                            out.println("Total visitors: " + even.getVisitorsNumber());
                            break;
                        default:
                            out.println("Command not found");

                    }
                    if (in.readLine().equalsIgnoreCase("pass")) {
                        tsNumber++;
                        even.setVisitorsNumber();
                    }

                }

            } catch (IOException ex) {
            }
        }
    }

    public static class Even {

        long visitorsNumber = 0;

        public synchronized long getVisitorsNumber() {
            return visitorsNumber;
        }

        public synchronized void setVisitorsNumber() {
            visitorsNumber++;
        }
    }
}
