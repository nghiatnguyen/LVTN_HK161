/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.element;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TRONGNGHIA
 */
public class CorefChain {    
    
    private List<Integer> chains;
    
    public CorefChain(){
        this.chains = new ArrayList<>();
    }

    /**
     * @return the chains
     */
    public List<Integer> getChain() {
        return chains;
    }

    /**
     * @param npId the NP to add
     */
    public void addCoref(int npId) {        
        this.chains.add(npId);                
    }
}
