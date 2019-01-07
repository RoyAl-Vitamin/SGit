package vi.al.ro.configuration;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RepoComponent {

    private Logger logger = LoggerFactory.getLogger(RepoComponent.class);

    private String name;

    private String path;

    private Git git;

    private File localPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void cloneRepo() {
        logger.info("repo name == " + name + " path == " + path + " tmpdir == " + System.getProperty("java.io.tmpdir"));
        try {
            localPath = File.createTempFile(name, "");
            Files.delete(localPath.toPath());
        } catch (IOException iOE) {
            logger.error("IOException == ", iOE);
        }
        logger.info("localPath == " + localPath.getAbsolutePath());

        try {
            git = Git.cloneRepository()
                    .setURI(path)
                    .setDirectory(localPath)
                    .setCloneAllBranches(true)
                    .call();
        } catch (GitAPIException gAPIE) {
            logger.error("GitAPIException == ", gAPIE);
        }
    }

    public List<String> getBranches() {
        // List all branches
        List<Ref> call;
        try {
            call = git.branchList().call();
        } catch (GitAPIException e) {
            logger.error("GitAPIException == ", e);
            return Collections.EMPTY_LIST;
        }

        for (Ref ref : call) {
            logger.debug("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
        }

        return call.stream().map(Ref::getName).collect(Collectors.toList());
    }

    public List<File> getFiles(File file) {
        if (file == null) file = localPath;

        if (!file.getAbsolutePath().startsWith(localPath.getAbsolutePath())) {
            return Collections.EMPTY_LIST;
        }
        if (!file.isDirectory()) {
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(Objects.requireNonNull(file.listFiles()));
    }

    public List<File> getFiles(String repoPath) {
        File file = findEntryPoint(repoPath);

        return getFiles(file);
    }

    public boolean isDir(String repoPath) {
        File file = findEntryPoint(repoPath);

        return file.isDirectory();
    }

    public String getFullPath(String repoPath) {
        File file = new File(localPath, repoPath);
        return file.getAbsolutePath();
    }

    private File findEntryPoint(String repoPath) {
        File file;
        if (repoPath == null || repoPath.length() == 0) {
            file = localPath;
        } else {
            file = new File(localPath, repoPath);
        }
        return file;
    }

    public List<RevCommit> getCommits() {
        List<Ref> call;
        try {
            call = git.branchList().call();
        } catch (GitAPIException e) {
            logger.error("GitAPIException == ", e);
            return Collections.EMPTY_LIST;
        }

        Repository repo = git.getRepository();
        RevWalk walk = new RevWalk(repo);

        List<RevCommit> listCommits = new ArrayList<>();
        for (Ref branch : call) {

            String branchName = branch.getName();

            System.out.println("Commits of branch: " + branch.getName());
            System.out.println("-------------------------------------");

            Iterable<RevCommit> commits = null;
            try {
                commits = git.log().all().call();
            } catch (GitAPIException e) {
                logger.error("GitAPIException == ", e);
            } catch (IOException e) {
                logger.error("IOException == ", e);
            }

            if (commits == null) {
                return Collections.EMPTY_LIST;
            }

            for (RevCommit commit : commits) {
                boolean foundInThisBranch = false;

                RevCommit targetCommit = null;
                try {
                    targetCommit = walk.parseCommit(repo.resolve(commit.getName()));
                } catch (IOException e) {
                    logger.error("IOException == ", e);
                    continue;
                }

                for (Map.Entry<String, Ref> e : repo.getAllRefs().entrySet()) {

                    if (e.getKey().startsWith(Constants.R_HEADS)) {

                        try {
                            if (walk.isMergedInto(targetCommit, walk.parseCommit(e.getValue().getObjectId()))) {

                                String foundInBranch = e.getValue().getName();
                                if (branchName.equals(foundInBranch)) {
                                    foundInThisBranch = true;
                                    break;
                                }
                            }
                        } catch (IOException e1) {
                            logger.error("IOException == ", e);
                        }
                    }
                }

                if (foundInThisBranch) {
                    listCommits.add(commit);
                    /*
                    System.out.println(commit.getName());
                    System.out.println(commit.getAuthorIdent().getName());
                    System.out.println(new Date(commit.getCommitTime() * 1000L));
                    System.out.println(commit.getFullMessage());
                    */
                }
            }
        }
        return listCommits;
    }

    public boolean isConnected() {
        return git != null && git.getRepository() != null;
    }
}
