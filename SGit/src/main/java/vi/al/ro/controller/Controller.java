package vi.al.ro.controller;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import vi.al.ro.configuration.RepoComponent;

import java.io.File;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Controller
public class Controller {

    private final RepoComponent repo;

    public Controller(RepoComponent repo) {
        this.repo = repo;
    }

    @RequestMapping("/")
    public String index(Map<String, Object> model) {
        return "redirect:/index.html";
    }

    @RequestMapping("/clone")
    public String indexClone(Map<String, Object> model, @RequestBody MultiValueMap<String, String> formData) {

        String repoName = formData.getFirst("name");
        String repoPath = formData.getFirst("url");

        repo.setName(repoName);
        repo.setPath(repoPath);

        repo.cloneRepo();

//        List<String> branchesName = repo.getBranches();
//        List<File> files = repo.getFiles("");
//
//        model.put("repoName", repoName);
//        model.put("branches", branchesName);
//        model.put("files", files);
        return "repo";
    }

    @RequestMapping("/create")
    public String indexCreate(Map<String, Object> model) {
        return "repo";
    }

    @RequestMapping("/repo/{repo_path}")
    public String indexClone(Map<String, Object> model, @PathVariable("repo_path") String repoPath) {

        List<String> branchesName = repo.getBranches();
        List<File> files = repo.getFiles(repoPath);

        model.put("repoName", repo.getName());
        model.put("branches", branchesName);
        model.put("files", files);
        return "repo";
    }
}
