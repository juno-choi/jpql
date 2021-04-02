package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        //db와의 연결
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        //트랜잭션 정의
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        //code
        try{

            Member member= new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);


            /*TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
            //결과가 여러개일경우(결과가 없으면 빈 리스트 반환)
            List<Member> resultList = query.getResultList();

            TypedQuery<Member> query2 = em.createQuery("select m from Member m where m.id = 10", Member.class);
            //결과가 한개일경우 (결과가 정확하게 하나가 나와야함. exception 떨어짐)
            Member singleResult = query2.getSingleResult();*/

            Member singleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println(singleResult);


            tx.commit();
        }catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        } finally{
            //자원 반납
            em.close();
        }
        //자원 반납
        emf.close();
    }
}
