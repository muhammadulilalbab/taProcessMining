/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package object;

/**
 *
 * @author Ulil
 */
public class causal {
    private String causal1;
    private String causal2;
    
    public causal(String a, String b)
    {
        causal1 = a;
        causal2 = b;
        
    }
    
    public void setcausal1(String a)
    {
        causal1 = a;
    }
    
    public void setcausal2(String a)
    {
        causal2 = a;
    }
    
    
    public String getcausal1()
    {
        return causal1;
    }
    
    public String getcausal2()
    {
        return causal2;
    }
}
