/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complit2lexo;

/**
 *
 * @author Simone Marchi
 */
public class LexicoUnitFactory {
    
    public static AbstractLexicoUnit createUnit(String type, String id) throws Exception {
        if(type!=null) {
		if(Utils.MUS.equalsIgnoreCase(type)) return new MorphologicalUnit(id);
		else if(Utils.PHU.equalsIgnoreCase(type)) return new PhonologicalUnit(id);
		else if(Utils.USYN.equalsIgnoreCase(type)) return new SyntacticUnit(id);
		else if(Utils.USEM.equalsIgnoreCase(type)) return new SemanticUnit(id);
                else throw new Exception("Type "  + type + " is unknown");
	} else {
            return null;
        }
    }
}
