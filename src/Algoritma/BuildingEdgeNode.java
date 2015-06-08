/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritma;


import DB.ConnectDB;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.mysql.jdbc.Driver;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import java.util.Hashtable;
import object.*;

/**
 *
 * @author ADMIN
 */
public class BuildingEdgeNode 
{
    ConnectDB cn = new ConnectDB();
    ConnectDB cn2 = new ConnectDB();
    ConnectDB cn3 = new ConnectDB();
    
    Statement stt;
    Statement stt2;
    Statement stt3;
    
    ArrayList<sequence> seq = new ArrayList<sequence>();
    ArrayList<causal_helper> caus_helper= new ArrayList<causal_helper>();
    ArrayList<causal> caus = new ArrayList<causal>();
    ArrayList<node> nd = new ArrayList<node>();
    ArrayList<edge> eg = new ArrayList<edge>();
    
    public BuildingEdgeNode(ArrayList<causal> c, ArrayList<causal_helper> ch, ArrayList<sequence> s)
    {
        seq=s;
        caus=c;
        caus_helper=ch;
    }
    
    public boolean findNode(String n)
    {
        boolean hasil = false;
        for (int i=0; i<nd.size();i++)
        {
            if(nd.get(i).getNode().equalsIgnoreCase(n))
            {
                hasil=true;
                break;
            }
        }
        return hasil;
    }
    
    public boolean findEdge(String e1,String e2)
    {
        boolean hasil = false;
        for (int i=0; i<eg.size();i++)
        {
            if(eg.get(i).getEdge1().equalsIgnoreCase(e1) && eg.get(i).getEdge2().equalsIgnoreCase(e2))
            {
                hasil=true;
                break;
            }
        }
        return hasil;
    }
    
    public void updateNode(String n)
    {
        for (int i=0; i<nd.size();i++)
        {
            if (nd.get(i).getNode().equalsIgnoreCase(n))
            {
                int tempJml = nd.get(i).getJml()+1;
                nd.set(i,new node(n, tempJml));
                break;
            }
        }
    }
    
    public void updateEdge(String e1, String e2)
    {
        for (int i=0; i<eg.size();i++)
        {
            if(eg.get(i).getEdge1().equalsIgnoreCase(e1) && eg.get(i).getEdge2().equalsIgnoreCase(e2))
            {
                int tempJml = eg.get(i).getJml()+1;
                eg.set(i,new edge(e1,e2, tempJml));
                break;
            }
        }
    }
    
