package shop.mtcoding.blog.model.resume;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.model.skill.Skill;
import shop.mtcoding.blog.model.user.User;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ResumeRepository {

    private final EntityManager em;

    @Transactional
    public Integer save(ResumeRequest.WriteDTO requestDTO, User sessionUser) {
        String q = """
                insert into resume_tb (user_id, area, edu, career, introduce, port_link, title, is_public, created_at) 
                values (?, ?, ?, ?, ?, ?, ?, ?, now())
                """;

        Query query = em.createNativeQuery(q);
        query.setParameter(1, sessionUser.getId());
        query.setParameter(2, requestDTO.getArea());
        query.setParameter(3, requestDTO.getEdu());
        query.setParameter(4, requestDTO.getCareer());
        query.setParameter(5, requestDTO.getIntroduce());
        query.setParameter(6, requestDTO.getPortLink());
        query.setParameter(7, requestDTO.getTitle());
        query.setParameter(8, requestDTO.getIsPublic());

        query.executeUpdate();

//        String a = """
//                select * from resume_tb where id = (select max(id) from resume_tb);
//                """;
//
//        Query skillQuery = em.createNativeQuery(a, Resume.class);
//        Resume resume = (Resume) skillQuery.getSingleResult();

        String a = """
            select max(id) from resume_tb ;
            """;
        Query query1 = em.createNativeQuery(a);
        Integer resumeId= (Integer) query1.getSingleResult();



        return resumeId; // resumeId 반환



    }


     public List<Resume> findAll() {
          String q = """
                  select * from resume_tb order by id desc
                  """;

          Query query = em.createNativeQuery(q, Resume.class);
          List<Resume> resumeList = query.getResultList();

          return resumeList;
      }

    public List<Resume> findByUserId(User sessionUser) {
        String q = """
                  select * from resume_tb where user_id = ? order by id desc
                  """;

        Query query = em.createNativeQuery(q, Resume.class);
        query.setParameter(1, sessionUser.getId());
        List<Resume> resumeList = query.getResultList();

        return resumeList;
    }


//   public List<Object[]> findAll(Integer userId) {
////        String q = """
////                select * from resume_tb order by id desc
////                """;
//
//       String q = """
//              SELECT r.id, r.title, r.edu, r.career, r.area, s.resume_id, s.name , s.color
//              FROM resume_tb r
//              join user_tb u
//              ON (r.user_id = u.id)
//              join skill_tb s
//              on r.id = s.resume_id
//              where r.id = ?;
//               """;
//
//       Query query = em.createNativeQuery(q);
//
//       query.setParameter(1,userId);
//
//       List<Object[]> resumeList = query.getResultList();
//
//       return resumeList;
//   }

    public Resume findById(int id) {
        String q = """
                select * from resume_tb where id = ?
                """;
        Query query = em.createNativeQuery(q,Resume.class);
        query.setParameter(1, id);

        Resume resume = (Resume) query.getSingleResult();
        return resume;
    }



    public List<Resume> findByResumeAndUser(Integer id) {
         String q = """
                 select r.*  from resume_tb r inner join user_tb u on r.user_id = u.id where r.id = ?;
                 """;

        Query query = em.createNativeQuery(q, Resume.class);
        query.setParameter(1,id);
        List<Resume> resumeList = query.getResultList();
        return resumeList;

    }


    @Transactional
    public void deleteById(int id) {
        Query query = em.createNativeQuery("delete  from resume_tb where id = ?");
        query.setParameter(1, id);
        query.executeUpdate();
    }



}