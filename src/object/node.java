/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package object;

/**
 *
 * @author Ulil
 */
public class node {
    private String node;
    private int jumlah;
    public node(String a, int b)
    {
        node =a;
        jumlah = b;
    }
    
    public String getNode()
    {
        return node;
    }
    
    public int getJml()
    {
        return jumlah;
    }
}
