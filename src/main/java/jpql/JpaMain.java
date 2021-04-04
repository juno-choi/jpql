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
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member= new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setType(MemberType.ADMIN);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();
            //String sql = "select m.username, 'HEELO', true from Member m " +
            //        "where m.type = jqpl.MemberType.ADMIN";
            String sql = "select m.username, 'HEELO', true from Member m " +
                            "where m.type = :userType";
            List<Object[]> result = em.createQuery(sql)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();

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
