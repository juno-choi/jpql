# jpql


* **2021-04-03**
  > jqpl  기본문법
  > * `getResultList() 와 getSingleResult`
  > 
  >  getResultList()
  >
  >  > 결과가 하나 이상일때, 없을경우 빈값을 반환
  > 
  >  getSingleResult()
  >
  >  > 결과가 하나일때
  >  >       
  >  > 없을경우 NoResultException
  >  >
  >  > 없을경우 NonUniqueResultException

  > * `파라미터 바인딩 - 이름 기준, 위치 기준`
  >
  >  이름 기준
  > 
  >  > ```java
  >  > Member singleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
  >  > .setParameter("username", "member1")
  >  > .getSingleResult();
  >  > ```
  >
  >  위치 기준 <h6>(사용하지 않는 편이 좋음)</h6>
  >
  >  > ```java 
  >  > Member singleResult = em.createQuery("select m from Member m where m.username = ?1", Member.class)
  >  > .setParameter(1, "member1")
  >  > .getSingleResult();
  >  > ```