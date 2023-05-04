package org.panda.tech.core.web.restful;

/**
 * 定义返回数据结构
 * 
 * @author fangen
 */
public interface Result {
	
	int getCode();
	
    String getMessage();
}
