/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritma;
import View.SetParameter;
import java.io.*;
import java.util.*;
/**
 *
 * @author ADMIN
 */
public class Preprocessing {
    String csvPath="";
    String sortedCsvPath;
    int columnCaseId;
    int columnActivity;
    int columnOriginator;
    int columnTimeStamp;
    int columnSemester;
    public void setCsvPath(String csv)
    {
       
        csvPath=csv+csvPath;
       // System.out.println(csvPath);
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
        //System.out.println("colom semester preprocess"+columnSemester);
    }
    private static String getField(String line, int value) {
        
    	return line.split(",")[value];// extract value you want to sort on 
    }
    
    public String getCsvPath()
    {
        return this.csvPath;
    }
    
    public int getColCaseId()
    {
        return columnCaseId;
    }
    
    public int getColActivity()
    {
        return columnActivity;
    }
     
    public int getOriginator()
    {
        return columnOriginator;
    }
    
    public int getTimeStamp()
    {
        return columnTimeStamp;
    }
    
    public int getSemester()
    {
        return columnSemester;
    }
    public void sortBasedTime()
    {
         BufferedReader reader = null;
         String csvSplitBy = ",";
       
        System.out.println(this.getCsvPath());
        try 
        {
                //buffered reader diisi dengan path dari csv file
		reader = new BufferedReader(new FileReader(this.getCsvPath()));
		Map<String, List<String>> map = new TreeMap<String, List<String>>();
                //digunakan untuk sorting tanggal
                //sorting tahap pertama sorting berdasarkan tanggal                
                String line = reader.readLine();//read header
        	while ((line = reader.readLine()) != null) {
                	String key = getField(line,columnTimeStamp);
                        List<String> l = map.get(key);
                        if (l == null) {
                                l = new LinkedList<String>();
                        	map.put(key, l);
                        }
                	l.add(line);

            	}
                reader.close();
                //sorting tahap pertama sorting berdasarkan tanggal
                File file = new File("C:\\Users\\Admin\\Documents\\Sorted.csv");
                sortedCsvPath = "C:\\Users\\Admin\\Documents\\Sorted.csv";
                file.getParentFile().mkdirs();
                FileWriter writer = new FileWriter(file);
               
                //writer disini agar tidak ada kolom yang hilang
                writer.write("sebagai pelengkap sorting\n");
                
                //mulai menulis ulang hasil sorting kedalam file csv
        	for (List<String> list : map.values()) 
                {
                       for (String val : list) 
                       {
                                         writer.write(val);
                                         writer.write("\n");
                       }
                }
                writer.close();
          
                
               
                
 
	} 
        catch (FileNotFoundException e)
        {
		e.printStackTrace();
	}
        catch (IOException e)
        {
		e.printStackTrace();
	} finally 
        {
		if (reader != null)
                {
			try 
                        {
                            reader.close();
			} 
                        catch (IOException e)
                        {
				e.printStackTrace();
			}
		}
	}
        this.sortBaseCase();
    }
     
    
    public void sortBaseCase()
    {
        
        BufferedReader reader = null;
        String csvSplitBy = ",";
       
        
        try {
                //buffered reader diisi dengan path dari csv file
		reader = new BufferedReader(new FileReader(csvPath));
		Map<String, List<String>> map = new TreeMap<String, List<String>>();
                //digunakan untuk sorting caseId
                //sorting tahap kedua sorting berdasarkan caseId                
                String line = reader.readLine();//read header
        	while ((line = reader.readLine()) != null) {
                	String key = getField(line,columnCaseId);
                        List<String> l = map.get(key);
                        if (l == null) {
                                l = new LinkedList<String>();
                        	map.put(key, l);
                        }
                	l.add(line);

            	}
                reader.close();
               
                //menentukan path dari file hasil sorting
                File file = new File("C:\\Users\\Admin\\Documents\\Sorted.csv");
                sortedCsvPath = "C:\\Users\\Admin\\Documents\\Sorted.csv";
                file.getParentFile().mkdirs();
                FileWriter writer = new FileWriter(file);
               
              
        	for (List<String> list : map.values()) {
                                                            for (String val : list) {
                                                                                	writer.write(val);
                                                                                        writer.write("\n");
                                                        	}
                }
                writer.close();
                
                
                //memanggil fungsi  sorting kedua untuk sorting berdasarkan case id
               // this.makeMxml();
                
                
                //memanngil fugsi insert to database
                
 
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
        
        
       
        
    }
      
       
        
}
