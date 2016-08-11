package pl.com.bottega.documentmanagement.api;

import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.documentmanagement.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

/**
 * Created by bartosz.paszkowski on 30.07.2016.
 */
@Service
public class NPlus1SelectSimulator {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertTestData(){
        Employee employee = new Employee(randomString(), randomString(), new EmployeeId(98762L));
        entityManager.persist(employee);
        for (int i = 0 ; i<1000; i++){
            Document d = new Document(new DocumentNumber(randomString()), randomString(), randomString(), employee);
            d.tag(Sets.newHashSet(new Tag("one"), new Tag("two"), new Tag("three")));
            entityManager.persist(d);
        }
    }

    @Transactional
    public void simulate(){
        Query query = entityManager.createQuery("from Document d join fetch d.tags", Document.class).setMaxResults(10);
        List<Document> documents = query.getResultList();
        for (Document d : documents){
            System.out.println(d.toString()+" ");
            for (Tag t : d.tags())
                System.out.println(t.toString()+" ");
            System.out.println();
        }
    }

    @Transactional
    public Document getDocument(){
        Query query = entityManager.createQuery("from Document d JOIN FETCH d.tags");
        query.setMaxResults(1);
        return (Document)query.getResultList().get(0);
    }

    private String randomString(){
        return  UUID.randomUUID().toString();
    }
}
