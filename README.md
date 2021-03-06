# jpql
   출처 : https://www.inflearn.com/course/ORM-JPA-Basic/lecture/21723


# jqpl  기본문법

`getResultList()`
  결과가 하나 이상일때, 없을경우 빈값을 반환
 
`getSingleResult()`
결과가 하나일때

  결과가 없을경우 NoResultException

  결과가 둘 이상일 경우 NonUniqueResultException

* 파라미터 바인딩 - 이름 기준, 위치 기준

  이름 기준

  ```java
  Member singleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
  .setParameter("username", "member1")
  .getSingleResult();
  ```

  위치 기준 <h6>(사용하지 않는 편이 좋음)</h6>

  ```java 
  Member singleResult = em.createQuery("select m from Member m where m.username = ?1", Member.class)
  .setParameter(1, "member1")
  .getSingleResult();
  ```

# 프로젝션(select)
* 엔티티 타입 프로젝션
```java
List<Member> result = em.createQuery("select m from Member m", Member.class)
.getResultList();
```
```java
List<Team> result2 = em.createQuery("select m.team from Member m", Team.class)
.getResultList();
//join을 직접 표시하여 실행가능 직접 표시하는걸 추천
List<Team> result3 = em.createQuery("select t from Member m join m.team t", Team.class)
.getResultList();
```
* 임베디드 타입 프로젝션
```java
List<Address> result4 = em.createQuery("select o.address from Order o", Address.class)
.getResultList();
```
* 스칼라 타입 프로젝션
```java
//object[]로 반환
List result5 = em.createQuery("select m.username, m.age from Member m")
.getResultList();
```
```java
//제네릭에 object[] 선언 
List<Object[]> result6 = em.createQuery("select m.username, m.age from Member m") 
.getResultList(); 
```
```java
//new 명령어로 조회 (가장 추천)
List<MemberDTO> result7 = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class) 
.getResultList(); 
```

# 페이징 API
`setFirstResult`
  시작 번호

`setMaxResults`
  종료 번호
 
```java
List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
.setFirstResult(0)  
.setMaxResults(10)
.getResultList(); 
```

# join
**1:N 관계인지 확인 후 1:N의 fetch를 LAZY 확인 필수!**
* inner join
```java
String sql = "select m from Member m inner join m.team t"; 
List<Member> result = em.createQuery(sql, Member.class)   
.getResultList(); 
```
* outer join
```java
String sql = "select m from Member m left join m.team t"; 
List<Member> result = em.createQuery(sql, Member.class)   
.getResultList(); 
```
* theta join
```java
String sql = "select m from Member m, Team t where m.username = t.name";
List<Member> result = em.createQuery(sql, Member.class)   
.getResultList();
```
* ON 절 추가하여 join 필터링

  hibernate 5.1 추가
```java
//일반 join
String sql = "select m from Member m left join m.team t on t.name = 'teamA'";
List<Member> result = em.createQuery(sql, Member.class)   
.getResultList();
 
//연관관계가 없는 join (=theta join)
String sql = "select m from Member m left join Team t on m.username = t.name";
List<Member> result = em.createQuery(sql, Member.class)   
.getResultList();
```

# sub query
* sub query 예시
 
  jpa는 where과 having 절에서만 sub query를 지원함

  hibernate에서 select 절 sub query를 지원
```sql
# 기존의 sql문처럼 대상을 객체로하여 sub query를 만들어주면 됨
select m from Member m where m.age > (select avg(m2.age) from Member m2) 
```
* sub query function
  1. [NOT] EXISTS : sub query에 결과가 존재하면 참
  ```sql
  select m from Member m where exists (select t from m.team t where t.name = ‘팀A')
  ```
  2. ALL : 모두 만족하면 참
  ```sql
  select o from Order o where o.orderAmount > ALL (select p.stockAmount from Product p)
  ```
  3. ANY, SOME : 하나라도 만족하면 참
  ```sql
  select m from Member m where m.team = ANY (select t from Team t)
  ```
  4. [NOY] IN : 결과 중 하나라도 같은 것이 있으면 참
  ```sql
  select m from Member m where m.age IN (1, 2, 3, 4, 10)
  ```

# jpql 타입 표현

`문자`
  'hello', 'she"s'

`숫자`
  10L(Long), 10D(Double), 10F(Flat)

`boolean`
  TRUE, FALSE (대,소 상관없음)

