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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.mysql.jdbc.Driver;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import object.*;
/**
 *
 * @author ADMIN
 */
public class BuildingSequence {
    String activityName;
    String originatorName;
    String semesterName;
    ConnectDB con = new ConnectDB();
    ConnectDB con2 = new ConnectDB();
    ConnectDB con3 = new ConnectDB();
    ConnectDB con4 = new ConnectDB();
    Statement stat;
    Statement stat2;
    Statement stat3;
    Statement stat4;
    String csvPath="";
    int columnCaseId;
    int columnActivity;
    int columnOriginator;
    int columnTimeStamp;
    int columnSemester;
    ArrayList<sequence> seq = new ArrayList<sequence>();
    ArrayList<causal_helper> caus_helper= new ArrayList<causal_helper>();
    ArrayList<causal> caus = new ArrayList<causal>();
    
    public boolean findcaushelper(String c1, String c2)
    {
        boolean hasil=false;
        for(int i=0; i < caus_helper.size(); i++){
            if(caus_helper.get(i).getcausal1().equals(c1) && caus_helper.get(i).getcausal2().equals(c2)){
                hasil=true;
                break;
            }
        }
        return hasil;
    }
    
    public boolean findcaushelper_next(String c1, String c2)
    {
        boolean hasil=false;
        for(int i=0; i < caus_helper.size(); i++){
            if(caus_helper.get(i).getcausal1().equals(c1) && caus_helper.get(i).getcausal2().equals(c2) && caus_helper.get(i).getcausal3().equals(c1)){
                hasil=true;
                break;
            } else if(caus_helper.get(i).getcausal1().equals(c2) && caus_helper.get(i).getcausal2().equals(c1) && caus_helper.get(i).getcausal3().equals(c1)){
                hasil=true;
                break;
            }
        }
        return hasil;
    }
    
    public void cek_causal()
    {
        for(int i=0; i<caus.size();i++)
        {
            
            String tempCaus1 = caus.get(i).getcausal1();
            String tempCaus2 = caus.get(i).getcausal2();
//            for (int k=0; k<caus.size();k++)
//                {
//                   String causTemp1 = caus.get(k).getcausal1();
//                   String causTemp2 = caus.get(k).getcausal2();
//                    if (causTemp1.equals(tempCaus2) && causTemp2.equals(tempCaus1) && !findcaushelper_next(tempCaus1, tempCaus2) && !tempCaus1.equals(tempCaus2))
//                    {                    
//                        caus.remove(k);
//                        caus.remove(i);
//                        break;
//                    }
//                }
            if (findcaushelper_next(tempCaus1, tempCaus2) || findcaushelper_next(tempCaus2,tempCaus1))
            {
                System.out.println("causal helper");
                System.out.println("causal 1 "+tempCaus1);
                System.out.println("causal 2 "+tempCaus2);
               
            }else if(tempCaus1.equals(tempCaus2))
            {
                System.out.println("causal sama");
                System.out.println("causal 1 "+tempCaus1);
                System.out.println("causal 2 "+tempCaus2);
               
            }else
            {
                for (int k=0; k<caus.size();k++)
                {
                       
                    if (caus.get(k).getcausal1().equals(tempCaus2) && caus.get(k).getcausal2().equals(tempCaus1))
                    {                    
                        caus.remove(k);
                        caus.remove(i);
                        break;
                    }
                }
            }
            
        }
    }
    
    public boolean findcaus(String c1, String c2)
    {
        boolean hasil=false;
        for(int i=0; i < caus.size(); i++){
            if(caus.get(i).getcausal1().equals(c1) && caus.get(i).getcausal2().equals(c2)){
                hasil=true;
                break;
            }
        }
        return hasil;
    }
    
    public void findcaus_delete(String c1, String c2)
    {
        for(int i=0; i < caus.size(); i++){
            if(caus.get(i).getcausal1().equals(c1) && caus.get(i).getcausal2().equals(c2)){
                caus.remove(new causal(c1, c2));
                break;
            }
        }
        
    }
    
