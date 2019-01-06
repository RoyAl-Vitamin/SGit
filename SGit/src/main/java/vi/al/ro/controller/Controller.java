package vi.al.ro.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import vi.al.ro.configuration.RepoComponent;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Controller
public class Controller {

    private Logger logger = LoggerFactory.getLogger(Controller.class);

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

        return "redirect:/repo";
    }

    @RequestMapping("/create")
    public String indexCreate(Map<String, Object> model) {
        return "redirect:/repo";
    }

    @RequestMapping(value = {"/repo", "/repo/"})
    public String indexCloneWithoutParam(Map<String, Object> model) {
        return indexClone(model, "");
    }

    @RequestMapping(value = "/repo/**")
    public String indexCloneWithParam(Map<String, Object> model, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String relativePath = requestURI.replaceFirst("^/repo/", "");
        return indexClone(model, relativePath);
    }

    private String indexClone(Map<String, Object> model, String repoPath) {

        List<String> branchesName = repo.getBranches();
        if (repo.isDir(repoPath)) {
            List<File> files = repo.getFiles(repoPath);
            files.sort((o1, o2) -> {
                if (o1.isDirectory() && !o2.isDirectory()) {
                    return -1;
                }
                if (!o1.isDirectory() && o2.isDirectory()) {
                    return 1;
                }
                if ((o1.isDirectory() && o2.isDirectory()) || (!o1.isDirectory() && !o2.isDirectory())) {
                    return o1.getName().compareTo(o2.getName());
                }
                return 0;
            });
            model.put("files", files);
        } else {
            List<String> lines = null;
            try {
                lines = Files.readAllLines(Paths.get(repo.getFullPath(repoPath)), Charset.defaultCharset());
            } catch (IOException e) {
                logger.error("IOException: ", e);
            }
            model.put("file", lines);
        }

        model.put("repoName", repo.getName());
        model.put("branches", branchesName);
        return "repo";
    }
}
