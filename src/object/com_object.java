/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package object;

/**
 *
 * @author ADMIN
 */
public class com_object {
    String case_id;
    String comf;
    
    public com_object(String a, String b)
    {
        case_id = a;
        comf = b;
    }
    
    public String getcase_id()
    {
        return case_id;
    }
    
    public String getseq()
    {
        return comf;
    }
}
