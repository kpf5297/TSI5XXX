import java.io.*;
import java.net.Socket;

public class TSI5XXXFlowMeter {
    private String ipAddress;
    private int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public TSI5XXXFlowMeter(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public boolean connect() {
        try {
            socket = new Socket(ipAddress, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void disconnect() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendCommand(String command) {
        if (out == null || in == null) {
            return "Not connected to the flow meter.";
        }
    
        out.println(command);

        // delay 100ms
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        StringBuilder response = new StringBuilder();
        try {
            int character;
            while ((character = in.read()) != -1) {
                char c = (char) character;
                if (c == '\r') {
                    break;
                }
                response.append(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error while receiving data.";
        }


            


        return response.toString().trim();
    }
    

    // public String sendCommand(String command) {
    //     if (out == null || in == null) {
    //         return "Not connected to the flow meter.";
    //     }

    //     out.println(command);

    //     // delay 100ms
    //     try {
    //         Thread.sleep(100);
    //     } catch (InterruptedException e) {
    //         e.printStackTrace();
    //     }

    //     StringBuilder response = new StringBuilder();
    //     try {
    //         String line;

    //         // if ((line = in.readLine()) != null) {
    //         //     response.append(line).append("\n");
    //         // }

    //         while ((line = in.readLine()) != null) {


    //             // Assuming LF is ignored, so we look for CR
    //             // if (line.equals("\n") || line.equals("\r") || line.equals("\r\n")) {
                    
    //                 // print the response
    //                 System.out.println(line);

    //             //     break;
    //             // }
    //             if (line.equals("\r")) {
    //                 break;
    //             }

    //             response.append(line).append("\n");
    //         }

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         return "Error while receiving data.";
    //     }
    //     // return response.toString().trim();
    //     return response.toString();
    // }

    public static void main(String[] args) {
        TSI5XXXFlowMeter flowMeter = new TSI5XXXFlowMeter("169.254.84.189", 3607);
        if (flowMeter.connect()) {
            // Example commands


            for (int i = 0; i < 100; i++) {
                String response = flowMeter.sendCommand("DCFTPHLI0001");
                System.out.println("Response: " + response);

                // // delay 100ms
                // try {
                //     Thread.sleep(50);
                // } catch (InterruptedException e) {
                //     e.printStackTrace();
                // }
            }
            // String response2 = flowMeter.sendCommand("DCFTPHLI0001");
            // System.out.println("Response 2: " + response2);


            flowMeter.disconnect();
        } else {
            System.out.println("Failed to connect to the flow meter.");
        }
    }
}
