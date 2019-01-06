
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    static PrintWriter os=null;
    public static void main(String args[]) throws IOException{


        InetAddress address=InetAddress.getLocalHost();
        Socket s1=null;
        String line=null;
        BufferedReader br=null;
        BufferedReader is=null;

        PrintWriter ss=null;

        try {
            s1=new Socket(address, 10001); // You can use static final constant PORT_NUM
            br= new BufferedReader(new InputStreamReader(System.in));
            is=new BufferedReader(new InputStreamReader(s1.getInputStream()));
            os= new PrintWriter(s1.getOutputStream());
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        System.out.println("Client Address : "+address);
        System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

        String response=null;
        try{
            line=br.readLine();
            while(line.compareTo("QUIT")!=0) {
                if (line.compareTo("GetAList") == 0) {
                    os.println(line);
                    os.flush();
                    ss = new PrintWriter(s1.getOutputStream());
                    System.out.println(ss);
                } else if (line.compareTo("upload") ==0)
                {   System.out.println("Please Type Your File Directory:");
                    os.println(line);
                    os.flush();
                    line=br.readLine();
                    System.out.println(line);
                    int tmp=(int)(new File(line).length());
                    os.println(tmp);
                    os.flush();
                    String line2=line;
                    line=tmp+"";
                    os.println(line);
                    os.flush();
                    response = is.readLine();
                    System.out.println("Server Response : " + response);
                    sendFile(line2,s1);
                    line="Upload Done";

                }else if (line.compareTo("22") ==0)
                {
                    os.println(line);
                    os.flush();
                    line="C:\\Users\\MastixPower\\Desktop\\cat.jpg";
                    System.out.println(line);
                    int tmp=(int)(new File(line).length());
                    os.println(tmp);
                    os.flush();
                    response = is.readLine();
                    System.out.println("Server Response : " + response);
                    sendFile(line,s1);
                    line="Upload Done";
                    os.println(line);
                    os.flush();
                    response = is.readLine();
                    System.out.println("Server Response : " + response);

                }else{
                    os.println(line);
                    os.flush();
                    response = is.readLine();
                    System.out.println("Server Response : " + response);
                }

                line=br.readLine();
            }



        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("Socket read Error");
        }
        finally{

            is.close();os.close();br.close();s1.close();
            System.out.println("Connection Closed");

        }

    }

    static public void sendFile(String file, Socket s) throws IOException {
    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
    FileInputStream fis = new FileInputStream(file);
    int tmp=(int)(new File(file).length());
    int x=0;
    byte[] buffer = new byte[4096];
    //System.out.println(tmp);
    for(int i=0;i<tmp;i+=buffer.length)
    {
        x+=buffer.length;
        dos.write(buffer);
    }
        System.out.println(x);
    fis.close();
    dos.close();
}
}
