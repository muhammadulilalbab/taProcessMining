/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritma;

import DB.ConnectDB;
import javax.swing.JApplet;
import java.awt.*;
import java.awt.geom.*;
import com.mysql.jdbc.Driver;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

import org.jgraph.*;
import org.jgraph.graph.*;

import org.jgrapht.*;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;

// resolve ambiguity
import org.jgrapht.graph.DefaultEdge;

/**
 *
 * @author Ulil
 */
public class AgregatingGraph extends JApplet {

    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    private static final long serialVersionUID = 3256444702936019250L;
    private static final Color DEFAULT_BG_COLOR = Color.decode("#FFFFFF");
    private static final Dimension DEFAULT_SIZE = new Dimension(1280, 760);
    private JGraphModelAdapter<String, DefaultEdge> jgAdapter;
    ConnectDB con = new ConnectDB();
    ConnectDB cn2 = new ConnectDB();
    ConnectDB cn3 = new ConnectDB();
    Statement stat;
    Statement stat2;
    public Statement statement4;
    public Statement statement2;
    public Statement statement3;
      public AgregatingGraph() throws SQLException{
       
       
       
    }
    public void init() {
        con.getKoneksi();
        String jmlnode1 = "A";
        String jmlnode2 = "B";
        try {
            stat = (Statement) con.koneksi.createStatement();
            stat2 = (Statement) con.koneksi.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(BuildingSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            // TODO start asynchronous download of heavy resources
            
            ListenableGraph<String, DefaultEdge> g =
            new ListenableDirectedMultigraph<String, DefaultEdge>(DefaultEdge.class);

        // create a visualization using JGraph, via an adapter
            jgAdapter = new JGraphModelAdapter<String, DefaultEdge>(g);

            JGraph jgraph = new JGraph(jgAdapter);

            adjustDisplaySettings(jgraph);
            getContentPane().add(jgraph);
            resize(DEFAULT_SIZE);
            String  cekNode = "Select node,jumlah from node_table";
            stat.executeQuery(cekNode);
            ResultSet res = stat.getResultSet();
            int i = 0;
            while (res.next()){
                String node = res.getString("node");
                String jml = res.getString("jumlah");
                g.addVertex(node);
                positionVertexAt(node, 50, 100);
                i++;
            }
            res.close();
            
            String  cekEdge = "Select edge_1, edge_2,jumlah from edge_table";
            stat.executeQuery(cekEdge);
            ResultSet edg = stat.getResultSet();
            while (edg.next()){
                String edge1 = edg.getString("edge_1");
                String edge2 = edg.getString("edge_2");
                int jmlEdge = edg.getInt("jumlah");
                double jml = (double) jmlEdge;
                String  cekJmlNode1 = "Select node,jumlah from node_table where node=\""+edge1+"\"";
                stat2.executeQuery(cekJmlNode1);
                ResultSet resnode1 = stat2.getResultSet();
                if (resnode1.next())
                {
                    jmlnode1 = resnode1.getString("jumlah");
                }
                
                String  cekJmlNode2 = "Select node,jumlah from node_table where node=\""+edge2+"\"";
                stat2.executeQuery(cekJmlNode2);
                ResultSet resnode2 = stat2.getResultSet();
                if (resnode2.next()){
                    jmlnode2 = resnode2.getString("jumlah");
                }
                String aaa= edge1;
                String bbb=  edge2;
                /*System.out.println(aaa);
                System.out.println(bbb);*/
                
                g.addEdge(edge1, edge2);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(AgregatingGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // TODO overwrite start(), stop() and destroy() methods
   
    private void adjustDisplaySettings(JGraph jg)
    {
        jg.setPreferredSize(DEFAULT_SIZE);

        Color c = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            colorStr = getParameter("bgcolor");
        } catch (Exception e) {
        }

        if (colorStr != null) {
            c = Color.decode(colorStr);
        }

        jg.setBackground(c);
    }

    @SuppressWarnings("unchecked") // FIXME hb 28-nov-05: See FIXME below
    private void positionVertexAt(Object vertex, int x, int y)
    {
        DefaultGraphCell cell = jgAdapter.getVertexCell(vertex);
        AttributeMap attr = cell.getAttributes();
        Rectangle2D bounds = GraphConstants.getBounds(attr);

        Rectangle2D newBounds =
            new Rectangle2D.Double(
                x,
                y,
                bounds.getWidth(),
                bounds.getHeight());

        GraphConstants.setBounds(attr, newBounds);

        // TODO: Clean up generics once JGraph goes generic
        AttributeMap cellAttr = new AttributeMap();
        cellAttr.put(cell, attr);
        jgAdapter.edit(cellAttr, null, null, null);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * a listenable directed multigraph that allows loops and parallel edges.
     */
    private static class ListenableDirectedMultigraph<V, E>
        extends DefaultListenableGraph<V, E>
        implements DirectedGraph<V, E>
    {
        private static final long serialVersionUID = 1L;

        ListenableDirectedMultigraph(Class<E> edgeClass)
        {
            super(new DirectedMultigraph<V, E>(edgeClass));
        }
    }
}
