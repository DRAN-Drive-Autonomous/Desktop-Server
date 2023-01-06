package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

public class Server {
	static ServerSocket ss;
	static Socket clientSocket;
	
	public static void startServer1(int portNum) {
        try {
        	System.out.println("Server started");
            ss = new ServerSocket(portNum);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static String startServer2() {
		String data = "";
        try {
            System.out.println("Server is waiting for client!");
            clientSocket = ss.accept();
            System.out.println("Connection created");

            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(),StandardCharsets.UTF_8)));
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            data = br.readLine();
            String hostname = data;
            out.println(hostname);
            out.flush();
            
            data = br.readLine();
            String ip = data;
            out.println(ip);
            out.flush();
    
            data = br.readLine();
            String ipu = data;
            out.println(ipu);
            out.flush();
            
            data = br.readLine();
            String port = data;
            out.println(port);
            out.flush();
            
            data = String.format("<b style='text-align:center;'>CLIENT</b><br><br><br>Host Name: %s<br><br>IP Addresses:<br>1) Used to connect: %s<br>2) Client Local: %s<br><br>Port Number used to connect: %s", hostname, ipu, ip, port);
//            System.out.println("Client says: "+ data);
//            ss.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(data);
        return data;
    }
	
	public static void stopServer() throws IOException {
		ss.close();
	}

    public static void getIPAddresses() throws Exception {
        Integer count = 0;
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            // drop inactive
            if (!networkInterface.isUp())
                continue;

            // smth we can explore
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while(addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                count++;
                System.out.println(String.format("%2d) NetInterface: Name: %s; IP Address: %s",
                        count, networkInterface.getDisplayName(), addr.getHostAddress()));
            }
        }
    }
    
    public String getINetworkDetailsHTML() throws Exception {
    	InetAddress serverIP = InetAddress.getLocalHost();
    	String networkInterfaceHTML = "<b style='text-align:center;'>SERVER</b><br><br><br>Host Name (local connection): "+serverIP.getHostName();
        Integer count = 0;
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        networkInterfaceHTML += "<br><br>Network Interfaces:";

        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            // drop inactive
            if (!networkInterface.isUp())
                continue;

            // smth we can explore
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while(addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                count++;
                networkInterfaceHTML += String.format("<br>%2d)%s - %s",
                        count, networkInterface.getDisplayName(), addr.getHostAddress());
            }
        }
        return networkInterfaceHTML;
    }
}
