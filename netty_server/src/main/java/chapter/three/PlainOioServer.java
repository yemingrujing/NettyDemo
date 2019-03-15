package chapter.three;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * 未使用 Netty 的阻塞网络编程
 *
 * @author Wei.Guang
 * @create 2019-03-15 11:09
 **/
public class PlainOioServer {

    public void server(int port) throws IOException {
        // 将服务器绑定到指定端口
        final ServerSocket socket = new ServerSocket(port);
        try {
            for(;;) {
                // 接收连接
                final Socket clientSocket = socket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OutputStream out;
                        try {
                            out = clientSocket.getOutputStream();
                            out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));
                            out.flush();
                            // 关闭连接
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                clientSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
