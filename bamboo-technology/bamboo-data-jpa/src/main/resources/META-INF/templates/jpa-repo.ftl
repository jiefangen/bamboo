package ${packageName};

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import ${entityClassName};

/**
 * @author bamboo-code-generator
 */
public interface ${repoClassSimpleName} extends JpaRepositoryImplementation<${entityClassSimpleName}, ${keyClassSimpleName}> {
}
