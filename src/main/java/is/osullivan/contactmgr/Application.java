package is.osullivan.contactmgr;

import is.osullivan.contactmgr.model.Contact;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.hibernate.service.ServiceRegistry;

public class Application {
    // Hold a reusable reference to a session factory
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        // Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Contact contact = new Contact.ContactBuilder("Donal", "O'Sullivan")
                .withPhone(12341234L)
                .withEmail("asdfasfd")
                .build();
        // Open a session
        Session session = sessionFactory.openSession();
        // Begin a transaction
        session.beginTransaction();
        // Use the session to save the contact
        session.save(contact);
        // Commit the transaction
        session.getTransaction().commit();
        // Close the session
        session.close();
    }
}
