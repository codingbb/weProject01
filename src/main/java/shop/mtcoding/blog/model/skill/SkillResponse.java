package shop.mtcoding.blog.model.skill;

import lombok.Data;
import lombok.NoArgsConstructor;

public class SkillResponse {

    @NoArgsConstructor
    @Data
    public static class SkillDTO {
        private Integer id;
        private String name;
        private Integer resumeId;

    }


    @Data
    public static class ResumeSkillDTO{
        private String name;
        private String color;

        public ResumeSkillDTO(String name, String color) {
            this.name = name;
            this.color = color;
        }
    }
}
