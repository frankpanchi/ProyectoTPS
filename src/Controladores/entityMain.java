/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author PANCHY
 */


public class entityMain {
    
private static final EntityManagerFactory ent = Persistence.createEntityManagerFactory("ColegioTPSPU");

public entityMain()
{}

public static EntityManagerFactory getInstance()
{
return ent;
}
        
    
}
