package vi.al.ro.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vi.al.ro.beans.RepoBean;
import vi.al.ro.entity.Repo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@org.springframework.stereotype.Controller
public class Controller {

    private final RepoBean repoBean;

    public Controller(RepoBean repoBean) {
        this.repoBean = repoBean;
    }

    @Value("${welcome.message:test}")
    private String message = "Hello World";

    @RequestMapping("/")
    public String index(Map<String, Object> model) {
        model.put("message", this.message);
        return "index";
    }

    @RequestMapping("/listRepo")
    public String listRepos(Map<String, Object> model) {

        Set<Repo> listRepos = repoBean.getRepos();
        model.put("repos", listRepos);
        return "reposList";
    }

    @RequestMapping("/repo/{repo_name}")
    public String repoBrowser(Map<String, Object> model, @PathVariable("repo_name") String repoName) {

        Optional<Repo> repo = repoBean.getRepoByName(repoName);
        if (repo.isPresent()) {
            model.put("repo", repo.get());
            return "repo";
        } else {
            return "redirect:/404";
        }
    }
}
