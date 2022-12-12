package utils;


import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestUsers {

  public static void main(String[] args) {

    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    EntityManager em = emf.createEntityManager();
    
    // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
    // CHANGE the three passwords below, before you uncomment and execute the code below
    // Also, either delete this file, when users are created or rename and add to .gitignore
    // Whatever you do DO NOT COMMIT and PUSH with the real passwords

    User user = new User("user", "test123","Mogens", 20202020, "Værebrovej 18", 2880);
    User admin = new User("admin", "test123", "Konrad", 30303030, "Liljevej 13", 2900);
    User both = new User("user_admin", "test123", "Bertram", 40404040, "Lykkevej 108", 4000);

    /*if(admin.getPassword().equals("test")||user.getPassword().equals("test")||both.getPassword().equals("test"))
      throw new UnsupportedOperationException("You have not changed the passwords");*/

    em.getTransaction().begin();
    em.persist(user);
    em.persist(admin);
    em.persist(both);
    em.getTransaction().commit();
    //System.out.println("PW: " + user.getPassword());
    System.out.println("Testing user with OK password: " + user.verifyPassword("test"));
    System.out.println("Testing user with wrong password: " + user.verifyPassword("test1"));
    System.out.println("Created TEST Users");
   
  }

}