    public void save_db()
    {
        con.getKoneksi();
        con2.getKoneksi();
        try {
            stat = (Statement) con.koneksi.createStatement();
            stat2 = (Statement) con2.koneksi.createStatement();
            
            for(int i=0; i < seq.size(); i++){
                System.out.println("Proses memasukkan sequence ke database");
                stat.executeUpdate("INSERT INTO `instance_graph`(`case_id`, `sequence`) VALUES ('"+seq.get(i).getcase_id()+"','"+seq.get(i).getseq()+"')");
            }
            
            String cCasRf = "SELECT no FROM causal_table_helper";
            ResultSet rc = stat.executeQuery(cCasRf);
            if (rc.next())
            {
                stat2.executeUpdate("DELETE FROM `causal_table_helper` WHERE 1");
            }            
            for(int i=0; i < caus_helper.size(); i++){
                stat.executeUpdate("INSERT INTO `causal_table_helper`(`causal1`, `causal2`, `causal3`) VALUES ('"+caus_helper.get(i).getcausal1()+"','"+caus_helper.get(i).getcausal2()+"','"+caus_helper.get(i).getcausal3()+"')");
                               
            }
            
            caus_helper.clear();
            
            String cekSomeCas = "SELECT no FROM causal_table";
            ResultSet rrs = stat.executeQuery(cekSomeCas);
            if (rrs.next())
            {
                stat2.executeUpdate("DELETE FROM `causal_table` WHERE 1");
            }
            
            for(int i=0; i < caus.size(); i++){
                System.out.println("Proses memasukkan causal relation ke DB");
                stat.executeUpdate("INSERT INTO `causal_table`(`causal_1`, `causal_2`) VALUES ('"+caus.get(i).getcausal1()+"','"+caus.get(i).getcausal2()+"')");
                    
            }
        } catch (SQLException ex) {
            Logger.getLogger(BuildingSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Proses pembuatan sequence dan causal berakhir");
        System.out.println("Menuju proses pembuatan edge dan node");
    }
    
    public void setCsvPath(String csv)
    {       
        csvPath=csv+csvPath;        
    }
    
    public void setColCaseId(String caseid)
    {        
        columnCaseId = Integer.parseInt(caseid)-1;
    }
    
    public void setColActivity(String act)
    {
        columnActivity = Integer.parseInt(act)-1;
    }
    
    public void setColOriginator(String ori)
    {
         columnOriginator  = Integer.parseInt(ori)-1;
    }
    
    public void setColTimeStamp(String time)
    {
        columnTimeStamp = Integer.parseInt(time)-1;
    }
    public void setColSemester(String smt)
    {
        columnSemester = Integer.parseInt(smt)-1;
    }
    
    public void setActivity(String act)
    {
        activityName = act;
    }
    
    public void setOriginator(String or)
    {
        originatorName = or;
    }
    
    public void addCausalAndCausalHelperFromDB()
    {
        con.getKoneksi();
        con2.getKoneksi();
        try {
            stat = (Statement) con.koneksi.createStatement();
            stat2 = (Statement) con2.koneksi.createStatement();
            String cekCaus = "Select causal_1,causal_2 from causal_table";
            ResultSet cs = stat.executeQuery(cekCaus);
            while (cs.next())
            {
                String caus1 = cs.getString("causal_1");
                String caus2 = cs.getString("causal_2");
                caus.add(new causal(caus1, caus2));
                
            }
            
            String cekCausHelper="Select causal1,causal2,causal3 from causal_table_helper";
            ResultSet ch = stat2.executeQuery(cekCausHelper);
            while (ch.next())
            {
                String ch1 = ch.getString("causal1");
                String ch2 = ch.getString("causal2");
                String ch3 = ch.getString("causal3");
                caus_helper.add(new causal_helper(ch1, ch2, ch3));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(BuildingSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void insertSequence ()
    {
        System.out.println("Proses pembuatan sequence dan causal relation dimulai");
        con.getKoneksi();
        con2.getKoneksi();
        con3.getKoneksi();
        con4.getKoneksi();
        
        
        String sequence= "";
        String lastSequence;
        String tempSeq = "";
        String seqSplitBy=",";
        String comCaseId="";
        String comSmt="";
        BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
       
        try {
            stat = (Statement) con.koneksi.createStatement();
            stat2 = (Statement) con2.koneksi.createStatement();
            stat3 = (Statement) con3.koneksi.createStatement();
            stat4 = (Statement) con3.koneksi.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(BuildingSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try 
        {     
                
                br = new BufferedReader(new FileReader(csvPath));
		while ((line = br.readLine()) != null) 
                {
                        String[] caseId = line.split(cvsSplitBy);                                         
                        //mengambil nilai case id
                        String caseValue = caseId[columnCaseId].toString();
                        String originator= caseId[columnOriginator].toString();
                        String semester = caseId[columnSemester].toString();
                        if (caseValue.equalsIgnoreCase(originator))
                        {
                            this.setOriginator("");
                        }else
                        {
                            if (originator.matches("ittkaprodi(.*)"))
                            {
                                this.setOriginator("_K");
                            }else if(originator.matches(".*baa"))
                            {
                                this.setOriginator("_B");
                            }else if(originator.equalsIgnoreCase("superadmin"))
                            {
                                this.setOriginator("_S");
                            }else if(originator.matches("ittwadek(.*)"))
                            {
                                this.setOriginator("_W");
                            }else if(originator.equalsIgnoreCase("ittppdu"))
                            {
                                this.setOriginator("_P");
                            }else{
                                this.setOriginator("_D");
                            }
                        }
                    /*melakukan rename acitivity name
                     
                     *Insert MK = I_kodeMK_
                     *Delete MK = D_kodeMK
                     *Siap ACC = A
                     *Reset = B
                     *ACC = C
                     *Cetek KSM = E
                     */
                    
                        
                        
                        switch (caseId[columnActivity].toString()) 
                        {                    
                                        
                                case "SIAP ACC":
                                this.setActivity("A"+originatorName);                            
                                    break;
                        
                                case "RESET":
                                    this.setActivity("B"+originatorName);                           
                                    break;
                       
                                case "ACC":
                                    this.setActivity("C"+originatorName);                            
                                    break;
                                    
                                case "CETAK KSM":
                                    this.setActivity("E"+originatorName);                           
                                    break;
                                
                                default:
                                    String tempAct = caseId[columnActivity].toString();
                                    this.setActivity(tempAct+originatorName);
                                    break;                                
                        
                        }
                               
                        
                      // end of rename activity                     
                                     
                    //mulai proses memasukkan nilai activity 
                        if (comSmt==""){
                            sequence = activityName;
                            this.setOriginator("");
                        }else if (comSmt.equalsIgnoreCase(semester))
                        {
                            if(comCaseId.equalsIgnoreCase(caseValue))
                            {
                                 sequence = sequence+","+activityName;
                                 this.setOriginator("");
                            }
                            else if(!comCaseId.equalsIgnoreCase(caseValue))
                            {   
                                this.setOriginator("");                 
                                try {
                                    seq.add(new sequence(comCaseId, sequence));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }                     
                            
                                //Proses pembentukan causal relation
                                //split masing-masing nilai sequence
                                String[] arrSeq = sequence.split(seqSplitBy);
                                int lengtSeq = arrSeq.length-2;
                                for (int i=0; i<arrSeq.length-2;i++)
                                {
                                     String cas1 = arrSeq[i].toString();
                                     String cas2 = arrSeq[i+1].toString();
                                     String cas3 = arrSeq[i+2].toString();
                                     if (cas1.equals(cas3))
                                         {
                                             if (cas1.equals(cas2))
                                             {
                                                 
                                             }else
                                             {
                                                 if (findcaushelper(cas1, cas2))
                                                 {
                                                 
                                                 }else{
                                                    caus_helper.add(new causal_helper(cas1, cas2, cas3));
                                                  } 
                                             }
                                                                                        
                                         }
                                }
                                 
                                for (int i=0; i<arrSeq.length-1;i++)
                                {
                                     String cas1 = arrSeq[i].toString();
                                     String cas2 = arrSeq[i+1].toString();
                                                                       
                                     if (cas1.equals(cas2))
                                     {
                                           if (findcaus(cas1, cas2)){
                                             
                                              }else{
                                                    caus.add(new causal(cas1, cas2));
                                              }
                                              
                                     }else
                                     {
                                              if (findcaus(cas1, cas2))
                                              {
                                              }
                                              else
                                              {   
                                                    caus.add(new causal(cas1, cas2));
                                              }
                                     }                             
                                } 
                               
                                sequence = activityName;
                            }
                            
                        }else if (!comSmt.equalsIgnoreCase(semester))
                        {
                                this.setOriginator("");                 
                                
                                try {
                                    seq.add(new sequence(comCaseId, sequence));
                                    
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }                     
                            
                                //Proses pembentukan causal relation
                                //split masing-masing nilai sequence
                                String[] arrSeq = sequence.split(seqSplitBy);
                                int lengtSeq = arrSeq.length-2;
                                for (int i=0; i<arrSeq.length-2;i++)
                                {
                                     String cas1 = arrSeq[i].toString();
                                     String cas2 = arrSeq[i+1].toString();
                                     String cas3 = arrSeq[i+2].toString();
                                     if (cas1.equals(cas3))
                                         {
                                             if (cas1.equals(cas2))
                                             {
                                                 
                                             }else
                                             {
                                                 if (findcaushelper(cas1, cas2))
                                                 {
                                                 
                                                 }else{
                                                    caus_helper.add(new causal_helper(cas1, cas2, cas3));
                                                    
                                                 } 
                                             }
                                                                                        
                                         }
                                }
                                 
                                for (int i=0; i<arrSeq.length-1;i++)
                                {
                                     String cas1 = arrSeq[i].toString();
                                     String cas2 = arrSeq[i+1].toString();
                                                                       
                                     if (cas1.equals(cas2))
                                     {
                                             if (findcaus(cas1, cas2)){
                                             
                                              }else{
                                                    caus.add(new causal(cas1, cas2));                                                         
                                              }
                                              
                                     }else
                                     {
                                              if (findcaus(cas1, cas2))
                                              {
                                             
                                              }else{ 
                                                    caus.add(new causal(cas1,cas2));
                                               }
                                     }                             
                                } 
                               
                                sequence = activityName;
                               
                                
                        }
                               
                        System.out.println("Sabar Proses masih berjalan");
                        comCaseId = caseId[columnCaseId].toString();
                        comSmt = caseId[columnSemester].toString();
                }
                
                sequence = sequence;       
                seq.add(new sequence(comCaseId, sequence));
                                 
                
                      
                String[] arrSeq = sequence.split(seqSplitBy);
                int lengtSeq = arrSeq.length-2;
                for (int i=0; i<arrSeq.length-2;i++)
                                {
                                     String cas1 = arrSeq[i].toString();
                                     String cas2 = arrSeq[i+1].toString();
                                     String cas3 = arrSeq[i+2].toString();
                                     if (cas1.equals(cas3))
                                         {
                                             if (cas1.equals(cas2))
                                             {
                                                 
                                             }else
                                             {
                                                 if (findcaushelper(cas1, cas2))
                                                 {
                                                 
                                                 }else{
                                                    caus_helper.add(new causal_helper(cas1, cas2, cas3));
                                                  } 
                                             }
                                                                                        
                                         }
                                }
                for (int i=0; i<arrSeq.length-1;i++)
                {
                    String cas1 = arrSeq[i].toString();
                    String cas2 = arrSeq[i+1].toString();
                                                                       
                    if (cas1.equals(cas2))
                    {
                        if (findcaus(cas1, cas2)){
                        }else
                        {
                            caus.add(new causal(cas1, cas2));
                        }
                                              
                    }else
                    {
                        if (findcaus(cas1, cas2))
                        {
                                             
                        }else
                        { 
                            caus.add(new causal(cas1,cas2));
                        }
                    } 
                }
                   
        this.cek_causal();
        save_db();
                
        BuildingEdgeNode c = new BuildingEdgeNode(caus, caus_helper, seq);
//        c.insertEdgeAndNode();
        c.insertNode();
       c.coba();
        //c.insertEdge();
        c.save_db();
        
	} catch (FileNotFoundException e) 
        {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}finally 
        {
		if (br != null) 
                {
			try 
                        {
				br.close();
			} catch (IOException e) 
                        {
				e.printStackTrace();
			}
		}
	}
    }
    
    
   
}
