package ${packageName};

import org.springframework.data.jpa.repository.JpaRepository;
import ${entityClassName};

/**
 * @author bamboo-code-generator
 */
public interface ${repoClassSimpleName} extends JpaRepository<${entityClassSimpleName}, ${keyClassSimpleName}> {
}
