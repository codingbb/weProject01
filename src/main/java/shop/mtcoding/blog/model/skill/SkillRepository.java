package shop.mtcoding.blog.model.skill;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.model.resume.Resume;
import shop.mtcoding.blog.model.resume.ResumeRequest;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class SkillRepository {
    private final EntityManager em;

    public List<Skill> findAllV2(int jobsId) {
        Query query = em.createNativeQuery("select * from skill_tb where jobs_id = ?", Skill.class);
        query.setParameter(1, jobsId);
        return query.getResultList();
    }

//    public List<Skill> findSkillName(SkillResponse.SkillDTO skillDTO) {
//        Query query = em.createNativeQuery("select * from skill_tb where resume_id = ?", Skill.class);
//        query.setParameter(1, skillDTO.getResume_id());
//        return query.getResultList();
//    }

    public List<Skill> findAllSkill(int resumeId) {
        String q = """
                  select * from skill_tb where resume_id = ?
                  """;

        Query query = em.createNativeQuery(q, Skill.class);
        query.setParameter(1, resumeId);
        try {
            List<Skill> skillList = query.getResultList();
            return skillList;
        } catch (Exception e) {
            return null;
        }
    }


    @Transactional
    public void save(String skill, int resumeId) {
        String q = """
                insert into skill_tb (resume_id, jobs_id, name, role) values (?, ?, ?, ?);
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, resumeId);
        query.setParameter(2, null);
        query.setParameter(3, skill);
        query.setParameter(4, 1);
        query.executeUpdate();

    }




}
