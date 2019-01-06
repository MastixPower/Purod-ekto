import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server{
    public static int serverPort=10001;
    static public List<Thread> list=new ArrayList<>();
    static int number=0;
    public static void main(String[] args) {
        ServerSocket ss= null;
        try {
            ss = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            try {
                Socket clientSocket= ss.accept();
                System.out.println("Connection correct");
                //System.out.println(this.getId());
                ServerThread st= new ServerThread(clientSocket);
                list.add(st);
                System.out.println(list+"\n"+list.size());
                st.start();
            }catch (IOException e){
                e.printStackTrace();
                System.out.println("Client Socket Error");
            }
        }

    }
    public static void getInt(int x){
        Server.number=x;
    }
}

class ServerThread extends Thread{

    String line=null;
    BufferedReader  is = null;
    PrintWriter os=null;
    PrintWriter ss=null;
    static Socket s=null;
    public ServerThread(Socket s){
        this.s=s;
    }
    public void run() {
        String x=""+(this.getId()-11);
        String directory="D:\\TORrent_"+x;

        //System.out.println(directory);
        File file =new File(directory);
        file.mkdir();

        System.out.println(file.getAbsolutePath());
        File textFile=new File(file,"TXT_"+x+".txt");
        try {
            textFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            is= new BufferedReader(new InputStreamReader(s.getInputStream()));
            os=new PrintWriter(s.getOutputStream());

        }catch(IOException e){
            System.out.println("IO error in server thread");
        }

        try {
            line=is.readLine();
            while(line.compareTo("QUIT")!=0){

                os.println(line);
                os.flush();
                System.out.println("Response to Client  :  "+line+"\n");

                if(line.compareTo("GetAList")==0){

                    for(int i=0;i<Server.list.size();i++) {
                        System.out.println(Server.list.get(i)+"\n");
                    }
                }else if (line.compareTo("upload") ==0)
                {
                    line=is.readLine();
                    int tmp=Integer.parseInt(line);
                    saveFile(s,tmp);
                    System.out.println("dupa");
                    os.println(line);
                    os.flush();
                }if (line.compareTo("22") ==0)
                {try {
                    System.out.println("22");
                    line = is.readLine();
                    int tmp = Integer.parseInt(line);
                    saveFile(s, tmp);
                    System.out.println("dupa");
                    line = "Server Downloaded File";
                } catch (IOException e)
                {
                    e.printStackTrace();
                    System.out.println("download error");
                }
                }else line=is.readLine();



            }

        } catch (IOException e) {

            line=this.getName(); //reused String line for getting thread name
            System.out.println("IO Error/ Client "+line+" terminated abruptly");
        }
        catch(NullPointerException e){
            line=this.getName(); //reused String line for getting thread name
            System.out.println("Client "+line+" Closed");
        }

        finally{
            try{
                System.out.println("Connection Closing..");
                if (is!=null){
                    is.close();
                    System.out.println(" Socket Input Stream Closed");
                }

                if(os!=null){
                    os.close();
                    System.out.println("Socket Out Closed");
                }
                if (s!=null){
                    s.close();
                    System.out.println("Socket Closed");
                }

            }
            catch(IOException ie){
                System.out.println("Socket Close Error");
            }
        }//end finally
    }

        void saveFile(Socket clientSock,int tmp) throws IOException {
            DataInputStream dis = new DataInputStream(clientSock.getInputStream());
            FileOutputStream fos = new FileOutputStream("testfile.jpg");
            byte[] buffer = new byte[4096];
            int filesize = tmp; // Send file size in separate msg\
            int read = 0;
            int totalRead = 0;
            int remaining = filesize;
            while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {

                totalRead += read;
                remaining -= read;
                fos.write(buffer, 0, read);
            }
            System.out.println("read " + totalRead + " bytes.");
            os.println("read"+ totalRead+ "bytes.");
    }

}

