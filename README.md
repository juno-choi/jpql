# jpql


# jqpl  기본문법
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

# 프로젝션(select)
> * `엔티티 타입 프로젝션`
>  > ```java
>  > List<Member> result = em.createQuery("select m from Member m", Member.class)
>  > .getResultList();
>  > ```
>  > ```java
>  > List<Team> result2 = em.createQuery("select m.team from Member m", Team.class)
>  > .getResultList();
>  > //join을 직접 표시하여 실행가능 직접 표시하는걸 추천
>  > List<Team> result3 = em.createQuery("select t from Member m join m.team t", Team.class)
>  > .getResultList();
>  > ```
> * `임베디드 타입 프로젝션`
>  > ```java
>  > List<Address> result4 = em.createQuery("select o.address from Order o", Address.class)
>  > .getResultList();
>  > ```
> * `스칼라 타입 프로젝션`
>  > ```java
>  > //object[]로 반환
>  > List result5 = em.createQuery("select m.username, m.age from Member m")
>  > .getResultList();
>  > ```
>  > ```java
>  > //제네릭에 object[] 선언 
>  > List<Object[]> result6 = em.createQuery("select m.username, m.age from Member m") 
>  > .getResultList(); 
>  > ```
>  > ```java
>  > //new 명령어로 조회 (가장 추천)
>  > List<MemberDTO> result7 = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class) 
>  > .getResultList(); 
>  > ```

# 페이징 API
> * `setFirstResult`
> 
>  시작 번호
> * `setMaxResults`
> 
>  종료 번호
> 
>  > ```java
>  > List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
>  > .setFirstResult(0)  
>  > .setMaxResults(10)
>  > .getResultList(); 
>  > ```