    public void save_db()
    {
        cn.getKoneksi();
        cn2.getKoneksi();
        try {
            stt = (Statement) cn.koneksi.createStatement();
            stt2= (Statement) cn.koneksi.createStatement();
            for(int i=0; i < nd.size(); i++){
                String cekNode = "Select Node from Node_table where node=\""+nd.get(i).getNode()+"\"";                    
                ResultSet rsss = stt2.executeQuery(cekNode);
                if (rsss.next())
                {
                    stt.executeUpdate("update node_table set jumlah=jumlah+"+nd.get(i).getJml()+" where node=\""+nd.get(i).getNode()+"\"");
                }else
                {
                    stt.executeUpdate("INSERT INTO `node_table`(`Node`, `Jumlah`) VALUES ('"+nd.get(i).getNode()+"',"+nd.get(i).getJml()+")");
                }
                
            }
            nd.clear();
            for(int i=0; i < eg.size(); i++){
                String selectEdge = "SELECT edge_1, edge_2 FROM edge_table WHERE (edge_1=\""+eg.get(i).getEdge1()+"\" and edge_2=\""+eg.get(i).getEdge2()+"\")";                                   
                ResultSet ers =  stt2.executeQuery(selectEdge);
                if(ers.next())
                {
                    stt.executeUpdate("update edge_table set jumlah=jumlah+"+eg.get(i).getJml()+" where (edge_1=\""+eg.get(i).getEdge1()+"\" and edge_2=\""+eg.get(i).getEdge2()+"\")");
                }else
                {
                    stt.executeUpdate("INSERT INTO `edge_table`(`edge_1`, `edge_2`, `jumlah`) VALUES ('"+eg.get(i).getEdge1()+"','"+eg.get(i).getEdge2()+"',"+eg.get(i).getJml()+")");
                }    
                
                
            }
            eg.clear();
          
        } catch (SQLException ex) {
            Logger.getLogger(BuildingSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertNode()
    {
        System.out.println("Proses pembuatan edge dan node dimulai");
        int bar = 0;
        int seqLength = seq.size();
        String splitByComa = ",";
        for (int i=0; i<seq.size(); i++)
        {   
            String sequence = seq.get(i).getseq();
            String [] arSequence = sequence.split(splitByComa);
            int sequenceLength = arSequence.length;
            if(findNode("S"))
            {
                updateNode("S");
            }else
            {
                nd.add(new node("S", 1)); 
            }
              
            if(findNode("X"))
            {
                updateNode("X");
            }else
            {
                nd.add(new node("X", 1)); 
            }
            
            for (int k=0; k<arSequence.length;k++)
            {
                if (findNode(arSequence[k]))
                {
                    updateNode(arSequence[k]);
                }else
                {
                    nd.add(new node(arSequence[k], 1));                                                           
                }
            }         
                        
        
        bar++;
        float barFloat = (100*bar/seqLength)-1;
            //prog = (int) barFloat;
        System.out.println("Progress Node "+barFloat+"%");
        }
        
        System.out.println("Progress Node 100%");
        System.out.println("Proses node Selesai");
          
            
    }
    
    public void insertEdge()
    {
       // this.coba();
        System.out.println("Proses pembuatan edge ");
        int bar = 0;
        int seqLength = seq.size();
        String splitByComa = ",";
        
        for (int i=0; i<seq.size(); i++)
        {   
            String sequence = seq.get(i).getseq();
            String [] arSequence = sequence.split(splitByComa);
            int sequenceLength = arSequence.length;
            
            if (findEdge("S", arSequence[0]))
            {
                updateEdge("S", arSequence[0]);
            }else
            {
                eg.add(new edge("S", arSequence[0], 1));
            }
                
            if (findEdge(arSequence[arSequence.length-1],"X"))
            {
                updateEdge(arSequence[arSequence.length-1],"X");
            }else
            {
                eg.add(new edge(arSequence[arSequence.length-1],"X", 1));
            }
                
            for (int k=0; k<arSequence.length;k++)
            {
                
                for (int l=0; l<caus.size();l++)
                {
                    if(caus.get(l).getcausal1().equalsIgnoreCase(arSequence[k]))
                    {
                        //String causal1=caus.get(l).getcausal1();
                        String causal2=caus.get(l).getcausal2();
                        for(int m=k; m<arSequence.length;m++)
                        {
                            if(m>k)
                            {
                                if (arSequence[k].equals(arSequence[m]))
                                {
                                    if (causal2.equals(arSequence[m]))
                                    {
                                        if (findEdge(arSequence[k], arSequence[m]))
                                        {                                            
                                            updateEdge(arSequence[k], arSequence[m]);
                                        }else
                                        {
                                            eg.add(new edge(arSequence[k], arSequence[m], 1));
                                        }
                                    
                                    }
                                    break;
                                }else
                                {
                                    if (causal2.equals(arSequence[m]))
                                    {   
                                         if(arSequence[m].equals(arSequence[m-1]))
                                         {
                                            
                                         }else
                                         {
                                            if (findEdge(arSequence[k], arSequence[m]))
                                            {                                            
                                                updateEdge(arSequence[k], arSequence[m]);
                                            }else
                                            {
                                                eg.add(new edge(arSequence[k], arSequence[m], 1));
                                            }
                                         }   
                                    
                                    } 
                                }
                                        
                            }
                        }
                    }
                }
                        
            }
            bar++;
            float barFloat = (100*bar/seqLength)-1;
            System.out.println("Progress edge "+barFloat+"%");
        }
        System.out.println("Progress edge 100%");
        System.out.println("Proses edge Selesai");
          
            
    }
    
    
    public void coba()
    {
        System.out.println("Proses pembuatan edge ");
        int bar = 0;
        int seqLength = seq.size();
        String splitByComa = ",";
        List <Integer> domain = new ArrayList<>();
        for (int i=0; i<seq.size(); i++)
        {   
            domain.clear();
            String sequence = seq.get(i).getseq();
            String [] arSequence = sequence.split(splitByComa);
            int sequenceLength = arSequence.length;
            
            if (findEdge("S", arSequence[0]))
            {
                updateEdge("S", arSequence[0]);
                domain.add(0);
            }else
            {
                eg.add(new edge("S", arSequence[0], 1));
                domain.add(0);
            }
                
            if (findEdge(arSequence[arSequence.length-1],"X"))
            {
                updateEdge(arSequence[arSequence.length-1],"X");
                domain.add(arSequence.length-1);
            }else
            {
                eg.add(new edge(arSequence[arSequence.length-1],"X", 1));
                domain.add(arSequence.length-1);
            }
                
            for (int k=0; k<arSequence.length;k++)
            {
                boolean cekDomain = false;
                for (int d=0; d<domain.size(); d++)
                {
                    if (domain.get(d)==k)
                    {
                        cekDomain=true;
                    }
                }
                
                if (!cekDomain)
                {
                    for (int c=k;c<0;c--)
                    {
                        for (int l=0; l<caus.size();l++)
                        {
                            if (caus.get(l).getcausal2().equalsIgnoreCase(arSequence[k])&& caus.get(l).getcausal1().equalsIgnoreCase(arSequence[c]))
                            {
                                if (findEdge(arSequence[c], arSequence[k]))
                                {
                                    updateEdge(arSequence[c], arSequence[k]);
                                }else
                                {
                                    eg.add(new edge(arSequence[c],arSequence[k],1));
                                }
                                break;
                            }
                        }
                    }
                }
                //cekDomain=false;
                
                for (int l=0; l<caus.size();l++)
                {
                    if(caus.get(l).getcausal1().equalsIgnoreCase(arSequence[k]))
                    {
                        //String causal1=caus.get(l).getcausal1();
                        String causal2=caus.get(l).getcausal2();
                        for(int m=k; m<arSequence.length;m++)
                        {
                            if(m>k)
                            {
                                if (arSequence[k].equals(arSequence[m]))
                                {
                                    if (causal2.equals(arSequence[m]))
                                    {
                                        if (findEdge(arSequence[k], arSequence[m]))
                                        {                                            
                                            updateEdge(arSequence[k], arSequence[m]);
                                            domain.add(m);
                                        }else
                                        {
                                            eg.add(new edge(arSequence[k], arSequence[m], 1));
                                            domain.add(m);
                                        }
                                    
                                    }
                                    break;
                                }else
                                {
                                    if (causal2.equals(arSequence[m]))
                                    {   
                                         if(arSequence[m].equals(arSequence[m-1]))
                                         {
                                            
                                         }else
                                         {
                                            if (findEdge(arSequence[k], arSequence[m]))
                                            {                                            
                                                updateEdge(arSequence[k], arSequence[m]);
                                                domain.add(m);
                                            }else
                                            {
                                                eg.add(new edge(arSequence[k], arSequence[m], 1));
                                                domain.add(m);
                                                
                                            }
                                         }   
                                    
                                    } 
                                    break;
                                }
                                        
                            }
                        }
                    }
                }
                        
            }
            bar++;
            float barFloat = (100*bar/seqLength)-1;
            System.out.println("Progress edge "+barFloat+"%");
        }
        System.out.println("Progress edge 100%");
        System.out.println("Proses edge Selesai");
          
            
    }
    public void insertEdgeAndNode()
    {
        System.out.println("Proses pembuatan edge dan node dimulai");
        cn.getKoneksi();
        cn2.getKoneksi();
        cn3.getKoneksi();
        
        int bar = 0;
        int seqLength = seq.size();
        try 
        {
            stt  = (Statement) cn.koneksi.createStatement();
            stt2 = (Statement) cn2.koneksi.createStatement();
            stt3 = (Statement) cn3.koneksi.createStatement();
        } catch (SQLException ex) 
        {
            Logger.getLogger(BuildingEdgeNode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
            String splitByComa = ",";
            for (int i=0; i<seq.size(); i++)
            {   
                String sequence = seq.get(i).getseq();
                String [] arSequence = sequence.split(splitByComa);
                int sequenceLength = arSequence.length;
                if(findNode("S"))
                {
                    updateNode("S");
                }else
                {
                   nd.add(new node("S", 1)); 
                }
                
                if(findNode("X"))
                {
                    updateNode("X");
                }else
                {
                   nd.add(new node("X", 1)); 
                }
                
                
                if (findEdge("S", arSequence[0]))
                {
                    updateEdge("S", arSequence[0]);
                }else
                {
                    eg.add(new edge("S", arSequence[0], 1));
                }
                
                if (findEdge(arSequence[arSequence.length-1],"X"))
                {
                    updateEdge(arSequence[arSequence.length-1],"X");
                }else
                {
                    eg.add(new edge(arSequence[arSequence.length-1],"X", 1));
                }
                
                for (int k=0; k<arSequence.length;k++)
                {
                        //pembuatan node
//                        String cekNode = "Select Node from Node_table where node=\""+arSequence[k]+"\"";                    
//                        ResultSet rsss = stt2.executeQuery(cekNode);
                        
                        if (findNode(arSequence[k]))
                        {
//                            String updNode = "update node_table set jumlah=jumlah+1 where node=\""+arSequence[k]+"\" ";
//                            stt3.executeUpdate(updNode);
                              updateNode(arSequence[k]);
                              
                        }else
                        {
//                            String insertNode = "insert into node_table (node,jumlah) values(\""+arSequence[k]+"\",1)";
//                            stt3.executeUpdate(insertNode);
                              nd.add(new node(arSequence[k], 1));
                              
                              
                        }
                        for (int l=0; l<caus.size();l++)
                        {
                            if(caus.get(l).getcausal1().equalsIgnoreCase(arSequence[k]))
                            {
                                String causal1=caus.get(l).getcausal1();
                                String causal2=caus.get(l).getcausal2();
                                for(int m=k; m<arSequence.length;m++)
                                {
                                    if(m>k)
                                    {
                                         if (causal2.equals(arSequence[m]))
                                      {
//                                           String selectEdge = "SELECT edge_1, edge_2 FROM edge_table WHERE (edge_1=\""+causal1+"\" and edge_2=\""+causal2+"\")";                                   
//                                           ResultSet ers =  stt3.executeQuery(selectEdge);
                                           //pengecekan edge sudah terbentuk sebelumnya atau belum
                                           if (findEdge(causal1, causal2))
                                           {                                            
//                                                String updEdge="update edge_table set jumlah=jumlah+1 where (edge_1=\""+causal1+"\" and edge_2=\""+causal2+"\")";
//                                                stt3.executeUpdate(updEdge);
                                                updateEdge(causal1, causal2);
                                         
                                           } //kondisi pengecualian jika edge penghubung belum ada
                                            else
                                           {
//                                                String insEdge="insert into edge_table(edge_1,edge_2,jumlah) values (\""+causal1+"\",\""+causal2+"\",1)";
//                                                stt3.executeUpdate(insEdge);  
                                                eg.add(new edge(causal1, causal2, 1));
                                            }//akhir pengecekan edge
                                    
                                      } 
                                    }
                                }
                            }
                        }
                        
                }
                bar++;
                float barFloat = (100*bar/seqLength)-1;
                //prog = (int) barFloat;
                System.out.println("Progress Program "+barFloat+"%");
            }
            save_db();
            
            System.out.println("Progress Program 100%");
            System.out.println("Proses Selesai");
          
            
              
    }
    
    
    public void insertNodeAndEdge()
    {
        int jmlRow =0;
        int bar = 0;
        int prog =0;
        Hashtable h = new Hashtable();
        int s = 0;
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;
        int f = 0;
        int x = 0;
        
        Enumeration jml;
        String node;
        cn.getKoneksi();
        cn2.getKoneksi();
        cn3.getKoneksi();
        
        try 
        {
            stt  = (Statement) cn.koneksi.createStatement();
            stt2 = (Statement) cn2.koneksi.createStatement();
            stt3 = (Statement) cn3.koneksi.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(BuildingEdgeNode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try 
        {
            String cekJmlRow = "select count(*) as jml from instance_graph";
            stt.executeQuery(cekJmlRow);
            ResultSet resJmlRow = stt.getResultSet();
            if (resJmlRow.next())
            {
                jmlRow = resJmlRow.getInt("jml");
                System.out.println("jml row"+jmlRow);
            }
            
            String splitByComa = ",";
            String  cekSomeInstance = "Select sequence from instance_graph";            
            ResultSet res = stt.executeQuery(cekSomeInstance);
                       
            while (res.next())
            {
                    String sequence = res.getString("sequence");
                    String [] arSequence = sequence.split(splitByComa);
                    for (int i=0; i<arSequence.length;i++)
                    {
                        //pembuatan node
                        String cekNode = "Select Node from Node_table where node=\""+arSequence[i]+"\"";                    
                        ResultSet rsss = stt2.executeQuery(cekNode);
                        if (rsss.next())
                        {
                            String updNode = "update node_table set jumlah=jumlah+1 where node=\""+arSequence[i]+"\" ";
                            stt3.executeUpdate(updNode);
                        }else
                        {
                            String insertNode = "insert into node_table (node,jumlah) values(\""+arSequence[i]+"\",1)";
                            stt3.executeUpdate(insertNode);
                        }
                        String cekSomeCas = "SELECT causal_1, causal_2 FROM causal_table WHERE causal_1=\""+arSequence[i]+"\"";                    
                        stt2.executeQuery(cekSomeCas);
                        ResultSet rrs = stt2.getResultSet();                     
                        while (rrs.next())
                        {
                           String causal1 = rrs.getString("causal_1");
                           String causal2 = rrs.getString("causal_2");
                           
                           //pembuatan edge
                           for (int j=i; j<arSequence.length; j++)
                           {                              
                                if (j>i)
                                {
                                      if (causal2.equals(arSequence[j]))
                                      {
                                           String selectEdge = "SELECT edge_1, edge_2 FROM edge_table WHERE (edge_1=\""+causal1+"\" and edge_2=\""+causal2+"\")";                                   
                                           ResultSet ers =  stt3.executeQuery(selectEdge);
                                           //pengecekan edge sudah terbentuk sebelumnya atau belum
                                           if (ers.next())
                                           {                                            
                                                String updEdge="update edge_table set jumlah=jumlah+1 where (edge_1=\""+causal1+"\" and edge_2=\""+causal2+"\")";
                                                stt3.executeUpdate(updEdge);
                                         
                                           } //kondisi pengecualian jika edge penghubung belum ada
                                            else
                                           {
                                                String insEdge="insert into edge_table(edge_1,edge_2,jumlah) values (\""+causal1+"\",\""+causal2+"\",1)";
                                                stt3.executeUpdate(insEdge);                                
                                            }//akhir pengecekan edge
                                    
                                      } 
                                }
                                                     
                           }
                        } 
                      
                    }
                    
                    bar++;
                    float barFloat = 100*bar/jmlRow;
                    prog = (int) barFloat;
                    System.out.println("Progress Program "+barFloat+"%");
                    
                    
            }
           
            
            System.out.println("Progress Program Selesai");
        
        }  catch (SQLException ex) {
            Logger.getLogger(BuildingEdgeNode.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }
}