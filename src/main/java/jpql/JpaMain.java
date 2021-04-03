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

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();
            //inner join
            //String sql = "select m from Member m inner join m.team t";
            //outer join
            //String sql = "select m from Member m left join m.team t";
            //String sql = "select m from Member m, Team t where m.username = t.name";
            //on절 추가
            //String sql = "select m from Member m left join m.team t on t.name = 'teamA'";
            //연관 관계 없는 엔티티 외부 조인
            String sql = "select m from Member m left join Team t on m.username = t.name";

            List<Member> result = em.createQuery(sql, Member.class)
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
