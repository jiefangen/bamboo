package ${packageName};

import org.springframework.stereotype.Repository;
import org.panda.data.jpa.support.JpaRepoxSupport;
import ${entityClassName};

/**
 * @author bamboo-code-generator
 */
@Repository
public class ${repoClassSimpleName} extends JpaRepoxSupport<${entityClassSimpleName}> {
}
