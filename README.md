# jpql
   출처 : https://www.inflearn.com/course/ORM-JPA-Basic/lecture/21723


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

# join
> **1:N 관계인지 확인 후 1:N의 fetch를 LAZY 확인 필수!**
> * `inner join`
>  > ```java
>  > String sql = "select m from Member m inner join m.team t"; 
>  > List<Member> result = em.createQuery(sql, Member.class)   
>  > .getResultList(); 
>  > ```
> * `outer join`
>  > ```java
>  > String sql = "select m from Member m left join m.team t"; 
>  > List<Member> result = em.createQuery(sql, Member.class)   
>  > .getResultList(); 
>  > ```
> * `theta join`
>  > ```java
>  > String sql = "select m from Member m, Team t where m.username = t.name";
>  > List<Member> result = em.createQuery(sql, Member.class)   
>  > .getResultList();
>  > ```
> * `ON 절 추가하여 join 필터링`
> 
>  hibernate 5.1 추가
>  > ```java
>  > //일반 join
>  > String sql = "select m from Member m left join m.team t on t.name = 'teamA'";
>  > List<Member> result = em.createQuery(sql, Member.class)   
>  > .getResultList();
>  > 
>  > //연관관계가 없는 join (=theta join)
>  > String sql = "select m from Member m left join Team t on m.username = t.name";
>  > List<Member> result = em.createQuery(sql, Member.class)   
>  > .getResultList();
>  > ```

# sub query
> * `sub query 예시`
> 
>  jpa는 where과 having 절에서만 sub query를 지원함
> 
>  hibernate에서 select 절 sub query를 지원
>  > ```sql
>  > # 기존의 sql문처럼 대상을 객체로하여 sub query를 만들어주면 됨
>  > select m from Member m where m.age > (select avg(m2.age) from Member m2) 
>  > ```
> * `sub query function`
>  1. [NOT] EXISTS : sub query에 결과가 존재하면 참
>  > ```sql
>  > select m from Member m where exists (select t from m.team t where t.name = ‘팀A')
>  > ```
>  2. ALL : 모두 만족하면 참
>  > ```sql
>  > select o from Order o where o.orderAmount > ALL (select p.stockAmount from Product p)
>  > ```
>  3. ANY, SOME : 하나라도 만족하면 참
>  > ```sql
>  > select m from Member m where m.team = ANY (select t from Team t)
>  > ```
>  4. [NOY] IN : 결과 중 하나라도 같은 것이 있으면 참
>  > ```sql
>  > select m from Member m where m.age IN (1, 2, 3, 4, 10)
>  > ```

