package ${packageName};

import org.springframework.stereotype.Repository;
import support.jpa.org.panda.tech.data.JpaRepoxSupport;
import ${entityClassName};

/**
 * @author bamboo-code-generator
 */
@Repository
public class ${repoClassSimpleName} extends JpaRepoxSupport<${entityClassSimpleName}> {
}
