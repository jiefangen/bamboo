package ${packageName};

import org.springframework.stereotype.Repository;
import org.panda.data.jpa.support.JpaRepoxSupport;
<#if keyClassName??>import ${keyClassName};</#if>

import ${entityClassName};

/**
 * @author bamboo-code-generator
 */
@Repository
public interface ${repoClassSimpleName} extends JpaRepoxSupport<${entityClassSimpleName}> {
}
