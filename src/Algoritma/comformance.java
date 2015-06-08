/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritma;

import DB.ConnectDB;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import View.*;
import object.*;

/**
 *
 * @author ADMIN
 */
public class comformance {
    String activityName;
    String originatorName;
    String semesterName;
    String csvPath="";
    int columnCaseId;
    int columnActivity;
    int columnOriginator;
    int columnTimeStamp;
    int columnSemester;
    
    int FP=0;
    int FN=0;
    int TP=0;
    double Recall = 0;
    double Precision = 0;
    double FMeasure = 0;
    ConnectDB con = new ConnectDB();
    ConnectDB con2 = new ConnectDB();
    ConnectDB con3 = new ConnectDB();
    ConnectDB con4 = new ConnectDB();
    Statement stat;
    Statement stat2;
    Statement stat3;
    Statement stat4;
    ArrayList<com_object> comfor = new ArrayList<com_object>();
    ArrayList<edge> ed = new ArrayList<edge>();
    
    public void insertEdge()
    {
        con.getKoneksi();
        try
        {
            stat = (Statement) con.koneksi.createStatement();
            String selectEdge="Select edge_1, edge_2 from edge_table";
            ResultSet res = stat.executeQuery(selectEdge);
            while (res.next())
            {
                String edge1 = res.getString("edge_1");
                String edge2 = res.getString("edge_2");
                ed.add(new edge(edge1, edge2, 0));
            }
            
        }catch (SQLException ex) 
        {
            Logger.getLogger(comformance.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    public void addFP()
    {
        FP++;
    }
    
    public void addFN()
    {
        FN++;
    }
    
    public void addTP()
    {
        TP++;
    }
    public void setRecall()
    {
        double a = TP+FN;
        Recall = TP/a;
    }
    
    public void setPrecision()
    {
        double b = TP+FP;
        Precision = TP/b;
    }
    
    public void setFMeasure()
    {
        double c = 2*Precision*Recall;
        double d = Precision+Recall;
        FMeasure = c/d;
        
        Comformance_result aa = new Comformance_result();
        aa.setTP(TP);
        aa.setFN(FN);
        aa.setFP(FP);
        aa.setPrecision(Precision);
        aa.setRecall(Recall);
        aa.setFmeasure(FMeasure);
        aa.setVisible(true);
    }
    public int getFP()
    {
        return FP;
    }
    
    public int getFN()
    {
        return FN;
    }
    
    public int getTP()
    {
        return FP;
    }
    
    public double getRecall()
    {
        
        return Recall;
    }
    
    public double getPrecision()
    {
        return Precision;
    }    
    
    public double getFMeasure()
    {
        return FMeasure;
    }
    public void insertTrace()
    {
        System.out.println("Proses pembuatan trace untuk comformance");
        String sequence= "";
        String lastSequence;
        String tempSeq = "";
        String seqSplitBy=",";
        String comCaseId="";
        String comSmt="";
        BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
        
        try 
        {     
                // mulai membaca file cv
                //System.out.println("path"+csvPath);
                br = new BufferedReader(new FileReader(csvPath));
		
                //melakukan perulangan sampai data di baris csv habis
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
                            sequence = "S,"+activityName;
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
                                sequence = sequence+",X";                             
                                //System.out.println(sequence);
                                try {
                                    comfor.add(new com_object(comCaseId, sequence));
                                    
                                    System.out.println(sequence);
                                            //stat.executeUpdate("insert into instance_graph (case_id,sequence) values(\""+comCaseId+"\",\""+sequence+"\")");                                    
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }                     
                                sequence = "S,"+activityName;
                            }
                            
                        }else if (!comSmt.equalsIgnoreCase(semester))
                        {
                                this.setOriginator("");                 
                                sequence = sequence+",X";                             
                                System.out.println(sequence);
                                try {
                                    comfor.add(new com_object(comCaseId, sequence));
                                    //stat.executeUpdate("insert into instance_graph (case_id,sequence) values(\""+comCaseId+"\",\""+sequence+"\")");                                    
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }                     
                                sequence = "S,"+activityName;                              
                       }
                               
                        System.out.println("Sabar Proses masih berjalan");
                        //System.out.println("smt "+semester);
                        comCaseId = caseId[columnCaseId].toString();
                        comSmt = caseId[columnSemester].toString();
                }
                
                sequence = sequence+",X";       
                comfor.add(new com_object(comCaseId, sequence));
                System.out.println(sequence);
                //stat.executeUpdate("insert into instance_graph (case_id,sequence) values(\""+comCaseId+"\",\""+sequence+"\")");          
                               
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
    
    
    
    public void checkRecallAndPrecision()
    {   
        int TFN = 0;
        int TTP = 0;
        int TFP = 0;
        String seqSplitBy = ",";
        Boolean kondisiDomain = false;
        List <Integer> domain = new ArrayList<>();
        for (int i=0; i<comfor.size(); i++)
        {
            
            String sequence = comfor.get(i).getseq();
            String []tempSequence = sequence.split(seqSplitBy);
            
            for (int m=0; m<ed.size(); m++)
            {
               Boolean cekDomain = false;
               if(ed.get(m).getEdge1().equals(tempSequence[0]))
               {
                    for (int n=1; n<tempSequence.length; n++)
                    {
                        if(ed.get(m).getEdge2().equals(tempSequence[n]))
                        {
                            this.addTP();
                            TTP++;
                            cekDomain = true;
                                
                            Boolean cekInsertDomain = false;
                            for(int o=0; o<domain.size(); o++)
                            {
                                if(domain.get(o)==n)
                                {
                                    cekInsertDomain = true;
                                }
                            }
                                
                            if (!cekInsertDomain)
                            {
                                 domain.add(n);
                                
                            }
                               
                        }
                     }
                    
                     if(!cekDomain)
                     {
                         this.addFP();
                         TFP++;
                        
                     }
                    
                        
               }
            }                                  
            
            for (int j=1; j<tempSequence.length; j++)
            {
                for (int k=0; k<domain.size(); k++)
                {
                    int tempDomain = domain.get(k);
                    if (j==tempDomain)
                    {
                        kondisiDomain = true;
                        break;
                    }
                }
                
                if (!kondisiDomain)
                {
                    this.addFN();
                    TFN++;
                    
                }
                kondisiDomain =false;
                for (int m=0; m<ed.size(); m++)
                {
                    Boolean cekDomain = false;
                    if(ed.get(m).getEdge1().equals(tempSequence[j]))
                    {
                        for (int n=j+1; n<tempSequence.length; n++)
                        {
                            if(ed.get(m).getEdge2().equals(tempSequence[n]))
                            {
                                this.addTP();
                                TTP++;
                                cekDomain = true;
                                
                                Boolean cekInsertDomain = false;
                                for(int o=0; o<domain.size(); o++)
                                {
                                    if(domain.get(o)==n)
                                    {
                                        cekInsertDomain = true;
                                    }
                                }
                                
                                if (!cekInsertDomain)
                                {
                                    domain.add(n);
                                    
                                }
                               
                            }
                        }
                        if(!cekDomain)
                        {
                            this.addFP();
                            TFP++;
                           
                        }
                        
                    }
                }
                
            }
            for(int p=0; p<domain.size(); p++)
            {
                int tdom = domain.get(p);
                //System.out.print(tdom+" ");
                
            }
            //System.out.println("");
            domain.clear();
            if (TFN>0)
            {
                System.out.println(sequence);
            System.out.println("TP = "+TTP);
            System.out.println("FP = "+TFP);
            System.out.println("FN = "+TFN);
            System.out.println("FN TEMP = "+FN);
            }
            
            //System.out.println("FN TEMP = "+FN);
            TTP =0;
            TFP = 0;
            TFN = 0;
        }
    }
}