`ENUM` jpql.MemberType.Admin (패키지명 포함), 단 parameter로 받을땐 객체로 받아오면 됨!
```java
String sql = "select m.username, 'HEELO', true from Member m " +
"where m.type = :userType";
List<Object[]> result = em.createQuery(sql)
.setParameter("userType", MemberType.ADMIN)
.getResultList();
```
`Entity type` TYPE(m) = Member (상속 관계일때 사용)

`기타` =, >, <, <>, <=, >= , AND, OR, NOT, BETWEEN, LIKE, ISNULL, EXISTS, IN 다 지원
  
# 조건식

* 기본 CASE 식
```java
String query =
"select " +
        "case when m.age <= 10 then '학생요금'" +
        "when m.age >= 60 then '경로요금'" +
        "else '일반요금' end " +
"from Member m";
```
* 단순 CASE 식
```java
String query =
"select " +
        "case when '10대' then '학생요금'" +
        "when '60대 이상' then '경로요금'" +
        "else '일반요금' end " +
"from Member m";
```

* CASE 조건식
  
`COALESCE` 하나씩 조회해서 NULL이 아니면 반환
```sql
select coalesce(m.username, '이름 없는 회원') from Member m
```
`NULLIF` 두 값이 같으면 null 반환, 다르면 첫번째 값 반환
```sql
select nullif(m.username, '관리자') from Member m
```

# jpql 함수

* jqpl 기본함수
  
  기본 제공되는 sql 함수들 
  
  concat, substring, trim, lower, upper, length, locate, abs, sqrt, mod

  jpa 제공 
  
  size, index  
* 사용자 정의 함수

  사용자가 필요에 의해 등록하여 사용하는 함수
```sql
select function('함수명', i.name) from Item i
```

# 경로 표현식

<h6>묵시적 조인은 최대한 사용하지 않는 편이 좋다</h6>
<h6>명시적 조인으로 다른 개발자가 봐도 이해할 수 있는 코드를 만들어 놓는 편이 좋기 때문</h6>

`상태필드` 경로 탐색의 끝

```sql
select m.username from Member m
```

`단일 값 연관 경로` 묵시적 내부 조인(inner join) 발생, 탐색 O

  Member의 Team은 또 다른 Entity

```sql
select m.team.name from Member m
```

`컬렉션 값 연관 경로` 묵시적 내부 조인 발생, 탐색 X

Team의 members는 컬렉션(List)의 값을 가지는 column

```sql
select t.members from Team t
```

이 경우 From절에서 명시적 join으로 값을 가져올 수 있다.

```sql
select m.username from Team t join t.members m
```

# fetch join
<h6>엄청 중요함</h6>
```sql
select m from Member m join fetch m.team
```

위 같은 jqpl문을 작성시 sql문으로는 아래와 같이 실행됨

```sql
select m.*, t.* from member m inner join team t on m.team_id = t.id
```

fetch join 사용시 데이터가 1대N일 경우 N으로 늘어나게 되는데 distinct를 사용하여 중복을 제거할 수 있다.
```sql
select distinct m from Member m join fetch m.team
```

* fetch join의 한계
  1. fetch join 대상에는 별칭을 줄 수 없다. 
   
     hibernate는 가능 하지만 가급적 사용하지 않는 편이 좋다.
   
     객체 그래프를 탐색하는 JPA 특성과 맞지 않음

  2. 둘 이상의 컬렉션은 페치 조인 할 수 없다.

  3. 컬렉션을 페치 조인하면 페이징을 사용할 수 없다.
  

* 해결 방법
   1. entity별로 select 혹은 fetch join을 나누어 적용하여 정보를 가져온 뒤 DTO로 반환한다.
   2. mybatis나 jdbc를 사용하여 query문을 만들어서 정보를 가져온다.
  
`entity 를 query로 직접 사용할 경우` 해당 객체의 키 값을 사용한다.

# Named query - 정적쿼리만 가능

* application 로딩 시점에 초기화 후 계속해서 재사용 가능
  
* application 로딩 시점에 query 검증

  사용자가 직접 사용하기 전에 프로그램 자체에서 검열이 되므로 굉장한 이점

# 벌크 연산

* query 한번으로 테이블의 정보를 한번에 변경

* executeUpdate()의 결과는 영향받은 엔티티 수를 int로 반환

```java
int i = em.createQuery("update Member m set m.age = 20").executeUpdate();
```

* 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리를 날리기 때문에 벌크 연산을 먼저 실행하거나 벌크 연산 수행 후 영속성 컨텍스트를 초기화한다.
