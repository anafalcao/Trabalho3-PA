package eel418.ajaxcombibliotecajson;

import java.io.BufferedWriter;
import java.io.IOException;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mvc.modelo.AcessoBD;

public class Leitura extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");

        String medidor = request.getParameter("medidor");
        String temperatura = request.getParameter("temperatura");
        String umidade = request.getParameter("umidade");
        String datahora = request.getParameter("datahora");
        String serial = request.getParameter("serial");
        
        String resultado = new AcessoBD().guardar(
                                            medidor,
                                            temperatura,
                                            umidade,
                                            datahora,
                                            serial);

        BufferedWriter out = new BufferedWriter(response.getWriter());
        out.write("Resultado: " +  resultado);
        out.flush();
        
        JsonObjectBuilder obj = Json.createObjectBuilder();
        obj.add("datahora", datahora);
        obj.add("temperatura",temperatura);
        obj.add("umidade", umidade);
        obj.add("medidor", medidor);
        
        if (ValoresSocket.session != null) {
            ValoresSocket.session.getBasicRemote().sendText(obj.build().toString());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
