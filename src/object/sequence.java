/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package object;

/**
 *
 * @author Ulil
 */
public class sequence {
    String case_id;
    String seq;
    
    public sequence(String a, String b)
    {
        case_id = a;
        seq = b;
    }
    
    public String getcase_id()
    {
        return case_id;
    }
    
    public String getseq()
    {
        return seq;
    }
}
