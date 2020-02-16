package is.osullivan.contactmgr;

import is.osullivan.contactmgr.model.Contact;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.hibernate.service.ServiceRegistry;

import java.util.List;

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
        int id = save(contact);
        // Display a list of contacts before update
        System.out.printf("%n%nBefore update%n%n");
        fetchAllContacts().forEach(System.out::println);
        // Get the persisted contact
        Contact contactToBeUpdated = findContactById(id);
        // Update the contact
        contactToBeUpdated.setFirstname("NewName");
        // Persist the changes
        System.out.printf("%n%nUpdating...%n%n");
        update(contactToBeUpdated);
        System.out.printf("%n%nUpdate Complete!%n%n");
        // Display a list of contacts after update
        fetchAllContacts().forEach(System.out::println);
    }

    private static Contact findContactById(int id) {
        // Open a session
        Session session = sessionFactory.openSession();
        // Retrieve the contact
        Contact contact = session.get(Contact.class, id);
        // Close the session
        session.close();
        // Return the object
        return contact;
    }

    private static void update (Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();
        // Create transaction
        session.beginTransaction();
        // Update contact
        session.update(contact);
        // Commit the transaction
        session.getTransaction().commit();
        // Close the session
        session.close();
    }

    @SuppressWarnings("unchecked assignment")
    private static List<Contact> fetchAllContacts() {
        // Open a session
        Session session = sessionFactory.openSession();
        // Create criteria
        Criteria criteria = session.createCriteria(Contact.class);
        // Get list of objects using the criteria
        List<Contact> contacts = criteria.list();
        // CLose a session
        session.close();
        return contacts;
    }

    private static int save(Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();
        // Begin a transaction
        session.beginTransaction();
        // Use the session to save the contact
        int id = (int) session.save(contact);
        // Commit the transaction
        session.getTransaction().commit();
        // Close the session
        session.close();
        return id;
    }
}
