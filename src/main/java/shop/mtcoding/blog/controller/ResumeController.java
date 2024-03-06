package shop.mtcoding.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.dto.scrap.ScrapResponse;
import shop.mtcoding.blog.model.resume.Resume;
import shop.mtcoding.blog.model.resume.ResumeRepository;
import shop.mtcoding.blog.model.resume.ResumeRequest;
import shop.mtcoding.blog.model.scrap.ScrapRepository;
import shop.mtcoding.blog.model.skill.Skill;
import shop.mtcoding.blog.model.skill.SkillRepository;
import shop.mtcoding.blog.model.skill.SkillResponse;
import shop.mtcoding.blog.model.user.User;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ResumeController {

    private final HttpSession session;
    private final ResumeRepository resumeRepository;
    private final ScrapRepository scrapRepository;
    private final SkillRepository skillRepository;

    //save를 resume과 skill에 2번 해준다
    @PostMapping("/resume/save")
    public String save(ResumeRequest.WriteDTO requestDTO, HttpServletRequest request, SkillResponse.SkillDTO skillDTO) {
        System.out.println(requestDTO);

        User sessionUser = (User) session.getAttribute("sessionUser");
        //resume 이력서 저장
        int resumeId = resumeRepository.save(requestDTO, sessionUser);
        List<Skill> skillList = skillRepository.findAllSkill(resumeId);
        request.setAttribute("skillList",skillList);

//        for (String skill : requestDTO.getSkills()) {
//            skillRepository.save(skill, resumeId);
//        }

//        List<Skill> skillList = skillRepository.findAllSkill(skillDTO);
//        request.setAttribute("skillList", skillList);

//        List<Skill> skillList = skillRepository.findSkillName(skillDTO);
//        request.setAttribute("skillList", skillList);

        return "redirect:/resume/manageResume";
    }


    @GetMapping("/resume/manageResume")
    public String manageResume(HttpServletRequest request) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        List<Resume> resumeList = resumeRepository.findByUserId(sessionUser);



        System.out.println(resumeList);
        request.setAttribute("resumeList", resumeList);



        return "/resume/manageResume";
    }

//    @GetMapping("/resume/{userId}/manageResume")
//    public String manageResume(@PathVariable Integer userId, HttpServletRequest request) {
//        List<Object[]> resumeList = resumeRepository.findAll(userId);
//        List<ResumeRequest.UserViewDTO> userViewDTOList = new ArrayList<>();
//
//        Integer nextNumber = 1;
//        ResumeRequest.UserViewDTO userViewDTO = new ResumeRequest.UserViewDTO();
//
//        for (int i = 0; i < resumeList.size(); i++) {
//            Object[] user = resumeList.get(i);
//            if (userViewDTO.getId() == user[0]){
//                // 스킬 이름 생성
//                String color = "";
//                if (((String)user[7]).equals("yellow")){
//                    color = "badge rounded-pill text-bg-warning";
//                }
//                if(((String)user[7]).equals("red")){
//                    color = "badge rounded-pill text-bg-danger";
//                }
//                if(((String)user[7]).equals("blue")){
//                    color = "badge rounded-pill text-bg-info";
//                }
//                if(((String)user[7]).equals("green")){
//                    color = "badge rounded-pill text-bg-success";
//                }
//                if(((String)user[7]).equals("green")){
//                    color = "badge rounded-pill text-bg-success";
//                }
//                if(((String)user[7]).equals("green")){
//                    color = "badge rounded-pill text-bg-success";
//                }
//                SkillRequest.UserskillDTO userskillDTO = SkillRequest.UserskillDTO.builder().name((String) user[6]).color((String) user[7]).build();
//
//                //  userViewDTO.getSkillList().add(userskillDTO);
//
//            }else{
//                List<SkillRequest.CompskillDTO> skillList = new ArrayList<>();
//
//
//                String color = "";
//                if (((String)user[7]).equals("yellow")){
//                    color = "badge rounded-pill text-bg-warning";
//                }
//                if(((String)user[7]).equals("red")){
//                    color = "badge rounded-pill text-bg-danger";
//                }
//                if(((String)user[7]).equals("blud")){
//                    color = "badge rounded-pill text-bg-info";
//                }
//                if(((String)user[7]).equals("green")){
//                    color = "badge rounded-pill text-bg-success";
//                }
//                if(((String)user[7]).equals("purple")){
//                    color = "badge rounded-pill text-bg-success";
//                }
//                if(((String)user[7]).equals("green")){
//                    color = "badge rounded-pill text-bg-success";
//                }
//
//                skillList.add(SkillRequest.CompskillDTO.builder().name((String) user[6]).color(color).build());
//
//                userViewDTO = new ResumeRequest.UserViewDTO();
//                userViewDTO.setId((Integer) user[0]);
//                userViewDTO.setUserId((Integer) user[1]);
//                userViewDTO.setTitle((String) user[2]);
//                userViewDTO.setEdu((String) user[3]);
//                userViewDTO.setArea((String) user[4]);
//                userViewDTO.setResumeId((String) user[5]);
//                userViewDTO.setNumber(nextNumber++);
//                userViewDTO.setSkillList(skillList);
//
//                userViewDTOList.add(userViewDTO);
//
//                System.out.println(userViewDTOList);
//
//                session.setAttribute("resumeList",userViewDTOList);
//
//            }
//
//        }

//        request.setAttribute("resumeList", resumeList);
//
//        return "/resume/manageResume";
//    }


    @GetMapping("/resume/resumeDetail/{id}")
    public String resumeDetail (@PathVariable Integer id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionComp");

        Resume resumeDTO = resumeRepository.findById(id);
        request.setAttribute("resume", resumeDTO);

        if(sessionUser == null) {
            ScrapResponse.DetailDTO scrapDetailDTO = scrapRepository.findScrap(id);
            request.setAttribute("scrap", scrapDetailDTO);
        } else {
            ScrapResponse.DetailDTO scrapDetailDTO = scrapRepository.findScrap(id, sessionUser.getId());
            request.setAttribute("scrap", scrapDetailDTO);
        }
        return "/resume/resumeDetail";
    }

    @GetMapping("/resume/updateResumeForm")
    public String updateResumeForm () {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){ // 401
            return "redirect:/loginForm";
        }

        return "/resume/updateResumeForm";
    }


    @GetMapping("/resume/writeResumeForm")
    public String writeResumeForm() {

        return "/resume/writeResumeForm";
    }





    @PostMapping("/resume/{id}/delete")
    public String delete(@PathVariable int id, HttpServletRequest request){
        User sessionUser = (User) session.getAttribute("sessionComp");
        if(sessionUser == null){ // 401
            return "redirect:/loginForm";
        }
        Resume resumeDTO = resumeRepository.findById(id);
        resumeRepository.deleteById(id);

        request.setAttribute("resume", resumeDTO);

        return "redirect:/resume/manageResume";

    }

}