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

            em.flush();
            em.clear();

            //엔티티 프로젝션
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .getResultList();
            //엔티티 프로젝션 (단 join 발생)
            List<Team> result2 = em.createQuery("select m.team from Member m", Team.class)
                    .getResultList();
            //join을 직접 표시하여 실행가능
            List<Team> result3 = em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();
            //임베디드 프로젝션 (임베디드 타입의 결과값 조회)
            List<Address> result4 = em.createQuery("select o.address from Order o", Address.class)
                    .getResultList();
            //스칼라 타입 프로젝션
            //object 배열로 반환
            List result5 = em.createQuery("select m.username, m.age from Member m")
                    .getResultList();
            //제네릭에 object[] 선언
            List<Object[]> result6 = em.createQuery("select m.username, m.age from Member m")
                    .getResultList();
            //new 명령어로 조회 (가장 추천)
            List<MemberDTO> result7 = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
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
