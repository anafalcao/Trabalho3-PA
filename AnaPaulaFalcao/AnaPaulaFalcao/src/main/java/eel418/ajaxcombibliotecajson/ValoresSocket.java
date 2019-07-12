/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eel418.ajaxcombibliotecajson;

import javax.servlet.http.HttpServlet;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/valoresSocket")
public class ValoresSocket extends HttpServlet {
    public static Session session;
    
    @OnOpen
    public void onOpen(Session session) {
        ValoresSocket.session = session;
        System.out.println("--- OnOpen");
    }

    @OnError
    public void onError(Session session,java.lang.Throwable throwable) {
        System.out.println("--- OnError: "+throwable);
    }

    @OnClose
    public void onClose(Session session,CloseReason reason) {
        System.out.println("--- OnClose: "+reason);
    }
}
