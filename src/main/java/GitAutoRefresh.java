import org.apache.commons.lang3.StringUtils;
import utils.GitUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Isangi on 28/12/2017.
 */
public class GitAutoRefresh {

    public static void main(String[] args) {

        try {

            String mainPath = "D:\\Users\\Isangi\\IdeaProjects";
            List<String> paths = getReposPath(mainPath);

            for (String pathString: paths) {

                Path path = Paths.get(pathString.replaceAll(".gitignore",""));

                //Get the branches of the repo
                String branches = GitUtils.gitBranch(path);
                List<String> branchesList = new ArrayList<>();
                Collections.addAll(branchesList, branches.split(" "));
                //remove trash
                branchesList.remove("*");

                //Pull all the branches
                for (String branch : branchesList) {

                    if (StringUtils.isNotBlank(branch)) {

                        //remove current branch star
                        if (!branch.endsWith("*")) {
                            //checkout to the branch
                            System.out.println(GitUtils.gitCheckout(path, branch));
                        }
                        Boolean pullAvailable = GitUtils.gitStatus(path).contains("use \"git pull\" to update your local branch");
                        if (pullAvailable) {
                            System.out.println(GitUtils.gitPull(path));
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static List<String> getReposPath(String mainPath) {

        File dir = new File(mainPath);
        File[] files = dir.listFiles();
        List<String> paths = new ArrayList<>();

        //Search in first level folder
        for (File file: files) {

            File[] foundFile = file.listFiles((dir1, name) -> name.equalsIgnoreCase(".gitignore"));

            if (foundFile.length > 0) {
                paths.add(foundFile[0].getPath());
            }

        }

        return paths;
    }
}
