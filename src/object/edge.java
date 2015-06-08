/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package object;

/**
 *
 * @author Ulil
 */
public class edge {
    String edge1;
    String edge2;
    int jumlah;
    
    public edge(String e1,String e2, int j)
    {
        edge1=e1;
        edge2=e2;
        jumlah=j;
    }        
    
    public String getEdge1()
    {
        return edge1;
    }
    
    public String getEdge2()
    {
        return edge2;
    }
    
    public int getJml()
    {
        return jumlah;
    }
}
