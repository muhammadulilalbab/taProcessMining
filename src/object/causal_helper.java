/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package object;

/**
 *
 * @author Ulil
 */
public class causal_helper {
    private String causal1;
    private String causal2;
    private String causal3;
    
    public causal_helper(String a, String b, String c)
    {
        causal1 = a;
        causal2 = b;
        causal3 = c;
    }
    
    public String getcausal1()
    {
        return causal1;
    }
    
    public String getcausal2()
    {
        return causal2;
    }
    
    public String getcausal3()
    {
        return causal3;
    }
}
